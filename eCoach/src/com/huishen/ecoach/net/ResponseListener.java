package com.huishen.ecoach.net;

import android.util.Log;

import com.android.volley.Response.Listener;

/**
 * 该监听器会自动判断服务器的返回值并根据返回值判断请求是否成功，从而调用对应的回调方法。
 * <p>考虑到大部分时候应用逻辑上的请求失败是因为服务器返回值不正确（而不是网络原因），
 * 强烈推荐使用该监听器代替 Volley框架自带的Listener。</p>
 * 
 * @author Muyangmin
 * @create 2015-2-26
 */
public abstract class ResponseListener implements Listener<String> {

	private static final String LOG_TAG = "ResponseListener";

	// final to avoid overriding
	@Override
	public final void onResponse(String arg0) {
		Log.d(LOG_TAG, arg0);
		if (ResponseParser.isReturnSuccessCode(arg0)) {
			onSuccess(arg0);
		} else {
			onReturnBadResult(arg0);
		}
	}

	/**
	 * 当服务器返回值为默认的正确返回值时调用。
	 * 
	 * @param arg0
	 *            返回的结果。
	 * @see ResponseParser#isReturnSuccessCode(String)
	 */
	protected abstract void onSuccess(String arg0);

	/**
	 * 当服务器返回值为错误码时调用。
	 * 
	 * @param arg0
	 *            返回的结果。
	 */
	protected abstract void onReturnBadResult(String arg0);
}
