package com.ghut.passbox.activity.impl;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Account;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.util.AppUtils;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.ClientHelper.ClickListener;
import com.ghut.passbox.util.ClientHelper.ConfirmListener;

/**
 * @author Gary
 * 
 */
public class RecordListActivity extends HeaderActivity {

	private static final int MSG_REMOVE_ACCOUNT = 0;
	private static final int MSG_LOAD_ACCOUNT_OK = 1;

	private ImageView imgAdd;
	private ListView lvContent;
	private SimpleAdapter listAdapter;

	private List<Account> accountList;
	private Category category;

	private int longClickPosition = -1;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REMOVE_ACCOUNT:
			String res = (String) msg.obj;
			if (res.equals("ok")) {
				ClientHelper.showShortToast(RecordListActivity.this, "删除成功");
				loadFields();
			} else {
				ClientHelper.showShortToast(RecordListActivity.this, res);
			}
			break;
		case MSG_LOAD_ACCOUNT_OK:
			setAdapter();
			break;
		default:
			break;
		}
	}

	@Override
	protected String getGZTitle() {
		return "全部";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_record_list;
	}

	protected void setViews() {
		super.setViews();
		imgAdd = (ImageView) this.findViewById(R.id.imgAdd);
		lvContent = (ListView) this.findViewById(R.id.lvContent);
	}

	private void setAdapter() {
		String[] listData = new String[accountList.size()];
		for (int i = 0; i < accountList.size(); i++) {
			Account account = accountList.get(i);
			StringBuilder item = new StringBuilder();
			if (account.getCategory() != null) {
				item.append(account.getCategory().getCatName());
				item.append(" | ");
				item.append(account.getTitle());
				
				switch (account.getCategory().getCatType()) {
				case NORMAL:
					item.append("\n用户：");
					item.append(account.getLoginName());
					item.append("\n密码：");
					item.append(account.getLoginPwd());
					break;
				case EMAIL:
					item.append("\n邮箱：");
					item.append(account.getLoginName());
					item.append("\n密码：");
					item.append(account.getLoginPwd());
					break;
				case FINANCIAL:
					item.append("\n账户：");
					item.append(account.getLoginName());
					item.append("\n密码1：");
					item.append(account.getLoginPwd());
					item.append("\n密码2：");
					item.append(account.getSite());
					break;
				case NOTE:
					item.append("\n备注：");
					String remark = account.getRemark();
					if(remark.length() > 50) {
						remark = remark.substring(0, 50);
					}
					item.append(remark);
//					item.append("邮箱：");
//					item.append(account.getLoginName()).append("\n");
//					item.append("密码：").append(account.getLoginPwd());
					break;
				default:
					item.append("\n用户：");
					item.append(account.getLoginName());
					item.append("\n密码：");
					item.append(account.getLoginPwd());
					break;
				}
			}
			listData[i] = item.toString();
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
		showEditRecordActivity(position);
	}

	private void imgDelete_onClick(final int position) {
		ClientHelper.showDialog(this, "确认要删除吗", null, new ConfirmListener() {
			@Override
			public void onConfirm() {
				removeAccount(position);
			}
		});
	}

	protected void loadFields() {
		super.loadFields();
		Intent intent = RecordListActivity.this.getIntent();
		category = (Category) intent.getSerializableExtra(Constants.CATEGORY);
		if (category != null) {
			setGZTitle(category.getCatName());
		}

		new MessageThread() {
			public Message buildMessage() {
				accountList = serviceContext.getAccountByCategory(category);
				Message msg = new Message();
				msg.what = MSG_LOAD_ACCOUNT_OK;
				return msg;
			}
		}.start();
	}

	protected void setListeners() {
		super.setListeners();
		imgAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNewRecordActivity();
			}
		});

		lvContent.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 打开修改页面
				showEditRecordActivity(position);
			}
		});

		lvContent.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				longClickPosition = position;
				return false;
			}
		});

		lvContent
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {

						menu.setHeaderTitle("复制");

						if (longClickPosition != -1) {
							Account account = accountList
									.get(longClickPosition);
							if (account.getCategory() != null) {
								switch (account.getCategory().getCatType()) {
								case NORMAL:
									menu.add(0, 0, Menu.NONE, "标题");
									menu.add(0, 1, Menu.NONE, "用户");
									menu.add(0, 2, Menu.NONE, "密码");
									menu.add(0, 3, Menu.NONE, "网址");
									menu.add(0, 4, Menu.NONE, "备注");
									break;
								case EMAIL:
									menu.add(0, 0, Menu.NONE, "标题");
									menu.add(0, 1, Menu.NONE, "邮箱");
									menu.add(0, 2, Menu.NONE, "密码");
									menu.add(0, 3, Menu.NONE, "网址");
									menu.add(0, 4, Menu.NONE, "备注");
									break;
								case FINANCIAL:
									menu.add(0, 0, Menu.NONE, "标题");
									menu.add(0, 1, Menu.NONE, "账户");
									menu.add(0, 2, Menu.NONE, "密码1");
									menu.add(0, 3, Menu.NONE, "密码2");
									menu.add(0, 4, Menu.NONE, "备注");
									break;
								case NOTE:
									menu.add(0, 0, Menu.NONE, "标题");
									menu.add(0, 4, Menu.NONE, "备注");
									break;
								default:
									menu.add(0, 0, Menu.NONE, "标题");
									menu.add(0, 1, Menu.NONE, "用户");
									menu.add(0, 2, Menu.NONE, "密码");
									menu.add(0, 3, Menu.NONE, "网址");
									menu.add(0, 4, Menu.NONE, "备注");
									break;
								}
							}

						}
					}
				});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int id = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

		switch (item.getItemId()) {
		case 0:
			AppUtils.copy(accountList.get(id).getTitle());
			break;
		case 1:
			AppUtils.copy(accountList.get(id).getLoginName());
			break;
		case 2:
			AppUtils.copy(accountList.get(id).getLoginPwd());
			break;
		case 3:
			AppUtils.copy(accountList.get(id).getSite());
			break;
		case 4:
			AppUtils.copy(accountList.get(id).getRemark());
			break;
		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	private void removeAccount(int position) {
		final Account account = accountList.get(position);
		if (account == null) {
			ClientHelper.showShortToast(this, "记录不存在！");
			return;
		}

		new MessageThread() {
			public Message buildMessage() {
				String res = serviceContext.removeAccount(account);
				Message msg = new Message();
				msg.what = MSG_REMOVE_ACCOUNT;
				msg.obj = res;
				return msg;
			}
		}.start();
	}

	private void showEditRecordActivity(int position) {
		Account account = accountList.get(position);
		if (account == null) {
			ClientHelper.showShortToast(this, "记录不存在！");
			return;
		}

		Intent intent = new Intent(RecordListActivity.this,
				RecordEditActivity.class);
		intent.putExtra(Constants.ACCOUNT, account);
		intent.putExtra(Constants.CATEGORY, account.getCategory());
		intent.putExtra(Constants.OPER_ACTION, Constants.OPER_EDIT);
		startActivity(intent);
	}

	private void showNewRecordActivity() {
		if (category != null) {
			Intent intent = new Intent(this, RecordEditActivity.class);
			intent.putExtra(Constants.OPER_ACTION, Constants.OPER_ADDNEW);
			intent.putExtra(Constants.CATEGORY, category);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, CategorySelectActivity.class);
			intent.putExtra(Constants.OPER_ACTION, Constants.OPER_ADDNEW);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
