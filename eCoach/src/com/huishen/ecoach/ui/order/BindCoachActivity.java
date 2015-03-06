package com.huishen.ecoach.ui.order;

import java.util.HashMap;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.umeng.BindCoachPushData;
import com.huishen.ecoach.util.Uis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 绑定教练弹出框。
 * @author Muyangmin
 * @create 2015-3-6
 */
public class BindCoachActivity extends Activity implements OnClickListener{
	
	private static final String EXTRA_BINDCOACH = "bind";
	private static final String LOG_TAG = "BindCoachActivity";
	private BindCoachPushData bindData;
	
	public static Intent getIntent(Context context, BindCoachPushData bindData){
		Intent intent = new Intent(context, BindCoachActivity.class);
		intent.putExtra(EXTRA_BINDCOACH, bindData);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_coach);
		setFinishOnTouchOutside(false);
		try {
			bindData = (BindCoachPushData) getIntent().getSerializableExtra(EXTRA_BINDCOACH);
		} catch(Exception e) {
			Log.e(LOG_TAG, "FATAL ERROR: extra value must be instance of NewOrderPushData.");
			finish();
			return ;
		}
		displayBindInfo();
	}
	
	private void displayBindInfo() {
		TextView tvName = (TextView)findViewById(R.id.bindcoach_tv_name);
		TextView tvPhone = (TextView)findViewById(R.id.bindcoach_tv_phone);
		TextView tvAddiInfo = (TextView)findViewById(R.id.bindcoach_tv_addinfo);
		tvName.setText(bindData.name);
		tvPhone.setText(bindData.phoneNumber);
		tvAddiInfo.setText(bindData.addtionalInfo);
		Button btnConfirm = (Button)findViewById(R.id.bindcoach_btn_confirm);
		Button btnIgnore = (Button)findViewById(R.id.bindcoach_btn_ignore);
		btnConfirm.setOnClickListener(this);
		btnIgnore.setOnClickListener(this);
	}
	
	private void performAgreeBind(boolean agree){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_BINDCOACH_ENROLLID, bindData.enrollId);
		params.put(SRL.Param.PARAM_BINDCOACH_STATUS, String.valueOf(agree?1:2));
		NetUtil.requestStringData(SRL.Method.METHOD_BIND_COACH, params, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				Uis.toastShort(BindCoachActivity.this, R.string.str_bindcoach_info_operation_success);
				finish();
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				//ignore fail
				finish();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				//ignore fail
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bindcoach_btn_confirm:
			performAgreeBind(true);
			break;
		case R.id.bindcoach_btn_ignore:
			performAgreeBind(false);
			break;
		default:
			break;
		}
	}
}
