package com.ghut.passbox;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ghut.passbox.common.Constants;
import com.ghut.passbox.common.db.DBHelper;
import com.ghut.passbox.util.ClientHelper;
import com.ghut.passbox.util.FileUtils;

/**
 * 
 * @author Gary
 * 
 */
public class PassBoxApplication extends Application {

	private static final int MSG_INIT_FAILED = 0;

	private static final int MSG_DESTROY_FAILED = 1;

	private static PassBoxApplication application;

	private DBHelper dbHelper;

	private Map<String, Object> session = new HashMap<String, Object>();

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			String res = (String) msg.obj;
			switch (msg.what) {
			case MSG_INIT_FAILED:
			case MSG_DESTROY_FAILED:
				ClientHelper.showLongToast(PassBoxApplication.this, res);
				break;
			default:
				break;
			}
		}
	};

	public static PassBoxApplication getInstance() {
		return application;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		dbHelper = new DBHelper(this, Constants.DATABASE_NAME);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					onInit();
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = MSG_INIT_FAILED;
					msg.obj = e.getMessage();
					handler.sendMessage(msg);
				}
			}

		}).start();
	}

	private void onInit() {
		dbHelper.open();
	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					onDestroy();
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = MSG_DESTROY_FAILED;
					msg.obj = e.getMessage();
					handler.sendMessage(msg);
				}
			}

		}).start();
	}

	private void onDestroy() {
		dbHelper.close();
		String startsWith = Constants.DATABASE_NAME.substring(0,
				Constants.DATABASE_NAME.indexOf("\\."));
		FileUtils.cleanExternalCache(PassBoxApplication.this, startsWith);
	}

	public DBHelper getDBHelper() {
		return dbHelper;
	}

	public byte[] getAESKey() {
		return (byte[]) getSession(Constants.AES_KEY);
	}

	public byte[] getHMACKey() {
		return (byte[]) getSession(Constants.HMAC_KEY);
	}

	public void setSession(String key, Object o) {
		session.put(key, o);
	}

	public Object getSession(String key) {
		return session.get(key);
	}

	public void removeSession(String key) {
		session.remove(key);
	}
}
