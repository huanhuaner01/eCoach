package com.huishen.ecoach.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.huishen.ecoach.R;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener{

	private SlidingPaneLayout slidePaneLayout;
	private TextView tvDate;
	private ImageButton btnMe;
	
	//--- Slide Pane Widgets
	private TextView tvUserGuide, tvRecommend, tvSetting;
	private ToggleButton tgbMsgPush;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		retrieveWidgets();
		addListeners();
		displayDate();
	}
	
	/**
	 * Call {@link #setContentView(int)} before this method!
	 */
	private void retrieveWidgets(){
		slidePaneLayout = (SlidingPaneLayout)findViewById(R.id.main_slidepane);
		tvDate = (TextView) findViewById(R.id.main_tv_date);
		btnMe = (ImageButton)findViewById(R.id.main_btn_me);
		tvRecommend = (TextView)findViewById(R.id.pcenter_tv_recommend);
		tvSetting = (TextView)findViewById(R.id.pcenter_tv_setting);
		tvUserGuide = (TextView)findViewById(R.id.pcenter_tv_userguide);
		tgbMsgPush = (ToggleButton)findViewById(R.id.pcenter_tgb_msgpush);
	}
	
	private void addListeners(){
		btnMe.setOnClickListener(this);
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
	
	private void displayDate(){
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int dom = calendar.get(Calendar.DAY_OF_MONTH);
		String tail = new SimpleDateFormat("d日 M月 yyyy", Locale.CHINA).format(calendar.getTime());
		SpannableStringBuilder builder = new SpannableStringBuilder(tail);
		builder.setSpan(new AbsoluteSizeSpan(36, true), 0, 1,
				(dom >= 10) ? Spannable.SPAN_INCLUSIVE_EXCLUSIVE
						: Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tvDate.setText(builder);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_me:
			if (!slidePaneLayout.isOpen()){
				slidePaneLayout.openPane();
			}
			else {
				slidePaneLayout.closePane();
			}
			break;

		default:
			break;
		}
	}
}
