package com.dream.watch;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.dream.entity.StockRealData;
import com.dream.entity.StockTimeSharingData;
import com.dream.entity.StockWatchData;
import com.dream.image.ImageProcess;
import com.dream.util.StockPropertiesReader;

//分时监控
public class TimeSharingWatch {
	private static boolean isInit = false;
	private static JTable table;
	private static List<StockWatchData> stockWatchModels;

	public static void init(JTable table) {
		if (!isInit) {
			stockWatchModels = StockPropertiesReader.readStockWatchModel();
			TimeSharingWatch.table = table;
			isInit = true;
		}
	}

	public static void doWatch(StockTimeSharingData stockTimeSharingData, StockRealData stockRealData) {
		short hour = stockTimeSharingData.currentHour;
		short minute = stockTimeSharingData.currentMinute;
		BufferedImage bi = ImageProcess.getStockImageFromTimeSharing(stockTimeSharingData, stockRealData);
		int[] pixels = ImageProcess.getPixel(bi);
		for (int i = 0; i < stockWatchModels.size(); i++) {
			stockWatchModels.get(i).stockTimeSharingData.currentHour = hour;
			stockWatchModels.get(i).stockTimeSharingData.currentMinute = minute;
			BufferedImage bi2compare = ImageProcess.getStockImageFromTimeSharing(
					stockWatchModels.get(i).stockTimeSharingData, stockWatchModels.get(i).stockRealData);
			// 获取两个图的汉明距离（假设另一个图也已经按上面步骤得到灰度比较数组）
			int[] pixels2 = ImageProcess.getPixel(bi2compare);
		    int hammingDistance = ImageProcess.getHammingDistance(pixels, pixels2);
		    // 通过汉明距离计算相似度，取值范围 [0.0, 1.0]
		    double similarity = ImageProcess.calSimilarity(hammingDistance);
		    if(similarity>0.9){
		    	DefaultTableModel tm = (DefaultTableModel)TimeSharingWatch.table.getModel();
		    	tm.addRow(new Object[]{stockTimeSharingData.stockCode  + ":" + similarity});
		    	TimeSharingWatch.table.repaint();
		    }
		    System.out.println(stockTimeSharingData.stockCode + " vs " + stockWatchModels.get(i).stockCode + ":" + similarity);
		}

	}

	// private StockTimeSharingData getModel(){
	//
	// }

}
