package com.huishen.ecoach.ui.order;

import java.io.Serializable;

/**
 * 订单评价
 * 
 * @author Muyangmin
 * @create 2015-2-20
 */
public final class Evaluation implements Serializable {

	private static final long serialVersionUID = 1L;

	private float totalStar;
	private String content;
	private String from;
	private long time;
	private float attitudeStar;
	private float envStar;
	private float trafficStar;

	public float getTotalStar() {
		return totalStar;
	}

	public void setTotalStar(float totalStar) {
		this.totalStar = totalStar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getAttitudeStar() {
		return attitudeStar;
	}

	public void setAttitudeStar(float attitudeStar) {
		this.attitudeStar = attitudeStar;
	}

	public float getEnvStar() {
		return envStar;
	}

	public void setEnvStar(float envStar) {
		this.envStar = envStar;
	}

	public float getTrafficStar() {
		return trafficStar;
	}

	public void setTrafficStar(float trafficStar) {
		this.trafficStar = trafficStar;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Evaluation [totalStar=").append(totalStar)
				.append(", content=").append(content).append(", from=")
				.append(from).append(", time=").append(time)
				.append(", attitudeStar=").append(attitudeStar)
				.append(", envStar=").append(envStar).append(", trafficStar=")
				.append(trafficStar).append("]");
		return builder.toString();
	}
}
