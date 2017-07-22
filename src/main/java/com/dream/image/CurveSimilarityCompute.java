package com.dream.image;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

import com.dream.entity.Stock1MinData;
import com.dream.entity.StockTimeSharingData;
import com.dream.util.ReadXDTDataFile;

public class CurveSimilarityCompute {

	public static float computeSimilarity(StockTimeSharingData stockTimeSharingData1,
			StockTimeSharingData stockTimeSharingData2) {
		if(stockTimeSharingData1.stock1MinDatas.get("0931") == null || stockTimeSharingData2.stock1MinDatas.get("0931") == null)
			return 0;
		
		float distanceDifference = getDistanceDifference(stockTimeSharingData1, stockTimeSharingData2);
		int count = stockTimeSharingData1.stock1MinDatas.size();
		float similarity = (float) (1 - Math.sqrt(distanceDifference / count) / 2);
		return similarity;
	}

	// 获取距离差
	public static float getDistanceDifference(StockTimeSharingData stockTimeSharingData1,
			StockTimeSharingData stockTimeSharingData2) {
		int size1 = stockTimeSharingData1.stock1MinDatas.size();// 目前计算的点个数
		int size2 = stockTimeSharingData2.stock1MinDatas.size();
		int size = size1 > size2 ? size2 :size1;
		
		float result = 0;
		// 绝对位置，范围是-1到1
		float[] absolutePosition1 = getAbsolutPosition(stockTimeSharingData1, size);
		float[] absolutePosition2 = getAbsolutPosition(stockTimeSharingData2, size);

		for (int i = 0; i < absolutePosition1.length; i++) {
			result += Math.pow(Math.abs(absolutePosition1[i] - absolutePosition2[i]), 2);
		}
		return result;
	}

	public static float[] getAbsolutPosition(StockTimeSharingData stockTimeSharingData, int size) {
		float[] absolutePosition = new float[size];
		Iterator iter = stockTimeSharingData.stock1MinDatas.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			if(i == size)
				break;
			Map.Entry entry = (Map.Entry) iter.next();
			Stock1MinData stock1MinData = (Stock1MinData) entry.getValue();
			absolutePosition[i++] = (stock1MinData.price - stockTimeSharingData.lastDayClosePrice) * 10
					/ stockTimeSharingData.lastDayClosePrice;
		}
		return absolutePosition;
	}

	public static void main(String[] args) throws FileNotFoundException {

		StockTimeSharingData stockTimeSharingData1 = ReadXDTDataFile.getStock1MinData("000877", "20170703");
		StockTimeSharingData stockTimeSharingData2 = ReadXDTDataFile.getStock1MinData("300140", "20170713");
		System.out.println(stockTimeSharingData2.lastDayClosePrice);
		float result = CurveSimilarityCompute.computeSimilarity(stockTimeSharingData1, stockTimeSharingData2);
		System.out.println(result);
	}
}
