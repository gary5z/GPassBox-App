package com.ghut.passbox.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.impl.BackupRestoreActivity;
import com.ghut.passbox.common.Constants;
import com.ghut.passbox.util.FileUtils;

public class FileChooserActivity extends ListActivity {

	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = FileUtils.getSDDir();
	private String curPath = FileUtils.getSDDir();
	private TextView mPath;
	private Button btnOK;
	private FileChooserAdapter adapter;
	private String operation = Constants.OPER_BACKUP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_chooser);
		mPath = (TextView) findViewById(R.id.mPath);
		btnOK = (Button) findViewById(R.id.btnOK);

		// 当前操作
		Intent intent = this.getIntent();
		operation = intent.getStringExtra(Constants.OPER_ACTION);

		// 设置为单选模式
		this.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// 获取文件目录树
		getFileDir(rootPath);

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnOK_OnClick(v);
			}

		});
	}

	private void btnOK_OnClick(View v) {
		// 默认为当前目录
		String filePath = curPath;

		int selectedPosition = adapter.getSelectedPosition();
		if (selectedPosition == -1) {
			finish();
			return;
		}

		filePath = paths.get(selectedPosition);

		Intent data = new Intent(FileChooserActivity.this,
				BackupRestoreActivity.class);
		Bundle bundle = new Bundle();
		File file = new File(filePath);
		bundle.putString("file", file.getPath());
		data.putExtras(bundle);
		setResult(RESULT_OK, data);
		finish();
	}

	private void getFileDir(String filePath) {
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);

		File[] files = f.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// file is directory or not

				// 去除隐藏文件或目录
				if (pathname.getName().startsWith(".")) {
					return false;
				}

				// 显示目录
				if (pathname.isDirectory()) {
					return true;
				}

				// 备份
				if (Constants.OPER_BACKUP.equals(operation)) {
					return false;
				}

				// 还原
				if (Constants.OPER_RESTORE.equals(operation)) {
					String fileNameString = pathname.getName();
					String endNameString = fileNameString.substring(
							fileNameString.lastIndexOf(".") + 1,
							fileNameString.length());
					if (endNameString.equalsIgnoreCase("bak")) {
						return true;
					} else {
						return false;
					}
				}

				return true;
			}
		});

		if (!filePath.equals(rootPath)) {
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}

		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				if (lhs.isDirectory() && !rhs.isDirectory()) {
					return -1;
				}
				if (!lhs.isDirectory() && rhs.isDirectory()) {
					return 1;
				}

				return lhs.getName().compareTo(rhs.getName());
			}

		});

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			// check file is shp file or not
			// if is,add into list to show
			// if (checkShapeFile(file)) {
			// items.add(file.getName());
			// paths.add(file.getPath());
			// }
			items.add(file.getName());
			paths.add(file.getPath());
		}

		adapter = new FileChooserAdapter(this, items, paths);
		setListAdapter(adapter);
	}

	// open allocate file
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// File file = new File(paths.get(position));
		// if (file.isDirectory()) {
		// curPath = paths.get(position);
		// getFileDir(paths.get(position));
		// } else {
		// Intent data = new Intent(FileChooserActivity.this,
		// BackupRestoreActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putString("file", file.getPath());
		// data.putExtras(bundle);
		// setResult(2, data);
		// finish();
		// }
		curPath = paths.get(position);
		getFileDir(paths.get(position));
	}

	public boolean checkShapeFile(File file) {
		String fileNameString = file.getName();
		String endNameString = fileNameString.substring(
				fileNameString.lastIndexOf(".") + 1, fileNameString.length());
		// file is directory or not
		if (file.isDirectory()) {
			return true;
		}

		if (Constants.OPER_BACKUP.equals(operation)) {
			return false;
		} else if (Constants.OPER_RESTORE.equals(operation)) {
			if (endNameString.equalsIgnoreCase("bak")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

		// if (endNameString.equals("txt")) {
		// return true;
		// } else {
		// return false;
		// }
	}

}
