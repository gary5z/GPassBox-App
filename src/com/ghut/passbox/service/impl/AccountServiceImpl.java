package com.ghut.passbox.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ghut.passbox.common.ServiceContext;
import com.ghut.passbox.common.db.DBManager;
import com.ghut.passbox.model.Account;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.model.CategoryCount;
import com.ghut.passbox.service.AccountService;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 * 
 */
public class AccountServiceImpl implements AccountService {

	private final static String TABLE_NAME = "Account";

	private final static String[] COLUMNS = new String[] { "AcctId", "Title",
			"LoginName", "LoginPwd", "Site", "Remark", "CatId" };

	private DBManager dbMgr;

	public AccountServiceImpl(DBManager dbMgr) {
		this.dbMgr = dbMgr;
	}

	@Override
	public String addNew(Account account) throws Exception {
		String whereSQL = "Title=?";
		String[] params = new String[] { account.getTitle() };
		int count = dbMgr.count(TABLE_NAME, whereSQL, params);
		if (count > 0) {
			return account.getTitle() + " 已记录，请确认！";
		}

		ContentValues cValue = mapToValues(account);
		if (dbMgr.insert(TABLE_NAME, cValue) > 0) {
			return "ok";
		} else {
			return "新增失败！";
		}
	}

	@Override
	public String modify(Account account) throws Exception {
		String whereClause = "AcctId=?";
		String[] whereArgs = new String[] { String.valueOf(account.getAcctId()) };
		ContentValues cValue = mapToValues(account);
		int count = dbMgr.update(TABLE_NAME, cValue, whereClause, whereArgs);
		if (count > 0) {
			return "ok";
		} else {
			return "修改失败！";
		}
	}

	@Override
	public String remove(Account account) throws Exception {
		return remove(account == null ? -1 : account.getAcctId());
	}

	@Override
	public String remove(int acctId) throws Exception {
		String whereClause = "AcctId=?";
		String[] whereArgs = new String[] { String.valueOf(acctId) };
		int count = dbMgr.delete(TABLE_NAME, whereClause, whereArgs);
		if (count > 0) {
			return "ok";
		} else {
			return "删除失败！";
		}
	}

	@Override
	public List<Account> getAll() throws Exception {
		List<Account> result = new ArrayList<Account>();
		Cursor c = null;
		try {
			c = dbMgr.queryAll(TABLE_NAME, COLUMNS);
			while (c.moveToNext()) {
				result.add(mapToAccount(c));
			}
		} finally {
			dbMgr.close(c);
		}

		return result;
	}

	/**
	 * category：用于根据category分类过滤数据。如果传入值为－1，则查询所有。
	 */
	@Override
	public List<Account> getListByCategory(int catId) throws Exception {
		List<Account> result = new ArrayList<Account>();
		Cursor c = null;
		try {
			String[] selectionArgs = null;
			StringBuilder selection = new StringBuilder();
			if (catId != -1) {
				selection.append("CatId=?");
				selectionArgs = new String[] { String.valueOf(catId) };
			}
			c = dbMgr.query(TABLE_NAME, COLUMNS, selection.toString(),
					selectionArgs, "CatId, Title");
			while (c.moveToNext()) {
				result.add(mapToAccount(c));
			}
		} finally {
			dbMgr.close(c);
		}

		return result;
	}

	/**
	 * @param category
	 *            用于根据category分类过滤数据。如果传入值为－1，则查询该用户所有数据。
	 */
	@Override
	public List<Account> getListByCategory(Category category) throws Exception {
		return getListByCategory(category == null ? -1 : category.getCatId());
	}

	/**
	 * 查询所有类别。
	 */
	@Override
	public List<CategoryCount> getCategoryCounts() throws Exception {
		List<CategoryCount> result = new ArrayList<CategoryCount>();

		Cursor c = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(1) cnt, c.CatId, c.CatName, c.Remark \n");
			sql.append("from Account a, Category c \n");
			sql.append("where a.CatId=c.CatId \n");
			sql.append("group by c.CatId, c.CatName, c.Remark \n");
			sql.append("order by c.CatId");
			c = dbMgr.rawQuery(sql.toString(), new String[] {});
			while (c.moveToNext()) {
				CategoryCount categoryCount = new CategoryCount();
				Category category = new Category();
				category.setCatId(c.getInt(c.getColumnIndex("CatId")));
				category.setCatName(c.getString(c.getColumnIndex("CatName")));
				category.setRemark(c.getString(c.getColumnIndex("Remark")));
				categoryCount.setCategory(category);
				categoryCount.setCount(c.getInt(c.getColumnIndex("cnt")));
				result.add(categoryCount);
			}
		} finally {
			dbMgr.close(c);
		}

		return result;
	}

	@Override
	public String remove(Category category) throws Exception {
		String whereClause = null;
		String[] whereArgs = null;
		if (category != null) {
			whereClause = "CatId=?";
			whereArgs = new String[] { String.valueOf(category.getCatId()) };
		}
		int count = dbMgr.delete(TABLE_NAME, whereClause, whereArgs);
		if (count > 0) {
			return "ok";
		} else {
			return "删除失败！";
		}
	}

	private Account mapToAccount(Cursor c) {
		Account account = new Account();
		account.setAcctId(c.getInt(c.getColumnIndex("AcctId")));
		account.setTitle(c.getString(c.getColumnIndex("Title")));
		account.setEncryptedLoginName(c.getString(c.getColumnIndex("LoginName")));
		account.setEncryptedLoginPwd(c.getString(c.getColumnIndex("LoginPwd")));
		account.setSite(c.getString(c.getColumnIndex("Site")));
		account.setRemark(c.getString(c.getColumnIndex("Remark")));
		int catId = c.getInt(c.getColumnIndex("CatId"));
		ServiceContext serviceContext = ServiceContext.getInstance();
		account.setCategory(serviceContext.getCategory(catId));
		return account;
	}

	private ContentValues mapToValues(Account account) {
		ContentValues cValue = new ContentValues();
		if (account.getAcctId() > -1) {
			cValue.put("CatId", account.getAcctId());
		}
		if (StringUtils.isNotEmpty(account.getTitle())) {
			cValue.put("Title", account.getTitle());
		}
		if (StringUtils.isNotEmpty(account.getEncryptedLoginName())) {
			cValue.put("LoginName", account.getEncryptedLoginName());
		}
		if (StringUtils.isNotEmpty(account.getEncryptedLoginPwd())) {
			cValue.put("LoginPwd", account.getEncryptedLoginPwd());
		}
		if (StringUtils.isNotEmpty(account.getSite())) {
			cValue.put("Site", account.getSite());
		}
		if (StringUtils.isNotEmpty(account.getRemark())) {
			cValue.put("Remark", account.getRemark());
		}
		if (account.getCategory() != null) {
			cValue.put("CatId", account.getCategory().getCatId());
		}
		return cValue;
	}
}
