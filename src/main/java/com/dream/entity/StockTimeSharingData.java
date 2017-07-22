package com.dream.entity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dream.util.ReadXDTDataFile;
import com.dream.util.StockTool;
import com.dream.util.StringUtil;

/**
 * 分时数据
 */
public class StockTimeSharingData {
	public String stockCode;//
	public String date = "";// 格式yyyyMMdd
	public short currentHour;
	public short currentMinute;
	public long lastVolumn = 0;// 上一分钟到开盘的总成交量
	public float lastDayClosePrice;//上日收盘价
	public Map<String, Stock1MinData> stock1MinDatas = new LinkedHashMap();

	public StockTimeSharingData(String date) {
		Calendar time = Calendar.getInstance();
		this.date = date;
		this.currentHour = (short) time.get(Calendar.HOUR_OF_DAY);
		this.currentMinute = (short) time.get(Calendar.MINUTE);
	}

	public boolean updateStock1MinData(StockData sd) {
		if (!StockTool.isTradeTime(sd.time))
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
			if(lastVolumn == 0)//如果数据采集是从盘中开始的，则接受到的第一笔数据的成交量，按0计算成交量，避免过分失真
				stock1MinData.volume = 0;
			else
				stock1MinData.volume = sd.stockRealData.volume - lastVolumn;
			lastVolumn = sd.stockRealData.volume;
			stock1MinDatas.put(StringUtil.getHourMinute(sd.time), stock1MinData);
			lastDayClosePrice = sd.stockRealData.lastClosePrice;

			return true;
		}
		return false;

	}

	/**
	 * 从信达通的收盘数据中更新数据
	 * 
	 * @param sd
	 * @return
	 * @throws FileNotFoundException
	 */
	public void updateStock1MinDataFromXDTData() throws FileNotFoundException {
		StockTimeSharingData stockTimeSharingData = ReadXDTDataFile.getStock1MinData(stockCode, date);
		if(stockTimeSharingData == null)
			return;
			
		stock1MinDatas.putAll(stockTimeSharingData.stock1MinDatas);
		lastDayClosePrice = stockTimeSharingData.lastDayClosePrice;

		// 排序
		List<Map.Entry<String, Stock1MinData>> infoIds = new ArrayList<Map.Entry<String, Stock1MinData>>(
				stock1MinDatas.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Stock1MinData>>() {
			public int compare(Map.Entry<String, Stock1MinData> o1, Map.Entry<String, Stock1MinData> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
	}

}
