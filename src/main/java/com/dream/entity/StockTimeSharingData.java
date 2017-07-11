package com.dream.entity;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.dream.util.ReadXDTDataFile;
import com.dream.util.StockTool;
import com.dream.util.StringUtil;

/**
 * 分时数据
 */
public class StockTimeSharingData {
	public String stockCode;//
	public String date = "";//格式yyyyMMdd
	public short currentHour;
	public short currentMinute;
	public long lastVolumn;// 上一分钟到开盘的总成交量
	public Map<String, Stock1MinData> stock1MinDatas = new HashMap();
	
	public StockTimeSharingData(String date){
		this.date = date;
	}

	public boolean updateStock1MinData(StockData sd) {
		if(!StockTool.isTradeTime(sd.time))
			return false;
		
		short minute = StringUtil.getMinute(sd.time);
		short hour = StringUtil.getHour(sd.time);
		if (minute != currentMinute) {
			currentMinute = minute;
			currentHour = hour;
			Stock1MinData stock1MinData = new Stock1MinData();
			stock1MinData.hour = StringUtil.getHour(sd.time);
			stock1MinData.minute = minute;
			stock1MinData.price = sd.stockRealData.currentPrice;
			stock1MinData.volume = sd.stockRealData.volume - lastVolumn;
			lastVolumn = sd.stockRealData.volume;
			stock1MinDatas.put(StringUtil.getHourMinute(sd.time), stock1MinData);
			return true;
		}
		return false;

	}
	

	/**
	 * 从信达通的收盘数据中更新数据
	 * @param sd
	 * @return
	 * @throws FileNotFoundException 
	 */
	public void updateStock1MinDataFromXDTData(StockData sd) throws FileNotFoundException {
		StockTimeSharingData stockTimeSharingData = ReadXDTDataFile.getStock1MinData(sd.stockCode, date);
		stock1MinDatas.putAll(stockTimeSharingData.stock1MinDatas);
	}

}
