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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SnapSuccessActivity extends NewIntentParentActivity {
	
	private static final String EXTRA_PUSHDATA = "succorder";
	private static final String EXTRA_STUPOSITION = "stupos";
	
	private TextView tvStuPosition, tvCoachPosition;
	private Button btnComplaint;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private NewOrderPushData orderPushData;
	private String stuPosition;
	
	/**
	 * 获取启动该Activity的Intent。
	 * @param context 上下文信息
	 * @param pushData 订单基础数据
	 * @param stuPosition 学员地址的文字描述
	 * @return 返回封装的Intent。 
	 */
	public static final Intent getIntent(Context context,
			NewOrderPushData pushData, String stuPosition) {
		Intent intent = new Intent(context, SnapSuccessActivity.class);
		intent.putExtra(EXTRA_PUSHDATA, pushData);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snap_success);
		orderPushData = (NewOrderPushData) getIntent().getSerializableExtra(EXTRA_PUSHDATA);
		stuPosition = getIntent().getStringExtra(EXTRA_STUPOSITION);
		initWidgets();
	}
	
	private void initWidgets() {
		tvStuPosition = (TextView)findViewById(R.id.snap_success_tv_stu_postiion);
		tvCoachPosition = (TextView)findViewById(R.id.snap_success_tv_coach_postiion);
		btnComplaint = (Button)findViewById(R.id.snap_success_btn_complaint);
		btnComplaint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Uis.toastShort(SnapSuccessActivity.this, R.string.str_feature_unsupported);
			}
		});
		tvStuPosition.setText(stuPosition);
		
		BDLocation location = BaiduMapProxy.getInstance().getCachedLocation();
		tvCoachPosition.setText(location.getAddrStr());
		
		mMapView = (MapView) findViewById(R.id.snap_success_mapview);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius()).direction(100)
				.latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,13);	//15表示比例尺单位为500米
		mBaiduMap.animateMapStatus(u);
		LatLng stuPosLatLng = new LatLng(orderPushData.latitude, orderPushData.longitude);
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_snapup_success_stumarker);
		OverlayOptions ooA = new MarkerOptions().position(stuPosLatLng).icon(bdA)
				.zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooA);
		//add marker to set mark listener
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
}
