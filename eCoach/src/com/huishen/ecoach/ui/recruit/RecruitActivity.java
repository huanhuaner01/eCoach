package com.huishen.ecoach.ui.recruit;

import com.huishen.ecoach.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RecruitActivity extends Activity {
	
	public static Intent getIntent(Context context){
		Intent intent = new Intent(context, RecruitActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruit);
	}
}
