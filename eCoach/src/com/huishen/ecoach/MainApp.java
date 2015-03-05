package com.huishen.ecoach;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.huishen.ecoach.net.LruBitmapCache;
import com.huishen.ecoach.ui.login.Coach;
import com.huishen.ecoach.umeng.CustomUMessageHandler;
import com.umeng.message.PushAgent;

import android.app.Application;
import android.util.Log;

/**
 * 继承Application类，实现应用全局变量的管理，及部分第三方库的初始化工作。
 * @author Muyangmin
 * @create 2015-2-6
 */
public final class MainApp extends Application {
	
	private static MainApp instance;
	private Coach loginCoach = null;
	
	private static final String LOGIN_OBJECT_FILE = "weaklogin.ekq";	//confusing file name for safety
	private static final String LOG_TAG = "MainApp";
	
	private RequestQueue requestQueue;
	private ImageLoader mImageLoader;
	private LocationClient mLocationClient;
	private String sessionId;
	
	//Singleton
	public static MainApp getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this; //this first (and the only) instance created by system.
		init();
	}
	
	/**
	 * 初始化各种全局变量和第三方库。	
	 */
	private void init() {
		requestQueue = Volley.newRequestQueue(this); // Volley框架
		mImageLoader = new ImageLoader(requestQueue, new LruBitmapCache());// Volley框架
		PushAgent.getInstance(instance).setMessageHandler(
				new CustomUMessageHandler());// 友盟推送
		SDKInitializer.initialize(this); // 初始化百度地图
		mLocationClient = new LocationClient(this);
	}

	/**
	 * 提交新的网络请求。
	 */
	public final <T> void addNetworkRequest(Request<T> request){
		if (requestQueue != null){
			requestQueue.add(request);
		}
	}
	
	public final LocationClient getLocationClient(){
		return mLocationClient;
	}

	/**
	 * 获取ImageLoader对象。
	 */
	public final ImageLoader getmImageLoader() {
		return mImageLoader;
	}
	
	/**
	 * 获得缓存的SessionId。
	 * @return
	 */
	public final String getSessionId(){
		return sessionId;
	}
	/**
	 * 设置SessionId。
	 * @param sessionId
	 */
	public final void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}

	/**
	 * 获取当前已登录的教练的信息。如果没有设置，返回null。
	 */
	public Coach getLoginCoach() {
		if (loginCoach == null){
			Log.w(LOG_TAG, "No active login information, read it from local.");
			loginCoach = readLoginInformation();
			Log.d(LOG_TAG, "weak login:" + loginCoach.toString());
		}
		return loginCoach;
	}

	public void setLoginCoach(Coach loginCoach) {
		if (loginCoach!=null){
			this.loginCoach = loginCoach;
			saveLoginInformation(loginCoach);			
		}
	}
	
	private final void saveLoginInformation(Coach coach){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(openFileOutput(LOGIN_OBJECT_FILE, MODE_PRIVATE));
			oos.writeObject(coach);
			oos.close();
		} catch (Exception e) {
			Log.w(LOG_TAG, "Cannot write object into file.");
		}
	}
	
	private final Coach readLoginInformation(){
		try {
			ObjectInputStream ois = new ObjectInputStream(openFileInput(LOGIN_OBJECT_FILE));
			Coach weaklogin = (Coach) ois.readObject();
			ois.close();
			return weaklogin;
		} catch (Exception e) {
			Log.w(LOG_TAG, "Cannot read object into file.");
			return null;
		}
	}

}
