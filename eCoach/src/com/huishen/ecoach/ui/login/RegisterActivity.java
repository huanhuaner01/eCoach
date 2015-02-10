package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 负责引导完成用户的注册操作。
 * @author Muyangmin
 * @create 2015-2-10
 */
public class RegisterActivity extends RightSideParentActivity {
	
	public static Intent getIntent(Context context){
		Intent intent = new Intent(context, RegisterActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
}
