package com.dream.entity;

import java.util.Date;

/**
 * 盘口数据
 * 
 * @author ucs_yangshijie
 *
 */
public class StockHandicapData {
	public Date time;
	public long buy[] = new long[5];
	public float buyPrice[] = new float[5];
	public long sell[] = new long[5];
	public float sellPrice[] = new float[5];
}
