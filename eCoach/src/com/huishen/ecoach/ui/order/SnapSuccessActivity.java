package com.huishen.ecoach.ui.order;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.huishen.ecoach.R;
import com.huishen.ecoach.map.BaiduMapProxy;
import com.huishen.ecoach.ui.parent.NewIntentParentActivity;
import com.huishen.ecoach.umeng.NewOrderPushData;
import com.huishen.ecoach.util.Uis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SnapSuccessActivity extends NewIntentParentActivity implements OnClickListener{
	
	private static final String EXTRA_PUSHDATA = "succorder";
	private static final String EXTRA_STUPOSITION = "stupos";
	private static final String EXTRA_DISTANCE = "distance";
	
	private static final String LOG_TAG = "SnapSuccessActivity";
	
	private TextView tvStuPosition, tvCoachPosition;
	private Button btnComplaint;
	private ImageButton imgbtnCall;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private NewOrderPushData orderPushData;
	private String stuPosition;
	private double distance;
	
	/**
	 * 获取启动该Activity的Intent。
	 * @param context 上下文信息
	 * @param pushData 订单基础数据
	 * @param stuPosition 学员地址的文字描述
	 * @param distanceMeters 两者的距离，单位为米
	 * @return 返回封装的Intent。 
	 */
	public static final Intent getIntent(Context context,
			NewOrderPushData pushData, String stuPosition, double distanceMeters) {
		Intent intent = new Intent(context, SnapSuccessActivity.class);
		intent.putExtra(EXTRA_PUSHDATA, pushData);
		intent.putExtra(EXTRA_STUPOSITION, stuPosition);
		intent.putExtra(EXTRA_DISTANCE, distanceMeters);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snap_success);
		orderPushData = (NewOrderPushData) getIntent().getSerializableExtra(EXTRA_PUSHDATA);
		stuPosition = getIntent().getStringExtra(EXTRA_STUPOSITION);
		distance = getIntent().getDoubleExtra(EXTRA_DISTANCE, 0.0);
		initWidgets();
	}
	
	private void initWidgets() {
		tvStuPosition = (TextView)findViewById(R.id.snap_success_tv_stu_postiion);
		tvCoachPosition = (TextView)findViewById(R.id.snap_success_tv_coach_postiion);
		btnComplaint = (Button)findViewById(R.id.snap_success_btn_complaint);
		imgbtnCall = (ImageButton)findViewById(R.id.snap_success_imgbtn_makecall);
		btnComplaint.setOnClickListener(this);
		imgbtnCall.setOnClickListener(this);
		
		tvStuPosition.setText(stuPosition);
		drawMap();
	}
	
	private void drawMap(){
		BDLocation location = BaiduMapProxy.getInstance().getCachedLocation();
		tvCoachPosition.setText(location.getAddrStr());
		
		//绘制地图
		mMapView = (MapView) findViewById(R.id.snap_success_mapview);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius()).direction(100)
				.latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,getScaleRate(distance));	
		mBaiduMap.animateMapStatus(u);
		//加入学员图标覆盖物
		LatLng stuPosLatLng = new LatLng(orderPushData.latitude, orderPushData.longitude);
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_snapup_success_stumarker);
		OverlayOptions ooA = new MarkerOptions().position(stuPosLatLng).icon(bdA)
				.zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooA);
		//加入教练图标覆盖物
		LatLng cohPosLatLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		BitmapDescriptor bdB = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_snapup_success_coachmarker);
		OverlayOptions ooB = new MarkerOptions().position(cohPosLatLng)
				.icon(bdB).zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooB);
		//add marker to set mark listener
	}
	
	private int getScaleRate(double distance){
		if (distance < 1000){
			return 15;			//15表示比例尺单位为500米
		}
		else if (distance < 3000){
			return 13;
		}
		else if (distance < 8000){
			return 12;
		}
		return 10;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.snap_success_btn_complaint:
			Uis.toastShort(SnapSuccessActivity.this, R.string.str_feature_unsupported);
			break;
		case R.id.snap_success_imgbtn_makecall:
			String phone = orderPushData.phoneNumber;
			if (phone==null || phone.equals("")){
				Log.w(LOG_TAG, "no avaliable phonenumber.");
			}
			else{
				Log.d(LOG_TAG, "calling" + orderPushData.phoneNumber + "...");
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+orderPushData.phoneNumber));
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		
	}
}
