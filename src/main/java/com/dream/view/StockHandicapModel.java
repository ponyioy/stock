package com.dream.view;

import com.dream.entity.StockData;
import com.dream.entity.StockHandicapData;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.List;

public class StockHandicapModel extends DefaultTableModel {
    private StockHandicapData stockHandicapData;
    //	private String[] columnNames = { "代码", "名称", "当前价", "涨跌幅" };
    private String[] rowName = {"卖五", "卖四", "卖三", "卖二", "卖一", "买一", "买二", "买三", "买四", "买五"};

    public StockHandicapModel(StockHandicapData stockHandicapData) {
        this.stockHandicapData = stockHandicapData;
    }

    public void setStockHandicapData(StockHandicapData stockHandicapData) {
        this.stockHandicapData = stockHandicapData;
    }

    public int getRowCount() {
        return 10;
    }

    public int getColumnCount() {
        return 3;
    }

    public String getColumnName(int columnIndex) {
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return rowName[rowIndex];
            case 1:
                if (rowIndex < 5)
                    return stockHandicapData.sellPrice[4 - rowIndex];
                else
                    return stockHandicapData.buyPrice[rowIndex - 5];
            case 2:
                if (rowIndex < 5)
                    return stockHandicapData.sell[4 - rowIndex];
                else
                    return stockHandicapData.buy[rowIndex - 5];
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
