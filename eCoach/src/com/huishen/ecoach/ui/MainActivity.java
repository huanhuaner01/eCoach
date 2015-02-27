package com.huishen.ecoach.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.huishen.ecoach.MainApp;
import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.appointment.CalendarActivity;
import com.huishen.ecoach.ui.login.Coach;
import com.huishen.ecoach.ui.msg.MessageActivity;
import com.huishen.ecoach.ui.pcenter.SettingActivity;
import com.huishen.ecoach.ui.pcenter.UserGuideActivity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener{
	
	private static final String LOG_TAG = "MainActivity";

	private SlidingPaneLayout slidePaneLayout;
	private TextView tvDate;
	private ImageButton btnMe, btnMsg;
	private TextView tvBookManage, tvSnapup, tvSnapAnimBkg, tvRecruitManage;
	private boolean isSnaping = false;
	private Animation rotateAnimation = null;
	
	//--- Slide Pane Widgets
	private TextView tvOrdernum, tvRange, tvRecomIndex, tvStarLevel;
	private TextView tvUserGuide, tvRecommend, tvSetting;
	private RatingBar rtbStar;
	private ToggleButton tgbMsgPush;
	
	public static final Intent getIntent(Context context){
		Intent intent = new Intent(context, MainActivity.class);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		retrieveWidgets();
		addListeners();
		displayLoginInfo();
	}
	
	/**
	 * Call {@link #setContentView(int)} before this method!
	 */
	private void retrieveWidgets(){
		slidePaneLayout = (SlidingPaneLayout)findViewById(R.id.main_slidepane);
		tvDate = (TextView) findViewById(R.id.main_tv_date);
		btnMe = (ImageButton)findViewById(R.id.main_btn_me);
		btnMsg = (ImageButton)findViewById(R.id.main_btn_msg);
		tvBookManage = (TextView)findViewById(R.id.main_tv_book_manage);
		tvSnapup = (TextView)findViewById(R.id.main_tv_snapup);
		tvSnapAnimBkg = (TextView)findViewById(R.id.main_tv_snapup_animbkg);
		tvRecruitManage = (TextView)findViewById(R.id.main_tv_recruit_manage);
		tvRecommend = (TextView)findViewById(R.id.pcenter_tv_recommend);
		tvSetting = (TextView)findViewById(R.id.pcenter_tv_setting);
		tvUserGuide = (TextView)findViewById(R.id.pcenter_tv_userguide);
		tgbMsgPush = (ToggleButton)findViewById(R.id.pcenter_tgb_msgpush);
		tvStarLevel = (TextView)findViewById(R.id.pcenter_tv_starnum);
		tvOrdernum = (TextView)findViewById(R.id.pcenter_tv_ordernum);
		tvRange = (TextView)findViewById(R.id.pcenter_tv_range);
		tvRecomIndex = (TextView)findViewById(R.id.pcenter_tv_recomindex);
		rtbStar = (RatingBar)findViewById(R.id.pcenter_rating);
	}
	
	private void addListeners(){
		btnMe.setOnClickListener(this);
		btnMsg.setOnClickListener(this);
		tvBookManage.setOnClickListener(this);
		tvSnapup.setOnClickListener(this);
		tvRecommend.setOnClickListener(this);
		tvSetting.setOnClickListener(this);
		tvUserGuide.setOnClickListener(this);
		tgbMsgPush.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void displayLoginInfo() {
		//展示登录信息
		final Coach coach = MainApp.getInstance().getLoginCoach();
		Log.d(LOG_TAG, coach.toString());
		tvStarLevel.setText(buildCoachStarString(coach.getStarLevel()));
		tvOrdernum.setText(String.valueOf(coach.getOrderCount()));
		tvRange.setText(String.valueOf(coach.getRange()));
		tvRecomIndex.setText(String.valueOf(coach.getRecommendIndex()));
		tvDate.setText(buildDateString());
		rtbStar.setRating(coach.getStarLevel());
	}
	
	private final CharSequence buildCoachStarString(float rate){
		SpannableStringBuilder ssb = new SpannableStringBuilder(String.valueOf(rate));
		ForegroundColorSpan span = new ForegroundColorSpan(getResources()
				.getColor(R.color.color_pcenter_coach_star));
		ssb.setSpan(span, 0, ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		ssb.append(getResources().getString(R.string.str_pcenter_star_unit));
		return ssb;
	}
	
	//构建主页面的日期显示字符串
	private final CharSequence buildDateString(){
		SpannableStringBuilder builder = new SpannableStringBuilder();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int dom = calendar.get(Calendar.DAY_OF_MONTH);
		String day = String.valueOf(dom);
		Log.d(LOG_TAG, "dom:"+dom);
		builder.append(day)
				.setSpan(new RelativeSizeSpan(2), 0, day.length(),
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		String tail = new SimpleDateFormat("日 M月 yyyy", Locale.CHINA)
				.format(calendar.getTime());
		builder.append(tail);
		return builder;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_me://主页左上角me按钮
			if (!slidePaneLayout.isOpen()){
				slidePaneLayout.openPane();
			}
			else {
				slidePaneLayout.closePane();
			}
			break;
		case R.id.main_btn_msg:
			startActivity(MessageActivity.getIntent(this));
			break;
		case R.id.main_tv_book_manage:
			startActivity(CalendarActivity.getIntent(this));
			break;
		case R.id.main_tv_snapup:
			if (isSnaping){
				tvSnapAnimBkg.clearAnimation();
			}
			else {
				if (rotateAnimation==null){
					rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.main_btn_rotate);
				}
				tvSnapAnimBkg.startAnimation(rotateAnimation);
			}
			isSnaping = !isSnaping;
			break;
		case R.id.main_tv_recruit_manage:
			break;
		case R.id.pcenter_tv_userguide:
			startActivity(UserGuideActivity.getIntent(this));
			break;
		case R.id.pcenter_tv_setting:
			startActivity(SettingActivity.getIntent(this));
			break;
		default:
			break;
		}
	}
}