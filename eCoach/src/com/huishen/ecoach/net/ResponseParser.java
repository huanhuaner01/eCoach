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
	 * 检查返回值是否为正常值。
	 * @param str 服务器返回的字符串值
	 * @return 返回值等于 {@link SRL#RESULT_OK}，则返回true；否则返回false
	 */
	public static final boolean isReturnSuccessCode(String str) {
		try {
			JSONObject json = new JSONObject(str);
			// 因为opt方式的默认值是0，为避免和正常值0混淆，使用get方式。
			int code = json.getInt(SRL.RESULT_KEY_CODE);
			return code == SRL.RESULT_OK;
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Huh, this response may be broken.");
			return false;
		}
	}
}
