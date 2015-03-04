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
	public static final String KEY_LOGIN_AUTOLOGIN = "autologin";

	/**
	 * 保存最新登录成功的手机号。同一个设备上验证的手机号不允许多于一个，
	 * 因此每次都应该替换掉原来的手机号。注意区分该字段和{@link #KEY_REGISTER_VERIFIED_PHONE}的区别。
	 */
	public static final String KEY_LOGIN_LASTLOGIN_PHONE = "lastLogin";
	/**
	 * 保存最新登录成功的密码值。当用户手动退出登录后，清除该值。
	 */
	public static final String KEY_LOGIN_LASTLOGIN_PWD = "lastLoginpwd";

	/**
	 * 保存已验证过的手机号，其值为String类型。同一个设备上验证的手机号不允许多于一个，
	 * 因此每次都应该替换掉原来的手机号。
	 */
	public static final String KEY_REGISTER_VERIFIED_PHONE = "verifiedPhone";
	/**
	 * 保存设备的MobileFlag，其值为String类型。MobileFlag为每次登录的时候使用，每个用户的都不一样，
	 * 因此只需要在登录时替换掉原有的即可。
	 */
	public static final String KEY_MOBILE_FLAG = "mobileFlag";
	
	/**
	 * 保存已设置的密码，其值为String类型。
	 */
	public static final String KEY_REGISTER_PASSWORD = "pwd";
	/**
	 * 保存已设置的密码的MD5值，其值为String类型。
	 */
	public static final String KEY_REGISTER_PASSWORD_MD5 = "pwdmd5";
	/**
	 * 保存已设置的姓名，其值为String类型。
	 */
	public static final String KEY_REGISTER_COACH_NAME = "name";
	/**
	 * 保存已设置的驾校，其值为String类型。
	 */
	public static final String KEY_REGISTER_COACH_SCHOOL = "school";
	/**
	 * 保存已设置的车牌号，其值为String类型。
	 */
	public static final String KEY_REGISTER_COACH_CARNO = "carno";
	/**
	 * 保存已设置的教练证号，其值为String类型。
	 */
	public static final String KEY_REGISTER_COACH_CERTNO = "certno";
	/**
	 * 保存已上传的头像文件在服务器上的相对地址，其值为String类型。
	 */
	public static final String KEY_REGISTER_COACH_AVATAR = "avatar";
	/**
	 * 检查是否已经注册完毕（即所有注册步骤完成）,其值为boolean类型。
	 */
	public static final String KEY_REGISTER_COMPLETED = "regcomp";
}
