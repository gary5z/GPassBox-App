package com.ghut.passbox.activity.impl;

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

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.CategoryTypeEnum;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.model.Category;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 * 
 */
public class CategoryEditActivity extends HeaderActivity {

	private static final int MSG_SAVE_DATA = 0;
	private static final int MSG_LOAD_DATA_OK = 1;

	private Spinner spinType;
	private EditText txtName;
	private EditText txtRemark;
	private ImageView btnSave;

	private Category category;
	private String operAction = Constants.OPER_ADDNEW;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_SAVE_DATA:
			String res = (String) msg.obj;
			if (res.equals("ok")) {
				ClientHelper.showLongToast(CategoryEditActivity.this, "保存成功");
				finish();
			} else {
				ClientHelper.showShortToast(CategoryEditActivity.this, res);
			}
			break;
		case MSG_LOAD_DATA_OK:
			setAdapter();
			break;
		default:
			break;
		}
	}

	@Override
	protected String getGZTitle() {
		return "新增分类";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_category_edit;
	}

	protected void setViews() {
		super.setViews();
		spinType = (Spinner) this.findViewById(R.id.spinType);
		txtName = (EditText) this.findViewById(R.id.txtName);
		txtRemark = (EditText) this.findViewById(R.id.txtRemark);
		btnSave = (ImageView) this.findViewById(R.id.imgCheck);
		btnSave.setVisibility(View.VISIBLE);
	}

	protected void loadFields() {
		super.loadFields();
		Intent intent = this.getIntent();
		category = (Category) intent.getSerializableExtra(Constants.CATEGORY);
		operAction = intent.getStringExtra(Constants.OPER_ACTION);
		if (Constants.OPER_EDIT.equals(operAction) && category != null) {
			txtName.setText(category.getCatName());
			txtRemark.setText(category.getRemark());
			setGZTitle("修改分类");
		}

		new MessageThread() {
			public Message buildMessage() {
				Message msg = new Message();
				msg.what = MSG_LOAD_DATA_OK;
				return msg;
			}
		}.start();
	}

	private void setAdapter() {
		int selectedCategory = category == null ? 0
				: (category.getCatType().value - 1);

		ArrayAdapter<CategoryTypeEnum> adapter = new ArrayAdapter<CategoryTypeEnum>(
				this, android.R.layout.simple_spinner_item,
				CategoryTypeEnum.values());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinType.setAdapter(adapter);
		spinType.setSelection(selectedCategory);
	}

	protected void setListeners() {
		super.setListeners();

		spinType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				CategoryTypeEnum type = CategoryTypeEnum.valueOf(position - 1);
				if (category != null) {
					category.setCatType(type);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MessageThread() {
					public Message buildMessage() {
						Message msg = new Message();
						msg.what = MSG_SAVE_DATA;
						msg.obj = save();
						return msg;
					}
				}.start();
			}
		});
	}

	private String save() {
		String res = "ok";

		if (StringUtils.isEmpty(txtName.getText().toString())) {
			return "请输入" + txtName.getHint().toString() + "!";
		}

		if (category == null) {
			category = new Category();
		}
		category.setCatType((CategoryTypeEnum) spinType.getSelectedItem());
		category.setCatName(txtName.getText().toString());
		category.setRemark(txtRemark.getText().toString());

		if (Constants.OPER_ADDNEW.equals(operAction)) {
			res = serviceContext.addCategory(category);
		} else if (Constants.OPER_EDIT.equals(operAction)) {
			res = serviceContext.updateCategory(category);
		} else {
			res = "页面状态错误，请尝试重新登入！";
		}

		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
