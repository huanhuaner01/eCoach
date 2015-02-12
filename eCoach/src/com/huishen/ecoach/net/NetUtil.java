package com.huishen.ecoach.net;

import java.io.File;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.huishen.ecoach.MainApp;

/**
 * 放置与网络相关的快捷方法，并对外隐藏服务器的根地址。
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class NetUtil {

	/**
	 * 公用的网络请求日志标签。供匿名类等没有自己的TAG字段的对象使用。
	 */
	public static final String REQUEST_LOG_TAG = "NetRequest";

	/**
	 * 提交String数据请求。
	 * 
	 * @param relativePath 资源的相对位置
	 * @param listener 回调监听器
	 */
	public static final void requestStringData(String relativePath,
			Listener<String> listener) {
		if (relativePath == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		MainApp.getInstance().addNetworkRequest(
				new AbsStringRequest(relativePath, listener));
	}

	/**
	 * 提交带有参数的String数据请求。
	 * 
	 * @param relativePath 资源的相对位置
	 * @param listener 回调监听器
	 */
	public static final void requestStringData(String relativePath,
			final Map<String, String> params, Listener<String> listener) {
		if (relativePath == null || params == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		MainApp.getInstance().addNetworkRequest(
				new AbsStringRequest(relativePath, listener) {
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						return params;
					}
				});
	}

	/**
	 * 请求上传文件。
	 * @param file 要上传的文件
	 * @param relativePath 服务器的目标相对路径
	 * @param listener 回调监听器
	 */
	public static final void requestUploadFile(final File file,
			final String relativePath, final UploadResponseListener listener) {
		if (relativePath == null || listener == null) {
			throw new NullPointerException("params cannot be null!");
		}
		new HttpUploadTask(file, ServerAddressProvider.getServerAddress()
				+ relativePath, listener).execute();
	}
}
