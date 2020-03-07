/**
 * AccountBox - FileUtils
 * FileUtils.java 2016-6-27
 */
package com.ghut.passbox.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

/**
 * @author Gary
 * 
 */
public class FileUtils {

	public static final String getSDDir() {
		if (!checkSDcard()) {
			return "";
		}

		try {
			String SD_DIR = Environment.getExternalStorageDirectory()
					.toString();
			// String SD_DIR = Environment.getRootDirectory()
			// .toString();
			// LogUtils.error(FileChooserActivity.class, this.getFilesDir());
			// String SD_DIR = "/data/data/com.ghut.passbox";
			return SD_DIR;
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean checkSDcard() {
		String sdStutusString = Environment.getExternalStorageState();
		if (sdStutusString.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static void cleanExternalCache(Context context, String startsWith) {
		if (checkSDcard()) {
			deleteFiles(context.getExternalCacheDir(), startsWith);
		}
	}

	public static void deleteFiles(File directory, String startsWith) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item.isFile() && item.getName().startsWith(startsWith)) {
					item.delete();
				}
			}
		}
	}

	public static boolean isFile(String fileName) {
		boolean res = false;

		if (StringUtils.isNotEmpty(fileName)) {
			File file = new File(fileName);
			if (file.exists() && file.isFile()) {
				res = true;
			}
		}

		return res;
	}

	public static boolean isDirectory(String fileName) {
		boolean res = false;

		if (StringUtils.isNotEmpty(fileName)) {
			File file = new File(fileName);
			if (file.exists() && file.isDirectory()) {
				res = true;
			}
		}

		return res;
	}

	/**
	 * 复制文件
	 * 
	 * @param path
	 * @param source
	 */
	public static void copy(File from, File to) throws Exception {
		if (from == null || !from.isFile() || !from.exists()) {
			throw new IllegalArgumentException("源文件不存在！" + from);
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(from);
			os = new FileOutputStream(to);
			byte[] buf = new byte[1024];
			for (int count = -1; (count = is.read(buf)) > 0;) {
				os.write(buf, 0, count);
			}
			os.flush();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		} finally {
			close(is);
			close(os);
		}
	}

	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {

		}
	}
}
