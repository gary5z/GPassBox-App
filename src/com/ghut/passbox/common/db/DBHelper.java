package com.ghut.passbox.common.db;

import java.util.List;

import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.service.CategoryService;
import com.ghut.passbox.service.impl.CategoryServiceImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Gary
 * 
 */
public class DBHelper {

	private SQLiteDatabase db;
	private SQLiteDBHelper helper;

	public DBHelper(Context context, String dbName) {
		this.helper = new SQLiteDBHelper(context, dbName);
	}

	public SQLiteDatabase getSQLiteDB() {
		if (db != null) {
			return db;
		}

		open();
		return db;
	}

	public void executeAssetsSQL(String schemaName) {
		helper.executeAssetsSQL(db, schemaName);
	}

	public void execSQL(String sql) {
		db.execSQL(sql);
	}
	
	/**
	 * 方法2：检查表中某列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	public boolean checkColumnExists(String tableName, String columnName) {
		boolean result = false;
		Cursor cursor = null;
		
		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0",
					null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}

	public void open() {
		db = helper.getWritableDatabase();
		checkAndUpgrade();
		initData();
	}

	/**
	 * 检查升级
	 */
	public void checkAndUpgrade() {
		StringBuilder upgradeSQL = new StringBuilder(0);
		if(!checkColumnExists("System", "Version")) {
			upgradeSQL.append("ALTER TABLE System ADD Version INTEGER;");
			upgradeSQL.append("update System set Version="+Constants.VERSION + ";");
		}

		if(!checkColumnExists("Category", "CatType")) {
			upgradeSQL.append("ALTER TABLE Category ADD CatType INTEGER;");
			upgradeSQL.append("update Category set CatType=1 where CatId=1;");
			upgradeSQL.append("update Category set CatType=2 where CatId=2;");
			upgradeSQL.append("update Category set CatType=3 where CatId=3;");
			upgradeSQL.append("update Category set CatType=4 where CatId=1;");
		}
		
		if(upgradeSQL.length() > 0) {
			execSQL(upgradeSQL.toString());	
		}
	}
	
	public void initData() {
		// 初始分类数据
		CategoryService categoryService = new CategoryServiceImpl(new DBManager(db));
		try {
			List<Category> categoryList = categoryService.getAll();
			if(categoryList.isEmpty()) {
				executeAssetsSQL("init.sql");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * close database
	 */
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}

		if (helper != null) {
			helper = null;
		}
	}

}
