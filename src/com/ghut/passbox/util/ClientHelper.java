package com.ghut.passbox.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ghut.passbox.R;

/**
 * 
 * @author Gary
 * 
 */
@SuppressLint("NewApi")
public class ClientHelper {

	public static void showDialog(Context context, String msgContent) {
		showDialog(context, "提示", msgContent);
	}

	public static void showDialog(Context context, String title,
			String msgContent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msgContent);
		builder.create().show();
	}

	public static void showInputDialog(Context context, String title,
			ConfirmListener confirmListener) {
		EditText inputEditor = new EditText(context);
		confirmListener.setEdit(inputEditor);
		showDialog(context, title, confirmListener);
	}

	public static void showDialog(Context context, String title,
			ConfirmListener confirmListener) {
		showDialog(context, title, null, confirmListener);
	}

	public static void showDialog(Context context, String title,
			String content, ConfirmListener confirmListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title); // 标题
		builder.setMessage(content); // 内容
		builder.setIcon(android.R.drawable.ic_dialog_info); // 设置对话框标题前的图标
		builder.setView(confirmListener.getEdit());
		builder.setPositiveButton("确定", confirmListener);
		builder.setNegativeButton("取消", null);
		builder.setCancelable(true); // 设置按钮是否可以按返回键取消,false则不可以取消
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true); // 设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
		dialog.show();
	}

	public static class ConfirmListener implements
			DialogInterface.OnClickListener {

		private EditText edit;

		@Override
		public void onClick(DialogInterface dialog, int which) {
			try {
				String input = (edit == null) ? "" : edit.getText().toString();
				onConfirm(input);
			} catch (Exception e) {
				LogUtils.error(ClientHelper.class, e);
			}

			try {
				onConfirm();
			} catch (Exception e) {
				LogUtils.error(ClientHelper.class, e);
			}
		}

		public void onConfirm(String input) {

		};

		public void onConfirm() {

		};

		/**
		 * @param edit
		 *            the edit to set
		 */
		public void setEdit(EditText edit) {
			this.edit = edit;
		}

		/**
		 * @return the edit
		 */
		public EditText getEdit() {
			return edit;
		}
	}

	public static void showLongToast(Context context, Object msg) {
		Toast.makeText(context, msg == null ? "" : msg.toString(),
				Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, Object msg) {
		Toast.makeText(context, msg == null ? "" : msg.toString(),
				Toast.LENGTH_SHORT).show();
	}

	public static void sendEmail(Context context, String receiver, String title) {
		Intent data = new Intent(Intent.ACTION_SENDTO);
		data.setData(Uri.parse("mailto:" + receiver));
		data.putExtra(Intent.EXTRA_SUBJECT, title);
		data.putExtra(Intent.EXTRA_TEXT, "");
		context.startActivity(data);
	}

	public static void sendEmail(Context context, String receiver,
			String title, File attachment) {

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		String[] tos = { receiver }; // 接收人
		// String[] ccs = { "gary_z@163.com" }; // 抄送人
		// String[] bccs = {"gary_z@163.com"}; // 密送人
		emailIntent.putExtra(Intent.EXTRA_EMAIL, tos);
		// intent.putExtra(Intent.EXTRA_CC, ccs);
		// intent.putExtra(Intent.EXTRA_BCC, bccs);
		// intent.putExtra(Intent.EXTRA_TEXT, content); // 邮件内容
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, title); // 主题

		emailIntent.setType("plain/text"); // 无附件，纯文本
		if (attachment != null && attachment.isFile() && attachment.exists()) {
			// intent.setType("image/*");
			// intent.setType("message/rfc882");
			emailIntent.setType("application/octet-stream");
			try {
				emailIntent.putExtra(Intent.EXTRA_STREAM,
						Uri.parse("file://" + attachment.getCanonicalPath()));
			} catch (IOException e) {
				LogUtils.error(ClientHelper.class, e);
			}
		}
		Intent.createChooser(emailIntent, "Choose Email Client");
		context.startActivity(emailIntent);
	}

	/**
	 * 创始带有more图标的adapter
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static SimpleAdapter buildExpendAdapter(Context context,
			String[] listData) {

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		for (int i = 0; listData != null && i < listData.length; i++) {
			Map<String, String> dm = new HashMap<String, String>();
			dm.put("data", listData[i]);
			data.add(dm);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.activity_main_item, new String[] { "data" },
				new int[] { R.id.lblCatMsgTv });
		return adapter;
	}

	/**
	 * 创建不带more图标的adapter
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static ListAdapter buildListAdapter(Context context,
			String[] listData) {

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		for (int i = 0; listData != null && i < listData.length; i++) {
			Map<String, String> dm = new HashMap<String, String>();
			dm.put("data", listData[i]);
			data.add(dm);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.activity_entry_item, new String[] { "data" },
				new int[] { R.id.lblItemTv }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);

				ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
				ImageView imgDelete = (ImageView) view
						.findViewById(R.id.imgDelete);

				imgEdit.setVisibility(View.GONE);
				imgDelete.setVisibility(View.GONE);

				return view;
			}
		};
		return adapter;
	}

	/**
	 * 创建不带more图标的adapter
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static SimpleAdapter buildListAdapter(final Context context,
			String[] listData, final ClickListener editListener,
			final ClickListener deleteListener) {

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		for (int i = 0; listData != null && i < listData.length; i++) {
			Map<String, String> dm = new HashMap<String, String>();
			dm.put("data", listData[i]);
			data.add(dm);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.activity_entry_item, new String[] { "data" },
				new int[] { R.id.lblItemTv }) {

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				View view = super.getView(position, convertView, parent);

				// 编辑/删除 按钮
				ImageView imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
				ImageView imgDelete = (ImageView) view
						.findViewById(R.id.imgDelete);

				imgEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (editListener != null) {
							editListener.onClick(position);
						}
					}
				});
				imgDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (deleteListener != null) {
							deleteListener.onClick(position);
						}
					}
				});
				return view;
			}
		};
		return adapter;
	}

	public static class ClickListener {

		/**
		 * @param position
		 */
		public void onClick(int position) {
			// TODO Auto-generated method stub

		}
	}
}
