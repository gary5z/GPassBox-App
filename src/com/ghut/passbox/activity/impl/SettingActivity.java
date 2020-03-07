package com.ghut.passbox.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;
import com.ghut.passbox.util.ClientHelper;

/**
 * 
 * @author Gary
 *
 */
public class SettingActivity extends HeaderActivity {

	private ListView lvContent;

	@Override
	protected String getGZTitle() {
		return "设置";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_setting;
	}

	protected void setViews() {
		super.setViews();
		lvContent = (ListView) this.findViewById(R.id.lvContent);
		setAdapter();
	}

	private void setAdapter() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		addLine(data, "分类管理");
		addLine(data, "修改密码");
		addLine(data, "备份还原");
		addLine(data, "联系我");
		addLine(data, "关于");

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.activity_main_item, new String[] { "data" },
				new int[] { R.id.lblCatMsgTv });
		lvContent.setAdapter(adapter);
	}

	private void addLine(List<Map<String, String>> data, String displayText) {
		Map<String, String> totalMap = new HashMap<String, String>();
		totalMap.put("data", displayText);
		data.add(totalMap);
	}

	protected void setListeners() {
		super.setListeners();
		lvContent.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				lvContent_OnItemClickListener(position);
			}
		});
	}

	private void lvContent_OnItemClickListener(int position) {
		switch (position) {
		case 0:
			showCategory();
			break;
		case 1:
			showRepwd();
			break;
		case 2:
			showBackup();
			break;
		case 3:
			contactMe();
			break;
		case 4:
			showAbout();
			break;
		}
	}

	public void showCategory() {
		Intent intent = new Intent(this, CategoryListActivity.class);
		startActivity(intent);
	}

	public void showRepwd() {
		Intent intent = new Intent(this, RepwdActivity.class);
		startActivity(intent);
	}

	public void showBackup() {
		Intent intent = new Intent(this, BackupRestoreActivity.class);
		startActivity(intent);
	}

	public void contactMe() {
		ClientHelper.sendEmail(this, "gary_z@163.com", "Hello, GZ Hut");
//		Intent email = new Intent(android.content.Intent.ACTION_SEND);  
//		//邮件发送类型：无附件，纯文本  
//		email.setType("plain/text");  
//		//邮件接收者（数组，可以是多位接收者）  
//		String[] emailReciver = new String[]{"123@qq.com","456@163.com"};  
//		  
//		String  emailTitle = "标题";  
//		String emailContent = "内容";  
//		//设置邮件地址  
//		 email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);  
//		//设置邮件标题  
//		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);  
//		//设置发送的内容  
//		email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);  
//		 //调用系统的邮件系统  
//		Intent.createChooser(email, "请选择邮件发送软件");
//		startActivity(email);
		
//		Intent data=new Intent(Intent.ACTION_SENDTO);  
//		data.setData(Uri.parse("mailto:qq10000@qq.com"));  
//		data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");  
//		data.putExtra(Intent.EXTRA_TEXT, "这是内容");  
//		startActivity(data); 
	}

	public void showAbout() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
