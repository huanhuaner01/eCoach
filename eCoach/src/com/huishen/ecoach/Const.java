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
	 * 检查是否首次使用应用,其值为boolean类型。
	 */
	public static final String KEY_FIRSTUSE = "first";
	/**
	 * 检查是否允许自动登录,其值为boolean类型。
	 */
	public static final String KEY_AUTO_LOGIN = "autologin";
	
	/**
	 * 设置启动界面最低显示时间，单位为ms。
	 */
	public static final int SPLASH_MIN_LENGTH = 1000;

	/**
	 * 保存已验证过的手机号，其值为String类型。
	 */
	public static final String KEY_VERIFIED_PHONE = "verifiedPhone";
	/**
	 * 保存已设置的密码，其值为String类型。
	 */
	public static final String KEY_PASSWORD = "pwd";
	/**
	 * 保存已设置的密码的MD5值，其值为String类型。
	 */
	public static final String KEY_PASSWORD_MD5 = "pwdmd5";
	/**
	 * 保存已设置的姓名，其值为String类型。
	 */
	public static final String KEY_COACH_NAME = "name";
	/**
	 * 保存已设置的驾校，其值为String类型。
	 */
	public static final String KEY_COACH_SCHOOL = "school";
	/**
	 * 保存已设置的车牌号，其值为String类型。
	 */
	public static final String KEY_COACH_CARNO = "carno";
	/**
	 * 保存已设置的教练证号，其值为String类型。
	 */
	public static final String KEY_COACH_CERTNO = "certno";
	/**
	 * 保存已上传的头像文件在服务器上的相对地址，其值为String类型。
	 */
	public static final String KEY_COACH_AVATAR = "avatar";
	/**
	 * 检查是否已经注册完毕（即所有注册步骤完成）,其值为boolean类型。
	 */
	public static final String KEY_REGISTER_COMPLETED = "regcomp";
}
