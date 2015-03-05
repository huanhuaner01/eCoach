package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import com.huishen.ecoach.umeng.BindCoachPushData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BindCoachActivity extends Activity {
	
	private static final String EXTRA_BINDCOACH = "bind";
	private static final String LOG_TAG = "BindCoachActivity";
	private BindCoachPushData bindData;
	
	public static Intent getIntent(Context context, BindCoachPushData bindData){
		Intent intent = new Intent(context, BindCoachActivity.class);
		intent.putExtra(EXTRA_BINDCOACH, bindData);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_coach);
		try {
			bindData = (BindCoachPushData) getIntent().getSerializableExtra(EXTRA_BINDCOACH);
		} catch(Exception e) {
			Log.e(LOG_TAG, "FATAL ERROR: extra value must be instance of NewOrderPushData.");
			finish();
			return ;
		}
		displayBindInfo();
	}
	
	private void displayBindInfo() {
		TextView tvName = (TextView)findViewById(R.id.bindcoach_tv_name);
		TextView tvPhone = (TextView)findViewById(R.id.bindcoach_tv_phone);
		TextView tvAddiInfo = (TextView)findViewById(R.id.bindcoach_tv_addinfo);
		tvName.setText(bindData.name);
		tvPhone.setText(bindData.phoneNumber);
		tvAddiInfo.setText(bindData.addtionalInfo);
	}
}
