package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.util.Packages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends RightSideParentActivity {

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, AboutUsActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		TextView tvVersion = (TextView) findViewById(R.id.aboutus_tv_version);
		tvVersion.setText(Packages.getVersionName(this));
	}
}
