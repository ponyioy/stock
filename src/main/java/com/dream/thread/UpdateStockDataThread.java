package com.dream.thread;

import java.util.List;

import javax.swing.JTable;

import com.dream.entity.StockData;
import com.dream.entity.TimeSharingDataPool;
import com.dream.exception.StockDataException;
import com.dream.handle.ViewHandle;
import com.dream.util.StockDataFetcher;
import com.dream.view.StockListModel;
import com.dream.watch.TimeSharingWatch;

public class UpdateStockDataThread implements Runnable {
	private List<String> stockCodes;
	private ViewHandle viewHandle;
	
	public UpdateStockDataThread(List<String> stockCodes, ViewHandle viewHandle){
		this.stockCodes = stockCodes;
		this.viewHandle = viewHandle;
	}
	public void run() {
		if(this.stockCodes == null)
			return;
		TimeSharingDataPool.init(stockCodes);
		int i = 0;
		while(true){
			try {
				List<StockData> stockDatas = StockDataFetcher.fetchStockData(stockCodes);
				viewHandle.doStockDataUpdate(stockDatas);
				
				//更新分时数据
				TimeSharingDataPool.updateTimeSharingData(stockDatas);

			} catch (StockDataException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
