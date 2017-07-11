package com.dream.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	
	public static void jointStockCode(StringBuffer target, String stockCode){
		if(stockCode.length() != 6)
			return; 
		if(stockCode.charAt(0) == '6')
			target.append("sh");
		else
			target.append("sz");
		target.append(stockCode);
	}
	
	/**
	 * 获取分钟数
	 * @param time 格式 HH:mm:ss
	 * @return
	 */
	public static short getMinute(String time){
		if(time == null || time.length() != 8)
			return 0;
		return Short.parseShort(time.substring(3, 5));
	}

	/**
	 * 获取小时数
	 * @param time 格式 HH:mm:ss
	 * @return
	 */
	public static short getHour(String time){
		if(time == null || time.length() != 8)
			return 0;
		return Short.parseShort(time.substring(0, 2));
	}

	/**
	 * 获取小时数
	 * @param time 格式 HH:mm:ss
	 * @return
	 */
	public static String getHourMinute(String time){
		if(time == null || time.length() != 8)
			return "";
		return time.substring(0, 2) + time.substring(3, 5);
	}
	/**
	 * 获取年份
	 * @param date 格式 YYYYMMDD
	 * @return
	 */
	public static short getYear(String date){
		if(date == null || date.length() != 8)
			return 0;
		return Short.parseShort(date.substring(0, 4));
	}

	/**
	 * 获取年份
	 * @param date 格式 YYYYMMDD
	 * @return
	 */
	public static short getMonth(String date){
		if(date == null || date.length() != 8)
			return 0;
		return Short.parseShort(date.substring(4, 6));
	}

	/**
	 * 获取年份
	 * @param date 格式 YYYYMMDD
	 * @return
	 */
	public static short getDay(String date){
		if(date == null || date.length() != 8)
			return 0;
		return Short.parseShort(date.substring(6, 8));
	}
	
	public static String formatYYYYMMDD(Date date){
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		   String dateString = formatter.format(date);
		   return dateString;
	}
}
