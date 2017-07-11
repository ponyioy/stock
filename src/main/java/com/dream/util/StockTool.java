package com.dream.util;

public class StockTool {
	public static boolean isTradeTime(String time) {
		short tempTime = Short.parseShort(time.substring(0, 2) + time.substring(3, 5));
		if (tempTime >= Constant.START_TIME && tempTime <= Constant.END_TIME)
			return true;
		return false;
	}
}
