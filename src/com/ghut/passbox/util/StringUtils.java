package com.ghut.passbox.util;

/**
 * 
 * @author Gary
 *
 */
public class StringUtils {

	public static boolean isEmpty(String s) {
		if (s == null || s.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

}
