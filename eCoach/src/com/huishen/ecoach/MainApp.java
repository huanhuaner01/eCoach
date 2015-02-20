package com.huishen.ecoach;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.huishen.ecoach.ui.login.Coach;

import android.app.Application;

/**
 * 继承Application类，实现应用全局变量的管理，及部分第三方库的初始化工作。
 * @author Muyangmin
 * @create 2015-2-6
 */
public final class MainApp extends Application {
	
	private static MainApp instance;
	private Coach loginCoach = null;
	
	private RequestQueue requestQueue;
	
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
	private void init(){
		requestQueue = Volley.newRequestQueue(this);
	}

	/**
	 * 提交新的网络请求。
	 */
	public final <T> void addNetworkRequest(Request<T> request){
		if (requestQueue != null){
			requestQueue.add(request);
		}
	}

	/**
	 * 获取当前已登录的教练的信息。如果没有设置，返回null。
	 */
	public Coach getLoginCoach() {
		return loginCoach;
	}

	public void setLoginCoach(Coach loginCoach) {
		this.loginCoach = loginCoach;
	}
}
