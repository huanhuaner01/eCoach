package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.Const;
import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.util.Prefs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 负责引导完成用户的注册操作。
 * 
 * @author Muyangmin
 * @create 2015-2-10
 */
public class RegisterActivity extends RightSideParentActivity implements
		VerifyFragment.NextStepListener, ProfileFragment.NextStepListener,
		UploadCertifyFragment.NextStepListener {
	private static final String LOG_TAG = "RegisterActivity";

	private FragmentManager fragMgr = getFragmentManager();
	private TextView tvStepVerify, tvStepProfile, tvStepUpload;

	// 在Preferences文件中存放步骤信息的KEY。
	private static final String KEY_STEP_VERIFY_COMPLETED = "stepv";
	private static final String KEY_STEP_PROFILE_COMPLETED = "stepp";

	private static final int CONTAINER = R.id.register_frame_container;

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, RegisterActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		tvStepProfile = (TextView) findViewById(R.id.register_tv_step_profile);
		tvStepUpload = (TextView) findViewById(R.id.register_tv_step_upload);
		tvStepVerify = (TextView) findViewById(R.id.register_tv_step_verify);
		Fragment content = null;
		// 验证已完成步骤。注意，此处及本类中其他方法均假定XML文件中仅有tvStepVerify的enabled属性为true。
		if (Prefs.getBoolean(this, KEY_STEP_PROFILE_COMPLETED)) {
			Log.d(LOG_TAG, "STEP_PROFILE_COMPLETED");
			content = new UploadCertifyFragment();
			tvStepVerify.setEnabled(false);
			tvStepUpload.setEnabled(true);
		} else if (Prefs.getBoolean(this, KEY_STEP_VERIFY_COMPLETED)) {
			Log.d(LOG_TAG, "STEP_VERIFY_COMPLETED");
			content = new ProfileFragment();
			tvStepVerify.setEnabled(false);
			tvStepProfile.setEnabled(true);
		} else {
			content = new VerifyFragment();
		}
		fragMgr.beginTransaction().add(CONTAINER, content).commit();
	}

	@Override
	public void onVerifyPhoneStepCompleted(String phoneNumber) {
		tvStepVerify.setEnabled(false);
		tvStepProfile.setEnabled(true);
		Prefs.setString(this, Const.KEY_VERIFIED_PHONE, phoneNumber);
		Prefs.setBoolean(this, KEY_STEP_VERIFY_COMPLETED, true);
		fragMgr.beginTransaction().replace(CONTAINER, new ProfileFragment())
				.commit();
	}

	@Override
	public void onFillProfileStepCompleted() {
		tvStepProfile.setEnabled(false);
		tvStepUpload.setEnabled(true);
		Prefs.setBoolean(this, KEY_STEP_PROFILE_COMPLETED, true);
		fragMgr.beginTransaction()
				.replace(CONTAINER, new UploadCertifyFragment()).commit();
	}

	@Override
	public void onUploadCertifyStepCompleted() {
		Log.d(LOG_TAG, "all step completed. removing old keys...");
		Prefs.removeKey(this, KEY_STEP_PROFILE_COMPLETED);
		Prefs.removeKey(this, KEY_STEP_VERIFY_COMPLETED);
		Prefs.setBoolean(this, Const.KEY_REGISTER_COMPLETED, true);
	}
}
