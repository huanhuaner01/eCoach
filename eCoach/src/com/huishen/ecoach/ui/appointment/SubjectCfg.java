package com.huishen.ecoach.ui.appointment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

class SubjectCfg {

	private static final String LOG_TAG = "SubjectCfg";
	
	/**
	 * 用于格式化时间字符串。
	 */
	private static final String TIME_PERIOD_SEPARATOR = "-";
	
	protected String name;
	protected int amCount;
	protected int pmCount;
	protected int ntCount;
	protected String amtime;
	protected String pmtime;
	protected String nttime;
	
	protected SubjectCfg() {

	}

	protected SubjectCfg(String name, int amCount, int pmCount, int nightCount,
			String amtime, String pmtime, String nttime) {
		super();
		this.name = name;
		this.amCount = amCount;
		this.pmCount = pmCount;
		this.ntCount = nightCount;
		this.amtime = amtime;
		this.pmtime = pmtime;
		this.nttime = nttime;
	}

	protected int getAppointLimit() {
		return amCount + pmCount + ntCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAmCount(int amCount) {
		this.amCount = amCount;
	}

	public void setPmCount(int pmCount) {
		this.pmCount = pmCount;
	}

	public void setNtCount(int ntCount) {
		this.ntCount = ntCount;
	}

	public void setAmtime(String amtime) {
		this.amtime = amtime;
	}

	public void setPmtime(String pmtime) {
		this.pmtime = pmtime;
	}

	public void setNttime(String nttime) {
		this.nttime = nttime;
	}
	

	/**
	 * 解析服务器返回的时间字符串。
	 * @return 解析成功则返回使用  {@value #TIME_PERIOD_SEPARATOR}分隔的串，否则返回原串。
	 */
	protected static final String parseTimeString(String server){
		//使用正则表达式解析服务器字符串，确保不管服务器返回的串中分割符是什么都可以转化
		if (!server.matches("\\d{1,2}:\\d{1,2}[^0-9]\\d{1,2}:\\d{1,2}")){
			Log.w(LOG_TAG, "Server string doesnot match basic pattern!");
			return server;
		}
		Pattern pattern = Pattern.compile("\\d{1,2}:\\d{1,2}");
		Matcher matcher = pattern.matcher(server);
		StringBuilder sb = new StringBuilder();
		if (matcher.find()){
			sb.append(server.substring(matcher.start(), matcher.end()));
			if (matcher.find()){
				sb.append(TIME_PERIOD_SEPARATOR);
				sb.append(server.substring(matcher.start(), matcher.end()));
				Log.d(LOG_TAG, "parse string success.");
				return sb.toString();
			}
		}
		Log.d(LOG_TAG, "parse fail:"+sb.toString());
		return server;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubjectCfg [name=").append(name).append(", amCount=")
				.append(amCount).append(", pmCount=").append(pmCount)
				.append(", ntCount=").append(ntCount).append(", amtime=")
				.append(amtime).append(", pmtime=").append(pmtime)
				.append(", nttime=").append(nttime).append("]");
		return builder.toString();
	}

}