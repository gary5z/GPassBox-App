package com.ghut.passbox.activity.impl;

import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.StringUtils;

/**
 * 
 * @author Gary
 *
 */
public class RepwdActivity extends HeaderActivity {

	private static final int MSG_SAVE = 0;

	private EditText txtPwd;
	private EditText txtNewPwd;
	private EditText txtConfirmPwd;
	private Button btnSave;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_SAVE:
			String res = (String) msg.obj;
			if (res.equals("ok")) {
				ClientHelper.showLongToast(RepwdActivity.this, "修改成功");
				finish();
			} else {
				ClientHelper.showShortToast(RepwdActivity.this, res);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected String getGZTitle() {
		return "重设密码";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_repwd;
	}

	protected void setViews() {
		super.setViews();
		txtPwd = (EditText) this.findViewById(R.id.txtPwd);
		txtNewPwd = (EditText) this.findViewById(R.id.txtNewPwd);
		txtConfirmPwd = (EditText) this.findViewById(R.id.txtConfirmPwd);
		btnSave = (Button) this.findViewById(R.id.btnSave);
	}

	protected void setListeners() {
		super.setListeners();
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MessageThread() {
					public Message buildMessage() {
						Message msg = new Message();
						msg.what = MSG_SAVE;
						msg.obj = save();
						return msg;
					}
				}.start();
			}
		});
	}

	private String save() {
		String res = "ok";
		String oldpwd = txtPwd.getText().toString();
		String newpwd = txtNewPwd.getText().toString();

		if (StringUtils.isEmpty(oldpwd)) {
			return "请输入密码！";
		}
		if (StringUtils.isEmpty(newpwd)) {
			return "请输入新密码！";
		}
		if (StringUtils.isEmpty(txtConfirmPwd.getText().toString())) {
			return "请输入确认密码！";
		}
		if (!newpwd.equals(txtConfirmPwd.getText().toString())) {
			return "两次输入的密码不一致！";
		}

		try {
			res = serviceContext.modifyPwd(oldpwd, newpwd);	
		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
