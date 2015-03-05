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
import com.huishen.ecoach.util.Prefs;

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
	
	//用于将最后获得的地理位置保存到本地文件，降低崩溃率
	private static final String KEY_CACHED_LAT = "lat";
	private static final String KEY_CACHED_LNG = "lng";
	private static final String KEY_CACHED_ADDR = "addr";
	
	private BaiduMapProxy() {
	}

	public static BaiduMapProxy getInstance() {
		return instance;
	}

	/**
	 * 发起定位请求。如果重复调用，不会做任何处理。
	 * @param context 上下文信息。
	 */
	public void startLocate(final Context context) {
		mLocationClient = MainApp.getInstance().getLocationClient();
		if (mLocationClient.isStarted()){
			Log.d(LOG_TAG, "LocationClient has started, avoid dumplicate request.");
			return ;
		}
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
					saveLocationToLocal(context, location);
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
		mLocationClient.start();
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
	
	//将地理信息数据保存到本地文件中
	private void saveLocationToLocal(Context context, BDLocation location){
		Prefs.setString(context, KEY_CACHED_ADDR, location.getAddrStr());
		Prefs.setString(context, KEY_CACHED_LAT, String.valueOf(location.getLatitude()));
		Prefs.setString(context, KEY_CACHED_LNG, String.valueOf(location.getLongitude()));
	}
	
	//从缓存的本地文件中读取数据。
	private BDLocation readLocationFromLocal(Context context){
		BDLocation location = new BDLocation();
		location.setAddrStr(Prefs.getString(context, KEY_CACHED_ADDR));
		location.setLatitude(Double.parseDouble(Prefs.getString(context, KEY_CACHED_LAT)));
		location.setLongitude(Double.parseDouble(Prefs.getString(context, KEY_CACHED_LNG)));
		return location;
	}
	
	public BDLocation getCachedLocation() {
		if (mCachedLocation != null){
			return mCachedLocation;
		}
		else {
			Log.w(LOG_TAG, "no cached location avaliable, return local information.");
			return readLocationFromLocal(MainApp.getInstance().getApplicationContext());
		}
	}
	
	private String buildLocationString(BDLocation loc) {
		return new StringBuilder(100).append("(").append(loc.getLongitude()).append(",")
				.append(loc.getLatitude()).append(")").append(",")
				.append("精度=").append(loc.getRadius()).append("米,")
				.append(loc.getCity()).append(" ").append(loc.getDistrict()).append(",")
				.append(loc.getAddrStr()).toString();
	}

}
