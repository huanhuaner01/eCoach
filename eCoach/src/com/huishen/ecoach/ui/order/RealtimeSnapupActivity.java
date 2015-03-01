package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import com.huishen.ecoach.umeng.NewOrderPushData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 实时抢单页面，平时无链接指向这里，当且仅当有新订单的推送消息到来时才自动弹出。
 * @author Muyangmin
 * @create 2015-2-28
 */
public class RealtimeSnapupActivity extends Activity {

	private static final String LOG_TAG = "RealtimeSnapupActivity";
	private static final String EXTRA_NEWORDER = "neworder";
	
	private ImageButton btnClose, btnSnapup;
	private TextView tvContent;
	
	public static final Intent getIntent(Context context, NewOrderPushData data){
		Intent intent = new Intent(context, RealtimeSnapupActivity.class);
		intent.putExtra(EXTRA_NEWORDER, data);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_snapup);
		Log.d(LOG_TAG, "activity has started.");
		final NewOrderPushData data;
		try {
			data = (NewOrderPushData) getIntent().
					getSerializableExtra(EXTRA_NEWORDER);	//类型错误时会抛出ClassCastException，但null不会
			if (data == null){
				throw new ClassCastException();	//Java 6不支持 multi-catch，抛出一个ClassCastException方便处理。
			}
			Log.d(LOG_TAG, data.toString());
		} catch (ClassCastException e) {
			Log.e(LOG_TAG, "FATAL ERROR: extra value must be instance of NewOrderPushData.");
//			finish();
			return ;
		}
		displayOrderInfo(data);
	}
	
	private void displayOrderInfo(NewOrderPushData data) {
		tvContent = (TextView) findViewById(R.id.realtime_snapup_tv_demand);
		tvContent.setText(data.content);
	}
}
