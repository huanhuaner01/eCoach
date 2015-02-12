package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 负责引导完成用户的注册操作。
 * 
 * @author Muyangmin
 * @create 2015-2-10
 */
public class RegisterActivity extends RightSideParentActivity implements
		VerifyFragment.NextStepListener {
	private static final String LOG_TAG = "RegisterActivity";

	private FragmentManager fragMgr = getFragmentManager();
	private TextView tvStepVerify, tvStepProfile, tvStepUpload;

	private static final int CONTAINER = R.id.register_frame_container;

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, RegisterActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		tvStepProfile = (TextView)findViewById(R.id.register_tv_step_profile);
		tvStepUpload = (TextView)findViewById(R.id.register_tv_step_upload);
		tvStepVerify = (TextView)findViewById(R.id.register_tv_step_verify);
		fragMgr.beginTransaction().add(CONTAINER, new VerifyFragment())
				.commit();
	}

	@Override
	public void onVerifyPhoneStepCompleted() {
		tvStepVerify.setEnabled(false);
		tvStepProfile.setEnabled(true);
		fragMgr.beginTransaction().replace(CONTAINER, new ProfileFragment())
				.commit();
	}
}
