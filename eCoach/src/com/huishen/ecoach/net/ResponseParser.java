package com.huishen.ecoach.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 处理应用内网络请求结果逻辑。
 * 
 * @author Muyangmin
 * @create 2015-2-13
 */
public final class ResponseParser {

	private static final String LOG_TAG = "ResponseParser";

	/**
	 * <p><b>已过时。</b>使用 {@link ResponseParser#getReturnCode(String)}代替。</p>
	 * 检查返回值是否为正常值。
	 * @param str 服务器返回的字符串值
	 * @return 返回值等于 {@link SRL#RESULT_OK}，则返回true；否则返回false
	 */
	@Deprecated
	public static final boolean isReturnSuccessCode(String str) {
		try {
			JSONObject json = new JSONObject(str);
			// 因为opt方式的默认值是0，为避免和正常值0混淆，使用get方式。
			int code = json.getInt(SRL.ReturnField.FIELD_RETURN_CODE);
			return code == SRL.ReturnCode.RESULT_OK;
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Huh, this response may be broken.");
			handleJSONException(e);
			return false;
		}
	}
	/**
	 * 解析服务器的返回值，
	 * @param str 服务器返回的字符串值
	 * @return 如果解析成功，则返回服务器的状态码；否则返回 {@link Integer#MIN_VALUE}。
	 */
	public static final int getReturnCode(String str) {
		try {
			JSONObject json = new JSONObject(str);
			// 因为opt方式的默认值是0，为避免和正常值0混淆，使用get方式。
			int code = json.getInt(SRL.ReturnField.FIELD_RETURN_CODE);
			return code;
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Huh, this response may be broken.");
			handleJSONException(e);
			return Integer.MIN_VALUE;
		}
	}
	
	/**
	 * 从返回值中取得指定Key的信息。
	 * @param str 返回的信息
	 * @param key 要取得的信息字段名
	 * @return 如果返回的状态码不为默认值或不存在对应的key，返回null
	 */
	public static final String getStringFromResult(String str, String key){
		try {
			Log.d(LOG_TAG, "parsing result[key=" + key + "]:" + str);
			JSONObject json = new JSONObject(str);
			return json.optString(key, null);
		} catch (JSONException e) {
			handleJSONException(e);
			return null;
		}
	}
	
	private static final void handleJSONException(JSONException e){
		Log.e(LOG_TAG, e.getMessage());
		e.printStackTrace();
	}
}
