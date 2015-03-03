package com.huishen.ecoach.umeng;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import com.huishen.ecoach.umeng.UmengPushConst.NewOrder;

/**
 * 代表新订单的消息推送。
 * @author Muyangmin
 * @create 2015-3-1
 */
public final class NewOrderPushData extends PushData {
	
	private static final long serialVersionUID = 1L;
	
	public final String orderId;		//订单ID
	public final String versionUID;		//订单版本号
	public final String stuName;		//学生姓名
	public final String content;		//需求内容
	public final long createTime;		//开始时间
	public final long deadline;			//截止时间
	public final String voicePath;		//语音位置
	public final double latitude; 		//纬度
	public final double longitude; 		//经度
//	public final float distance;		//距离
//	public final String detailPosition;	//详细地点
	
	public NewOrderPushData(Map<String, String> extra) {
		super(extra);
		orderId = extra.get(NewOrder.PARAM_ORDERID);
		versionUID = extra.get(NewOrder.PARAM_ORDERVERSION);
		stuName = extra.get(NewOrder.PARAM_STUDENT_NAME);
		content = extra.get(NewOrder.PARAM_CONTENT);
		createTime = getLong(extra, NewOrder.PARAM_CREATETIME);
		deadline = getLong(extra, NewOrder.PARAM_DEADLINE);
		voicePath = extra.get(NewOrder.PARAM_VOICE_PATH);
//		distance = getFloat(extra, NewOrder.PARAM_DISTANCE);
		latitude = getDouble(extra, NewOrder.PARAM_LATITUDE);
		longitude = getDouble(extra, NewOrder.PARAM_LONGITUDE);
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		StringBuilder builder = new StringBuilder();
		builder.append("NewOrderPushData [orderId=").append(orderId)
				.append(", versionUID=").append(versionUID)
				.append(", stuName=").append(stuName).append(", content=")
				.append(content).append(", createTime=").append(sdf.format(createTime))
				.append(", deadline=").append(sdf.format(deadline)).append(", voicePath=")
				.append(voicePath).append(", latitude=").append(latitude)
				.append(", longitude=").append(longitude)/*.append(", distance=")
				.append(distance)*/.append(", msgType=").append(msgType)
				.append("]");
		return builder.toString();
	}
}
