/**
 * @encoding UTF-8
 */
package com.huishen.ecoach.umeng;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * 继承自友盟默认消息处理器，主要负责处理自定义消息、定制通知栏样式等。
 * @author Muyangmin
 * @create 2014-10-30
 * @version 1.0
 */
public final class CustomUMessageHandler extends UmengMessageHandler {
	
	private static final String LOG_TAG = "CustomUMessageHandler";
	
	/**
	 * 处理自定义消息。
	 */
	@Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
		Log.d(LOG_TAG, "custom message received.extra="+msg.extra);
		//使用参数传递信息，因此只关心extra字段。
		if (msg.extra != null){
			dispatchMessage(context, msg.extra);	
		}
    }
	
	private void dispatchMessage(Context context, Map<String, String> extra){
		Intent intent = new Intent();
		PushData data = null;	//附加数据，通过具体消息类型实例化其子类
		switch (PushData.getPushDataType(extra)){
			default:
				intent = new Intent(UmengPushConst.Action.ACTION_UNKNOWN_PUSHDATA);
				break;
		}
		intent.putExtra(UmengPushConst.EXTRA_PUSHDATA, data);
		context.sendOrderedBroadcast(intent, null);
		Log.d(LOG_TAG, "dispatch message : send broadcast completed.");
		// 更新数量广播，否则，在“我的”页面，收到了信息，却未更新数量
//		Intent ai = new Intent(Const.TDB_HP_NUM_REFRESH_ACTION);
//		context.sendBroadcast(ai);
	}
}
