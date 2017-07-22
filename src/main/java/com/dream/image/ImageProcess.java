package com.dream.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.data.Range;
import org.jfree.data.time.Minute;
import org.jstockchart.JStockChartFactory;
import org.jstockchart.area.PriceArea;
import org.jstockchart.area.TimeseriesArea;
import org.jstockchart.area.VolumeArea;
import org.jstockchart.axis.TickAlignment;
import org.jstockchart.axis.logic.CentralValueAxis;
import org.jstockchart.axis.logic.LogicDateAxis;
import org.jstockchart.axis.logic.LogicNumberAxis;
import org.jstockchart.dataset.TimeseriesDataset;
import org.jstockchart.demo.data.DataFactory;
import org.jstockchart.model.TimeseriesItem;
import org.jstockchart.util.DateUtils;

import com.dream.entity.StockRealData;
import com.dream.entity.StockTimeSharingData;

public class ImageProcess {

	public static BufferedImage getStockImageFromTimeSharing(StockTimeSharingData stockTimeSharingData,
			StockRealData stockRealData) {
		if (stockTimeSharingData.stock1MinDatas.size() == 0)
			return null;
		if (stockTimeSharingData.stock1MinDatas.get("0931") == null)// 如果数据不是从931开始的，不生成图像数据
			return null;
		DataFactory dataFactory = new DataFactory();
		// 'data' is a list of TimeseriesItem instances.
		List<TimeseriesItem> data = dataFactory.getTimeseriesItem(stockTimeSharingData);

		// the 'timeline' indicates the segmented time range '00:00-11:30,
		// 13:00-24:00'.
		SegmentedTimeline timeline = new SegmentedTimeline(SegmentedTimeline.MINUTE_SEGMENT_SIZE, 1351, 89);
		timeline.setStartTime(SegmentedTimeline.firstMondayAfter1900() + 780 * SegmentedTimeline.MINUTE_SEGMENT_SIZE);
		// timeline.setStartTime(SegmentedTimeline.firstMondayAfter1900()+780 *
		// SegmentedTimeline.MINUTE_SEGMENT_SIZE);

		// Creates timeseries data set.
		TimeseriesDataset dataset = new TimeseriesDataset(Minute.class, 1, timeline, true);
		dataset.addDataItems(data);

		// Creates logic price axis.
		CentralValueAxis logicPriceAxis = new CentralValueAxis(stockRealData.lastClosePrice,
				new Range(dataset.getMinPrice().doubleValue(), dataset.getMaxPrice().doubleValue()), 9,
				new DecimalFormat(".00"));
		PriceArea priceArea = new PriceArea(logicPriceAxis);
		priceArea.setPriceColor(Color.black);

		// Creates logic volume axis.
		LogicNumberAxis logicVolumeAxis = new LogicNumberAxis(
				new Range(dataset.getMinVolume().doubleValue(), dataset.getMaxVolume().doubleValue()), 5,
				new DecimalFormat("0"));
		VolumeArea volumeArea = new VolumeArea(logicVolumeAxis);
		volumeArea.setVolumeColor(Color.black);

		TimeseriesArea timeseriesArea = new TimeseriesArea(priceArea, volumeArea,
				createlogicDateAxis(DateUtils.createDate(2008, 1, 1)));

		JFreeChart jfreechart = JStockChartFactory.createTimeseriesChart("", dataset, timeline, timeseriesArea, false);

		String imageFile = "E:/temp/" + stockTimeSharingData.stockCode + stockTimeSharingData.currentHour
				+ stockTimeSharingData.currentMinute + ".jpg";
		try {
			// if(stockTimeSharingData.stockCode.equals("603799"))
			ChartUtilities.saveChartAsPNG(new File(imageFile), jfreechart, 545, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jfreechart.createBufferedImage(545, 300);

	}

	// Specifies date axis ticks.
	private static LogicDateAxis createlogicDateAxis(Date baseDate) {
		LogicDateAxis logicDateAxis = new LogicDateAxis(baseDate, new SimpleDateFormat("HH:mm"));
		logicDateAxis.addDateTick("09:30", TickAlignment.START);
		logicDateAxis.addDateTick("10:00");
		logicDateAxis.addDateTick("10:30");
		logicDateAxis.addDateTick("11:00");
		// logicDateAxis.addDateTick("11:30", TickAlignment.END);
		// logicDateAxis.addDateTick("13:00", TickAlignment.START);
		logicDateAxis.addDateTick("13:00");
		logicDateAxis.addDateTick("13:30");
		logicDateAxis.addDateTick("14:00");
		logicDateAxis.addDateTick("14:30");
		logicDateAxis.addDateTick("15:00", TickAlignment.END);
		return logicDateAxis;
	}

	// 全流程
	public static void main(String[] args) throws IOException {

		BufferedImage bi = ImageIO.read(new File("E:/temp/600973131.jpg"));
		BufferedImage bi1 = ImageIO.read(new File("E:/temp/600990131.jpg"));
		int[] pixels = ImageProcess.getPixel(bi);
		// 获取两个图的汉明距离（假设另一个图也已经按上面步骤得到灰度比较数组）
		int[] pixels2 = ImageProcess.getPixel(bi1);
		int hammingDistance = ImageProcess.getHammingDistance(pixels, pixels2);
		// 通过汉明距离计算相似度，取值范围 [0.0, 1.0]
		double similarity = ImageProcess.calSimilarity(hammingDistance);

		System.out.println(similarity);
	}



	public static int[] getPixel(BufferedImage bi) {
		// 转换至灰度
		Image image = toGrayscale(bi);
		// 缩小成32x32的缩略图
		image = scale(image);
		File file = new File("E:/"+bi.hashCode()+".jpg");
		try {
			ImageIO.write(convertToBufferedFrom(image), "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 获取灰度像素数组
		int[] pixels = getPixels(image);
		// 获取平均灰度颜色
		int averageColor = getAverageOfPixelArray(pixels);
		// 获取灰度像素的比较数组（即图像指纹序列）
		pixels = getPixelDeviateWeightsArray(pixels, averageColor);
		return pixels;
	}

	// 将任意Image类型图像转换为BufferedImage类型，方便后续操作
	public static BufferedImage convertToBufferedFrom(Image srcImage) {
		BufferedImage bufferedImage = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(srcImage, null, null);
		g.dispose();
		return bufferedImage;
	}

	// 转换至灰度图
	public static BufferedImage toGrayscale(BufferedImage sourceBuffered) {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		BufferedImage grayBuffered = op.filter(sourceBuffered, null);
		return grayBuffered;
	}

	// 缩放至32x32像素缩略图
	public static Image scale(Image image) {
		int scale = 128;
		image = image.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
		return image;
	}

	// 获取像素数组
	public static int[] getPixels(Image image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int[] pixels = convertToBufferedFrom(image).getRGB(0, 0, width, height, null, 0, width);
		return pixels;
	}

	// 获取灰度图的平均像素颜色值
	public static int getAverageOfPixelArray(int[] pixels) {
		Color color;
		long sumRed = 0;
		for (int i = 0; i < pixels.length; i++) {
			color = new Color(pixels[i], true);
			sumRed += color.getRed();
		}
		int averageRed = (int) (sumRed / pixels.length);
		return averageRed;
	}

	// 获取灰度图的像素比较数组（平均值的离差）
	public static int[] getPixelDeviateWeightsArray(int[] pixels, final int averageColor) {
		Color color;
		int[] dest = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			color = new Color(pixels[i], true);
			dest[i] = color.getRed() - averageColor > 0 ? 1 : 0;
		}
		return dest;
	}

	// 获取两个缩略图的平均像素比较数组的汉明距离（距离越大差异越大）
	public static int getHammingDistance(int[] a, int[] b) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i] == b[i] ? 0 : 1;
		}
		return sum;
	}

	// 通过汉明距离计算相似度
	public static double calSimilarity(int hammingDistance) {
		int length = 32 * 32;
		double similarity = (length - hammingDistance) / (double) length;

		// 使用指数曲线调整相似度结果
		similarity = java.lang.Math.pow(similarity, 2);
		return similarity;
	}
}
