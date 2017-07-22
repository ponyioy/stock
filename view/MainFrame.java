package com.dream.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableRowSorter;

import com.dream.entity.StockData;
import com.dream.entity.TimeSharingDataPool;
import com.dream.exception.StockDataException;
import com.dream.handle.ViewHandle;
import com.dream.thread.UpdateStockDataThread;
import com.dream.thread.UpdateStockImageThread;
import com.dream.util.StockDataFetcher;
import com.dream.util.StockPropertiesReader;
import com.dream.view.model.StockHandicapModel;
import com.dream.view.model.StockListModel;
import com.dream.view.model.StockWatchListModel;
import com.dream.watch.TimeSharingWatch;

public class MainFrame extends JFrame {

	private String currentStockCode = "";
//	private int currentRow = 0;
	final JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件
	final ImagePanel[] jPanelSons = new ImagePanel[4];
	final JTable stockInfoTable = new JTable();
	final JTable stockHandicapTable = new JTable();
	final JTable stockWatchTable = new JTable();// 监控列表
	List<String> stockPool = null;
	Map<String, StockData> stockDatas = null;
	JScrollPane watchScrollPane = null;
	RowSorter<StockWatchListModel> sorter;

	public MainFrame() {
		// initData();

		// 得到要显示的用户列表对象：
		stockPool = StockPropertiesReader.readStockPool();
		try {
			stockDatas = StockDataFetcher.fetchStockData(stockPool);
			currentStockCode = stockPool.get(0);
		} catch (StockDataException e) {
			e.printStackTrace();
			return;
		}

		initComponent();

		TimeSharingWatch.init(viewHandle);

		setThread();

		// List<String> stockPool = StockPropertiesReader.readStockPool();
	}

	private ViewHandle viewHandle = new ViewHandle() {
		public void doStockDataUpdate(Map<String, StockData> stockDatas) {
			StockListModel model = (StockListModel) stockInfoTable.getModel();
			model.setStockDatas(stockPool, stockDatas);
			stockInfoTable.repaint();

			updateHandicapForStock(stockDatas.get(currentStockCode));
		}
		
		public void doStockWatchModelUpdate(String stockCode, String time, float similarity){
	    	StockWatchListModel model = (StockWatchListModel)stockWatchTable.getModel();
	    	NumberFormat numberFormat = NumberFormat.getInstance();
	    	numberFormat.setMaximumFractionDigits(2);
	    	String result = numberFormat.format(similarity*100);
	    	model.update(stockCode, new String[]{time,result + "%"});
//	    	model.addRow(new Object[]{stockCode, time, result+"%"});
//	    	model.fireTableStructureChanged();
//	    	sorter.toggleSortOrder(1);
	    	stockWatchTable.repaint();
	    	
	    	
//	    	stockWatchTable.validate();  
//	    	stockWatchTable.updateUI();  
		}
	};

	private void setThread() {

		UpdateStockDataThread usd = new UpdateStockDataThread(stockPool, viewHandle);
		Thread t = new Thread(usd);
		t.start();

		UpdateStockImageThread usi = new UpdateStockImageThread(this);
		Thread t1 = new Thread(usi);
		t1.start();
	}

	/**
	 * 初始化窗口
	 */
	private void initComponent() {

		setLayout(new BorderLayout());

		JToolBar toolBar = getToolBar();
		this.add(toolBar, BorderLayout.NORTH);

		JScrollPane scrollPane = getLeftPane();
		if (scrollPane == null)
			return;
		this.add(scrollPane, BorderLayout.WEST);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		JPanel centerPanel = getCenterPanel();
		this.add(centerPanel, BorderLayout.CENTER);

		JPanel rightPanel = getRightPanel();
		this.add(rightPanel, BorderLayout.EAST);

		// 设置标题
		this.setTitle("");
		// 设置窗口大小
		this.setSize(1000, 500);
		// 将窗口设置为显示,要写在最后一句
		this.setVisible(true);

		if (stockPool != null && stockPool.size() > 0) {
			currentStockCode = stockPool.get(0);
			updateImageForStock();
		}
	}

	private JToolBar getToolBar(){
		 JButton button1 = new JButton("同步当天数据");
		 JToolBar bar = new JToolBar();
	     bar.add(button1);
	     button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TimeSharingDataPool.updateTodayDataFromXDTData();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
	     return bar;
	}

	private JPanel getCenterPanel() {
		String[] tabNames = { "分时图", "日线图", "周线图", "月线图" };
		// 第一个标签下的JPanel
		for (int i = 0; i < tabNames.length; i++) {
			jPanelSons[i] = new ImagePanel();
			jTabbedpane.addTab(tabNames[i], null, jPanelSons[i], tabNames[i]);//
		}

		jTabbedpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateImageForStock();
			}
		});

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1, 1));
		panel.add(jTabbedpane);
		return panel;
	}

	private JPanel getRightPanel() {

		JPanel panel = new JPanel();
		// stockHandicapTable.setBounds(0, 0, 150, 300);
		StockHandicapModel stockHandicapModel = new StockHandicapModel(stockDatas.get(currentStockCode).stockHandicapData);

		stockHandicapTable.setModel(stockHandicapModel);// 将模型设给表格
		stockHandicapTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

//		String[] headers = { "代码", "时间", "相似度" };
//		Object[][] cellData = null;
//		DefaultTableModel model = new DefaultTableModel(cellData, headers) {
//			public boolean isCellEditable(int row, int column) {
//				return false;
//			}
//		};

		StockWatchListModel model = new StockWatchListModel(new HashMap<String, String[]>());
		watchScrollPane = new JScrollPane(stockWatchTable);
		// 超出大小后，JScrollPane自动出现滚动条
		watchScrollPane.setAutoscrolls(true);
		watchScrollPane.repaint();
		stockWatchTable.setDefaultRenderer(Object.class, new MyTableStyle());
		stockWatchTable.setModel(model);
		stockWatchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 设定表格在面板上的大小
		stockWatchTable.setPreferredScrollableViewportSize(new Dimension(200, 250));
		stockWatchTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				currentStockCode = (String) stockWatchTable.getModel().getValueAt(stockWatchTable.getSelectedRow(), 0);
				updateImageForStock();
				updateHandicapForStock(stockDatas.get(currentStockCode));
			}
		});
		sorter = new TableRowSorter<StockWatchListModel>(model);
		stockWatchTable.setRowSorter(sorter);
		
		panel.setLayout(new BorderLayout());
		panel.add(stockHandicapTable, BorderLayout.NORTH);
		panel.add(watchScrollPane, BorderLayout.SOUTH);
		return panel;
	}

	private JScrollPane getLeftPane() {

		stockInfoTable.setBounds(0, 0, 150, 500);

		JScrollPane scrollPane = new JScrollPane(stockInfoTable);
		// 超出大小后，JScrollPane自动出现滚动条
		scrollPane.setAutoscrolls(true);

		// 创建我们实现的TableModel对象,创建时要传入用户列表对象
		StockListModel stockListModel = new StockListModel(stockPool, stockDatas);
		stockInfoTable.setModel(stockListModel);// 将模型设给表格
		stockInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// stockInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		stockInfoTable.setDefaultRenderer(Object.class, new MyTableStyle());

		// 设定表格在面板上的大小
		stockInfoTable.setPreferredScrollableViewportSize(new Dimension(200, 190));
		stockInfoTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				currentStockCode = (String) stockInfoTable.getModel().getValueAt(stockInfoTable.getSelectedRow(), 0);
				updateImageForStock();
				updateHandicapForStock(stockDatas.get(currentStockCode));
			}
		});

		return scrollPane;
	}

	private void updateHandicapForStock(StockData stockData) {
		StockHandicapModel model = (StockHandicapModel) stockHandicapTable.getModel();
		model.setStockHandicapData(stockData.stockHandicapData);
		stockHandicapTable.repaint();
	}

	public void updateImageForStock() {

		int count = jTabbedpane.getSelectedIndex();
		byte[] output = new byte[0];
		switch (count) {
		case 0:
			output = StockDataFetcher.getStockMinImage(currentStockCode);
			break;
		case 1:
			output = StockDataFetcher.getStockDailyImage(currentStockCode);
			break;
		case 2:
			output = StockDataFetcher.getStockWeeklyImage(currentStockCode);
			break;
		case 3:
			output = StockDataFetcher.getStockMonthlyImage(currentStockCode);
			break;
		default:
			break;
		}
		BufferedImage bufferedImage = null;
		try {
			InputStream buffIn = new ByteArrayInputStream(output, 0, output.length);
			bufferedImage = ImageIO.read(buffIn);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		jPanelSons[count].loadPhoto(bufferedImage);
		jPanelSons[count].repaint();

	}

	public static void main(String[] args) {
		new MainFrame();
		// try {
		// URL url =
		// StockDataFetcher.class.getClassLoader().getResource("stockPool.txt");
		// Object a = url.getContent();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
