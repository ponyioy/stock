package com.dream.view;

import com.dream.entity.StockData;
import com.dream.exception.StockDataException;
import com.dream.handle.ViewHandle;
import com.dream.thread.UpdateStockDataThread;
import com.dream.thread.UpdateStockImageThread;
import com.dream.util.StockDataFetcher;
import com.dream.util.StockPropertiesReader;
import com.dream.watch.TimeSharingWatch;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainFrame extends JFrame {

	private String currentStockCode = "";
	private int currentRow = 0;
	final JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件
	final ImagePanel[] jPanelSons = new ImagePanel[4];
	final JTable stockInfoTable = new JTable();
	final JTable stockHandicapTable = new JTable();
	final JTable stockWatchTable = new JTable();//监控列表
	List<String> stockPool = null;
	List<StockData> stockDatas = null;
	
	public MainFrame() {
		// initData();

		// 得到要显示的用户列表对象：
		stockPool = StockPropertiesReader.readStockPool();
		try {
			stockDatas = StockDataFetcher.fetchStockData(stockPool);
		} catch (StockDataException e) {
			e.printStackTrace();
			return;
		}

		initComponent();
		
		TimeSharingWatch.init(stockWatchTable);
		
		setThread();
		
		
		// List<String> stockPool = StockPropertiesReader.readStockPool();
	}
	
	private ViewHandle viewHandle = new ViewHandle() {
		
		public void doStockDataUpdate(List<StockData> stockDatas) {
			StockListModel model = (StockListModel)stockInfoTable.getModel();
			model.setStockDatas(stockDatas);
			stockInfoTable.repaint();
			
			updateHandicapForStock(stockDatas.get(currentRow));
		}
	};
	
	private void setThread(){
		
		
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
		
		if(stockPool != null && stockPool.size() > 0){
			currentStockCode = stockPool.get(0);
			updateImageForStock();
		}
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
	
	private JPanel getRightPanel(){

		JPanel panel = new JPanel();
//		stockHandicapTable.setBounds(0, 0, 150, 300);
		StockHandicapModel stockHandicapModel = new StockHandicapModel(stockDatas.get(currentRow).stockHandicapData);

		stockHandicapTable.setModel(stockHandicapModel);// 将模型设给表格
		stockHandicapTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



		String[] headers = { "代码", "名称", "时间" };
		Object[][] cellData = null;

		DefaultTableModel model = new DefaultTableModel(cellData, headers) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JScrollPane scrollPane = new JScrollPane(stockWatchTable);
		// 超出大小后，JScrollPane自动出现滚动条
		scrollPane.setAutoscrolls(true);
		stockWatchTable.setDefaultRenderer(Object.class, new MyTableStyle());
		stockWatchTable.setModel(model);
		stockWatchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 设定表格在面板上的大小
		stockWatchTable.setPreferredScrollableViewportSize(new Dimension(200, 250));

		panel.setLayout(new BorderLayout());
		panel.add(stockHandicapTable, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.SOUTH);
		return panel;
	}

	
	private JScrollPane getLeftPane() {

		stockInfoTable.setBounds(0, 0, 150, 500);

		JScrollPane scrollPane = new JScrollPane(stockInfoTable);
		// 超出大小后，JScrollPane自动出现滚动条
		scrollPane.setAutoscrolls(true);

		// 创建我们实现的TableModel对象,创建时要传入用户列表对象
		StockListModel stockListModel = new StockListModel(stockDatas);
		stockInfoTable.setModel(stockListModel);// 将模型设给表格
		stockInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// stockInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		stockInfoTable.setDefaultRenderer(Object.class, new MyTableStyle());

		// 设定表格在面板上的大小
		stockInfoTable.setPreferredScrollableViewportSize(new Dimension(200, 190));
		stockInfoTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				currentStockCode = (String) stockInfoTable.getModel().getValueAt(stockInfoTable.getSelectedRow(), 0);
				currentRow = stockInfoTable.getSelectedRow();
				updateImageForStock();
				updateHandicapForStock(stockDatas.get(currentRow));
			}
		});

		return scrollPane;
	}

	private void updateHandicapForStock(StockData stockData){
		StockHandicapModel model = (StockHandicapModel)stockHandicapTable.getModel();
		model.setStockHandicapData(stockData.stockHandicapData);
		stockHandicapTable.repaint();
	}

	public void updateImageForStock(){

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
