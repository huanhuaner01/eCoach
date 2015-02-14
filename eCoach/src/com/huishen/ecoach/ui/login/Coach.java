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

	private String phoneNumber; // 手机号
	private String name; // 教练姓名
	private String school; // 所属驾校
	private String carno; // 车牌号
	private String certno; // 证件号

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Coach [phoneNumber=").append(phoneNumber)
				.append(", name=").append(name).append(", school=")
				.append(school).append(", carno=").append(carno)
				.append(", certno=").append(certno).append("]");
		return builder.toString();
	}

}
