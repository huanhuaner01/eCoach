package com.huishen.ecoach.ui.msg;

import java.io.Serializable;

/**
 * @author Muyangmin
 * @create 2015-2-20
 */
public final class AppMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private MessageType type;
	private String title;
	private String content;
	private long time;
	private String detailURL;

	/**
	 * 标记消息的类型。
	 * 
	 * @author Muyangmin
	 * @create 2015-2-20
	 */
	public static enum MessageType {
		/**
		 * 系统消息
		 */
		TYPE_SYSTEM,
		/**
		 * 活动消息
		 */
		TYPE_ACTION,
		/**
		 * 其他类型
		 */
		TYPE_OTHER;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetailURL() {
		return detailURL;
	}

	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}
}
