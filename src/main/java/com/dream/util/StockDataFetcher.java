package com.dream.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.dream.entity.StockData;
import com.dream.exception.StockDataException;


public class StockDataFetcher {
    public static final String URL_PREMIX = "http://hq.sinajs.cn/list=";
    public static final String URL_MIN_PREMIX = "http://image.sinajs.cn/newchart/min/n/";
    public static final String URL_DAILY_PREMIX = "http://image.sinajs.cn/newchart/daily/n/";
    public static final String URL_WEEKLY_PREMIX = "http://image.sinajs.cn/newchart/weekly/n/";
    public static final String URL_MONTHLY_PREMIX = "http://image.sinajs.cn/newchart/monthly/n/";
    public static final String URL_IMAGE_SUFFIX = ".gif";

    public static List<StockData> fetchStockData(List<String> stockCodes) throws StockDataException {
        StringBuffer url = new StringBuffer(URL_PREMIX);
        for (int i = 0; i < stockCodes.size(); i++) {
//			if(stockCodes.get(i).charAt(0) == '6')
//				url.append("sh");
//			else
//				url.append("sz");
//			url.append(stockCodes.get(i));
            StringUtil.jointStockCode(url, stockCodes.get(i));
            if (i != stockCodes.size() - 1)
                url.append(",");
        }
        String respond = HttpAgent.sendPost(url.toString(), "");
        return analyseRespondData(stockCodes, respond);
    }

    ;


    private static List<StockData> analyseRespondData(List<String> stockCodes, String information) throws StockDataException {
        if (information == null || information.length() == 0)
            return null;
        List<StockData> stockDatas = new ArrayList();
        String[] orgResult = information.split("=");
        if (stockCodes.size() != orgResult.length - 1) {
            throw new StockDataException();
        }

        for (int i = 0; i < orgResult.length - 1; i++) {
            String[] result = orgResult[i + 1].substring(orgResult[i + 1].indexOf('"') + 1, orgResult[i + 1].lastIndexOf('"')).split(",");
            StockData sd = new StockData();
            if (result.length == 33) {
                sd.stockCode = stockCodes.get(i);
                sd.stockName = result[0];
                /**
                 * 0：”大秦铁路”，股票名字；
                 1：”27.55″，今日开盘价；
                 2：”27.25″，昨日收盘价；
                 3：”26.91″，当前价格；
                 4：”27.55″，今日最高价；
                 5：”26.20″，今日最低价；
                 6：”26.91″，竞买价，即“买一”报价；
                 7：”26.92″，竞卖价，即“卖一”报价；
                 8：”22114263″，成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
                 9：”589824680″，成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
                 10：”4695″，“买一”申请4695股，即47手；
                 11：”26.91″，“买一”报价；
                 12：”57590″，“买二”
                 13：”26.90″，“买二”
                 14：”14700″，“买三”
                 15：”26.89″，“买三”
                 16：”14300″，“买四”
                 17：”26.88″，“买四”
                 18：”15100″，“买五”
                 19：”26.87″，“买五”
                 20：”3100″，“卖一”申报3100股，即31手；
                 21：”26.92″，“卖一”报价
                 (22, 23), (24, 25), (26,27), (28, 29)分别为“卖二”至“卖四的情况”
                 30：”2008-01-11″，日期；
                 31：”15:05:32″，时间；
                 */
                sd.stockRealData.openPrice = Float.parseFloat(result[1]);
                sd.stockRealData.lastClosePrice = Float.parseFloat(result[2]);
                sd.stockRealData.currentPrice = Float.parseFloat(result[3]);
                sd.stockRealData.highestPrice = Float.parseFloat(result[4]);
                sd.stockRealData.lowestPrice = Float.parseFloat(result[5]);
                sd.stockRealData.volume = Long.parseLong(result[8]);
                sd.stockRealData.turnover = Float.parseFloat(result[9]);

                for (int j = 0; j < 5; j++) {
                    sd.stockHandicapData.buy[j] = Long.parseLong(result[10 + j * 2]) / 100;
                    sd.stockHandicapData.buyPrice[j] = Float.parseFloat(result[11 + j * 2]);
                    sd.stockHandicapData.sell[j] = Long.parseLong(result[20 + j * 2]) / 100;
                    sd.stockHandicapData.sellPrice[j] = Float.parseFloat(result[21 + j * 2]);
                }

                sd.date = result[30];
                sd.time = result[31];
            }

            stockDatas.add(sd);
        }
        return stockDatas;
    }

    public static byte[] getStockMinImage(String stockCode) {
        return getStockImage(URL_MIN_PREMIX, stockCode);
    }

    public static byte[] getStockDailyImage(String stockCode) {
        return getStockImage(URL_DAILY_PREMIX, stockCode);
    }

    public static byte[] getStockWeeklyImage(String stockCode) {
        return getStockImage(URL_WEEKLY_PREMIX, stockCode);
    }

    public static byte[] getStockMonthlyImage(String stockCode) {
        return getStockImage(URL_MONTHLY_PREMIX, stockCode);
    }

    private static byte[] getStockImage(String premix, String stockCode) {
        StringBuffer url = new StringBuffer(premix);
        StringUtil.jointStockCode(url, stockCode);
        url.append(URL_IMAGE_SUFFIX);
        ByteArrayOutputStream baos;
        try {
            baos = HttpAgent.downloadNet(url.toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return baos.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        byte[] b = StockDataFetcher.getStockDailyImage("000856");
        FileOutputStream out = new FileOutputStream("E:/1.gif", true);
        System.out.println(new String(b));
        out.write(b, 0, b.length);
//		ArrayList<String> sd = new ArrayList<String>();
//		sd.add("000856");
//		sd.add("000413");
//		try {
//			List<StockData> result = StockDataFetcher.fetchStockData(sd);
//			System.out.println("helloworld");
//		} catch (StockDataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }

}


