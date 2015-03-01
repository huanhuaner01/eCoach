package com.huishen.ecoach.ui.login;

import java.util.HashMap;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.util.MsgEncryption;
import com.huishen.ecoach.util.Uis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswordActivity extends RightSideParentActivity implements
		OnClickListener {

	private static final String EXTRA_PHONE = "phone";
	
	private EditText editPhone, editCode, editPassword;
	private Button btnVerify, btnReset;
	
	/**
	 * 获得一个Intent，启动后所有的输入域都是空。
	 */
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, ForgetPasswordActivity.class);
		return intent;
	}
	
	/**
	 * 获得一个Intent，启动后会自动显示缺省电话号码。
	 * 
	 * @param defaultPhone
	 *            要显示的电话号码。如果为null，则什么也不显示，
	 *            即与 {@link #getIntent(Context)}的返回值一样。
	 */
	public static final Intent getIntent(Context context, String defaultPhone){
		Intent intent = new Intent(context, ForgetPasswordActivity.class);
		if (defaultPhone != null){
			intent.putExtra(EXTRA_PHONE, defaultPhone);
		}
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		initWidgets();
		//设置默认的电话号码
		String phone = getIntent().getStringExtra(EXTRA_PHONE);
		if (phone!=null){
			editPhone.setText(phone);
			btnVerify.setEnabled(true);
		}
	}
	
	private void initWidgets() {
		editPhone = (EditText) findViewById(R.id.forgetpwd_edit_phone);
		editCode = (EditText) findViewById(R.id.forgetpwd_edit_vcode);
		editPassword = (EditText) findViewById(R.id.forgetpwd_edit_pwd);
		btnVerify = (Button) findViewById(R.id.forgetpwd_btn_verify);
		btnReset = (Button) findViewById(R.id.forgetpwd_btn_confirm_reset);
		btnVerify.setOnClickListener(new VerifyButtonClickListener(
				ForgetPasswordActivity.this, editPhone, btnVerify, editCode){
			@Override
			protected String getRequestUri() {
				return SRL.Method.METHOD_GET_VCODE_IF_EXIST;
			}
			
			@Override
			protected String getRequestMobileParam() {
				return SRL.Param.PARAM_RESETPWD_GETVCODE_MOBILE;
			}
			
			@Override
			protected ResponseListener getResponseListener() {
				return new ResponseListener() {
					
					@Override
					protected void onSuccess(String arg0) {
						//无需处理，用户只需等待短信下达。
					}
					
					@Override
					protected void onReturnBadResult(int errorCode, String arg0) {
						switch (errorCode) {
						case SRL.ReturnCode.ERR_GETVCODE_PHONE_NOT_EXIST:
							Uis.toastShort(ForgetPasswordActivity.this,
									R.string.str_forgetpwd_err_phone_notexist);
							resetVerifyComponents();
							break;
						case SRL.ReturnCode.ERR_GETVCODE_FAILTOSEND:
									//忽略这个请求错误信息，因为用户不应该看到。
						default:	//忽略
						}
					}
				};
			}
		});
		editPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				btnVerify.setEnabled(s.length()>0 ? true : false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		editCode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length()!=getResources().getInteger(R.integer.verifycode_valid_length)){
					btnReset.setEnabled(false);
				}
				else {
					btnReset.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		btnReset.setOnClickListener(this);
	}
	
	private void performResetPassword() {
		final String phone = editPhone.getText().toString();
		final String vcode = editCode.getText().toString();
		final String pwd = editPassword.getText().toString();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_MOBILE_NUMBER, phone);
		params.put(SRL.Param.PARAM_VCODE, vcode);
		params.put(SRL.Param.PARAM_PASSWORD, MsgEncryption.md5Encryption(pwd));
		NetUtil.requestStringData(SRL.Method.METHOD_RESET_PASSWORD, params,
				new ResponseListener() {

					@Override
					protected void onSuccess(String arg0) {
						Uis.toastShort(ForgetPasswordActivity.this,
								R.string.str_forgetpwd_info_reset_success);
						// finish();
					}

					@Override
					protected void onReturnBadResult(int errorCode, String arg0) {
						switch (errorCode) {
						case SRL.ReturnCode.ERR_RESETPWD_VCODE_NOTMATCH:
							Uis.toastShort(ForgetPasswordActivity.this,
									R.string.str_forgetpwd_err_vcode_wrong);
							break;
						case SRL.ReturnCode.ERR_RESETPWD_FAIL:
							Uis.toastShort(ForgetPasswordActivity.this,
									R.string.str_forgetpwd_err_reset_fail);
							break;
						default:
							break;
						}
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.forgetpwd_btn_confirm_reset){
			performResetPassword();
		}
	}
}
