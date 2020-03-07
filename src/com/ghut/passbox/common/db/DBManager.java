package com.ghut.passbox.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Gary
 * 
 */
public class DBManager {
	private SQLiteDatabase db;

	public DBManager(SQLiteDatabase db) {
		this.db = db;
	}

	public int count(String table, String whereClause, String[] whereArgs) {
		Cursor cursor = null;
		int count = 0;
		try {
			cursor = query(table, null, whereClause, whereArgs);
			count = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return count;
	}

	public Cursor queryAll(String table) {
		return query(table, null, null, null);
	}

	public Cursor queryAll(String table, String[] columns) {
		return query(table, columns, null, null);
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs) {
		Cursor cursor = db.query(table, columns, selection, selectionArgs,
				null, null, null);
		return cursor;
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String orderBy) {
		Cursor cursor = db.query(table, columns, selection, selectionArgs,
				null, null, orderBy);
		return cursor;
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;
	}

	public void close(Cursor c) {
		if (c != null) {
			c.close();
		}
	}

	public long insert(String table, ContentValues values) {
		if (values == null || values.size() == 0) {
			return 0;
		}

		return db.insert(table, null, values);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
	}

	public int updateAll(String table, ContentValues values) {
		return update(table, values, null, null);
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
	}
	
	public void execSQL(String sql) {
		db.execSQL(sql);
	}
	
	// private void convert2Utf8(ContentValues values) throws
	// UnsupportedEncodingException {
	// if(values == null) {
	// return;
	// }
	//
	// Set<Map.Entry<String, Object>> valueSet = values.valueSet();
	// for(Map.Entry<String, Object> entry : valueSet) {
	// if(entry.getValue() instanceof String) {
	// String value = new String(entry.getValue().toString().getBytes(),
	// Constants.UTF8);
	// entry.setValue(value);
	// }
	// }
	// }
}
