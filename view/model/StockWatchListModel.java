package com.dream.view.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class StockWatchListModel extends DefaultTableModel {
	private HashMap<String, String[]> stockWatchList;
	private ArrayList stockCodes;
	private String[] columnNames = { "代码", "时间", "相似度" };

	public StockWatchListModel(HashMap<String, String[]> stockWatchList) {
		this.stockWatchList = stockWatchList;
		this.stockCodes = new ArrayList<String>(stockWatchList.keySet());
	}

//	public void setStockWatchList(HashMap<String, String[]> stockWatchList) {
//		this.stockWatchList = stockWatchList;
//		this.stockCodes = new ArrayList<String>(stockWatchList.keySet());
//	}
	
//	public HashMap<String, String[]> getStockWatchList(){
//		return stockWatchList;
//	}
	int i = 0;
	public void update(String stockCode, String[] value){
//		if(stockWatchList.get(stockCode)!=null){
//			stockWatchList.remove(stockCode);
//			this.stockCodes = new ArrayList(stockWatchList.keySet());
//			return;
//		}
		stockWatchList.put(stockCode, value);

//        List<Map.Entry<String,String[]>> list = new ArrayList<Map.Entry<String,String[]>>(stockWatchList.entrySet());
//		Collections.sort(list, new Comparator<Map.Entry<String, String[]>>() {
//			public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
//				return (o1.getValue()[0]).toString().compareTo(o2.getValue()[0]);
//			}
//		});
		
		this.stockCodes = new ArrayList(stockWatchList.keySet());
//		if(stockWatchList.size() != stockCodes.size()){
////			this.fireTableDataChanged();
//		}
	}

	public int getRowCount() {
		if (stockWatchList == null)
			return 0;
		int size = this.stockWatchList.size();
		return size;
		
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return columnNames[columnIndex];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
//		System.out.println("rowIndex:"+rowIndex+"columnIndex:"+columnIndex);
		if(stockCodes.size() == 0)
			return null;
		switch (columnIndex) {
		case 0:
			return stockCodes.get(rowIndex);
		case 1:
			return stockWatchList.get(stockCodes.get(rowIndex))[0];
		case 2:
			return stockWatchList.get(stockCodes.get(rowIndex))[1];
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
