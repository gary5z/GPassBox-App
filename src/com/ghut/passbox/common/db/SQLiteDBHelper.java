/**
 * AccountBox - SQLiteDBHelper
 * SQLiteDBHelper.java 2016-7-2
 */
package com.ghut.passbox.common.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghut.passbox.util.LogUtils;

/**
 * @author Gary
 *
 */
public class SQLiteDBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private Context context;

	public SQLiteDBHelper(Context context, String dbName) {
		super(context, dbName, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS person"
//				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT)");
		executeAssetsSQL(db, "schema.sql");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
	}

	public void executeAssetsSQL(SQLiteDatabase db, String schemaName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context
					.getAssets().open(schemaName)));

			String line;
			String buffer = "";
			while ((line = in.readLine()) != null) {
				buffer += line;
				if (line.trim().endsWith(";")) {
					db.execSQL(buffer.replace(";", ""));
					buffer = "";
				}
			}
		} catch (IOException e) {
			LogUtils.error(SQLiteDBHelper.class, e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				LogUtils.error(SQLiteDBHelper.class, e);
			}
		}
	}
}
