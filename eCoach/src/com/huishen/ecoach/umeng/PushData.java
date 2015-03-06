package com.huishen.ecoach.umeng;

import java.io.Serializable;
import java.util.Map;

import android.util.Log;


/**
 * 代表所有推送消息的父类，主要提供基本的框架支持。
 * 该类的子类所有的字段都应该仿效该类作为Final字段，方便操作并可以提升性能。
 * 并且，子类的构造器必须显式调用父类构造器。
 * @author Muyangmin
 * @create 2015-2-28
 */
public abstract class PushData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 未知类型的消息推送。
	 */
	public static final int TYPE_UNKNOWN = -1;
	/**
	 * 新订单推送。
	 */
	public static final int TYPE_NEWORDER = 1001;
	/**
	 * 绑定教练推送。
	 */
	public static final int TYPE_BINDCOACH = 1002;
	
	public final int msgType; // 消息类型

	public PushData(Map<String, String> map) {
		msgType = Integer.parseInt(map.get(UmengPushConst.PARAM_MSG_TYPE));
	}

	public final int getPushType() {
		return msgType;
	}

	/**
	 * 获取从参数中解析出来的消息类型。
	 * @param extra 友盟参数列表。
	 * @return 返回消息类型，若为 {@link #TYPE_UNKNOWN}，则表示参数有误。
	 */
	public static int getPushDataType(Map<String, String> extra) {
		int type = TYPE_UNKNOWN;
		try {
			type = Integer.parseInt(extra.get(UmengPushConst.PARAM_MSG_TYPE));
		} catch (NumberFormatException e) {
			Log.e(PushData.class.getSimpleName(), "bad format message!");
			e.printStackTrace();
		}
		return type;
	}
	/**
	 * 从Map中取出相关的值，默认返回 -1。
	 */
	protected final long getLong(Map<String, String> extra, String key){
		String value = extra.get(key);
		long res = -1L;
		if (value!=null){
			try {
				res = Long.parseLong(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * 从Map中取出相关的值，默认返回 {@link Float#NaN}。
	 */
	protected final float getFloat(Map<String, String> extra, String key){
		String value = extra.get(key);
		float res = Float.NaN;
		if (value!=null){
			try {
				res = Float.parseFloat(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	/**
	 * 从Map中取出相关的值，默认返回 {@link Double#NaN}。
	 */
	protected final double getDouble(Map<String, String> extra, String key){
		String value = extra.get(key);
		double res = Double.NaN;
		if (value!=null){
			try {
				res = Double.parseDouble(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

}
