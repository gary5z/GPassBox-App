package com.ghut.passbox.activity.impl;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Account;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.LogUtils;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 * 
 */
public class RecordEditActivity extends HeaderActivity {

	private static final int MSG_SAVE_ACCOUNT = 0;
	private static final int MSG_LOAD_CATEGORY_OK = 1;

	private TextView lblUserName;
	private TextView lblPassword;
	private TextView lblSite;
	private TextView lblRemark;

	private Spinner spinCategory;
	private EditText txtTitle;
	private EditText txtUserName;
	private EditText txtPassword;
	private EditText txtSite;
	private EditText txtRemark;
	private ImageView btnSave;
	
	private TableRow rowTitle;
	private ImageView imgTitleLine;
	private TableRow rowName;
	private ImageView imgNameLine;
	private TableRow rowPassword;
	private ImageView imgPasswordLine;
	private TableRow rowSite;
	private ImageView imgSiteLine;
	private TableRow rowRemark;
	private ImageView imgRemarkLine;

	private List<Category> categoryList;
	private Category category;
	private Account account;
	private String operAction = Constants.OPER_ADDNEW;
	private Class<?> lastActivity;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_SAVE_ACCOUNT:
			String res = (String) msg.obj;
			if (res.equals("ok")) {
				ClientHelper.showLongToast(this, "保存成功");

				LogUtils.info(RecordEditActivity.class, "lastActivity: "
						+ lastActivity);

				// refresh lastActivity
				// if(lastActivity != null) {
				// Intent intent = new Intent(this, lastActivity);
				// intent.putExtra(Constants.CATEGORY, category);
				// startActivity(intent);
				// }

				finish();
			} else {
				ClientHelper.showShortToast(RecordEditActivity.this, res);
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
		return "新增记录";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_record_edit;
	}

	protected void setViews() {
		super.setViews();
		lblUserName = (TextView) this.findViewById(R.id.lblUserName);
		lblPassword = (TextView) this.findViewById(R.id.lblPassword);
		lblSite = (TextView) this.findViewById(R.id.lblSite);
		lblRemark = (TextView) this.findViewById(R.id.lblRemark);
		
		spinCategory = (Spinner) this.findViewById(R.id.spinCategory);
		txtTitle = (EditText) this.findViewById(R.id.txtTitle);
		txtUserName = (EditText) this.findViewById(R.id.txtUserName);
		txtPassword = (EditText) this.findViewById(R.id.txtPassword);
		txtSite = (EditText) this.findViewById(R.id.txtSite);
		txtRemark = (EditText) this.findViewById(R.id.txtRemark);
		btnSave = (ImageView) this.findViewById(R.id.imgCheck);
		btnSave.setVisibility(View.VISIBLE);
		
		rowTitle = (TableRow) this.findViewById(R.id.rowTitle);
		imgTitleLine = (ImageView) this.findViewById(R.id.imgTitleLine);
		rowName = (TableRow) this.findViewById(R.id.rowName);
		imgNameLine = (ImageView) this.findViewById(R.id.imgNameLine);
		rowPassword = (TableRow) this.findViewById(R.id.rowPassword);
		imgPasswordLine = (ImageView) this.findViewById(R.id.imgPasswordLine);
		rowSite = (TableRow) this.findViewById(R.id.rowSite);
		imgSiteLine = (ImageView) this.findViewById(R.id.imgSiteLine);
		rowRemark = (TableRow) this.findViewById(R.id.rowRemark);
		imgRemarkLine = (ImageView) this.findViewById(R.id.imgRemarkLine);
	}

	protected void loadFields() {
		super.loadFields();
		Intent intent = this.getIntent();
		category = (Category) intent.getSerializableExtra(Constants.CATEGORY);
		account = (Account) intent.getSerializableExtra(Constants.ACCOUNT);
		operAction = intent.getStringExtra(Constants.OPER_ACTION);

		LogUtils.info(this.getClass(), intent.getExtras());

		if (Constants.OPER_EDIT.equals(operAction) && account != null) {
			txtTitle.setText(account.getTitle());
			txtUserName.setText(account.getLoginName());
			txtPassword.setText(account.getLoginPwd());
			txtSite.setText(account.getSite());
			txtRemark.setText(account.getRemark());
			setGZTitle("修改记录");
		}
		
		// 根据category设置录入页面显示
		resetControlView();

		new MessageThread() {
			public Message buildMessage() {
				categoryList = serviceContext.getAllCategory();
				Message msg = new Message();
				msg.what = MSG_LOAD_CATEGORY_OK;
				return msg;
			}
		}.start();
	}
	
	private void resetControlView() {
		rowTitle.setVisibility(View.VISIBLE);
		imgTitleLine.setVisibility(View.VISIBLE);
		rowName.setVisibility(View.VISIBLE);
		imgNameLine.setVisibility(View.VISIBLE);
		rowPassword.setVisibility(View.VISIBLE);
		imgPasswordLine.setVisibility(View.VISIBLE);
		rowSite.setVisibility(View.VISIBLE);
		imgSiteLine.setVisibility(View.VISIBLE);
		rowRemark.setVisibility(View.VISIBLE);
		imgRemarkLine.setVisibility(View.VISIBLE);
		
		if(category != null) {
			switch (category.getCatType()) {
			case NORMAL:
				lblUserName.setText("用户*");
				txtUserName.setHint("用户");
				
				lblPassword.setText("密码*");
				txtPassword.setHint("密码");
				
				lblSite.setText("网址");
				txtSite.setHint("网址");
				
				lblRemark.setText("备注");
				txtRemark.setHint("备注");
				break;
			case EMAIL:
				lblUserName.setText("邮箱*");
				txtUserName.setHint("邮箱");
				
				lblPassword.setText("密码*");
				txtPassword.setHint("密码");
				
				lblSite.setText("网址");
				txtSite.setHint("网址");
				
				lblRemark.setText("备注");
				txtRemark.setHint("备注");
				break;
			case FINANCIAL:
				lblUserName.setText("账户*");
				txtUserName.setHint("账户");
				
				lblPassword.setText("密码1*");
				txtPassword.setHint("密码1");
				
				lblSite.setText("密码2");
				txtSite.setHint("密码2");
				
				lblRemark.setText("备注");
				txtRemark.setHint("备注");
				break;
			case NOTE:
				lblRemark.setText("备注*");
				txtRemark.setHint("备注");
				rowTitle.setVisibility(View.VISIBLE);
				imgTitleLine.setVisibility(View.VISIBLE);
				rowName.setVisibility(View.GONE);
				imgNameLine.setVisibility(View.GONE);
				rowPassword.setVisibility(View.GONE);
				imgPasswordLine.setVisibility(View.GONE);
				rowSite.setVisibility(View.GONE);
				imgSiteLine.setVisibility(View.GONE);
				rowRemark.setVisibility(View.VISIBLE);
				imgRemarkLine.setVisibility(View.VISIBLE);
				break;
			default:
				lblUserName.setText("用户*");
				txtUserName.setHint("用户");
				break;
			}
		}

	}

	private void setAdapter() {
		int selectedCategory = 0;
		for (int i = 0; category != null && i < categoryList.size(); i++) {
			if (category.equals(categoryList.get(i))) {
				selectedCategory = i;
			}
		}

		// SimpleAdapter adapter = new SimpleAdapter(this, data,
		// R.layout.activity_main_list_item, new String[] { "data" },
		// new int[] { R.id.lblCatMsgTv });
		String[] categoryArray = new String[categoryList.size()];
		for (int i = 0; i < categoryList.size(); i++) {
			categoryArray[i] = categoryList.get(i).getCatName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categoryArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCategory.setAdapter(adapter);
		spinCategory.setSelection(selectedCategory);
	}

	protected void setListeners() {
		super.setListeners();
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new MessageThread() {
					public Message buildMessage() {
						Message msg = new Message();
						msg.what = MSG_SAVE_ACCOUNT;
						msg.obj = save();
						return msg;
					}
				}.start();
			}
		});

		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				category = categoryList.get(position);
				if (account != null) {
					account.setCategory(category);
				}

				// 根据category设置录入页面显示
				resetControlView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	private String save() {

		String res = "ok";

		category = categoryList.get(spinCategory
				.getSelectedItemPosition());
		if(category == null) {
			return "请选择分类";
		}

		switch (category.getCatType()) {
		case NORMAL:
			if (StringUtils.isEmpty(txtTitle.getText().toString())) {
				return "请输入标题!";
			}

			if (StringUtils.isEmpty(txtUserName.getText().toString())) {
				return "请输入用户!";
			}

			if (StringUtils.isEmpty(txtPassword.getText().toString())) {
				return "请输入密码!";
			}
			break;
		case EMAIL:
			if (StringUtils.isEmpty(txtTitle.getText().toString())) {
				return "请输入标题!";
			}

			if (StringUtils.isEmpty(txtUserName.getText().toString())) {
				return "请输入邮箱!";
			}

			if (StringUtils.isEmpty(txtPassword.getText().toString())) {
				return "请输入密码!";
			}
			break;
		case FINANCIAL:
			if (StringUtils.isEmpty(txtTitle.getText().toString())) {
				return "请输入标题!";
			}

			if (StringUtils.isEmpty(txtUserName.getText().toString())) {
				return "请输入账户!";
			}

			if (StringUtils.isEmpty(txtPassword.getText().toString())) {
				return "请输入密码1!";
			}
			break;
		case NOTE:
			if (StringUtils.isEmpty(txtTitle.getText().toString())) {
				return "请输入标题!";
			}

			if (StringUtils.isEmpty(txtRemark.getText().toString())) {
				return "请输入备注!";
			}
			break;
		default:
			break;
		}

		if (account == null) {
			account = new Account();
		}
		account.setCategory(category);
		account.setTitle(txtTitle.getText().toString());
		account.setLoginName(txtUserName.getText().toString());
		account.setLoginPwd(txtPassword.getText().toString());
		account.setSite(txtSite.getText().toString());
		account.setRemark(txtRemark.getText().toString());

		if (Constants.OPER_ADDNEW.equals(operAction)) {
			res = serviceContext.addAccount(account);
		} else if (Constants.OPER_EDIT.equals(operAction)) {
			res = serviceContext.updateAccount(account);
		} else {
			res = "抱歉，页面状态不正确，请尝试重新登入！";
		}

		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
