package com.ghut.passbox.activity.impl;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ghut.passbox.R;
import com.ghut.passbox.activity.HeaderActivity;

/**
 * @author Gary
 *
 */
public class AboutActivity extends HeaderActivity {
	
	private Button btnMore;

	@Override
	protected String getGZTitle() {
		return "关于";
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_about;
	}
	
	@Override
	protected void setViews() {
		super.setViews();
		btnMore = (Button)this.findViewById(R.id.btnMore);
	}
	
	@Override
	protected void setListeners() {
		super.setListeners();
		btnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnMore_onClick(v);
			}

		});
	}

	private void btnMore_onClick(View v) {
		Uri uri = Uri.parse("http://mp.weixin.qq.com/s?__biz=MzIxMTQxMjA4OQ==&mid=2247483692&idx=1&sn=d18ab3dd49953b625e4eb521c59826b4&scene=0#wechat_redirect");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
