package com.huishen.ecoach.ui.appointment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 日历ViewPager的管理适配器
 * 
 * @author zhanghuan
 * 
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {
	private String beginDate, endDate; // 选区开始时间和结束时间保持
	private boolean isSection = false;
	private CalendarInterface listener;

	public CalendarPagerAdapter(FragmentManager fm, CalendarInterface listener) {
		super(fm);
		this.listener = listener;
	}

	/**
	 * 设置选区
	 */
	public void setSection(String beginDate, String endDate) {
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.isSection = true;
	}

	@Override
	public Fragment getItem(int position) {
		if (this.isSection) {
			return new CalendarPagerFragment(position, this.beginDate,
					this.endDate, this.listener);
		}
		return new CalendarPagerFragment(position, this.listener);
	}

	@Override
	public int getCount() {
		return CalendarUtil.getAllMonth();
	}

}
