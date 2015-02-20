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
	 * 存放服务器上可调用的方法名称。 
	 * @author Muyangmin
	 * @create 2015-2-20
	 */
	public static final class Method{

		/**
		 * 完善注册信息(第三步保存操作)
		 * <p>
		 * 参数:<br/>
		 * mobile:18782920468;//手机号<br/>
		 * username:王贵;//用户名<br/>
		 * school:九洲驾校;//所属驾校<br/>
		 * busNumber: K1052a;//车牌号<br/>
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
		public static final String METHOD_FINISH_REGISTER = "/adr/updateCoh";
		// ----------------------------------教练注册模块BEGIN-------------------------------------------
		
		/**
		 * 通过短信获取验证码。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{code:0|1}0:发送成功，1:发送异常
		 */
		public static final String METHOD_GET_VERIFY_CODE = "/adr/sendCohVCode";
		/**
		 * 用户登录方法。 需要提供的参数有：用户名(username/String)，密码(password/String)。
		 */
		public static final String METHOD_LOGIN = "/adr/cohLogin";
		/**
		 * 提交注册的信息（第一步） 参数: mobile:18782920468;//手机号
		 * password:e10adc3949ba59abbe56e057f20f883e;//密码,(先加密再传-md5) 返回值:{code:0|1}
		 * 0:注册成功，1:注册异常
		 */
		public static final String METHOD_REGISTER_COACH = "/adr/registerCoh";
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
		 * 验证注册码 参数:vcode:4456 返回值:{code:0|1} 0:匹配成功，1:匹配失败
		 */
		public static final String METHOD_VERIFY_VCODE = "/adr/cohVcode";
		/**
		 * 修改用户的登录密码。
		 */
		public static final String METHOD_MODIFY_PASSWORD = "";
		/**
		 * 提交用户的意见反馈。
		 */
		public static final String METHOD_FEEDBACK = "";
	}
	
	/**
	 * 存放调用服务器资源时使用的参数名称。
	 * @author Muyangmin
	 * @create 2015-2-20
	 */
	public static final class Param{

		/**
		 * 车牌号参数。
		 */
		public static final String PARAM_CARNO = "busNumber";
		/**
		 * 教练证号码参数。
		 */
		public static final String PARAM_COACH_CERTNO = "certificateNo";
		/**
		 * 手机号参数。
		 */
		public static final String PARAM_MOBILE_NUMBER = "mobile";
		/**
		 * 密文密码。
		 */
		public static final String PARAM_PASSWORD = "password";
		/**
		 * 教练头像参数。
		 */
		public static final String PARAM_PATH_AVATAR = "headImg";
		/* 以下四行为其他证件参数。 */
		public static final String PARAM_PATH_CERT1 = "vouImg1";
		public static final String PARAM_PATH_CERT2 = "vouImg2";
		public static final String PARAM_PATH_CERT3 = "vouImg3";
		public static final String PARAM_PATH_CERT4 = "vouImg4";
		/**
		 * 所属驾校参数。
		 */
		public static final String PARAM_SCHOOL = "school";
		/**
		 * 教练姓名参数。
		 */
		public static final String PARAM_USERNAME = "username";
		/**
		 * 修改密码时使用，旧密码。
		 */
		public static final String PARAM_OLDPWD = PARAM_PASSWORD;
		/**
		 * 修改密码时使用，新密码。
		 */
		public static final String PARAM_NEWPWD = "newpwd";
		/**
		 * 提交意见反馈时使用，信息内容。
		 */
		public static final String PARAM_FEEDBACK_CONTENT = "content";
	}
	
	/**
	 * 存放返回码（多为错误代码）信息。
	 * @author Muyangmin
	 * @create 2015-2-20
	 */
	public static final class ReturnCode{
		/**
		 * 登录返回值：密码错误。
		 */
		public static final int ERR_LOGIN_WRONGPWD = 1;
		/**
		 * 登录返回值：帐户被禁用。
		 */
		public static final int ERR_LOGIN_ACCOUNT_FORBIDDEN = 2;
		/**
		 * 通用返回值，代表成功信息。
		 */
		public static final int RESULT_OK = 0;
	}
	
	/**
	 * 存放返回值字段信息。
	 * @author Muyangmin
	 * @create 2015-2-20
	 */
	public static final class ReturnField{
		/**
		 * 登录返回字段：教练姓名。
		 */
		public static final String FIELD_COACH_NAME = "name";
		/**
		 * 登录返回字段：教练所属驾校。
		 */
		public static final String FIELD_COACH_SCHOOL = "school";
		/**
		 * 登录返回字段：教练车牌号。
		 */
		public static final String FIELD_COACH_CARNO = "carno";
		/**
		 * 登录返回字段：教练手机。
		 */
		public static final String FIELD_COACH_PHONE = "phone";
		/**
		 * 登录返回字段：教练证号。
		 */
		public static final String FIELD_COACH_CERTNO = "certno";
		/**
		 * 通用信息返回参数，用于登录等。
		 */
		public static final String FIELD_INFO = "info";
		/**
		 * 通用返回参数，代表请求结果。
		 */
		public static final String FIELD_RETURN_CODE = "code";
		/**
		 * 上传文件、图片等时服务器返回的代表相对位置的Key。
		 */
		public static final String FIELD_URI = "uri";
	}
}
