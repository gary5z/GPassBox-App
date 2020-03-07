package com.ghut.passbox.util;

import android.util.Log;

/**
 * 
 * @author Gary
 *
 */
public class LogUtils {

	public static void info(Class<?> clazz, Object msg) {
		Log.i(clazz.getSimpleName(), msg == null ? "" : msg.toString());
	}

	public static void error(Class<?> clazz, Object msg) {
		Log.e(clazz.getSimpleName(), msg == null ? "" : msg.toString());
	}

	public static void error(Class<?> clazz, Object msg, Throwable e) {
		Log.e(clazz.getSimpleName(), msg == null ? "" : msg.toString(), e);
	}

	public static void error(Class<?> clazz, Throwable e) {
		Log.e(clazz.getSimpleName(), "", e);
	}
}
