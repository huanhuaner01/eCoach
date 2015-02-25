package com.huishen.ecoach.ui.appointment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentFragmentActivity;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 装载日历控件的activity
 * 
 * @author zhanghuan
 * 
 */
public class CalendarActivity extends RightSideParentFragmentActivity implements
		CalendarInterface {

	private static final String LOG_TAG = "MainActivity";

	private static final String EXTRA_BEGIN_DATE = "beginDate";
	private static final String EXTRA_END_DATE = "endDate";
	private static final String EXTRA_SECTION_STATUS = "isSection";

	private TextView calendartitle;
	private ImageButton pre, next;
	private ViewPager mPager;
	private String beginDate, endDate; // 选区开始时间和结束时间
	private boolean isSection = false; // 是否使用选区

	/**
	 * 获得一个没有选区的Intent。
	 */
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, CalendarActivity.class);
		return intent;
	}

	/**
	 * 获得一个有选区的Intent。
	 * 
	 * @param context
	 *            上下文信息
	 * @param beginDate
	 *            起始日期，必须满足yyyy-MM-dd格式。
	 * @param endDate
	 *            终止日期，必须满足yyyy-MM-dd格式。
	 * @return 返回封装的Intent。
	 */
	public static final Intent getIntent(Context context, String beginDate,
			String endDate) {
		if (!(beginDate.matches("yyyy-MM-dd") && endDate.matches("yyyy-MM-dd"))) {
			throw new IllegalArgumentException(
					"Date params must match yyyy-MM-dd pattern!");
		}
		Intent intent = new Intent(context, CalendarActivity.class);
		intent.putExtra(EXTRA_SECTION_STATUS, true);
		intent.putExtra(EXTRA_BEGIN_DATE, beginDate);
		intent.putExtra(EXTRA_END_DATE, endDate);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		isSection = getIntent().getBooleanExtra(EXTRA_SECTION_STATUS, false);
		if (isSection) {
			beginDate = getIntent().getStringExtra(EXTRA_BEGIN_DATE);
			endDate = getIntent().getStringExtra(EXTRA_END_DATE);
			Log.d(LOG_TAG, "beginDate:" + beginDate + ",endDate:" + endDate);
		}
		registView();
		init();
	}

	/**
	 * 注册组件
	 */
	private void registView() {
		mPager = (ViewPager) this.findViewById(R.id.pager);
		calendartitle = (TextView) this.findViewById(R.id.calendar_month);
		pre = (ImageButton) this.findViewById(R.id.calendar_month_pre);
		next = (ImageButton) this.findViewById(R.id.calendar_month_next);
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		CalendarPagerAdapter mPagerAdapter = new CalendarPagerAdapter(
				getSupportFragmentManager(), this);

		/************** 选区功能 *********************/
		if (this.isSection) {
			mPagerAdapter.setSection(beginDate, endDate);
		}
		/************** 选区功能结束 ******************/

		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int index) {
				Date date = CalendarUtil.getDate(index);
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM", Locale.US);
				String d = df.format(date);
				calendartitle.setText(d);
			}

		});
		setToDay();
		// //////////////设置前一页和后一页的点击效果和事件
		this.pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mPager.getCurrentItem() > 0) {
					mPager.setCurrentItem(mPager.getCurrentItem() - 1);
				} else {
					Toast.makeText(CalendarActivity.this, "对不起，已经是第一页了",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		this.next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mPager.getCurrentItem() != mPager.getChildCount() - 1) {
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				} else {
					Toast.makeText(CalendarActivity.this, "对不起，已经是最后一页了",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

	/**
	 * 设置选中今天
	 */
	private void setToDay() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int mouth = cal.get(Calendar.MONTH);
		// int day = cal.get(Calendar.DATE) ;
		int index = (year - 1970) * 12 + mouth;

		if (this.isSection) {
			int begin = CalendarUtil.getMouthsForDate(this.beginDate);
			int end = CalendarUtil.getMouthsForDate(endDate);
			if (index >= begin && index <= end) {
				this.mPager.setCurrentItem(index);
			} else {
				this.mPager.setCurrentItem(begin);
			}
		} else {
			this.mPager.setCurrentItem(index);
		}
	}

	/**
	 * 返回监听
	 */
	@Override
	public void resultdata(String data) {
		Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
	}
}
