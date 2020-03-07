package com.ghut.passbox.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ghut.passbox.common.CategoryTypeEnum;
import com.ghut.passbox.common.db.DBManager;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.service.CategoryService;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 * 
 */
public class CategoryServiceImpl implements CategoryService {

	private final static String TABLE_NAME = "Category";

	private DBManager dbMgr;

	public CategoryServiceImpl(DBManager dbMgr) {
		this.dbMgr = dbMgr;
	}

	@Override
	public String addNew(Category model) throws Exception {
		String whereSQL = "CatName=?";
		String[] params = new String[] { model.getCatName() };
		int count = dbMgr.count(TABLE_NAME, whereSQL, params);
		if (count > 0) {
			return "记录已存在！";
		}

		ContentValues cValue = mapToValues(model);
		if (dbMgr.insert(TABLE_NAME, cValue) > 0) {
			return "ok";
		} else {
			return "新增失败！";
		}
	}

	@Override
	public String modify(Category model) throws Exception {
		String whereClause = "CatName=?";
		String[] whereArgs = new String[] { model.getCatName() };
		ContentValues cValue = mapToValues(model);
		int count = dbMgr.update(TABLE_NAME, cValue, whereClause, whereArgs);
		if (count > 0) {
			return "ok";
		} else {
			return "修改失败！";
		}
	}

	@Override
	public String remove(Category model) throws Exception {
		return remove(model.getCatId());
	}

	@Override
	public String remove(int acctId) throws Exception {
		String whereClause = "CatId=?";
		String[] whereArgs = new String[] { String.valueOf(acctId) };
		int count = dbMgr.delete(TABLE_NAME, whereClause, whereArgs);
		if (count > 0) {
			return "ok";
		} else {
			return "删除失败！";
		}
	}

	@Override
	public List<Category> getAll() throws Exception {
		List<Category> result = new ArrayList<Category>();
		Cursor c = null;
		try {
			String[] columns = new String[] { "CatId", "CatName", "CatType", "Remark" };
			c = dbMgr.queryAll(TABLE_NAME, columns);
			while (c.moveToNext()) {
				Category category = new Category();
				category.setCatId(c.getInt(c.getColumnIndex("CatId")));
				category.setCatName(c.getString(c.getColumnIndex("CatName")));
				category.setCatType(CategoryTypeEnum.valueOf(c.getInt(c.getColumnIndex("CatType"))));
				category.setRemark(c.getString(c.getColumnIndex("Remark")));
				result.add(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.close(c);
		}

		return result;
	}

	private ContentValues mapToValues(Category category) {
		ContentValues cValue = new ContentValues();
		if (category.getCatId() > -1) {
			cValue.put("CatId", category.getCatId());
		}
		if (category.getCatType() != null) {
			cValue.put("CatType", category.getCatType().value);
		}
		if (StringUtils.isNotEmpty(category.getCatName())) {
			cValue.put("CatName", category.getCatName());
		}
		if (StringUtils.isNotEmpty(category.getRemark())) {
			cValue.put("Remark", category.getRemark());
		}
		return cValue;
	}

	@Override
	public Category get(int catId) throws Exception {
		Category category = null;
		Cursor c = null;
		try {
			String[] columns = new String[] { "CatId", "CatName", "CatType", "Remark" };
			String selection = "CatId=?";
			String[] selectionArgs = new String[] { String.valueOf(catId) };
			c = dbMgr.query(TABLE_NAME, columns, selection, selectionArgs);
			if (c.moveToNext()) {
				category = new Category();
				category.setCatId(c.getInt(c.getColumnIndex("CatId")));
				category.setCatType(CategoryTypeEnum.valueOf(c.getInt(c.getColumnIndex("CatType"))));
				category.setCatName(c.getString(c.getColumnIndex("CatName")));
				category.setRemark(c.getString(c.getColumnIndex("Remark")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.close(c);
		}

		return category;
	}
}
