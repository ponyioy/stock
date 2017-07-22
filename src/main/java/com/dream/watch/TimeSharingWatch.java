package com.dream.watch;

import java.util.List;

import com.dream.entity.StockRealData;
import com.dream.entity.StockTimeSharingData;
import com.dream.entity.StockWatchData;
import com.dream.handle.ViewHandle;
import com.dream.image.CurveSimilarityCompute;
import com.dream.util.StockPropertiesReader;

//分时监控
public class TimeSharingWatch {
	private static boolean isInit = false;
	private static List<StockWatchData> stockWatchModels;
	private static ViewHandle viewHandle;

	public static void init(ViewHandle viewHandle) {
		if (!isInit) {
			stockWatchModels = StockPropertiesReader.readStockWatchModel();
			TimeSharingWatch.viewHandle = viewHandle;
			isInit = true;
		}
	}

	public static void doWatch(StockTimeSharingData stockTimeSharingData, StockRealData stockRealData) {
		// short hour = stockTimeSharingData.currentHour;
		// short minute = stockTimeSharingData.currentMinute;
		// BufferedImage bi =
		// ImageProcess.getStockImageFromTimeSharing(stockTimeSharingData,
		// stockRealData);
		// if(bi == null)
		// return;
		// int[] pixels = ImageProcess.getPixel(bi);
		for (int i = 0; i < stockWatchModels.size(); i++) {
			// stockWatchModels.get(i).stockTimeSharingData.currentHour = hour;
			// stockWatchModels.get(i).stockTimeSharingData.currentMinute =
			// minute;
			// BufferedImage bi2compare =
			// ImageProcess.getStockImageFromTimeSharing(
			// stockWatchModels.get(i).stockTimeSharingData,
			// stockWatchModels.get(i).stockRealData);
			// // 获取两个图的汉明距离（假设另一个图也已经按上面步骤得到灰度比较数组）
			// int[] pixels2 = ImageProcess.getPixel(bi2compare);
			// int hammingDistance = ImageProcess.getHammingDistance(pixels,
			// pixels2);
			// // 通过汉明距离计算相似度，取值范围 [0.0, 1.0]
			// double similarity = ImageProcess.calSimilarity(hammingDistance);
			float similarity = CurveSimilarityCompute.computeSimilarity(stockTimeSharingData,
					stockWatchModels.get(i).stockTimeSharingData);
			if (similarity > 0.85) {
				viewHandle.doStockWatchModelUpdate(stockTimeSharingData.stockCode,
						Integer.toString(stockTimeSharingData.currentHour * 100 + stockTimeSharingData.currentMinute), similarity);
//				System.out.println(
//						stockTimeSharingData.stockCode + " vs " + stockWatchModels.get(i).stockCode + ":" + similarity);
			}
		}

	}

	// private StockTimeSharingData getModel(){
	//
	// }

}
