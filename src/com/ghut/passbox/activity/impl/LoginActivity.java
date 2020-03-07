package com.ghut.passbox.activity.impl;

import android.content.Intent;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.AbstractActivity;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.StringUtils;

/**
 * @author Gary
 *
 */
public class LoginActivity extends AbstractActivity {

	private static final int MSG_LOGIN_SUCCESS = 0;

	private static final int MSG_LOGIN_FAILED = 1;

	private EditText txtLoginPwd;
	private EditText txtConfirmPwd;
	private Button btnLogin;
	private boolean hasRegistered = false;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_LOGIN_SUCCESS:
			ClientHelper.showLongToast(this, "登录成功！");
			showMenu();
			finish();
			break;
		case MSG_LOGIN_FAILED:
			btnLogin.setEnabled(true);
			ClientHelper.showShortToast(LoginActivity.this, msg.obj);
			break;
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_login;
	}

	protected void setViews() {
		super.setViews();
		txtLoginPwd = (EditText) this.findViewById(R.id.txtLoginPwd);
		txtConfirmPwd = (EditText) this.findViewById(R.id.txtConfirmPwd);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);

		if (serviceContext.hasRegistered()) {
			hasRegistered = true;
			txtConfirmPwd.setVisibility(View.GONE);
			txtLoginPwd.setHint("密码");
		}
	}

	protected void setListeners() {
		super.setListeners();
		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btnLogin.setEnabled(false);
				new MessageThread() {
					public Message buildMessage() {
						Message msg = new Message();
						login(msg);
						return msg;
					}
				}.start();
			}
		});
	}

	private void login(final Message msg) {
		msg.what = MSG_LOGIN_FAILED;
		String loginPwd = txtLoginPwd.getText().toString();
		if (StringUtils.isEmpty(loginPwd)) {
			msg.obj = "请输入密码!";
			return;
		}

		if (!hasRegistered) {
			String confirmPwd = txtConfirmPwd.getText().toString();
			if (StringUtils.isEmpty(confirmPwd)) {
				msg.obj = "请输入确认密码!";
				return;
			}

			if (!loginPwd.equals(confirmPwd)) {
				msg.obj = "两次输入的密码不一致，请重新输入!";
				return;
			}
		}

		String result = serviceContext.login(loginPwd);
		if ("ok".equals(result)) {
			msg.what = MSG_LOGIN_SUCCESS;
			msg.obj = "登录成功！";
		} else {
			msg.obj = result;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showMenu() {
		Intent intent = new Intent(this, MainActivity.class);
//		Intent intent = new Intent(this, SpinerWindowDemoActivity.class);
//		Intent intent = new Intent(this, RichEditorActivityDemo.class);
		startActivity(intent);
		finish();
	}

}
