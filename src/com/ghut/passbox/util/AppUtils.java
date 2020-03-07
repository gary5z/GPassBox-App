/**
 * AccountBox - AppUtils
 * AppUtils.java 2016-7-2
 */
package com.ghut.passbox.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.ghut.passbox.PassBoxApplication;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.common.db.DBHelper;

/**
 * @author Gary
 * 
 */
@SuppressLint("NewApi")
public class AppUtils {

	public static PassBoxApplication getApplication() {
		return PassBoxApplication.getInstance();
	}

	/**
	 * 复制到剪贴板
	 * 
	 * @param text
	 */
	public static void copy(String text) {
		ClipboardManager myClipboard = (ClipboardManager) getApplication()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData myClip = ClipData.newPlainText("text", text);
		myClipboard.setPrimaryClip(myClip);
	}

	/**
	 * 从剪贴板获取数据
	 * 
	 * @return
	 */
	public static String paste() {
		String res = null;

		ClipboardManager myClipboard = (ClipboardManager) getApplication()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData myClip = myClipboard.getPrimaryClip();

		ClipData.Item item = myClip.getItemAt(0);
		if (myClip.getItemCount() > 0) {
			res = item == null ? null : item.getText().toString();
		}

		return res;
	}

	/**
	 * aes解密
	 * 
	 * @param cryptoText
	 * @return
	 */
	public static String aesDecrypt(String cryptoText) {
		String result = null;

		try {
			result = CryptoUtils.aesDecrypt(getApplication().getAESKey(),
					cryptoText);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		return result;
	}

	/**
	 * aes加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String aesEncrypt(String plainText) {
		String result = null;

		try {
			result = CryptoUtils.aesEncrypt(getApplication().getAESKey(),
					plainText);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		return result;
	}

	/**
	 * 获取数据库文件
	 * 
	 * @return
	 */
	public static File getDatabaseFile() {
		return getApplication().getDatabasePath(Constants.DATABASE_NAME);
	}

	public static DBHelper getDBHelper() {
		return getApplication().getDBHelper();
	}

	public static void setEncryptKey(String pwd) {
		setSession(Constants.HMAC_KEY, CryptoUtils.utf8Encode(pwd));
		setSession(Constants.AES_KEY, CryptoUtils.utf8Encode(pwd));
	}

	public static byte[] getAESKey() {
		return (byte[]) getSession(Constants.AES_KEY);
	}

	public static byte[] getHMACKey() {
		return (byte[]) getSession(Constants.HMAC_KEY);
	}

	public static void setSession(String key, Object o) {
		getApplication().setSession(key, o);
	}

	public static Object getSession(String key) {
		return getApplication().getSession(key);
	}

	public static String getStringSession(String key) {
		return (String) getSession(key);
	}

	public static void removeSession(String key) {
		getApplication().removeSession(key);
	}
}
