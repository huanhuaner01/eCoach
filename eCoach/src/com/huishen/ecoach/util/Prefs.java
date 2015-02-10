package com.huishen.ecoach.util;

import com.huishen.ecoach.Const;

import android.content.Context;

/**
 * 为Preferences相关的操作提供快捷方式，对客户端代码隐藏使用的Preferences文件名和打开方式。
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class Prefs {
	/**
	 * 设置key对应的Preferences值。
	 */
	public static final void setBoolean(Context context, String key,
			boolean value) {
		context.getSharedPreferences(Const.PREFS_APP, Context.MODE_PRIVATE)
				.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取key对应的Preferences值，默认返回false。
	 */
	public static final boolean getBoolean(Context context, String key) {
		return context.getSharedPreferences(Const.PREFS_APP,
				Context.MODE_PRIVATE).getBoolean(key, false);
	}
	/**
	 * 获取key对应的Preferences值，默认返回 defaultValue。
	 */
	public static final boolean getBoolean(Context context, String key, boolean defautValue) {
		return context.getSharedPreferences(Const.PREFS_APP,
				Context.MODE_PRIVATE).getBoolean(key, defautValue);
	}
}
