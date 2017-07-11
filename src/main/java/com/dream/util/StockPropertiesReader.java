package com.dream.util;

import com.alibaba.fastjson.JSON;
import com.dream.entity.StockWatchData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StockPropertiesReader {
    private static final String STOCK_POOL_FILE_NAME = "resources/stockPool.txt";
    private static final String WATCH_MODEL_FILE_NAME = "resources/watchModel.txt";

    public static List<String> readStockPool() {
        List<String> stockPool = new ArrayList<String>();
        BufferedReader reader = null;
        try {

            URL url = (new StockPropertiesReader()).getClass().getResource(STOCK_POOL_FILE_NAME);
            InputStream is = url.openStream();
            InputStreamReader ir = new InputStreamReader(is, "UTF-8");
            // URL url =(new
            // StockPropertiesReader()).getClass().getClassLoader().getResourceAsStream(STOCK_POOL_FILE_NAME);
            // InputStream inputStream = (InputStream) (;
            // InputStreamReader ir = new InputStreamReader(inputStream);
            reader = new BufferedReader(ir);
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                stockPool.add(tempString);
                // System.out.println("line " + line + ": " + tempString);
                // line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return stockPool;
    }

    public static List<StockWatchData> readStockWatchModel() {

        List<StockWatchData> stockWatchModels = new ArrayList();
        BufferedReader reader = null;
        try {
            URL url = (new StockPropertiesReader()).getClass().getResource(WATCH_MODEL_FILE_NAME);
            InputStream is = url.openStream();
            InputStreamReader ir = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(ir);
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String[] tempString1 = tempString.split(" ");
                String stockCode = tempString1[0];
                String date = tempString1[1];
                StockWatchData swm = new StockWatchData();
                swm.stockCode = stockCode;
                swm.stockRealData = ReadXDTDataFile.getStockDayData(stockCode, date);
                swm.stockTimeSharingData = ReadXDTDataFile.getStock1MinData(stockCode, date);
                
                stockWatchModels.add(swm);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return stockWatchModels;
    }
}
