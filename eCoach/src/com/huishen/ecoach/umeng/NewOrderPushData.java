package com.huishen.ecoach.umeng;

import java.util.Map;

import com.huishen.ecoach.umeng.UmengPushConst.NewOrder;

/**
 * 代表新订单的消息推送。
 * @author Muyangmin
 * @create 2015-3-1
 */
public final class NewOrderPushData extends PushData {
	
	private static final long serialVersionUID = 1L;
	
	public final String orderId;
	public final String versionUID;
	public final String stuName;
	public final String content;
	
	public NewOrderPushData(Map<String, String> extra) {
		super(extra);
		orderId = extra.get(NewOrder.PARAM_ORDERID);
		versionUID = extra.get(NewOrder.PARAM_ORDERVERSION);
		stuName = extra.get(NewOrder.PARAM_STUDENT_NAME);
		content = extra.get(NewOrder.PARAM_CONTENT);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewOrderPushData [orderId=").append(orderId)
				.append(", versionUID=").append(versionUID)
				.append(", stuName=").append(stuName).append(", content=")
				.append(content).append("]");
		return builder.toString();
	}
}
