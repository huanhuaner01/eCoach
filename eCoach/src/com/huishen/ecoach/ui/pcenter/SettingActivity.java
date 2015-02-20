package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

//设置界面
public class SettingActivity extends RightSideParentActivity implements OnClickListener{
	
	private RelativeLayout rlViewProfile, rlModifypwd, rlUpdateSoftware,
			rlFeedback, rlAboutUs;

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initWidgets();
	}
	
	private final void initWidgets() {
		rlViewProfile = (RelativeLayout) findViewById(R.id.setting_rl_viewprofile);
		rlModifypwd = (RelativeLayout) findViewById(R.id.setting_rl_modifypwd);
		rlUpdateSoftware = (RelativeLayout) findViewById(R.id.setting_rl_update_software);
		rlFeedback = (RelativeLayout) findViewById(R.id.setting_rl_feedback);
		rlAboutUs = (RelativeLayout) findViewById(R.id.setting_rl_aboutus);
		
		rlViewProfile.setOnClickListener(this);
		rlModifypwd.setOnClickListener(this);
		rlUpdateSoftware.setOnClickListener(this);
		rlFeedback.setOnClickListener(this);
		rlAboutUs.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_rl_viewprofile:
			startActivity(ViewProfileActivity.getIntent(this));
			break;
		case R.id.setting_rl_modifypwd:
			startActivity(ModifyPasswordActivity.getIntent(this));
			break;
		case R.id.setting_rl_update_software:
			
			break;
		case R.id.setting_rl_feedback:
			startActivity(FeedbackActivity.getIntent(this));
			break;
		case R.id.setting_rl_aboutus:
			startActivity(AboutUsActivity.getIntent(this));
			break;
		default:
			break;
		}
	}
	
}
