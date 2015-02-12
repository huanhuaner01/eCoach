package com.huishen.ecoach.net;

/**
 * 该类中的SRL是Server Resource Location 的缩写，表示资源在服务器上的相对位置，为方便客户端代码调用故使用SRL缩写。
 * 该类中只存放常量，不提供其他任何方法实现。
 * <p>在实际编程中，建议使用 {@link #RESULT_OK}字段进行返回值测试，而不是硬编码其数值。</p>
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class SRL {
	
	/**
	 * 通用返回值，代表成功信息。
	 */
	public static final int RESULT_OK = 0;
	
	/**
	 * 用户登录方法。
	 * 需要提供的参数有：用户名(username/String)，密码(password/String)。
	 */
	public static final String METHOD_LOGIN = "/login.do";
	
	/**
	 * 通过短信获取验证码。
	 * 参数 : {@link #PARAM_MOBILE_NUMBER}
	 * 返回值:{code:0|1}0:发送成功，1:发送异常
	 */
	public static final String METHOD_GET_VERIFY_CODE = "/adr/sendCohVCode";
	/**
	 * 手机号参数。
	 */
	public static final String PARAM_MOBILE_NUMBER = "mobile";
}
