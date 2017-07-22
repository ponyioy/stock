package com.dream.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class HistoryTrendSearchFrame extends JFrame{
	final JTable resultTable = new JTable();
	final JTextField stockCodeField = new JTextField();
	final DateChooser beginDateField = new DateChooser();
	final DateChooser endDateField = new DateChooser();
	final JButton confirm = new JButton("搜索");
	
	public HistoryTrendSearchFrame() {
		initComponent();
	}
	
	private void initComponent(){
		setLayout(new BorderLayout());
		
		JPanel northPanel = getNorthPanel();
		this.add(northPanel, BorderLayout.NORTH);
		
		
		JScrollPane scrollPane = getSouthPanel();
		if (scrollPane == null)
			return;
		this.add(scrollPane, BorderLayout.SOUTH);
		

		// 设置标题
		this.setTitle("");
		// 设置窗口大小
		this.setSize(500, 500);
		// 将窗口设置为显示,要写在最后一句
		this.setVisible(true);
	}

	private JScrollPane getSouthPanel() {
		// TODO Auto-generated method stub

		resultTable.setBounds(0, 0, 480, 400);

		JScrollPane scrollPane = new JScrollPane(resultTable);
		// 超出大小后，JScrollPane自动出现滚动条
		scrollPane.setAutoscrolls(true);
		
		resultTable.setModel(new DefaultTableModel());// 将模型设给表格
		
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultTable.setDefaultRenderer(Object.class, new MyTableStyle());
		
		return scrollPane;
	}

	private JPanel getNorthPanel() {
		// TODO Auto-generated method stub
		JPanel panel = new JPanel();
		setLayout(new BorderLayout());
		Box box = Box.createVerticalBox();
		Box row1 = Box.createHorizontalBox();
		Box row2 = Box.createHorizontalBox();
		row1.add(new JLabel("股票代码："));
		stockCodeField.setPreferredSize(new Dimension(50, 10));
		row1.add(Box.createHorizontalStrut(5)); // 创建组件之间的距离
		row1.add(stockCodeField);
		row1.add(Box.createHorizontalStrut(5)); // 创建组件之间的距离
		row1.add(new JLabel("开始日期："));
		row1.add(Box.createHorizontalStrut(5)); // 创建组件之间的距离
		row1.add(beginDateField);
		row1.add(Box.createHorizontalStrut(5)); // 创建组件之间的距离
		row1.add(new JLabel("结束日期："));
		row1.add(Box.createHorizontalStrut(5)); // 创建组件之间的距离
		row1.add(endDateField);
		
		row2.add(confirm);
		confirm.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String stockCode = stockCodeField.getText();
				String beginDate = beginDateField.getDateString();
				String endDate = endDateField.getDateString();
			}
		});
		
		box.add(row1);
		box.add(row2);
		
		panel.add(box, BorderLayout.CENTER);
		
		return panel;
	}
	
	public static void main(String[] args) {
		new HistoryTrendSearchFrame();
	}
}
