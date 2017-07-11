package com.dream.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.dream.entity.Stock1MinData;
import com.dream.entity.StockRealData;
import com.dream.entity.StockTimeSharingData;

/**
 * 读取信达通的数据
 */
public class ReadXDTDataFile {
	public static String LC1_FILE_SZ_PATH = "C:/zd_gfzq/vipdoc/sz/minline/";
	public static String LC1_FILE_SH_PATH = "C:/zd_gfzq/vipdoc/sh/minline/";
	public static String DAY_FILE_SZ_PATH = "C:/zd_gfzq/vipdoc/sz/lday/";
	public static String DAY_FILE_SH_PATH = "C:/zd_gfzq/vipdoc/sh/lday/";
	public static String LC1_SUFFIX = ".lc1";
	public static String DAY_SUFFIX = ".day";

	/**
	 *
	 * 一、通达信日线*.day文件 文件名即股票代码 每32个字节为一天数据 每4个字节为一个字段，每个字段内低字节在前 00 ~ 03 字节：年月日,
	 * 整型 04 ~ 07 字节：开盘价*100， 整型 08 ~ 11 字节：最高价*100, 整型 12 ~ 15 字节：最低价*100, 整型
	 * 16 ~ 19 字节：收盘价*100, 整型 20 ~ 23 字节：成交额（元），float型 24 ~ 27 字节：成交量（股），整型 28 ~
	 * 31 字节：上日收盘*100, 整型
	 * 
	 * 
	 * 二、通达信5分钟线*.lc5文件和*.lc1文件 文件名即股票代码 每32个字节为一个5分钟数据，每字段内低字节在前 00 ~ 01
	 * 字节：日期，整型，设其值为num，则日期计算方法为： year=floor(num/2048)+2004;
	 * month=floor(mod(num,2048)/100); day=mod(mod(num,2048),100); 02 ~ 03 字节：
	 * 从0点开始至目前的分钟数，整型 04 ~ 07 字节：开盘价，float型 08 ~ 11 字节：最高价，float型 12 ~ 15
	 * 字节：最低价，float型 16 ~ 19 字节：收盘价，float型 20 ~ 23 字节：成交额，float型 24 ~ 27
	 * 字节：成交量（股），整型 28 ~ 31 字节：（保留）
	 */

	/**
	 * 以字节为单位读取文件，读取信达通的日线数据源。 返回某一天的日数据 如果找不到某一天的数据，则返回null
	 *
	 * @param date
	 *            格式YYYYMMDD
	 */
	public static StockRealData getStockDayData(String stockCode, String date) throws FileNotFoundException {
		String fileName;
		if (stockCode.charAt(0) == '6')
			fileName = DAY_FILE_SH_PATH + "sh" + stockCode + DAY_SUFFIX;
		else
			fileName = DAY_FILE_SZ_PATH + "sz" + stockCode + DAY_SUFFIX;
		return readDayFileByBytes(fileName, date);
	}

	/**
	 * 以字节为单位读取文件，读取信达通的日线数据源。 返回某一天的日数据 如果找不到某一天的数据，则返回null
	 *
	 * @param date
	 *            格式YYYYMMDD
	 */
	public static StockRealData readDayFileByBytes(String fileName, String date) throws FileNotFoundException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		InputStream in = null;

		try {
			// 一次读多个字节
			byte[] tempBytes = new byte[32];
			int byteread = 0;
			in = new FileInputStream(fileName);
			int temp = Integer.parseInt(date);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempBytes)) != -1) {
				if (temp == bytesToInt(tempBytes, 0)) {
					StockRealData sdd = new StockRealData();
					sdd.openPrice = bytesToInt(tempBytes, 4) / 100;
					sdd.closePrice = bytesToInt(tempBytes, 16) / 100;
					sdd.highestPrice = bytesToInt(tempBytes, 8) / 100;
					sdd.lowestPrice = bytesToInt(tempBytes, 12) / 100;
					sdd.turnover = byte2float(tempBytes, 20);
					sdd.volume = bytesToInt(tempBytes, 24);
					sdd.lastClosePrice = bytesToInt(tempBytes, 28) / 100;
					return sdd;

				}

				// System.out.println(num +" "+ bytesToInt(tempBytes, 4));
			}
			return null;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}

		return null;
	}

	/**
	 * 以字节为单位读取文件，读取信达通的1分钟线数据源。 返回某一天的1分钟数据 如果找不到某一天的数据，则返回null
	 *
	 * @param date
	 *            格式YYYYMMDD
	 */
	public static StockTimeSharingData getStock1MinData(String stockCode, String date) throws FileNotFoundException {
		String fileName;
		if (stockCode.charAt(0) == '6')
			fileName = LC1_FILE_SH_PATH + "sh" + stockCode + LC1_SUFFIX;
		else
			fileName = LC1_FILE_SZ_PATH + "sz" + stockCode + LC1_SUFFIX;
		return readMinFileByBytes(fileName, date);
	}

	/**
	 * 以字节为单位读取文件，读取信达通的分时数据源。
	 *
	 * @param date
	 *            格式yyyymmdd
	 */
	public static StockTimeSharingData readMinFileByBytes(String fileName, String date) throws FileNotFoundException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		InputStream in = null;

		if (date.length() != 8) {
			throw new NumberFormatException();
		}

		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));

		try {
			// 一次读多个字节
			byte[] tempBytes = new byte[32];
			byte[] skipBytes = new byte[239 * 32];
			int byteread = 0;
			in = new FileInputStream(fileName);
			StockTimeSharingData stockTimeSharingData = new StockTimeSharingData(date);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempBytes)) != -1) {

				int num = bytesToInt(tempBytes, 0);

				int mins = (num >> 16);
				int mds = num & 0xffff;
				int hour = (mins / 60);
				int minute = mins % 60;

				int year1 = (int) Math.floor(mds / 2048) + 2004;
				int month1 = (int) Math.floor(Math.floorMod(mds, 2048) / 100);
				int day1 = Math.floorMod(Math.floorMod(mds, 2048), 100);

				// System.out.println(year1 + " " + month1 + " " + day1);

				if (year == year1 && month == month1 && day == day1) {
					Stock1MinData smd = new Stock1MinData();
					smd.hour = (short) hour;
					smd.minute = (short) minute;
					smd.price = byte2float(tempBytes, 16);
					smd.volume = bytesToInt(tempBytes, 24);
					stockTimeSharingData.stock1MinDatas.put(String.format("%04d", hour * 100 + minute), smd);

					// System.out.println(hour + ":" + minute + " " +
					// smd.price);
				} else {
					in.read(skipBytes);// 跳过239个32byte，直接到下一个交易日数据
				}

				// System.out.println(year);
				// System.out.println(month);
				// System.out.println(day);
				// System.out.println(hour);
				// System.out.println(minute);
			}
			if (stockTimeSharingData.stock1MinDatas.size() != 0)
				return stockTimeSharingData;
			else
				return null;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
		return null;
	}

	/**
	 * 字节转换为浮点
	 *
	 * @param b
	 *            字节（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 */
	public static float byte2float(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
	 *
	 * @param ary
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] ary, int offset) {
		int value;
		value = (int) ((ary[offset] & 0xFF) | ((ary[offset + 1] << 8) & 0xFF00) | ((ary[offset + 2] << 16) & 0xFF0000)
				| ((ary[offset + 3] << 24) & 0xFF000000));
		return value;
	}

	public static void main(String[] args) throws FileNotFoundException {
		getStock1MinData("000877", "20170705");
	}
}
