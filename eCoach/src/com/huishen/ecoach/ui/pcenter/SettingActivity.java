package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.Const;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.MainActivity;
import com.huishen.ecoach.ui.login.LoginActivity;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.umeng.UmengServiceProxy;
import com.huishen.ecoach.util.Prefs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

//设置界面
public class SettingActivity extends RightSideParentActivity implements OnClickListener{
	
	private static final String LOG_TAG = "SettingActivity";
	
	private RelativeLayout rlUpdateSoftware,
			rlFeedback, rlAboutUs;
	private Button btnLogout;

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
//		rlModifypwd = (RelativeLayout) findViewById(R.id.setting_rl_modifypwd);
		rlUpdateSoftware = (RelativeLayout) findViewById(R.id.setting_rl_update_software);
		rlFeedback = (RelativeLayout) findViewById(R.id.setting_rl_feedback);
		rlAboutUs = (RelativeLayout) findViewById(R.id.setting_rl_aboutus);
		btnLogout = (Button)findViewById(R.id.setting_btn_logout);
		
//		rlModifypwd.setOnClickListener(this);
		rlUpdateSoftware.setOnClickListener(this);
		rlFeedback.setOnClickListener(this);
		rlAboutUs.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
	}
	
	private void performLogout(){
		//注销登录这个请求即使失败也不让用户知道 >_<
		NetUtil.requestStringData(SRL.Method.METHOD_LOGOUT, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				//just ignore
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				//just ignore
			}
		});
		setResult(MainActivity.RESULT_CODE_LOGOUT);
		Prefs.setBoolean(this, Const.KEY_LOGIN_AUTOLOGIN, false);
		Prefs.removeKey(this, Const.KEY_LOGIN_LASTLOGIN_PWD);
		Log.d(LOG_TAG, "autologin flag has removed.");
		UmengServiceProxy.stopPushService(this);
		startActivity(LoginActivity.getIntent(this));
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.setting_rl_modifypwd:
//			startActivity(ModifyPasswordActivity.getIntent(this));
//			break;
		case R.id.setting_rl_update_software:
			
			break;
		case R.id.setting_rl_feedback:
			startActivity(FeedbackActivity.getIntent(this));
			break;
		case R.id.setting_rl_aboutus:
			startActivity(AboutUsActivity.getIntent(this));
			break;
		case R.id.setting_btn_logout:
			performLogout();
			break;
		default:
			break;
		}
	}
	
}
