package com.huishen.ecoach.ui.order;

import java.io.File;
import java.util.HashMap;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.huishen.ecoach.R;
import com.huishen.ecoach.map.BaiduMapProxy;
import com.huishen.ecoach.map.DistanceCalculator;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.OnProgressChangedListener;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.dialog.MessageDialogFragment;
import com.huishen.ecoach.ui.dialog.MessageDialogFragment.OnButtonClickedListener;
import com.huishen.ecoach.umeng.NewOrderPushData;
import com.huishen.ecoach.util.FileUtil;
import com.huishen.ecoach.util.SimpleAudioPlayer;
import com.huishen.ecoach.util.Uis;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 实时抢单页面，平时无链接指向这里，当且仅当有新订单的推送消息到来时才自动弹出。
 * @author Muyangmin
 * @create 2015-2-28
 */
public class RealtimeSnapupActivity extends Activity implements
		android.view.View.OnClickListener, OnButtonClickedListener {

	private static final String LOG_TAG = "RealtimeSnapupActivity";
	private static final String EXTRA_NEWORDER = "neworder";

	private NewOrderPushData orderData;
	
	private ImageButton imgbtnClose;
	private Button btnSnapup;
	private TextView tvContent, tvDistance, tvDistrict, tvDetailPosition;
	
	private String reverseGeoAddr;	//订单反编码的位置数据
	
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
			finish();
			return ;
		}
		displayOrderInfo(orderData);
		//播放语音提醒
		SimpleAudioPlayer.getInstance().playAssetsAudio(this, "ring_neworder.wav");
		//开启震动
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (vibrator.hasVibrator()){
			vibrator.vibrate(getResources().getInteger(R.integer.viborator_time_neworder));
		}
	}
	
	private void displayOrderInfo(NewOrderPushData data) {
		tvContent = (TextView) findViewById(R.id.realtime_snapup_tv_demand);
		tvDistrict = (TextView)findViewById(R.id.realtime_snapup_tv_cityregion);
		tvDetailPosition = (TextView)findViewById(R.id.realtime_snapup_tv_stuposition);
		tvDistance = (TextView)findViewById(R.id.realtime_snapup_tv_distance);
		imgbtnClose = (ImageButton)findViewById(R.id.realtime_snapup_imgbtn_close);
		btnSnapup = (Button)findViewById(R.id.realtime_snapup_btn_snapup);
		tvContent.setText(data.content);
		imgbtnClose.setOnClickListener(this);
		btnSnapup.setOnClickListener(this);
		BDLocation currentLocation = BaiduMapProxy.getInstance().getCachedLocation();
		double distanceMeters;
		if (currentLocation==null){
			Log.w(LOG_TAG, "currentLocation is null");
			distanceMeters = 0;
		}
		else{
			distanceMeters = DistanceCalculator.GetLongDistance(
				orderData.longitude, orderData.latitude,
				currentLocation.getLongitude(), currentLocation.getLatitude());
		}
		tvDistance.setText(String.format(
				getString(R.string.str_realtime_snapup_distance),
				distanceMeters / 1000));
		if (Double.isNaN(data.latitude)){
			Log.w(LOG_TAG, "no valid latitude found, skipping reversegeo.");
		}
		else {
			queryDistrict(data.latitude, data.longitude);
		}
		downloadAudio(data);
	}
	
	private void downloadAudio(NewOrderPushData data){
		if (data.voicePath==null || data.voicePath.equals("") || data.voicePath.equals("null")){
			findViewById(R.id.realtime_snapup_rl_voiceline).setVisibility(View.GONE);
			findViewById(R.id.realtime_snapup_img_demand_txtonly).setVisibility(View.VISIBLE);
			return ;
		}
		final Button btnAudio = (Button) findViewById(R.id.realtime_snapup_imgbtn_voice);
		btnAudio.setText("请稍候，正在下载订单音频...");
		File serverFile = new File(orderData.voicePath);
		final File orderFile = new File(FileUtil.getTemporaryAudioPath()
				+ File.separator + serverFile.getName());
		Log.d(LOG_TAG, "downloading audio:"+orderFile.getAbsolutePath());
		NetUtil.requestDownloadFile(orderData.voicePath, orderFile, new OnProgressChangedListener() {
			
			@Override
			public void onTaskFinished() {
				btnAudio.setText("点击播放");
				btnAudio.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						SimpleAudioPlayer.getInstance().playFileAudio(
							RealtimeSnapupActivity.this, orderFile.getAbsolutePath());
					}
				});
			}
			
			@Override
			public void onTaskFailed() {
				btnAudio.setText(">_<下载失败，无法播放");
			}
			
			@Override
			public void onProgressChanged(int min, int max, int progress) {
				Log.d(LOG_TAG, "downloader onTick : "+progress);
			}
		});
	}

	/**
	 * 执行反地理编码
	 * @param ltg 纬度
	 * @param lng 经度
	 */
	private void queryDistrict(double ltg, double lng) {
		// 创建地理编码检索实例
		final GeoCoder geoCoder = GeoCoder.newInstance();
		//
		OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
			// 反地理编码查询结果回调函数
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				Log.d(LOG_TAG, "revert geo:result received");
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					// 没有检测到结果
					Uis.toastShort(RealtimeSnapupActivity.this, "位置查询失败");
				}
				AddressComponent addr = result.getAddressDetail();
				tvDistrict.setText(addr.city+" "+addr.district);
				reverseGeoAddr = result.getAddress();
				tvDetailPosition.setText(reverseGeoAddr);
				// 释放地理编码检索实例
				geoCoder.destroy();
			}

			// 地理编码查询结果回调函数
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				//忽略
			}
		};
		// 设置地理编码检索监听者
		geoCoder.setOnGetGeoCodeResultListener(listener);
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(ltg, lng)));
	}
	
	//执行抢单操作。
	private final void performSnapup() {
		BDLocation currentLocation = BaiduMapProxy.getInstance().getCachedLocation();
		final double distanceMeters = DistanceCalculator.GetLongDistance(
				orderData.longitude, orderData.latitude,
				currentLocation.getLongitude(), currentLocation.getLatitude());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SRL.Param.PARAM_ORDER_ID, orderData.orderId);
		map.put(SRL.Param.PARAM_ORDER_VERSION, orderData.versionUID);
		map.put(SRL.Param.PARAM_ORDER_COACH_GPS, currentLocation.getLatitude()+","+currentLocation.getLongitude());
		map.put(SRL.Param.PARAM_ORDER_COACH_POS, currentLocation.getAddrStr());
		NetUtil.requestStringData(SRL.Method.METHOD_EXECUTE_SNAPUP, map, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				Uis.toastShort(RealtimeSnapupActivity.this, "抢单成功, Mua~");
				startActivity(SnapSuccessActivity.getIntent(RealtimeSnapupActivity.this,
						orderData, reverseGeoAddr, distanceMeters));
				finish();
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				switch (errorCode) {
				case SRL.ReturnCode.ERR_ORDER_NOT_EXIST:
					MessageDialogFragment.newWarningInstance(
							R.string.str_realtime_snapup_err_order_notexist)
							.show(getFragmentManager(), "");
					break;
				case SRL.ReturnCode.ERR_ORDER_OUT_OF_DATE:
					MessageDialogFragment.newWarningInstance(
							R.string.str_realtime_snapup_err_order_outofdate)
							.show(getFragmentManager(), "");
					break;
				case SRL.ReturnCode.ERR_ORDER_NOT_VALID:
					MessageDialogFragment.newWarningInstance(
							R.string.str_realtime_snapup_err_order_notvalid)
							.show(getFragmentManager(), "");
					break;
				case SRL.ReturnCode.ERR_ORDER_CANCELED:
					MessageDialogFragment.newWarningInstance(
							R.string.str_realtime_snapup_err_order_cancelled)
							.show(getFragmentManager(), "");
					break;
				case SRL.ReturnCode.ERR_ORDER_SNAPPED_BY_OTHER:
					MessageDialogFragment.newWarningInstance(
							R.string.str_realtime_snapup_err_order_snappedbyother)
							.show(getFragmentManager(), "");
					break;
				default:
					MessageDialogFragment.newWarningInstance(
							R.string.str_err_unknown_reason)
							.show(getFragmentManager(), "");
					break;
				}
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

	@Override
	public void onConfirmButtonClicked(DialogFragment fragment) {
		fragment.dismiss();
		finish();
	}
}
