package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.NewIntentParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SnapSuccessActivity extends NewIntentParentActivity {
	
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, SnapSuccessActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snap_success);
	}
}
