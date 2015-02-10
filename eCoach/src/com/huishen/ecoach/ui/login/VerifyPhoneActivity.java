package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyPhoneActivity extends Activity {

	private EditText editPhoneNumber, editVerifyCode;
	private Button btnVerify, btnStart, btnBack;
	private TextView tvProtocal;

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, VerifyPhoneActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_phone);
		editPhoneNumber = (EditText) findViewById(R.id.verify_edit_number);
		editVerifyCode = (EditText) findViewById(R.id.verify_edit_vcode);
		btnVerify = (Button) findViewById(R.id.verify_btn_verify);
		btnStart = (Button) findViewById(R.id.verify_btn_start);
		tvProtocal = (TextView) findViewById(R.id.verify_tv_protocal);
		tvProtocal.setText(buildProtocalText());
	}

	private final CharSequence buildProtocalText() {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(getResources().getString(
				R.string.str_verify_phone_protocal_prefix));
		int protocalStartIndex = ssb.length();
		ssb.append(getResources().getString(
				R.string.str_verify_phone_protocal_name));
		ClickableSpan clickableSpan = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				Log.d("", "clicked.");
				displayProtocal();
			}
		};
		tvProtocal.setMovementMethod(LinkMovementMethod.getInstance());
		ssb.setSpan(clickableSpan, protocalStartIndex, ssb.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.color_theme_orange));
		ssb.setSpan(colorSpan, protocalStartIndex, ssb.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	private final void displayProtocal() {
		Toast.makeText(this, "我是协议，不准不遵守，啦啦啦~", Toast.LENGTH_SHORT).show();
	}

}
