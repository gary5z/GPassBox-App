package com.ghut.passbox.activity.impl;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.util.ClientHelper;

/**
 * @author Gary
 *
 */
public class CategorySelectActivity extends HeaderActivity {

	private static final int MSG_LOAD_CATEGORY_OK = 0;

	private ListView lvContent;

	private List<Category> categoryList;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_LOAD_CATEGORY_OK:
			setAdapter();
			break;
		default:
			break;
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_category_select;
	}

	@Override
	protected String getGZTitle() {
		return "选择分类";
	}

	protected void setViews() {
		super.setViews();
		lvContent = (ListView) this.findViewById(R.id.lvContent);
	}

	private void setAdapter() {
		String[] listData = new String[categoryList.size()];
		for (int i = 0; i < categoryList.size(); i++) {
			listData[i] = categoryList.get(i).getCatName();
		}

		ListAdapter adapter = ClientHelper.buildListAdapter(this, listData);

		lvContent.setAdapter(adapter);
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
		lvContent.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				showNewRecordActivity(position);
			}
		});
	}

	private void showNewRecordActivity(int position) {
		Category category = categoryList.get(position);
		Intent intent = new Intent(this, RecordEditActivity.class);
		intent.putExtra(Constants.CATEGORY, category);
		intent.putExtra(Constants.OPER_ACTION, Constants.OPER_ADDNEW);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
