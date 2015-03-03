package com.huishen.ecoach.map;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.huishen.ecoach.MainApp;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;

/**
 * @author Muyangmin
 * @create 2015-3-3
 */
public final class BaiduMapProxy {

	private static final String LOG_TAG = "BaiduMapProxy";

	private static final BaiduMapProxy instance = new BaiduMapProxy();
	private static final int LOCATE_PERIOD = 5 * 60 * 1000;// 设置发起定位请求的间隔时间

	private BDLocation mCachedLocation;
	private LocationClient mLocationClient;
	
	private BaiduMapProxy() {
	}

	public static BaiduMapProxy getInstance() {
		return instance;
	}

	public void startLocate(Context context) {
		mLocationClient = new LocationClient(context.getApplicationContext());
		BDLocationListener myListener = new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				int type = location.getLocType();
				Log.d(LOG_TAG, "loc type = " + type);
				// Receive Location
				if (type == BDLocation.TypeNetWorkLocation
						|| type == BDLocation.TypeGpsLocation) {
					Log.d(LOG_TAG, buildLocationString(location));
					mCachedLocation = location;
					syncGps(location);
				}
			}
		};
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(LOCATE_PERIOD);
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		mLocationClient.setLocOption(option);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		Log.d(LOG_TAG, "已经开始定位");
	}
	
	private final void syncGps(BDLocation location){
		long id = MainApp.getInstance().getLoginCoach().getId();
		if (id <= 0){
			Log.w(LOG_TAG, "no login coach, skipping sync.");
			return ;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_COACH_ID, String.valueOf(id));
		params.put(SRL.Param.PARAM_SYNCGPS_LAT, String.valueOf(location.getLatitude()));
		params.put(SRL.Param.PARAM_SYNCGPS_LNG, String.valueOf(location.getLongitude()));
		NetUtil.requestStringData(SRL.Method.METHOD_SYNC_GPS, params, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				Log.i(LOG_TAG, "finish sync gps.");
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				//ignore.
				Log.w(LOG_TAG, "Fail to sync gps.");
			}
		});
	}
	
	public BDLocation getCachedLocation() {
		return mCachedLocation;
	}
	
	private String buildLocationString(BDLocation loc) {
		return new StringBuilder(100).append("(").append(loc.getLongitude()).append(",")
				.append(loc.getLatitude()).append(")").append(",")
				.append("精度=").append(loc.getRadius()).append("米,")
				.append(loc.getCity()).append(" ").append(loc.getDistrict()).append(",")
				.append(loc.getAddrStr()).toString();
	}

}
