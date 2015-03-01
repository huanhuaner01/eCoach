package com.huishen.ecoach.umeng;

/**
 * 存储推送相关的常量。
 * @author Muyangmin
 * @create 2015-2-28
 */
public final class UmengPushConst {
	/**
	 * 每次当应用启动后DeviceToken第一次可用（注册成功后或启动时检测到已注册后）时发出该广播。
	 * 该广播会附带一个参数 {@link #EXTRA_DEVICE_TOKEN}。
	 */
	public static final String ACTION_DEVICETOKEN_AVALIABLE = "com.huishen.ecoach.DEVICETOKEN_AVALIABLE";
	/**
	 * 数据类型：String。表示友盟的DeviceToken。
	 */
	public static final String EXTRA_DEVICE_TOKEN = "devicetoken";
	/**
	 * 别名的类型。
	 */
	protected static final String ALIAS_TYPE = "coh-client";
	/**
	 * 用于在广播中携带推送消息数据。
	 * @see Action
	 */
	protected static final String EXTRA_PUSHDATA = null;
	/**
	 * 动态广播的优先级。
	 */
	protected static final int ACTIVE_RECEIVER_PRIORITY = 100;
	/**
	 * 用于从推送消息参数列表中取出消息类型。
	 */
	protected static final String PARAM_MSG_TYPE = "msgType"; 
	
	/**
	 * 包含推送信息的广播的Action集合。
	 * @author Muyangmin
	 * @create 2015-2-28
	 */
	public static final class Action{
		/**
		 * 未知推送内容。
		 */
		public static final String ACTION_UNKNOWN_PUSHDATA = "com.huishen.ecoach.UNKNOWN_PUSHDATA";
	}
	
	/**
	 * 学员下单推送信息。
	 * @author Muyangmin
	 * @create 2015-2-28
	 */
	protected static final class NewOrder{
		/**
		 * 订单ID。
		 */
		public static final String PARAM_ORDERID = "tempOrderId";
		/**
		 * 订单版本号。
		 */
		public static final String PARAM_ORDERVERSION = "versionUID";
		/**
		 * 下单学员姓名。
		 */
		public static final String PARAM_STUDENT_NAME = "stuName";
		/**
		 * 订单需求内容。
		 */
		public static final String PARAM_CONTENT = "content";
	}
}