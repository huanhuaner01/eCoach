package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends Activity {
	
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, LoginActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
}
