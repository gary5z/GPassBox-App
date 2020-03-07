package com.ghut.passbox.activity.impl;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.model.CategoryCount;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.ClientHelper.ConfirmListener;

/**
 * @author Gary
 * 
 */
public class MainActivity extends HeaderActivity {

	private static final int MSG_REMOVE_ACCOUNT = 0;
	private static final int MSG_LOAD_CATEGORY_OK = 1;
	private static final int MSG_DONT_EXIT = 2;

	private ImageView imgAdd;
	private ListView lvContent;
	private SimpleAdapter listAdapter;

	private List<CategoryCount> categoryCounts;

	// 定义是否退出程序的标记
	private boolean isExit = false;

	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REMOVE_ACCOUNT:
			String res = (String) msg.obj;
			if (res.equals("ok")) {
				ClientHelper.showShortToast(MainActivity.this, "删除成功！");
				loadFields();
			} else {
				ClientHelper.showShortToast(MainActivity.this, res);
			}
			break;
		case MSG_LOAD_CATEGORY_OK:
			setAdapter();
			break;
		case MSG_DONT_EXIT:
			isExit = false;
		default:
			break;
		}
	}

	@Override
	protected String getGZTitle() {
		return "GZ密码箱";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	protected void setViews() {
		super.setViews();
		imgAdd = (ImageView) this.findViewById(R.id.imgAdd);
		lvContent = (ListView) this.findViewById(R.id.lvContent);

		// 修改为设置按钮
		setBackImage(R.drawable.setting_black);
	}

	private void setAdapter() {
		String[] listData = new String[categoryCounts.size() + 1];

		// 在首先位置固定插入一条记录：“全部记录”
		addTotalAtFirstLine(listData);

		for (int i = 0; i < categoryCounts.size(); i++) {
			CategoryCount categoryCount = categoryCounts.get(i);
			listData[i + 1] = categoryCount.toString();
		}

		listAdapter = ClientHelper.buildExpendAdapter(this, listData);
		lvContent.setAdapter(listAdapter);
	}

	private void addTotalAtFirstLine(String[] listData) {
		int total = 0;
		for (CategoryCount cc : categoryCounts) {
			total += cc.getCount();
		}
		listData[0] = "全部记录 " + total;
	}

	protected void loadFields() {
		super.loadFields();
		new MessageThread() {
			public Message buildMessage() {
				categoryCounts = serviceContext.getCategoryCounts();
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
				showAddRecord();
			}
		});

		lvContent.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				showRecordList(position);
			}
		});

		lvContent.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {

				ClientHelper.showDialog(MainActivity.this, "要删除该分类数据吗",
						new ConfirmListener() {
							@Override
							public void onConfirm() {
								lvContent_onLongClick(position);
							}
						});

				return true;
			}
		});
	}

	private void lvContent_onLongClick(final int position) {
		new MessageThread() {
			@Override
			protected Message buildMessage() {
				Category category = null;
				if (position > 0) {
					CategoryCount categoryCount = categoryCounts
							.get(position - 1);
					category = categoryCount == null ? null : categoryCount
							.getCategory();
				}
				String res = serviceContext.removeAccount(category);
				Message msg = new Message();
				msg.what = MSG_REMOVE_ACCOUNT;
				msg.obj = res;
				return msg;
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 判断用户是否点击的是返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 如果isExit标记为false，提示用户再次按键
			if (!isExit) {
				isExit = true;
				ClientHelper.showShortToast(this, "再按一次退出程序");
				// 如果用户没有在2秒内再次按返回键的话，就发送消息标记用户为不退出状态
				sendEmptyMessageDelayed(MSG_DONT_EXIT, 3000);
			}
			// 如果isExit标记为true，退出程序
			else {
				// 退出程序
				finish();
			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void imgBack_OnClickListener() {
		showSettings();
	}

	private void showSettings() {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

	private void showAddRecord() {
		Intent intent = new Intent(this, CategorySelectActivity.class);
		startActivity(intent);
	}

	private void showRecordList(int position) {
		Intent intent = new Intent(this, RecordListActivity.class);
		if (position > 0) {
			CategoryCount categoryCount = categoryCounts.get(position - 1);
			intent.putExtra(Constants.CATEGORY, categoryCount.getCategory());
		}
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
