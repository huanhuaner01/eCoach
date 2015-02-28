package com.huishen.ecoach.ui.login;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.huishen.ecoach.Const;
import com.huishen.ecoach.MainApp;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.MainActivity;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.util.Prefs;
import com.huishen.ecoach.util.Uis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends RightSideParentActivity implements
		OnClickListener {
	private static final String LOG_TAG = "LoginActivity";
	
	private EditText editUsername, editPassword;
	private Button btnLogin, btnRegister, btnForgetPwd;
	
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, LoginActivity.class);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initWidgets();
		if (Prefs.getBoolean(this, Const.KEY_AUTO_LOGIN)){
			String phone = Prefs.getString(this, Const.KEY_LAST_LOGIN_PHONE);
			String pwd = Prefs.getString(this, Const.KEY_LAST_LOGIN_PWD);
			if (pwd != null && pwd.length() > 0) {
				Log.d("LoginActivity", "autologin[" + phone + "," + pwd + "]...");
				performLogin(phone, pwd);
			}
		}
	}
	private final void initWidgets(){
		editUsername = (EditText)findViewById(R.id.login_edit_username);
		editPassword = (EditText)findViewById(R.id.login_edit_pwd);
		btnLogin = (Button)findViewById(R.id.login_btn_login);
		btnRegister = (Button)findViewById(R.id.login_btn_register);
		btnForgetPwd = (Button)findViewById(R.id.login_btn_forgetpwd);
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnForgetPwd.setOnClickListener(this);
		//读取保存的用户名
		String lastLogin = Prefs.getString(LoginActivity.this, Const.KEY_LAST_LOGIN_PHONE);
		if (lastLogin!=null){
			editUsername.setText(lastLogin);
		}
	}
	
	//处理登录逻辑
	private final void performLogin(final String user, final String pwd){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_USERNAME, user);
		params.put(SRL.Param.PARAM_PASSWORD, pwd);
		NetUtil.requestStringData(SRL.Method.METHOD_LOGIN, params,
				new ResponseListener() {
					
					@Override
					protected void onSuccess(String arg0) {
						Uis.toastShort(LoginActivity.this, R.string.str_login_info_success);
						//设置全局变量
						MainApp.getInstance().setLoginCoach(parseLoginCoachInfo(arg0));
						//保存弱登录信息，清除注册信息
						Prefs.setString(LoginActivity.this, Const.KEY_LAST_LOGIN_PHONE, user);
						Prefs.setString(LoginActivity.this, Const.KEY_LAST_LOGIN_PWD, pwd);
						Prefs.setBoolean(LoginActivity.this, Const.KEY_AUTO_LOGIN, true);
						RegisterActivity.clearRegisterInformation(LoginActivity.this);
						//执行页面跳转
						LoginActivity.this.startActivity(MainActivity.getIntent(LoginActivity.this));
						finish();
					}
					
					@Override
					protected void onReturnBadResult(int errorCode, String arg0) {
						switch (errorCode) {
						case SRL.ReturnCode.ERR_LOGIN_ACCOUNT_FORBIDDEN:
							Uis.toastShort(LoginActivity.this,
									R.string.str_login_err_account_forbidden);
							return;
						case SRL.ReturnCode.ERR_LOGIN_WRONGPWD:
							Uis.toastShort(LoginActivity.this,
									R.string.str_login_err_wrongpwd);
							return;
						default:
							break;
						}
					}
				});
	}
	
	/**
	 * 解析登录教练的信息。
	 * @param str 服务器的原始返回字符串
	 */
	private final Coach parseLoginCoachInfo(String str){
		Coach coach = new Coach();
		try {
			//使用get强制有值时才做解析操作
			JSONObject shell = new JSONObject(str);
			JSONObject json = shell.getJSONObject(SRL.ReturnField.FIELD_INFO);
			//使用opt避免空指针等问题
			coach.setId(json.optLong(SRL.ReturnField.FIELD_COACH_ID));
			coach.setCarno(json.optString(SRL.ReturnField.FIELD_COACH_CARNO));
			coach.setCertno(json.optString(SRL.ReturnField.FIELD_COACH_CERTNO));
			coach.setName(json.optString(SRL.ReturnField.FIELD_COACH_NAME));
			coach.setPhoneNumber(json.optString(SRL.ReturnField.FIELD_COACH_PHONE));
			coach.setSchool(json.optString(SRL.ReturnField.FIELD_COACH_SCHOOL));
			coach.setAuditStatus(json.optInt(SRL.ReturnField.FIELD_COACH_AUDIT_STATUS));
			coach.setAvatarId(json.optString(SRL.ReturnField.FIELD_COACH_AVATAR));
			coach.setOrderCount(json.optInt(SRL.ReturnField.FIELD_COACH_ORDER_COUNT));
			coach.setRange(json.optInt(SRL.ReturnField.FIELD_COACH_RANGE));
			coach.setGoodRate((float) json.optDouble(SRL.ReturnField.FIELD_COACH_RECOMMEND_INDEX));
			coach.setStarLevel((float) json.optDouble(SRL.ReturnField.FIELD_COACH_STAR_LEVEL));
			//保存flag
			Prefs.setString(LoginActivity.this, Const.KEY_MOBILE_FLAG,
					json.getString(SRL.ReturnField.FIELD_MOBILE_FLAG));
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Fail to resolve server response.");
			e.printStackTrace();
		}
		return coach;
	}
	
	private final void performRegister(){
		startActivity(RegisterActivity.getIntent(this));
		//DO NOT finish this activity.
	}
	
	private final void performForgetPwd(){
		String phone = Prefs.getString(this, Const.KEY_LAST_LOGIN_PHONE);
		startActivity(ForgetPasswordActivity.getIntent(LoginActivity.this, phone));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_login:
			final String username = editUsername.getText().toString();
			final String pwd = editPassword.getText().toString();
			if (username.equals("")){
				Uis.toastShort(this, R.string.str_login_err_nouser);
				return ;
			}
			if (pwd.equals("")){
				Uis.toastShort(this, R.string.str_login_err_nopwd);
				return ;
			}
			performLogin(username, pwd);
			break;
		case R.id.login_btn_forgetpwd:
			performForgetPwd();
			break;
		case R.id.login_btn_register:
			performRegister();
			break;
		default:
			break;
		}
	}
}
