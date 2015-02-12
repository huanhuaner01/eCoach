package com.huishen.ecoach.net;

/**
 * 该类中的SRL是Server Resource Location 的缩写，表示资源在服务器上的相对位置，为方便客户端代码调用故使用SRL缩写。
 * 该类中只存放常量，不提供其他任何方法实现。
 * <p>
 * 在实际编程中，建议使用 {@link #RESULT_OK}字段进行返回值测试，而不是硬编码其数值。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-2-7
 */
public final class SRL {

	/**
	 * 通用返回值，代表成功信息。
	 */
	public static final int RESULT_OK = 0;

	/**
	 * 用户登录方法。 需要提供的参数有：用户名(username/String)，密码(password/String)。
	 */
	public static final String METHOD_LOGIN = "/login.do";

	// ----------------------------------教练注册模块BEGIN-------------------------------------------

	/**
	 * 通过短信获取验证码。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{code:0|1}0:发送成功，1:发送异常
	 */
	public static final String METHOD_GET_VERIFY_CODE = "/adr/sendCohVCode";
	/**
	 * 验证注册码 参数:vcode:4456 返回值:{code:0|1} 0:匹配成功，1:匹配失败
	 */
	public static final String METHOD_VERIFY_VCODE = "/adr/cohVcode";
	/**
	 * 手机号参数。
	 */
	public static final String PARAM_MOBILE_NUMBER = "mobile";
	/**
	 * 上传教练头像。 参数：无。 返回值:{code:0|1,url:'/attachment/coh-head/2015122593484.jpg'}
	 * 0:上传成功，1:上传异常,url:状态为0时的头像路径
	 */
	public static final String METHOD_UPLOAD_AVATAR = "/adr/uploadCohHead";
	/**
	 * 上传教练证件。 参数：无。
	 * 返回值:{code:0|1,url:'/attachment/coh-voucher/2015122593484.jpg'}
	 * 0:上传成功，1:上传异常,url:状态为0时的证件路径
	 */
	public static final String METHOD_UPLOAD_CERTIFICATES = "/adr/uploadCohVoucher";
	/**
	 * 验证手机号是否存在。 参数:mob=18782920468 返回值:{code:0|1} 0:不存在，1:存在
	 */
	public static final String METHOD_VERIFY_IF_NUMBER_EXIST = "/adr/vcohMob";

	/**
	 * 提交注册的信息。
	 * <p>
	 * 参数:<br/>
	 * mobile:18782920468;//手机号<br/>
	 * password:e10adc3949ba59abbe56e057f20f883e;//密码,(先加密再传-md5)<br/>
	 * username:王贵;//用户名<br/>
	 * school:九洲驾校;//所属驾校<br/>
	 * busNumber:K1052a;//车牌号<br/>
	 * certificateNo:510902199403150891;//证件号<br/>
	 * headImg:'/attachment/coh-head/2015122593483.jpg';//头像相对路径<br/>
	 * vouImg1:'/attachment/coh-voucher/2015122593483.jpg';//驾驶证<br/>
	 * vouImg2:'/attachment/coh-voucher/2015122593483.jpg';//行驶证<br/>
	 * vouImg3:'/attachment/coh-voucher/2015122593483.jpg';//运营证<br/>
	 * vouImg4:'/attachment/coh-voucher/2015122593483.jpg';//教练证<br/>
	 * </p>
	 * <p>
	 * 返回值:{code:0|1} 0:注册成功，1:注册异常
	 * </p>
	 */
	public static final String METHOD_FINISH_REGISTER = "/adr/registerCoh";

	// ---------------------------------教练注册模块END-------------------------------------------
}
