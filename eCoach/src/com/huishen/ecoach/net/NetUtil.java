package com.huishen.ecoach.net;

import com.android.volley.Response.Listener;
import com.huishen.ecoach.MainApp;

/**
 * 该类的存在只是为了放置与网络相关的快捷方法，以减小编码工作量。这种设计方法是不推荐的。
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class NetUtil {
	/**
	 * 提交String数据请求。
	 * @param relativePath 资源的相对位置
	 * @param listener 回调监听器
	 */
	public static final void requestStringData(String relativePath,
			Listener<String> listener) {
		if (relativePath==null || listener==null){
			throw new NullPointerException("params cannot be null!");
		}
		MainApp.getInstance().addNetworkRequest(new AbsStringRequest(relativePath, listener));
	}
}
