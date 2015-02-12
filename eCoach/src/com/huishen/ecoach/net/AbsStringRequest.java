package com.huishen.ecoach.net;

import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huishen.ecoach.BuildConfig;

/**
 * 继承 {@link StringRequest}类，简化创建对象的参数。
 * <p>
 * 该类型请求的默认创建方式是使用<b>UTF-8</b>进行编码并进行<b>POST</b>传输， 可以通过在构造器中传递参数改变传输方式， 重写
 * {@link #getParamsEncoding()}以改变编码集。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
public class AbsStringRequest extends StringRequest {

	private static final String LOG_TAG = AbsStringRequest.class
			.getSimpleName();

	/**
	 * 创建一个新的String网络访问请求，使用POST方式提交参数。
	 * 
	 * @param relativePath
	 *            服务器资源的相对位置。
	 * @param listener
	 *            处理结果的回调监听器。
	 */
	public AbsStringRequest(String relativePath, Listener<String> listener) {
		this(Method.POST, relativePath, listener);
	}

	/**
	 * 创建一个新的String网络访问请求。
	 * 
	 * @param method
	 *            参数的提交方式，应当使用 {@link Method}中的常量。
	 * @param relativePath
	 *            服务器资源的相对位置。
	 * @param listener
	 *            处理结果的回调监听器。
	 */
	public AbsStringRequest(int method, String relativePath,
			Response.Listener<String> listener) {
		super(method, ServerAddressProvider.getServerAddress() + relativePath,
				listener, defaultErrorListener);
		String url = ServerAddressProvider.getServerAddress() + relativePath;
		Log.d(LOG_TAG, url);
	}

	private static final ErrorListener defaultErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(LOG_TAG, error.getMessage());
			if (BuildConfig.DEBUG) {
				error.printStackTrace();
			}
		}
	};

	/**
	 * 指定参数的编码，子类通常不要重写该方法。
	 * 
	 * @return 始终返回 {@link HTTP#UTF_8}.
	 * @see com.android.volley.Request#getParamsEncoding()
	 */
	@Override
	protected String getParamsEncoding() {
		return HTTP.UTF_8;
	}
}
