package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//设置界面
public class SettingActivity extends RightSideParentActivity {

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}
}
