package com.dream.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dream.util.StringUtil;
import com.dream.watch.TimeSharingWatch; 

/**
 * 全局分时数据池
 * @author ucs_yangshijie
 *
 */
public class TimeSharingDataPool {
	private static Map<String, StockTimeSharingData> stockTimeSharingDatas;

	private static boolean isInit = false;
	private static TimeSharingDataPool timeSharingDataPool;
	
	public static void init(List<String> stockCodes){
		if(timeSharingDataPool == null){
			timeSharingDataPool = new TimeSharingDataPool(stockCodes);
			isInit = true;
		}
		
	}
	
	private TimeSharingDataPool(List<String> stockCodes){
		stockTimeSharingDatas = new HashMap<String,StockTimeSharingData>();
		String date = StringUtil.formatYYYYMMDD(new Date());
		for (int i = 0; i < stockCodes.size(); i++) {
			StockTimeSharingData stockTimeSharingData = new StockTimeSharingData(date);
			stockTimeSharingData.stockCode = stockCodes.get(i);
			stockTimeSharingDatas.put(stockCodes.get(i), stockTimeSharingData);
			
		}
		
	}
	
	public static void updateTimeSharingData(List<StockData> sds){
		if(!isInit)
			return;
		for (int i = 0; i < sds.size(); i++) {
			StockTimeSharingData stockTimeSharingData = stockTimeSharingDatas.get(sds.get(i).stockCode);
			if(stockTimeSharingData != null) {
				boolean flag = stockTimeSharingData.updateStock1MinData(sds.get(i));
				if(flag){
					TimeSharingWatch.doWatch(stockTimeSharingData, sds.get(i).stockRealData);
				}
				
			}
		}
	}

	public static Map<String, StockTimeSharingData> getStockTimeSharingDatas() {
		return stockTimeSharingDatas;
	}


}
