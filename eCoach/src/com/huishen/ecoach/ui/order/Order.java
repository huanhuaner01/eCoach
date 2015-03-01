package com.huishen.ecoach.ui.order;

import java.io.Serializable;

/**
 * 订单信息。
 * @author Muyangmin
 * @create 2015-2-20
 */
public final class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private long time;
	private String studentPosition;
	private String coachPosition;
	private boolean cancelled;
	private boolean evaluated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getStudentPosition() {
		return studentPosition;
	}

	public void setStudentPosition(String studentPosition) {
		this.studentPosition = studentPosition;
	}

	public String getStudyPosition() {
		return coachPosition;
	}

	public void setCoachPosition(String coachPosition) {
		this.coachPosition = coachPosition;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Order [id=").append(id).append(", time=").append(time)
				.append(", studentPosition=").append(studentPosition)
				.append(", targetPosition=").append(coachPosition)
				.append(", cancelled=").append(cancelled)
				.append(", evaluated=").append(evaluated).append("]");
		return builder.toString();
	}
}
