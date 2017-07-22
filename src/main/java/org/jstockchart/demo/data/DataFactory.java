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

package org.jstockchart.demo.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jstockchart.model.TimeseriesItem;

import com.alibaba.fastjson.JSON;
import com.dream.entity.Stock1MinData;
import com.dream.entity.StockTimeSharingData;
import com.dream.util.StringUtil;

/**
 * Queries data from db4o data file.
 * 
 * @author Sha Jiang
 */
public class DataFactory {

	private Calendar calendar = Calendar.getInstance();

	public DataFactory() {
	}

	/**
	 * Queries data.
	 * 
	 * @param startTime
	 *            data starting time.
	 * @param endTime
	 *            data ending time.
	 * @param step
	 *            time step.
	 * @param type
	 *            time type(Calendar.MINUTE, Calendar.SECOND, ...).
	 * @return <code>TimeseriesItem<code> list;
	 */
	public List<TimeseriesItem> getTimeseriesItem(StockTimeSharingData stockTimeSharingData) {
		List<TimeseriesItem> result = new ArrayList();
		Map<String, Stock1MinData> stock1MinDatas = stockTimeSharingData.stock1MinDatas;
		String strDate = stockTimeSharingData.date;
		Iterator iter = stock1MinDatas.entrySet().iterator();
		Calendar calendar = Calendar.getInstance();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Stock1MinData stock1MinData = (Stock1MinData) entry.getValue();
			int hour = stock1MinData.hour;
			int minute = stock1MinData.minute;

			if (hour * 100 + minute <= stockTimeSharingData.currentHour * 100 + stockTimeSharingData.currentMinute) {
				// Date date = new Date(StringUtil.getYear(strDate),
				// StringUtil.getYear(strDate),
				// StringUtil.getYear(strDate), hour, minute);
				calendar.set(StringUtil.getYear(strDate), StringUtil.getMonth(strDate), StringUtil.getDay(strDate),
						hour, minute);
				TimeseriesItem t = new TimeseriesItem(calendar.getTime(), stock1MinData.price,
						(double) stock1MinData.volume);
				result.add(t);
			}
		}
//		System.out.println(stockTimeSharingData.stockCode + ";" + JSON.toJSONString(result));
		return result;
	}

	private static final Comparator<TimeseriesItem> comparator = new Comparator<TimeseriesItem>() {
		public int compare(TimeseriesItem item1, TimeseriesItem item2) {
			return (int) (item1.getTime().getTime() - item2.getTime().getTime());
		}
	};
}
