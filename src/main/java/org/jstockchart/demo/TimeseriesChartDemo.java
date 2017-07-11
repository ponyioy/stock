/* ===========================================================
 * JStockChart : an extension of JFreeChart for financial market
 * ===========================================================
 *
 * Copyright (C) 2009, by Sha Jiang.
 *
 * Project Info:  http://code.google.com/p/jstockchart
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 */

package org.jstockchart.demo;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.title.TextTitle;
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

/**
 * Demo application for JStockChart timeseries.
 * 
 * @author Sha Jiang
 */
public class TimeseriesChartDemo {

	// Specifies date axis ticks.
	private static LogicDateAxis createlogicDateAxis(Date baseDate) {
		LogicDateAxis logicDateAxis = new LogicDateAxis(baseDate, new SimpleDateFormat("HH:mm"));
		logicDateAxis.addDateTick("09:30", TickAlignment.START);
		logicDateAxis.addDateTick("10:00");
		logicDateAxis.addDateTick("10:30");
		logicDateAxis.addDateTick("11:00");
//		logicDateAxis.addDateTick("11:30", TickAlignment.END);
//		logicDateAxis.addDateTick("13:00", TickAlignment.START);
		logicDateAxis.addDateTick("13:00");
		logicDateAxis.addDateTick("13:30");
		logicDateAxis.addDateTick("14:00");
		logicDateAxis.addDateTick("14:30");
		logicDateAxis.addDateTick("15:00", TickAlignment.END);
		return logicDateAxis;
	}

//	public static void main(String[] args) throws IOException {
//		String imageDir = "./images";
//		File images = new File(imageDir);
//		if (!images.exists()) {
//			images.mkdir();
//		}
//		String imageFile = imageDir + "/jstockchart-timeseries.png";
//
//		String dbFile = "db/jstockchart-timeseries.db4o";
//		String dataFile = "db/SH603133.txt";
//		
//		boolean debug = false;
//		
//		DataFactory dataFactory2 = new DataFactory(dataFile);
//		Date startTime = DateUtils.createDate(2008, 1, 1, 9, 30, 0);
//		Date endTime = DateUtils.createDate(2008, 1, 1, 15, 0, 0);
//		// 'data' is a list of TimeseriesItem instances.
////		List<TimeseriesItem> data = dataFactory.getTimeseriesItem(startTime, endTime, Calendar.MINUTE, 1);
//		List<TimeseriesItem> data;
//			data = dataFactory2.getTimeseriesItem();
//
//		// the 'timeline' indicates the segmented time range '00:00-11:30,
//		// 13:00-24:00'.
//		SegmentedTimeline timeline = new SegmentedTimeline(SegmentedTimeline.MINUTE_SEGMENT_SIZE, 1351, 89);
//		timeline.setStartTime(SegmentedTimeline.firstMondayAfter1900() + 780 * SegmentedTimeline.MINUTE_SEGMENT_SIZE);
////		System.out.println(SegmentedTimeline.firstMondayAfter1900() + 780 * SegmentedTimeline.MINUTE_SEGMENT_SIZE);
//		timeline.setStartTime(SegmentedTimeline.firstMondayAfter1900()+780 * SegmentedTimeline.MINUTE_SEGMENT_SIZE);
//
//		// Creates timeseries data set.
//		TimeseriesDataset dataset = new TimeseriesDataset(Minute.class, 1, timeline, true);
//		dataset.addDataItems(data);
//
//		// Creates logic price axis.
//		CentralValueAxis logicPriceAxis = new CentralValueAxis(new Double("33.89"),
//				new Range(dataset.getMinPrice().doubleValue(), dataset.getMaxPrice().doubleValue()), 9,
//				new DecimalFormat(".00"));
//		PriceArea priceArea = new PriceArea(logicPriceAxis);
//		priceArea.setPriceColor(Color.blue);
//		
//		// Creates logic volume axis.
//		LogicNumberAxis logicVolumeAxis = new LogicNumberAxis(
//				new Range(dataset.getMinVolume().doubleValue(), dataset.getMaxVolume().doubleValue()), 5,
//				new DecimalFormat("0"));
//		VolumeArea volumeArea = new VolumeArea(logicVolumeAxis);
//		volumeArea.setVolumeColor(Color.gray);
//
//		TimeseriesArea timeseriesArea = new TimeseriesArea(priceArea, volumeArea,
//				createlogicDateAxis(DateUtils.createDate(2008, 1, 1)));
//
//		JFreeChart jfreechart = JStockChartFactory.createTimeseriesChart("", dataset, timeline,
//				timeseriesArea, false);
//		String title = "sh603133       碳元科技           - 价格        -均线                                      2017-07-05   15:00:00";
//		TextTitle tt = new TextTitle(title, new Font("VeriBest Gerber 0", Font.PLAIN, 11));
//		jfreechart.setTitle(tt);
//		ChartUtilities.saveChartAsPNG(new File(imageFile), jfreechart, 545, 300);
//	}

}
