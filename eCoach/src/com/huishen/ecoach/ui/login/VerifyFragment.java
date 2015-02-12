package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;
import com.huishen.ecoach.logical.PasswordValidator;
import com.huishen.ecoach.logical.PasswordValidator.ValidateResult;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 用于注册时验证手机的Fragment。
 * @author Muyangmin
 * @create 2015-2-10
 */
public final class VerifyFragment extends Fragment {
	
	private static final String LOG_TAG = "VerifyFragment";
	
	private EditText editPhone, editPassword, editConfirmpwd, editVcode;
	private Button btnVcode, btnNextStep;
	
	private NextStepListener nsListener;
	private VerifyButtonClickListener vbListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_register_verify, null);
		initWidgets(view);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			nsListener = (NextStepListener)activity;
		} catch (ClassCastException cce) {
			Log.e(LOG_TAG, "ERROR: activity must implements NextStepListener!");
			nsListener = null;
		}
	}
	
	private final void initWidgets(View view){
		editPhone = (EditText)view.findViewById(R.id.register_fragv_edit_number);
		editPassword = (EditText)view.findViewById(R.id.register_fragv_edit_pwd);
		editConfirmpwd = (EditText)view.findViewById(R.id.register_fragv_edit_pwd_confirm);
		editVcode = (EditText)view.findViewById(R.id.register_fragv_edit_vcode);
		btnVcode = (Button)view.findViewById(R.id.register_fragv_btn_verify);
		btnNextStep = (Button)view.findViewById(R.id.register_fragv_btn_next);
		//验证码按钮逻辑
		vbListener = new VerifyButtonClickListener(getActivity(), editPhone, btnVcode);
		btnVcode.setOnClickListener(vbListener);
		//手机号逻辑：长度大于0时可以发送验证码
		editPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				btnVcode.setEnabled(s.length()>0 ? true : false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		//密码框逻辑：不符合验证规则时清零，并提示错误信息
		editPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					ValidateResult result = PasswordValidator.isValidPassword(
							getActivity(), editPassword.getText().toString());
					if (!result.ISVALID) {
						Toast.makeText(getActivity(), result.ERROR_MSG,
								Toast.LENGTH_SHORT).show();
						editPassword.setText("");
					}
				}
			}
		});
		//确认框逻辑：不与密码框内容相同时清零，并提示错误信息
		editConfirmpwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus){//失去焦点时校验。
					if (editPassword.length()<=0){
						Toast.makeText(getActivity(),
								getResources().getString(
												R.string.str_register_fragv_err_pwd_firstempty),
								Toast.LENGTH_SHORT).show();
						return ;
					}
					if (editConfirmpwd.length() > 0
							&& (!editConfirmpwd.getText().equals(
									editPassword.getText()))) {
						Toast.makeText(getActivity(),
								getResources().getString(
												R.string.str_register_fragv_err_pwd_nequal),
								Toast.LENGTH_SHORT).show();
						// clear password 
						editConfirmpwd.setText("");
						return ;
					}
				}
			}
		});
		//验证码框逻辑：长度填写正确后下一步按钮可用
		editVcode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				btnNextStep.setEnabled(s.length() == getResources().getInteger(
						R.integer.verifycode_valid_length) ? true : false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//下一步按钮逻辑：检查前面所有的框内容
		btnNextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO 细化错误提示
				if ( (editPhone.getText().length() <= 0) 
						|| (editPassword.getText().length() <= 0)
						|| (editConfirmpwd.getText().length() <= 0)
						|| (editVcode.getText().length() <= 0)){
					Toast.makeText(getActivity(), "请填写所有信息", Toast.LENGTH_SHORT).show();
					return ;
				}
				//检查密码相等
				if (!editConfirmpwd.getText().equals(editPassword.getText())){
					Toast.makeText(getActivity(), "两次密码填写不一致", Toast.LENGTH_SHORT).show();
				}
				//因验证码长度在验证码框已做检查，这里省略
				//检查验证码有效期
				if (!vbListener.isVcodeValid()){
					Log.d(LOG_TAG, "vcode is NOT valid.");
					Toast.makeText(getActivity(), "验证码已过有效期", Toast.LENGTH_SHORT).show();
				}
				//TODO 添加网络操作请求并在成功后才调用以下代码。
				if (nsListener != null){
					Log.d(LOG_TAG, "Step verify-phone completed.");
					nsListener.onVerifyPhoneStepCompleted();
				}
			}
		});
		
	}
	
	protected static interface NextStepListener{
		/**
		 * 当验证手机步骤完成时调用。
		 */
		void onVerifyPhoneStepCompleted();
	}
}
