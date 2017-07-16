package com.casic.bluebot.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.view.ProgressWheel;


public class ScanActivity extends BaseActivity {
	//constants
	private static final String LOG_TAG = "ScanActivity";
	//widgets
	private ProgressWheel pw_search;
	private TextView tv_info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.devicescan_title);
		
		setContentView(R.layout.activity_scan);
		//link widgets
		pw_search = (ProgressWheel) findViewById(R.id.pw_search);
		
		tv_info = (TextView) findViewById(R.id.tv_info);
		
		//demo data
		String info = "正在搜索设备....已发现1台可用设备";
		tv_info.setText(info);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

}
