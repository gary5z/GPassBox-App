/**
 * AccountBox - DateTimeUtils
 * DateTimeUtils.java 2016-7-1
 */
package com.ghut.passbox.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Gary
 * 
 */
public class DateTimeUtils {

	/**
	 * 返回格式化的日期时间，默认格式为：yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String currentTimeString() {
		return currentTimeString("yyyyMMddHHmmss");
	}

	@SuppressLint("SimpleDateFormat")
	public static String currentTimeString(String pattern) {
		String strDateTime = new SimpleDateFormat(pattern).format(new Date());
		return strDateTime;
	}
}
