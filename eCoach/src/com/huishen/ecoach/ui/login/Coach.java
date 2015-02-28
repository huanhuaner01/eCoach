package com.huishen.ecoach.ui.login;

import java.io.Serializable;

/**
 * 用于表示教练登录信息的实体类。该类的信息代表教练存储在服务器端的信息。
 * <p>
 * 应该始终使用该类的信息而不是试图读取本地存储的注册信息：为了安全起见，这些信息在注册成功后将被自动删除。
 * </p>
 * 
 * @author Muyangmin
 * @create 2015-2-14
 */
public final class Coach implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 教练状态：审核中。
	 */
	public static final int STATUS_AUDITING = 0;
	/**
	 * 教练状态：审核通过。
	 */
	public static final int STATUS_AUDIT_PASS = 1;
	/**
	 * 教练状态：审核未通过。
	 */
	public static final int STATUS_AUDIT_FAIL = 2;

	private long id;			//在服务器上的ID
	private String phoneNumber; //手机号
	private String name; 		//教练姓名
	private String school; 		//所属驾校
	private String carno; 		//车牌号
	private String certno; 		//证件号
	private String avatarId;	//头像地址
	private int auditStatus;	//审核状态
	private int range;			//排行榜
	private int orderCount;		//订单总数
	private float goodrate;		//好评率
	private float starLevel;	//评星等级

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	public String getCertno() {
		return certno;
	}

	public void setCertno(String certno) {
		this.certno = certno;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		if (auditStatus < 0 || auditStatus > 2) {
			throw new IllegalArgumentException("status must in range of 0~2");
		}
		this.auditStatus = auditStatus;
	}

	public String getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public float getGoodRate() {
		return goodrate;
	}

	public void setGoodRate(float goodrate) {
		this.goodrate = goodrate;
	}

	public float getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(float starLevel) {
		this.starLevel = starLevel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Coach [id=").append(id).append(", phoneNumber=")
				.append(phoneNumber).append(", name=").append(name)
				.append(", school=").append(school).append(", carno=")
				.append(carno).append(", certno=").append(certno)
				.append(", avatarId=").append(avatarId)
				.append(", auditStatus=").append(auditStatus)
				.append(", range=").append(range).append(", orderCount=")
				.append(orderCount).append(", goodrate=")
				.append(goodrate).append(", starLevel=")
				.append(starLevel).append("]");
		return builder.toString();
	}

}
