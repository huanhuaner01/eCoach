package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 实时抢单页面，平时无链接指向这里，当且仅当有新订单的推送消息到来时才自动弹出。
 * @author Muyangmin
 * @create 2015-2-28
 */
public class RealtimeSnapupActivity extends Activity {
	
	private static final String EXTRA_NEWORDER = "neworder";
	
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, RealtimeSnapupActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_snapup);
	}
}
