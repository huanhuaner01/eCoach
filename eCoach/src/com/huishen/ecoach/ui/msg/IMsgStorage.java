package com.huishen.ecoach.ui.msg;

import java.util.ArrayList;

/**
 * @author Muyangmin
 * @create 2015-2-20
 */
public interface IMsgStorage {
	/**
	 * 获取存储在介质上的所有消息。
	 * @return 返回消息的列表；如果没有取到或本来就没有，返回空列表。
	 */
	ArrayList<AppMessage> getAppMessages();
	/**
	 * 将一条消息存储到介质上。
	 * @param msg 希望进行存储的消息。
	 * @return 如果存储成功，返回true；否则返回false。
	 */
	boolean saveAppMessage(AppMessage msg);
	/**
	 * 清空介质上的所有消息，永久删除之。
	 */
	void clearAllMessages();
}
