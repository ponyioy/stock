package com.dream.handle;

import java.util.Map;

import com.dream.entity.StockData;

public interface ViewHandle {
	public void doStockDataUpdate(Map<String, StockData> stockDatas);
	public void doStockWatchModelUpdate(String stockCode, String time, float similarity);
}
