package com.ghut.passbox.activity.impl;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.ClientHelper.ClickListener;
import com.ghut.passbox.util.ClientHelper.ConfirmListener;

/**
 * @author Gary
 * 
 */
public class CategoryListActivity extends HeaderActivity {

	private static final int MSG_REMOVE_CATEGORY = 0;
	private static final int MSG_LOAD_CATEGORY_OK = 1;

	private ImageView imgAdd;
	private ListView lvContent;
	private SimpleAdapter listAdapter;

	private List<Category> categoryList;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REMOVE_CATEGORY:
			String res = (String) msg.obj;
			if (res.equals("ok")) {
				ClientHelper.showShortToast(CategoryListActivity.this, "删除成功！");
				loadFields();
			} else {
				ClientHelper.showShortToast(CategoryListActivity.this, res);
			}
			break;
		case MSG_LOAD_CATEGORY_OK:
			setAdapter();
			break;
		default:
			break;
		}
	}

	@Override
	protected String getGZTitle() {
		return "分类";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_category_list;
	}

	protected void setViews() {
		super.setViews();
		imgAdd = (ImageView) this.findViewById(R.id.imgAdd);
		lvContent = (ListView) this.findViewById(R.id.lvContent);
	}

	private void setAdapter() {
		String[] listData = new String[categoryList.size()];
		for (int i = 0; i < categoryList.size(); i++) {
			listData[i] = categoryList.get(i).getCatName();
		}

		listAdapter = ClientHelper.buildListAdapter(this, listData,
				new ClickListener() {

					@Override
					public void onClick(int position) {
						imgEdit_onClick(position);
					}
				}, new ClickListener() {

					@Override
					public void onClick(int position) {
						imgDelete_onClick(position);
					}
				});

		lvContent.setAdapter(listAdapter);
	}

	private void imgEdit_onClick(int position) {
		showCategoryEdit(position);
	}

	private void imgDelete_onClick(final int position) {
		ClientHelper.showDialog(this, "确认要删除吗", null, new ConfirmListener() {
			@Override
			public void onConfirm() {
				removeCategory(position);
			}
		});
	}

	protected void loadFields() {
		super.loadFields();
		new MessageThread() {
			public Message buildMessage() {
				categoryList = serviceContext.getAllCategory();
				Message msg = new Message();
				msg.what = MSG_LOAD_CATEGORY_OK;
				return msg;
			}
		}.start();
	}

	protected void setListeners() {
		super.setListeners();
		imgAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCategoryNew();
			}
		});

		lvContent.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 打开修改界面
				showCategoryEdit(position);
			}
		});

	}

	private void removeCategory(int position) {
		final Category category = categoryList.get(position);
		if (category == null) {
			ClientHelper.showShortToast(this, "记录不存在！");
			return;
		}

		new MessageThread() {
			public Message buildMessage() {
				String res = serviceContext.removeCategory(category);
				Message msg = new Message();
				msg.what = MSG_REMOVE_CATEGORY;
				msg.obj = res;
				return msg;
			}
		}.start();
	}

	private void showCategoryEdit(int position) {
		Category category = categoryList.get(position);
		Intent intent = new Intent(this, CategoryEditActivity.class);
		intent.putExtra(Constants.CATEGORY, category);
		intent.putExtra(Constants.OPER_ACTION, Constants.OPER_EDIT);
		startActivity(intent);
	}

	private void showCategoryNew() {
		Intent intent = new Intent(this, CategoryEditActivity.class);
		intent.putExtra(Constants.OPER_ACTION, Constants.OPER_ADDNEW);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
