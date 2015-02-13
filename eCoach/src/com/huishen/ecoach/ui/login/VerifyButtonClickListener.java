package com.huishen.ecoach.ui.login;

import java.util.HashMap;

import com.android.volley.Response;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.SRL;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 完成验证码按钮的逻辑。因逻辑复杂且至少两处使用验证码，故将其独立拆开。 但该类的设计不是很合理，责任和功能比较复杂。
 * 
 * @author Muyangmin
 * @create 2015-2-11
 */
class VerifyButtonClickListener implements OnClickListener {

	private static final String LOG_TAG = "VerifyButtonClickListener";

	private Context context;
	private EditText editPhoneNumber;
	private Button btnVerify;
	private boolean vcodeValid;
	private final int VALID_TIME;

	public VerifyButtonClickListener(Context context, EditText phone,
			Button verify) {
		this.context = context;
		this.editPhoneNumber = phone;
		this.btnVerify = verify;
		this.VALID_TIME = context.getResources().getInteger(
				R.integer.verifycode_valid_time_seconds);
	}

	@Override
	public void onClick(View arg0) {
		Log.d(LOG_TAG, "btn onclick");
		// check phone
		String num = editPhoneNumber.getText().toString();
		if (!num.matches("(86|\\+86)?1\\d{10}")) {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.str_verify_phone_err_not_valid_number),
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.d(LOG_TAG, "phone number check complete.Now sending request...");
		// 提交网络请求
		getVerifyCode(editPhoneNumber.getText().toString());
		// 控制禁止用户中途更改手机号
		editPhoneNumber.setEnabled(false);
		btnVerify.setEnabled(false);
		// 设置初始值为true
		vcodeValid = true;
		// maybe should be a class field to support cancel
		new CountDownTimer(VALID_TIME * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				btnVerify.setText(String.format(context.getResources()
						.getString(R.string.str_verify_phone_countdown),
						(int) (millisUntilFinished / 1000)));
			}

			@Override
			public void onFinish() {
				btnVerify.setText(R.string.str_verify_phone_err_request_retry);
				vcodeValid = false;
				btnVerify.setEnabled(true);
				editPhoneNumber.setEnabled(true);
			}
		}.start();
	}

	/**
	 * 向服务器发送请求，获取手机验证码。
	 * @param mobileNumber 手机号码
	 */
	private final void getVerifyCode(String mobileNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.PARAM_MOBILE_NUMBER, mobileNumber);
		NetUtil.requestStringData(SRL.METHOD_GET_VERIFY_CODE, params,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						Log.d(LOG_TAG, arg0);
					}
				});
	}

	/**
	 * 返回验证码的当前有效状态。
	 */
	public final boolean isVcodeValid() {
		return vcodeValid;
	}

}
