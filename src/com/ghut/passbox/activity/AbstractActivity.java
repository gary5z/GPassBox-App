package com.ghut.passbox.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ghut.passbox.common.ServiceContext;

/**
 * @author Gary
 *
 */
public abstract class AbstractActivity extends Activity {

	protected ServiceContext serviceContext;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			AbstractActivity.this.handleMessage(msg);
		}
	};

	protected void handleMessage(Message msg) {

	}

	protected class MessageThread extends Thread {
		@Override
		public void run() {
			Message msg = buildMessage();
			handler.sendMessage(msg);
		}

		protected Message buildMessage() {
			return null;
		}
	}

	protected void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}

	protected void sendEmptyMessageDelayed(int what, long delayMillis) {
		handler.sendEmptyMessageDelayed(what, delayMillis);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		serviceContext = ServiceContext.getInstance();

		setViews();
		setListeners();
	}

	protected abstract int getContentView();

	protected void setViews() {
		setContentView(getContentView());
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadFields();
	}

	protected void loadFields() {
		// TODO Auto-generated method stub

	}

	protected void setListeners() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
