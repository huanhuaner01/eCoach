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
		public static final String METHOD_FINISH_REGISTER = "/cohMobile/updateCoh";
		// ----------------------------------教练注册模块BEGIN-------------------------------------------
		
		/**
		 * 通过短信获取验证码。 参数 : {@link #PARAM_MOBILE_NUMBER} 返回值:{code:0|1}0:发送成功，1:发送异常
		 */
		public static final String METHOD_GET_VERIFY_CODE = "/cohMobile/sendCohVCode";
		/**
		 * 用户登录方法。参数:
		 * 	username=18782920468
		 * 	password=123456;//不加密
		 * 返回值:{code:0|1|2,info:{...}}
		 * 0:登录成功，1:用户名密码不正确,2:帐号被禁用 info:code为0时，有值 (教练个人信息数据)
		 */
		public static final String METHOD_LOGIN = "/cohMobile/cohLogin";
		/**
		 * 提交注册的信息（第一步） 参数: mobile:18782920468;//手机号
		 * password:e10adc3949ba59abbe56e057f20f883e;//密码,(先加密再传-md5) 返回值:{code:0|1}
		 * 0:注册成功，1:注册异常
		 */
		public static final String METHOD_REGISTER_COACH = "/cohMobile/registerCoh";
		/**
		 * 上传教练头像。 参数：无。 返回值:{code:0|1,url:'/attachment/coh-head/2015122593484.jpg'}
		 * 0:上传成功，1:上传异常,url:状态为0时的头像路径
		 */
		public static final String METHOD_UPLOAD_AVATAR = "/cohMobile/uploadCohHead";
		/**
		 * 上传教练证件。 参数：无。
		 * 返回值:{code:0|1,url:'/attachment/coh-voucher/2015122593484.jpg'}
		 * 0:上传成功，1:上传异常,url:状态为0时的证件路径
		 */
		public static final String METHOD_UPLOAD_CERTIFICATES = "/cohMobile/uploadCohVoucher";
		/**
		 * 验证注册码 参数:vcode:4456 返回值:{code:0|1} 0:匹配成功，1:匹配失败
		 */
		public static final String METHOD_VERIFY_VCODE = "/cohMobile/cohVcode";
		/**
		 * 修改用户的登录密码。
		 */
		public static final String METHOD_MODIFY_PASSWORD = "";
		/**
		 * 验证手机号是否存在于服务器端，存在则发送短信。 (用于重置密码时发送验证码)
		 * 参数:mob=18782920468 返回值:{code:0|1|2} 2:手机号不存在,0:发送验证码成功，1:发送验证码异常
		 */
		public static final String METHOD_GET_VCODE_IF_EXIST = "/cohMobile/vMobVcode";
		/**
		 * 重置用户的登录密码。
		 * 参数:
		 * vcode=4526
		 * password=e10adc3949ba59abbe56e057f20f883e;//加密后再上传-md5
		 * mobile=18782920468
		 * 返回值:{code:0|1|2}
		 * 0:重置成功，1:验证码不匹配,2:重置异常
		 */
		public static final String METHOD_RESET_PASSWORD = "/cohMobile/resetCohPwd";
		/**
		 * 提交用户的意见反馈。
		 */
		public static final String METHOD_FEEDBACK = "";
		
		/**
		 * 查询教练的抢单状态。
		 * 参数:	coachId:3;//教练ID
		 * 返回值:{model:0|1}0:关闭抢单状态，1:开启抢单状态
		 */
		public static final String METHOD_QUERY_SNAPUP_STATUS = "/cohMobile/queryModel";
		/**
		 * 参数:
		 * coachId:3;//教练ID
		 * model:0|1
		 * 返回值:{code:0|1}0:切换成功，1:发布异常
		 */
		public static final String METHOD_SET_SNAPUP_STATUS = "/cohMobile/ableModel";
	}
	
	/**
	 * 存放调用服务器资源时使用的参数名称。
	 * @author Muyangmin
	 * @create 2015-2-20
	 */
	public static final class Param{
		/**
		 * 通用参数MOBILE_FLAG，用于服务器鉴别Session。
		 */
		public static final String PARAM_MOBILE_FLAG = "mobileFlag";

		/**
		 * 车牌号参数。
		 */
		public static final String PARAM_CARNO = "busNumber";
		/**
		 * 教练证号码参数。
		 */
		public static final String PARAM_COACH_CERTNO = "certificateNo";
		/**
		 * 通用手机号参数。
		 */
		public static final String PARAM_MOBILE_NUMBER = "mobile";
		/**
		 * 验证码参数。
		 */
		public static final String PARAM_VCODE = "vcode";
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
		 * 重置密码时获取验证码的手机号参数。
		 */
		//为啥不等于mobile？我也不知道。
		public static final String PARAM_RESETPWD_GETVCODE_MOBILE = "mob";
		/**
		 * 提交意见反馈时使用，信息内容。
		 */
		public static final String PARAM_FEEDBACK_CONTENT = "content";
		/**
		 * 查询教练抢单模式状态时使用，教练ID。
		 */
		public static final String PARAM_COACH_ID = "coachId";
		/**
		 * 切换教练抢单模式状态时使用，是否开始。(0=结束，1=开始)
		 */
		public static final String PARAM_BEGIN_SNAPUP = "model";
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
		 * 发送验证码时返回：发送失败。
		 */
		public static final int ERR_GETVCODE_FAILTOSEND = 1;
		/**
		 * 发送验证码时返回：手机号码不存在。
		 */
		public static final int ERR_GETVCODE_PHONE_NOT_EXIST = 2;
		/**
		 * 重置密码时返回：验证码不匹配。
		 */
		public static final int ERR_RESETPWD_VCODE_NOTMATCH = 1;
		/**
		 * 重置密码时返回：重置失败。
		 */
		public static final int ERR_RESETPWD_FAIL = 2;
		/**
		 * 查询抢单模式时返回：模式已关闭。
		 */
		public static final int INFO_STATUS_CLOSED = 0;
		/**
		 * 查询抢单模式时返回：模式已开启。
		 */
		public static final int INFO_STATUS_OPEN = 1;
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
		//package access
		protected static final String FIELD_SESSIONID = "sessionid";
		/**
		 * 登录返回字段：MobileFlag
		 */
		public static final String FIELD_MOBILE_FLAG = "mobileFlag";
		/**
		 * 登录返回字段：教练ID。
		 */
		public static final String FIELD_COACH_ID = "id";
		/**
		 * 登录返回字段：教练姓名。
		 */
		public static final String FIELD_COACH_NAME = "coachName";
		/**
		 * 登录返回字段：教练所属驾校。
		 */
		public static final String FIELD_COACH_SCHOOL = "schoolName";
		/**
		 * 登录返回字段：教练车牌号。
		 */
		public static final String FIELD_COACH_CARNO = "busNumber";
		/**
		 * 登录返回字段：教练手机。
		 */
		public static final String FIELD_COACH_PHONE = "phone";
		/**
		 * 登录返回字段：教练证号。
		 */
		public static final String FIELD_COACH_CERTNO = "certificateNo";
		/**
		 * 登录返回字段：头像地址。
		 */
		public static final String FIELD_COACH_AVATAR = "head";
		/**
		 * 登录返回字段：审核状态。
		 */
		public static final String FIELD_COACH_AUDIT_STATUS = "status";
		/**
		 * 登录返回字段：评星。
		 */
		public static final String FIELD_COACH_STAR_LEVEL = "score";
		/**
		 * 登录返回字段：订单总数。
		 */
		public static final String FIELD_COACH_ORDER_COUNT = "orderNum";
		/**
		 * 登录返回字段：排行榜。
		 */
		public static final String FIELD_COACH_RANGE = "sort";
		/**
		 * 登录返回字段：推荐指数。
		 */
		public static final String FIELD_COACH_RECOMMEND_INDEX = "recom";
		/**
		 * 查询抢单模式返回字段：模式状态。
		 */
		public static final String FIELD_SNAPUP_STATUS = "model";
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
