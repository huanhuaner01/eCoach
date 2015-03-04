package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.MainApp;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.ui.login.Coach;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.widget.RoundImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewProfileActivity extends RightSideParentActivity {
	
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, ViewProfileActivity.class);
		return intent;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);
		initWidgets();
	}
	
	private final void initWidgets() {
		final Coach coach = MainApp.getInstance().getLoginCoach();
		if (coach==null){
			throw new IllegalStateException("The coach information should alreay be setted!");
		}
		TextView tvName, tvSchool, tvCarno, tvPhone, tvCertno;
		RoundImageView rimgAvatar;
		tvName = (TextView) findViewById(R.id.viewprofile_tv_name);
		tvSchool = (TextView) findViewById(R.id.viewprofile_tv_school);
		tvCarno = (TextView) findViewById(R.id.viewprofile_tv_carno);
		tvPhone = (TextView) findViewById(R.id.viewprofile_tv_phone);
		tvCertno = (TextView) findViewById(R.id.viewprofile_tv_certno);
		rimgAvatar = (RoundImageView)findViewById(R.id.viewprofile_rimg_avatar);

		tvName.setText(coach.getName());
		tvSchool.setText(coach.getSchool());
		tvCarno.setText(coach.getCarno());
		tvPhone.setText(coach.getPhoneNumber());
		tvCertno.setText(coach.getCertno());
		NetUtil.requestLoadImage(rimgAvatar, coach.getAvatarId(), R.drawable.default_personal_avatar);
	}
}
