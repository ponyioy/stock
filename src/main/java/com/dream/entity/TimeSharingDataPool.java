package com.dream.entity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dream.util.StringUtil;
import com.dream.watch.TimeSharingWatch;

/**
 * 全局分时数据池
 * 
 * @author ucs_yangshijie
 *
 */
public class TimeSharingDataPool {
	private static Map<String, StockTimeSharingData> stockTimeSharingDatas;

	private static boolean isInit = false;
	private static TimeSharingDataPool timeSharingDataPool;

	public static void init(List<String> stockCodes) {
		if (timeSharingDataPool == null) {
			timeSharingDataPool = new TimeSharingDataPool(stockCodes);
			isInit = true;
		}

	}

	private TimeSharingDataPool(List<String> stockCodes) {
		stockTimeSharingDatas = new HashMap<String, StockTimeSharingData>();
		String date = StringUtil.formatYYYYMMDD(new Date());
		for (int i = 0; i < stockCodes.size(); i++) {
			StockTimeSharingData stockTimeSharingData = new StockTimeSharingData(date);
			stockTimeSharingData.stockCode = stockCodes.get(i);
			stockTimeSharingDatas.put(stockCodes.get(i), stockTimeSharingData);

		}

	}

	public static void updateTimeSharingData(Map<String, StockData> sds) {
		if (!isInit)
			return;
		Iterator iter = sds.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, StockData> entry = (Map.Entry) iter.next();
			String key = entry.getKey();
			StockData val = entry.getValue();

			StockTimeSharingData stockTimeSharingData = stockTimeSharingDatas.get(key);
			if (stockTimeSharingData != null) {
				boolean flag = stockTimeSharingData.updateStock1MinData(val);
//				if (flag) {
					TimeSharingWatch.doWatch(stockTimeSharingData, val.stockRealData);
//				}

		}

		for (int i = 0; i < sds.size(); i++) {
			}
		}
	}

	public static void updateTodayDataFromXDTData() throws FileNotFoundException {
		Iterator iter = stockTimeSharingDatas.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			StockTimeSharingData stockTimeSharingData = (StockTimeSharingData) entry.getValue();
			stockTimeSharingData.updateStock1MinDataFromXDTData();
		}
	}

	public static Map<String, StockTimeSharingData> getStockTimeSharingDatas() {
		return stockTimeSharingDatas;
	}

}
