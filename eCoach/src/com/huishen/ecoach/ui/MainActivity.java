package com.huishen.ecoach.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.huishen.ecoach.R;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {

	private TextView tvDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		retrieveWidgets();
	}
	
	/**
	 * Call {@link #setContentView(int)} before this method!
	 */
	private void retrieveWidgets(){
		tvDate = (TextView) findViewById(R.id.main_tv_date);
		//Generate date string
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int dom = calendar.get(Calendar.DAY_OF_MONTH);
		String tail = new SimpleDateFormat("d日 M月 yyyy", Locale.CHINA).format(calendar.getTime());
		SpannableStringBuilder builder = new SpannableStringBuilder(tail);
		builder.setSpan(new AbsoluteSizeSpan(36, true), 0, 1,
				(dom >= 10) ? Spannable.SPAN_INCLUSIVE_EXCLUSIVE
						: Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tvDate.setText(builder);
	}
	
}
