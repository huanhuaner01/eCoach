package com.huishen.ecoach.umeng;

import java.util.Map;

import com.huishen.ecoach.umeng.UmengPushConst.BindCoach;

/**
 * @author Muyangmin
 * @create 2015-3-5
 */
public final class BindCoachPushData extends PushData {
	
	private static final long serialVersionUID = 1L;
	
	public final String avatar;				//学生头像
	public final String name;				//学员姓名
	public final String phoneNumber;		//学员手机号
	public final String addtionalInfo;	//附加信息
	public final long id;					//学员ID
	public final String enrollId;			//学员报名信息ID
	
	public BindCoachPushData(Map<String, String> map) {
		super(map);
		avatar = map.get(BindCoach.PARAM_AVATAR);
		name = map.get(BindCoach.PARAM_NAME);
		phoneNumber = map.get(BindCoach.PARAM_PHONE);
		addtionalInfo = map.get(BindCoach.PARAM_ADDI_INFO);
		id = getLong(map, BindCoach.PARAM_ID);
		enrollId = map.get(BindCoach.PARAM_ENROLLID);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BindCoachPushData [avatar=").append(avatar)
				.append(", name=").append(name).append(", phoneNumber=")
				.append(phoneNumber).append(", addtionalInfo=")
				.append(addtionalInfo).append(", id=").append(id)
				.append(", enrollId=").append(enrollId).append(", msgType=")
				.append(msgType).append("]");
		return builder.toString();
	}
	
}
