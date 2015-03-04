package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ServiceInfoActivity extends RightSideParentActivity {
	private WebView webview;
	private RadioGroup group;

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, ServiceInfoActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_info);
		registView();
		init();
	}

	/**
	 * 注册组件
	 */
	private void registView() {
		webview = (WebView) findViewById(R.id.service_info_webview);
		group = (RadioGroup) findViewById(R.id.service_info_group);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		// this may cause XSS, ignore it.
		webview.getSettings().setJavaScriptEnabled(true);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup g, int checkedId) {
				switch (checkedId) {
				case R.id.service_info_se:
					openUrl(NetUtil.getAbsolutePath(SRL.StaticWebPage.PAGE_SERVICE_PROTOCAL));
					break;
				case R.id.service_info_pri:
					openUrl(NetUtil.getAbsolutePath(SRL.StaticWebPage.PAGE_PRIVATE_POLICY));
					break;
				}
			}

		});
		group.check(R.id.service_info_se);
	}

	/**
	 * 打开一个网站
	 * 
	 * @param url
	 */
	private void openUrl(String url) {
		// Log.i("ServiceInfoActivity",url) ;
		webview.loadUrl(url);
	}

}
