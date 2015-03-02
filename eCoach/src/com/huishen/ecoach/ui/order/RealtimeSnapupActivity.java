package com.huishen.ecoach.ui.order;

import java.util.HashMap;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.dialog.MsgDialog;
import com.huishen.ecoach.umeng.NewOrderPushData;
import com.huishen.ecoach.util.Uis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 实时抢单页面，平时无链接指向这里，当且仅当有新订单的推送消息到来时才自动弹出。
 * @author Muyangmin
 * @create 2015-2-28
 */
public class RealtimeSnapupActivity extends Activity implements
		android.view.View.OnClickListener {

	private static final String LOG_TAG = "RealtimeSnapupActivity";
	private static final String EXTRA_NEWORDER = "neworder";

	private NewOrderPushData orderData;
	
	private ImageButton imgbtnClose;
	private Button btnSnapup;
	private TextView tvContent, tvDistance, tvCity, tvDetailPosition;
	
	public static final Intent getIntent(Context context, NewOrderPushData data){
		Intent intent = new Intent(context, RealtimeSnapupActivity.class);
		intent.putExtra(EXTRA_NEWORDER, data);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_snapup);
		setFinishOnTouchOutside(false);		//防止误点
		try {
			orderData = (NewOrderPushData) getIntent().
					getSerializableExtra(EXTRA_NEWORDER);	//类型错误时会抛出ClassCastException，但null不会
			if (orderData == null){
				throw new ClassCastException();	//Java 6不支持 multi-catch，抛出一个ClassCastException方便处理。
			}
		} catch (ClassCastException e) {
			Log.e(LOG_TAG, "FATAL ERROR: extra value must be instance of NewOrderPushData.");
//			finish();
			return ;
		}
		displayOrderInfo(orderData);
		//开启震动
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (vibrator.hasVibrator()){
			vibrator.vibrate(getResources().getInteger(R.integer.viborator_time_neworder));
		}
	}
	
	private void displayOrderInfo(NewOrderPushData data) {
		tvContent = (TextView) findViewById(R.id.realtime_snapup_tv_demand);
		tvCity = (TextView)findViewById(R.id.realtime_snapup_tv_cityregion);
		tvDetailPosition = (TextView)findViewById(R.id.realtime_snapup_tv_stuposition);
		tvDistance = (TextView)findViewById(R.id.realtime_snapup_tv_distance);
		imgbtnClose = (ImageButton)findViewById(R.id.realtime_snapup_imgbtn_close);
		btnSnapup = (Button)findViewById(R.id.realtime_snapup_btn_snapup);
		tvContent.setText(data.content);
		imgbtnClose.setOnClickListener(this);
		btnSnapup.setOnClickListener(this);
		if (data.city != null){
			tvCity.setText(data.city);
		}
		if (data.detailPosition != null){
			tvDetailPosition.setText(data.detailPosition);
		}
		tvDistance.setText(String.format(getString(R.string.str_realtime_snapup_distance), data.distance));
		if (data.voicePath==null){
			findViewById(R.id.realtime_snapup_rl_voiceline).setVisibility(View.GONE);
			findViewById(R.id.realtime_snapup_img_demand_txtonly).setVisibility(View.VISIBLE);
		}
	}
	
	//执行抢单操作。
	private final void performSnapup() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SRL.Param.PARAM_ORDER_ID, orderData.orderId);
		map.put(SRL.Param.PARAM_ORDER_VERSION, orderData.versionUID);
		map.put(SRL.Param.PARAM_ORDER_COACH_GPS, "104.065656,30.577716");
		map.put(SRL.Param.PARAM_ORDER_COACH_POS, "成都市环球中心W8");
		NetUtil.requestStringData(SRL.Method.METHOD_EXECUTE_SNAPUP, map, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				Uis.toastShort(RealtimeSnapupActivity.this, "抢单成功, Mua~");
				startActivity(SnapSuccessActivity.getIntent(RealtimeSnapupActivity.this));
				finish();
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				switch (errorCode) {
				case SRL.ReturnCode.ERR_ORDER_NOT_EXIST:
					MsgDialog.showDialog(RealtimeSnapupActivity.this, 
							R.string.str_realtime_snapup_err_order_notexist);
					break;
				case SRL.ReturnCode.ERR_ORDER_OUT_OF_DATE:
					MsgDialog.showDialog(RealtimeSnapupActivity.this, 
							R.string.str_realtime_snapup_err_order_outofdate);
					break;
				case SRL.ReturnCode.ERR_ORDER_NOT_VALID:
					MsgDialog.showDialog(RealtimeSnapupActivity.this, 
							R.string.str_realtime_snapup_err_order_notvalid);
					break;
				case SRL.ReturnCode.ERR_ORDER_CANCELED:
					MsgDialog.showDialog(RealtimeSnapupActivity.this, 
							R.string.str_realtime_snapup_err_order_cancelled);
					break;
				case SRL.ReturnCode.ERR_ORDER_SNAPPED_BY_OTHER:
					MsgDialog.showDialog(RealtimeSnapupActivity.this, 
							R.string.str_realtime_snapup_err_order_snappedbyother);
					break;
				default:
					MsgDialog.showDialog(RealtimeSnapupActivity.this,
							R.string.str_err_unknown_reason);
					break;
				}
				finish();
			}
		});
	}
	
	//屏蔽按键事件（主要是Back键）
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.realtime_snapup_imgbtn_close:
			//simply finish.
			finish();
			break;
		case R.id.realtime_snapup_btn_snapup:
			performSnapup();
			break;
		default:
			break;
		}
	}
}
