package com.huishen.ecoach.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 放置UI相关的快捷方法。
 * 
 * @author Muyangmin
 * @create 2015-2-14
 */
public final class Uis {

	/**
	 * 发送Toast消息。
	 * 
	 * @param context 上下文信息。
	 * @param strid 要进行提示的文本 Id。
	 */
	public static final void toastShort(Context context, int strid) {
		toastShort(context, context.getResources().getString(strid));
	}

	/**
	 * 发送Toast消息。
	 * 
	 * @param context 上下文信息。
	 * @param msg 要进行提示的文本内容。
	 */
	public static final void toastShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
