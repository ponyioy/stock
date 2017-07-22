package com.dream.view.model;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.dream.entity.StockData;
import com.dream.entity.StockTimeSharingData;

public class StockListModel extends DefaultTableModel {
	private Map<String, StockData> stockDatas;
	private String[] columnNames = { "代码", "名称", "当前价", "涨跌幅" };
	private List stockPool;

	public StockListModel(List stockPool, Map<String, StockData> stockDatas) {
		this.stockPool = stockPool;
		this.stockDatas = stockDatas;
	}
	
	public void setStockDatas(List stockPool, Map<String, StockData> stockDatas){
		this.stockPool = stockPool;
		this.stockDatas = stockDatas;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		if(stockDatas == null)
			return 0;
		return this.stockDatas.size();
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return columnNames[columnIndex];
	}

	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
		case 1:
		case 3:
			return String.class;
		case 2:
			return Double.class;
		}
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
			return stockDatas.get(stockPool.get(rowIndex)).stockCode;
		case 1:
			return stockDatas.get(stockPool.get(rowIndex)).stockName;
		case 2:
			return stockDatas.get(stockPool.get(rowIndex)).stockRealData.currentPrice;
		case 3:
			StockData stockData = stockDatas.get(stockPool.get(rowIndex));
			double currentPrint = stockData.stockRealData.currentPrice;
			double lastClosePrint = stockData.stockRealData.lastClosePrice;
			double scope = (currentPrint - lastClosePrint) / lastClosePrint;
			NumberFormat percentFormat = NumberFormat.getPercentInstance();
			percentFormat.setMaximumFractionDigits(2);
			String f = percentFormat.format(scope);
			return f;
		default:
			break;
		}
		return null;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

	}

	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

}
