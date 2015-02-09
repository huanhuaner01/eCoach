package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyPhoneActivity extends Activity {
	
	private EditText editPhoneNumber, editVerifyCode;
	private Button btnVerify, btnStart, btnBack;
	private TextView tvProtocal;
	
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, VerifyPhoneActivity.class);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_phone);
		editPhoneNumber = (EditText)findViewById(R.id.verify_edit_number);
		editVerifyCode = (EditText)findViewById(R.id.verify_edit_vcode);
		btnVerify = (Button)findViewById(R.id.verify_btn_verify);
		btnStart = (Button)findViewById(R.id.verify_btn_start);
		tvProtocal = (TextView)findViewById(R.id.verify_tv_protocal);
		tvProtocal.setText(buildProtocalText());
	}
	
	private final CharSequence buildProtocalText(){
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(getResources().getString(R.string.str_verify_phone_protocal_prefix));
		int protocalStartIndex = ssb.length();
		ssb.append(getResources().getString(R.string.str_verify_phone_protocal_name));
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(R.color.color_red);
//		URLSpan urlSpan = new URLSpan("http://www.baidu.com");
		ssb.setSpan(colorSpan, protocalStartIndex, ssb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//		ssb.setSpan(urlSpan, protocalStartIndex, ssb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return ssb;
	}
	
}
