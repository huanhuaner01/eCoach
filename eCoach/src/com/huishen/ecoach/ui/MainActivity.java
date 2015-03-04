package com.huishen.ecoach.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.huishen.ecoach.MainApp;
import com.huishen.ecoach.R;
import com.huishen.ecoach.map.BaiduMapProxy;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.appointment.CalendarActivity;
import com.huishen.ecoach.ui.login.Coach;
import com.huishen.ecoach.ui.msg.MessageActivity;
import com.huishen.ecoach.ui.pcenter.SettingActivity;
import com.huishen.ecoach.ui.pcenter.UserGuideActivity;
import com.huishen.ecoach.umeng.UmengServiceProxy;
import com.huishen.ecoach.util.Uis;
import com.huishen.ecoach.widget.RoundImageView;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
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
	private TextView tvName, tvOrdernum, tvRange, tvGoodrate, tvStarLevel;
	private TextView tvUserGuide, tvRecommend, tvSetting;
	private RoundImageView rimgAvatar;
	private RatingBar rtbStar;
	private ToggleButton tgbOrderAutoAlert;
	
	private static final int REQUEST_CODE_SETTING = 1001;
	public static final int RESULT_CODE_LOGOUT = 1002;
	
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
		refreshSnapupStatus();
		displayLoginInfo();
		UmengServiceProxy.startPushService(this);
		BaiduMapProxy.getInstance().startLocate(this);
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
		rimgAvatar = (RoundImageView)findViewById(R.id.pcenter_rimg_avatar);
		tvRecommend = (TextView)findViewById(R.id.pcenter_tv_recommend);
		tvSetting = (TextView)findViewById(R.id.pcenter_tv_setting);
		tvName = (TextView)findViewById(R.id.pcenter_tv_coachname);
		tvUserGuide = (TextView)findViewById(R.id.pcenter_tv_userguide);
		tgbOrderAutoAlert = (ToggleButton)findViewById(R.id.pcenter_tgb_order_autoalert);
		tvStarLevel = (TextView)findViewById(R.id.pcenter_tv_starnum);
		tvOrdernum = (TextView)findViewById(R.id.pcenter_tv_ordernum);
		tvRange = (TextView)findViewById(R.id.pcenter_tv_range);
		tvGoodrate = (TextView)findViewById(R.id.pcenter_tv_goodrates);
		rtbStar = (RatingBar)findViewById(R.id.pcenter_rating);
		rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.main_btn_rotate);
	}
	
	private void addListeners(){
		btnMe.setOnClickListener(this);
		btnMsg.setOnClickListener(this);
		tvBookManage.setOnClickListener(this);
		tvRecruitManage.setOnClickListener(this);
		tvSnapup.setOnClickListener(this);
		tvRecommend.setOnClickListener(this);
		tvSetting.setOnClickListener(this);
		tvUserGuide.setOnClickListener(this);
		tgbOrderAutoAlert.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// Feature not implement yet
			}
		});
	}
	
	//进入页面时查询并刷新抢单状态
	private final void refreshSnapupStatus(){
		//如果教练未通过审核，则无需查询
		final Coach coach = MainApp.getInstance().getLoginCoach();
		if (coach.getAuditStatus() != Coach.STATUS_AUDIT_PASS){
			Log.d(LOG_TAG, "This coach hasn't pass audit yet. Skiping query status.");
			return ;
		}
		//否则执行查询并更新
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_COACH_ID, String.valueOf(coach.getId()));
		NetUtil.requestStringData(SRL.Method.METHOD_QUERY_SNAPUP_STATUS, params, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				try {
					JSONObject json = new JSONObject(arg0);
					int code = json.getInt(SRL.ReturnField.FIELD_SNAPUP_STATUS);
					if (code==SRL.ReturnCode.INFO_STATUS_OPEN){
						isSnaping = true;	//设置标记位，否则将导致打开模式后下次开启应用第一次无法正确关闭模式的问题。
						tvSnapAnimBkg.startAnimation(rotateAnimation);
						tvSnapup.setText(R.string.str_main_menu_listening_snapup);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void displayLoginInfo() {
		//展示登录信息
		final Coach coach = MainApp.getInstance().getLoginCoach();
		Log.d(LOG_TAG, coach.toString());
		tvName.setText(buildCoachNameString(coach));
		tvStarLevel.setText(buildCoachStarString(coach.getStarLevel()));
		tvOrdernum.setText(String.valueOf(coach.getOrderCount()));
		tvRange.setText(String.valueOf(coach.getRange()));
		tvGoodrate.setText(String.valueOf(coach.getGoodRate())+"%");
		tvDate.setText(buildDateString());
		rtbStar.setRating(coach.getStarLevel());
		String avatar = coach.getAvatarId();
		//如果没有头像，则将导致直接访问网站首页浪费大量流量，故优化之
		if (avatar==null || avatar.equals("") || avatar.equals("null")){
			Log.d(LOG_TAG, "avatar is null, skipping load image.");
		}
		NetUtil.requestLoadImage(rimgAvatar, coach.getAvatarId(), R.drawable.default_personal_avatar);
	}
	
	private final CharSequence buildCoachNameString(Coach coach){
		SpannableStringBuilder ssb = new SpannableStringBuilder(coach.getName());
		int spanColor;
		int spanTxt;
		switch (coach.getAuditStatus()) {
		case Coach.STATUS_AUDITING:
			spanColor = R.color.color_pcenter_coach_auditing;
			spanTxt = R.string.str_pcenter_status_auditing;
			break;
		case Coach.STATUS_AUDIT_PASS:	//不展示
			return ssb;
		case Coach.STATUS_AUDIT_FAIL:
			spanColor = R.color.color_pcenter_coach_auditfail;
			spanTxt = R.string.str_pcenter_status_audit_fail;
			break;
		default:
			throw new IllegalStateException("this object has been modified manually.");
		}
		final int start = ssb.length();
		ForegroundColorSpan span = new ForegroundColorSpan(getResources()
				.getColor(spanColor));
		ssb.append(getResources().getText(spanTxt));
		ssb.setSpan(span, start, ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return ssb;
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
	
	/**
	 * 检查教练的审核状态，如果返回值为true，则可以执行进一步的操作，否则提示用户不能操作。
	 */
	private final boolean checkAuditStatus() {
		if (MainApp.getInstance().getLoginCoach().getAuditStatus()!=Coach.STATUS_AUDIT_PASS){
			Uis.toastShort(MainActivity.this, R.string.str_main_err_audit_notpassyet);
			return false;
		}
		return true;
	}
	
	/**
	 * 执行切换抢单动作。
	 * @param open 是否开启抢单模式
	 */
	private final void performChangeSnapupStatus(final boolean open){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_BEGIN_SNAPUP, String.valueOf(open? 1: 0));
		params.put(SRL.Param.PARAM_COACH_ID, String.valueOf(MainApp.getInstance().getLoginCoach().getId()));
		NetUtil.requestStringData(SRL.Method.METHOD_SET_SNAPUP_STATUS, params, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				isSnaping = open;
				if (isSnaping){
					tvSnapAnimBkg.startAnimation(rotateAnimation);
					tvSnapup.setText(R.string.str_main_menu_listening_snapup);
				}
				else{
					tvSnapAnimBkg.clearAnimation();
					tvSnapup.setText(R.string.str_main_menu_begin_snapup);
				}
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				Uis.toastShort(MainActivity.this, R.string.str_main_err_fail_snapup);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK){
			if (slidePaneLayout.isOpen()){
				slidePaneLayout.closePane();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
			if (checkAuditStatus()){
				startActivity(CalendarActivity.getIntent(this));
			}
			break;
		case R.id.main_tv_snapup:
			if (checkAuditStatus()){
				performChangeSnapupStatus(!isSnaping);
			}
			break;
		case R.id.main_tv_recruit_manage:
			if (checkAuditStatus()){
				Uis.toastShort(this, R.string.str_feature_unsupported);
			}
			break;
		case R.id.pcenter_tv_userguide:
			startActivity(UserGuideActivity.getIntent(this));
			break;
		case R.id.pcenter_tv_setting:
			startActivityForResult(SettingActivity.getIntent(this), REQUEST_CODE_SETTING);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SETTING){
			if (resultCode== RESULT_CODE_LOGOUT){
				finish();
			}
		}
	}
}