package com.ghut.passbox.activity.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.FileChooserActivity;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.FileUtils;
import com.ghut.passbox.util.StringUtils;
import com.ghut.passbox.util.ClientHelper.ConfirmListener;

/**
 * 
 * @author Gary
 * 
 */
public class BackupRestoreActivity extends HeaderActivity {

	private static final int MSG_BACKUP_SUCCESS = 0;
	private static final int MSG_BACKUP_FAILED = 1;
	private static final int MSG_RESTORE_SUCCESS = 2;
	private static final int MSG_RESTORE_FAILED = 3;
	private static final int MSG_SEND_EMAIL_SUCCESS = 4;
	private static final int MSG_SEND_EMAIL_FAILED = 5;

	private static final int OPERATE_BACKUP = 1;
	private static final int OPERATE_RESTORE = 2;
	// private static final int OPERATE_EMAIL=2;

	private static final String EMAIL_REGULAR = "(.+)@(.+)(\\.)(.+)";

	private TextView lblBackup;
	private TextView lblRestore;
	private TextView lblEmail;
	private ImageView btnSelectBackup;
	private ImageView btnSelectRestore;
	private ImageView btnBindEmail;
	private Button btnBackup;
	private Button btnRestore;
	private Button btnSendToEmail;

	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_BACKUP_SUCCESS:
			ClientHelper.showLongToast(BackupRestoreActivity.this, "已备份："
					+ msg.obj);
			break;
		case MSG_RESTORE_SUCCESS:
			ClientHelper.showShortToast(BackupRestoreActivity.this, "还原成功！");
			break;
		case MSG_SEND_EMAIL_SUCCESS:
			// ClientHelper.showShortToast(BackupRestoreActivity.this, "发送成功！");
			break;
		default:
			ClientHelper.showShortToast(BackupRestoreActivity.this, msg.obj);
			break;
		}
	}

	@Override
	protected String getGZTitle() {
		return "备份还原";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_backup_restore;
	}

	protected void setViews() {
		super.setViews();
		lblBackup = (TextView) this.findViewById(R.id.lblBackup);
		lblRestore = (TextView) this.findViewById(R.id.lblRestore);
		lblEmail = (TextView) this.findViewById(R.id.lblEmail);
		btnSelectBackup = (ImageView) this.findViewById(R.id.btnSelectBackup);
		btnSelectRestore = (ImageView) this.findViewById(R.id.btnSelectRestore);
		btnBindEmail = (ImageView) this.findViewById(R.id.btnBindEmail);
		btnBackup = (Button) this.findViewById(R.id.btnBackup);
		btnRestore = (Button) this.findViewById(R.id.btnRestore);
		btnSendToEmail = (Button) this.findViewById(R.id.btnSendToEmail);

		// 设置初始值
		if (StringUtils.isNotEmpty(serviceContext.getEmail())) {
			lblEmail.setText(serviceContext.getEmail());
		}
		lblBackup.setText(FileUtils.getSDDir());
	}

	protected void setListeners() {
		super.setListeners();
		btnSelectBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BackupRestoreActivity.this,
						FileChooserActivity.class);
				intent.putExtra(Constants.OPER_ACTION, Constants.OPER_BACKUP);
				startActivityForResult(intent, OPERATE_BACKUP);
			}
		});

		btnSelectRestore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BackupRestoreActivity.this,
						FileChooserActivity.class);
				intent.putExtra(Constants.OPER_ACTION, Constants.OPER_RESTORE);
				startActivityForResult(intent, OPERATE_RESTORE);
			}
		});

		btnBindEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bindEmail();
			}

		});

		btnBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MessageThread() {
					public Message buildMessage() {
						Message msg = new Message();
						backup(msg);
						return msg;
					}
				}.start();
			}
		});

		btnRestore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String file = lblRestore.getText().toString();
				if (!FileUtils.isFile(file)) {
					ClientHelper.showShortToast(BackupRestoreActivity.this,
							"请选择文件！");
					return;
				}

				String title = "请输入密码";
				ClientHelper.showInputDialog(BackupRestoreActivity.this, title,
						new ConfirmListener() {
							@Override
							public void onConfirm(final String input) {
								new MessageThread() {
									public Message buildMessage() {
										Message msg = new Message();
										restore(msg, input);
										return msg;
									}
								}.start();
							}
						});
			}
		});

		btnSendToEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MessageThread() {
					public Message buildMessage() {
						Message msg = new Message();
						sendEmail(msg);
						return msg;
					}
				}.start();
			}
		});

	}

	private void bindEmail() {
		ClientHelper.showInputDialog(this, "绑定邮箱",
				new ClientHelper.ConfirmListener() {

					@Override
					public void onConfirm(String input) {
						if (input.matches(EMAIL_REGULAR)) {
							String res = serviceContext.bindEmail(input);
							if ("ok".equals(res)) {
								lblEmail.setText(input);
							} else {
								ClientHelper.showShortToast(
										BackupRestoreActivity.this, res);
							}
						} else {
							ClientHelper.showShortToast(
									BackupRestoreActivity.this, "请输入正确的邮箱！");
						}
					}
				});
	}

	private void backup(final Message msg) {
		msg.what = MSG_BACKUP_FAILED;
		String path = lblBackup.getText().toString();
		if (StringUtils.isEmpty(path)) {
			msg.obj = "请选择备份目录!";
			return;
		}

		String res = serviceContext.backup(path);
		if (res.startsWith("ok")) {
			msg.what = MSG_BACKUP_SUCCESS;
			msg.obj = res.substring(res.indexOf("ok") + 1, res.length());
		}
	}

	private void restore(final Message msg, final String input) {
		msg.what = MSG_RESTORE_FAILED;
		String file = lblRestore.getText().toString();
		if (StringUtils.isEmpty(file)) {
			msg.obj = "请选择文件!";
			return;
		}

		msg.obj = serviceContext.restore(file, input);
		if ("ok".equals(msg.obj)) {
			msg.what = MSG_RESTORE_SUCCESS;
		}
	}

	private void sendEmail(final Message msg) {
		msg.what = MSG_SEND_EMAIL_FAILED;

		String email = lblEmail.getText().toString();
		if (StringUtils.isEmpty(email) || !email.matches(EMAIL_REGULAR)) {
			msg.obj = "请绑定邮箱!";
			return;
		}

		msg.obj = serviceContext.backupToEmail(this, email);
		if ("ok".equals(msg.obj)) {
			msg.what = MSG_SEND_EMAIL_SUCCESS;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (OPERATE_BACKUP == requestCode) {
			Bundle bundle = null;
			if (data != null && (bundle = data.getExtras()) != null) {
				lblBackup.setText(bundle.getString("file"));
			}
		}
		if (OPERATE_RESTORE == requestCode) {
			Bundle bundle = null;
			if (data != null && (bundle = data.getExtras()) != null) {
				lblRestore.setText(bundle.getString("file"));
			}
		}
		// if(OPERATE_EMAIL == requestCode){
		// Bundle bundle = null;
		// if(data!=null&&(bundle=data.getExtras())!=null){
		// lblEmail.setText(bundle.getString("file"));
		// }
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
