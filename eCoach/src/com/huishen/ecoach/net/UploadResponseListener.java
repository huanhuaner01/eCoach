package com.huishen.ecoach.net;

/**
 * 监听上传的结果。
 * @author Muyangmin
 * @create 2015-2-12
 */
public interface UploadResponseListener {
	/**
	 * 处理上传操作返回的结果。
	 * @param str 结果
	 */
	void onSuccess(String str);
	/**
	 * 处理上传操作返回的结果。
	 * @param httpCode HTTP状态码
	 */
	void onError(int httpCode);
	/**
	 * 监听上传百分比。
	 * @param hasFinished 已完成的百分比
	 */
	void onProgressChanged(int hasFinished);
}
