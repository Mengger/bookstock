package com.bookrecovery.until;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUntil {

	/**
	 * 获取根据相应格式当前时间
	 * @param dataFormate
	 * @return
	 */
	public static String getNowTime(String dataFormate) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dataFormate);
		return formatter.format(currentTime);
	}
}
