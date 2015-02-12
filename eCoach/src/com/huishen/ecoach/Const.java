package com.huishen.ecoach;

/**
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class Const {
	/**
	 * Preference XML file name.
	 */
	public static final String PREFS_APP = "eCoach";
	/**
	 * 检查是否首次使用应用,value为boolean类型。
	 */
	public static final String KEY_FIRSTUSE = "first";
	/**
	 * 设置启动界面最低显示时间，单位为ms。
	 */
	public static final int SPLASH_MIN_LENGTH = 1000;
	
	/**
	 * 保存已验证过的手机号，value为String类型。
	 */
	public static final String KEY_VERIFIED_PHONE = "verifiedPhone";
	
	/**
	 * 检查是否已经注册完毕（即所有注册步骤完成）,value为boolean类型。
	 */
	public static final String KEY_REGISTER_COMPLETED = "regcomp";
}
