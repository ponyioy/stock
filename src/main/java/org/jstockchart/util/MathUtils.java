package org.jstockchart.util;

import java.util.List;

import org.jstockchart.model.TimeseriesItem;

public class MathUtils {
	public static double getAveragePrice(List<TimeseriesItem> data){
		if(data == null)return 0;
		double totalPrice = 0;
		for (int i = 0; i < data.size(); i++) {
			TimeseriesItem t = data.get(i);
			totalPrice += t.getPrice();
		}
		return totalPrice/data.size();
	};
}
