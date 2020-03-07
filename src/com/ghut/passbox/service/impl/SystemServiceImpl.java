package com.ghut.passbox.service.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.ghut.passbox.common.db.DBManager;
import com.ghut.passbox.service.SystemService;
import com.ghut.passbox.util.AppUtils;
import com.ghut.passbox.util.CryptoUtils;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 * 
 */
public class SystemServiceImpl implements SystemService {

	private final static String TABLE_NAME = "System";

	private final static String ENTER_PWD = "EnterPwd";

	private final static String EMAIL = "Email";

	private final static String VERSION = "Version";

	private DBManager dbMgr;

	public SystemServiceImpl(DBManager dbMgr) {
		this.dbMgr = dbMgr;
	}

	@Override
	public synchronized boolean hasRegistered() throws Exception {
		int count = dbMgr.count(TABLE_NAME, null, null);
		
		if (count > 0) {
			return true;
		}
		return false;
	}
	
	private String query(String fieldName) throws Exception {
		String result = null;

		Cursor c = null;
		try {
			c = dbMgr.queryAll(TABLE_NAME, new String[] { fieldName });
			if (c.moveToFirst()) {
				result = c.getString(c.getColumnIndex(fieldName));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			dbMgr.close(c);
		}

		return result;
	}
	
	private long insert(String fieldName, String value) throws Exception {
		long count = 0;

		ContentValues cValue = new ContentValues();
		cValue.put(fieldName, value);
		count = dbMgr.insert(TABLE_NAME, cValue);
		
		return count;
	}
	
	private int update(String fieldName, String value) throws Exception {
		int count = 0;

		ContentValues cValue = new ContentValues();
		cValue.put(fieldName, value);
		count = dbMgr.update(TABLE_NAME, cValue, null, null);
		
		return count;
	}

	@Override
	public synchronized boolean verifyPwd(String pwd) throws Exception {
		boolean result = false;

		String storedPwd = query(ENTER_PWD);
		if (CryptoUtils.bcryptMatch(pwd, storedPwd)) {
			result = true;
		}

		return result;
	}

	public synchronized String login(String pwd) throws Exception {
		String result = "ok";
		String storedPwd = query(ENTER_PWD);
		if(StringUtils.isNotEmpty(storedPwd)) {
			if (!CryptoUtils.bcryptMatch(pwd, storedPwd)) {
				result = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷耄�";
			}
		} else {
			String encryptedPwd = CryptoUtils.bcryptEncrypt(pwd);
			if(insert(ENTER_PWD, encryptedPwd) == 0) {
				result = "锟斤拷歉锟斤拷锟斤拷锟矫筹拷始锟斤拷锟斤拷失锟杰ｏ拷锟斤拷锟斤拷锟皆ｏ拷";
			}
		}
		return result;
	}

	@Override
	public synchronized String modifyPwd(String oldpwd, String newpwd)
			throws Exception {
		String result = "ok";

		String storedPwd = query(ENTER_PWD);
		if(StringUtils.isNotEmpty(storedPwd)) {
			if (!CryptoUtils.bcryptMatch(oldpwd, storedPwd)) {
				result = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷耄�";
			} else {
				String encryptedPwd = CryptoUtils.bcryptEncrypt(newpwd);
				if (update(ENTER_PWD, encryptedPwd) == 0) {
					result = "锟睫革拷失锟杰ｏ拷锟斤拷锟斤拷锟皆ｏ拷";
				}
			}
		} else {
			String encryptedPwd = CryptoUtils.bcryptEncrypt(newpwd);
			if(insert(ENTER_PWD, encryptedPwd) == 0) {
				result = "锟斤拷歉锟斤拷锟斤拷锟矫筹拷始锟斤拷锟斤拷失锟杰ｏ拷锟斤拷锟斤拷锟皆ｏ拷";
			}
		}
		
		return result;
	}

	@Override
	public String bindEmail(String email) throws Exception {
		String result = "锟斤拷歉锟斤拷锟斤拷锟斤拷锟斤拷失锟杰ｏ拷锟斤拷锟斤拷锟皆ｏ拷";

		String encryptedEmail = AppUtils.aesEncrypt(email);
		if (update(EMAIL, encryptedEmail) > 0) {
			result = "ok";
		}
		
		return result;
	}

	@Override
	public String getEmail() throws Exception {
		String result = "";

		String storedEmail = query(EMAIL);
		if(StringUtils.isNotEmpty(storedEmail)) {
			result = AppUtils.aesDecrypt(storedEmail);
		}
		
		return result;
	}

	@Override
	public int getVersion() {
		int result = 0;

		Cursor c = null;
		try {
			c = dbMgr.queryAll(TABLE_NAME, new String[] { VERSION });
			if (c.moveToFirst()) {
				result = c.getInt(c.getColumnIndex(VERSION));
			}
		} catch (Exception e) {
//			throw e;
		} finally {
			dbMgr.close(c);
		}

		return result;
	}

}
