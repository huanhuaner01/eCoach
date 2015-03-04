package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

//用户指南界面
public class UserGuideActivity extends RightSideParentActivity {
	private WebView webview;
	
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, UserGuideActivity.class);
		return intent;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_guide);
		webview = (WebView) findViewById(R.id.userguide_webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(NetUtil.getAbsolutePath(SRL.StaticWebPage.PAGE_USER_GUIDE));
	}
}
