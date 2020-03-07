package com.ghut.passbox.activity;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghut.passbox.R;
import com.ghut.passbox.util.LogUtils;

/**
 * @author Gary
 *
 */
public abstract class HeaderActivity extends AbstractActivity {

	private ImageView imgBack;
	private TextView lblTitle;

	protected void setViews() {
		super.setViews();
		imgBack = (ImageView) this.findViewById(R.id.imgBack);
		lblTitle = (TextView) this.findViewById(R.id.lblTitle);
		lblTitle.setText(getGZTitle());
	}

	/**
	 * For extension purpose
	 * 
	 * @param resId
	 */
	protected void setBackImage(int resId) {
		imgBack.setImageResource(resId);
	}

	protected abstract String getGZTitle();

	protected void setGZTitle(String title) {
		lblTitle.setText(title);
	}

	protected void setListeners() {
		super.setListeners();
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgBack_OnClickListener();
			}
		});
	}

	protected void imgBack_OnClickListener() {
		LogUtils.info(this.getClass(), "fire back button!");
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
