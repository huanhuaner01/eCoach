package com.huishen.ecoach.ui.login;

import java.util.HashMap;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.util.Uis;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 完成验证码按钮的逻辑。主要有：
 * 1.完成验证码短信的下发；
 * 2.完成内容长度等的验证和提示；
 * 3.完成验证码有效期的检查逻辑。
 * 
 * @author Muyangmin
 * @create 2015-2-11
 */
abstract class VerifyButtonClickListener implements OnClickListener {

	private static final String LOG_TAG = "VerifyButtonClickListener";

	private Context context;
	private EditText editPhoneNumber, editVcode;
	private Button btnVerify;
	private boolean vcodeValid;
	private final int VALID_TIME;
	private CountDownTimer timer = null;
	//默认的回调监听。
 	private ResponseListener mResponseListener = new ResponseListener() {
		
		@Override
		protected void onSuccess(String arg0) {
			//无需处理，用户只需等待短信下达。
		}
		
		@Override
		protected void onReturnBadResult(int errorCode, String arg0) {
			Log.d(LOG_TAG, "Fail to send verify code.");
			//忽略这个请求错误信息，因为用户不应该看到。
		}
	};

	/**
	 * 构造一个监听器对象。
	 * @param context 上下文信息，用于显示错误Toast及获取字符串等。
	 * @param phone 手机号的输入框，用于控制内容
	 * @param verify 验证按钮本身的引用
	 * @param vcode 验证码输入框，用于控制内容的显示
	 */
	public VerifyButtonClickListener(Context context, EditText phone,
			Button verify, EditText vcode) {
		this.context = context;
		this.editPhoneNumber = phone;
		this.btnVerify = verify;
		this.editVcode = vcode;
		this.VALID_TIME = context.getResources().getInteger(
				R.integer.verifycode_valid_time_seconds);
	}

	@Override
	public void onClick(View arg0) {
		Log.d(LOG_TAG, "btn onclick");
		// check phone
		String num = editPhoneNumber.getText().toString();
		if (!num.matches("(86|\\+86)?1\\d{10}")) {
			Uis.toastShort(context, R.string.str_verify_phone_err_not_valid_number);
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
		//UI优化：自动清除验证码框原有的内容
		editVcode.setText("");
		// maybe should be a class field to support cancel
		timer = new CountDownTimer(VALID_TIME * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				btnVerify.setText(String.format(context.getResources()
						.getString(R.string.str_verify_phone_countdown),
						(int) (millisUntilFinished / 1000)));
			}

			@Override
			public void onFinish() {
				onTimerFinished();
			}
		};
		timer.start();
	}
	
	//计时器完成后的操作。
	private final void onTimerFinished(){
		btnVerify.setText(R.string.str_verify_phone_err_request_retry);
		vcodeValid = false;
		btnVerify.setEnabled(true);
		editPhoneNumber.setEnabled(true);
		timer = null;
	}
	

	/**
	 * 向服务器发送请求，获取手机验证码。
	 * @param mobileNumber 手机号码
	 */
	private final void getVerifyCode(String mobileNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(getRequestMobileParam(), mobileNumber);
		ResponseListener listener = getResponseListener();
		NetUtil.requestStringData(getRequestUri(), params,
				listener==null ? mResponseListener : listener);
	}
	
	/**
	 * 指定下发短信的Uri请求地址。
	 */
	protected abstract String getRequestUri();
	/**
	 * 返回请求参数中手机号的Key值。
	 */
	protected abstract String getRequestMobileParam();
	
	
	/**
	 * 指定下发短信后的回调监听器。
	 */
	protected ResponseListener getResponseListener(){
		return null;
	}
	
	/**
	 * 清除按钮上的计时器等信息，重置输入框、密码框等，模拟计时器完成后的操作。
	 */
	protected final void resetVerifyComponents(){
		if (timer != null){
			timer.cancel();
		}
		onTimerFinished();
	}

	/**
	 * 返回验证码的当前有效状态。
	 */
	public final boolean isVcodeValid() {
		return vcodeValid;
	}

}
