package com.huishen.ecoach.ui.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.huishen.ecoach.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 装有日历控件的fragment
 * 
 * @author zhanghuan
 * 
 */
public class CalendarPagerFragment extends Fragment {

	public static final String INDEX = "index", DATE_BEGIN = "begin",
			DATE_END = "end";
	private String TAG = "CalendarPagerFragment";
	private int mMonthIndex;
	private CalendarGridViewAdapter adapter;
	private Date beginDate, endDate; // 选区开始时间和结束时间
	private boolean isSection = false;
	private CalendarInterface listener;

	public CalendarPagerFragment(int monthIndex, CalendarInterface listener) {
		super();
		mMonthIndex = monthIndex;
		this.listener = listener;
	}

	public CalendarPagerFragment(int monthIndex, String beginDate,
			String endDate, CalendarInterface listener) {
		super();
		mMonthIndex = monthIndex;
		this.listener = listener;
		if (beginDate == null && endDate == null) {
			return;
		}
		setSection(beginDate, endDate);
	}

	/**
	 * 
	 * 设置选区
	 */
	public void setSection(String beginDate, String endDate) {

		this.beginDate = CalendarUtil.getDate(beginDate);
		this.endDate = CalendarUtil.getDate(endDate);
		if (this.beginDate == null) {
			this.beginDate = new GregorianCalendar(CalendarUtil.MIN_YEAR, 0, 1)
					.getTime();
		}
		if (this.endDate == null) {
			this.endDate = new GregorianCalendar(CalendarUtil.MAX_YEAR, 11, 31)
					.getTime();
		}
		Log.i(TAG, "endDate is " + this.endDate);
		this.isSection = true;
	}

	/**
	 * 判断某天是否在选区内
	 */
	private boolean dayisSection(Date date) {
		Log.i(TAG, "begindate is :" + beginDate.toString());
		int i = date.compareTo(beginDate);
		int j = date.compareTo(endDate);
		if (i >= 0 && j <= 0) {
			return true;
		}
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.frag_calendar, container, false);
		GridView grid = (GridView) view.findViewById(R.id.gridview);
		adapter = new CalendarGridViewAdapter(container.getContext(),
				getDays(), R.layout.layout_calendar_day, new String[] { "day" },
				new int[] { R.id.day });
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) view
						.getTag();
				Log.i(TAG, map.toString());
				if (Integer.parseInt(map.get("status").toString()) < 0) {
					return;
				}
				adapter.selectOption(position);
				Date date = CalendarUtil.getDate(mMonthIndex);
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/",
						Locale.US);
				String d = df.format(date);
				listener.resultdata(d + map.get("day"));
			}

		});
		return view;

	}

	private List<HashMap<String, Object>> getDays() {
		int daynum = CalendarUtil.getDayNum(mMonthIndex);
		int dayweek = CalendarUtil.getDayWeek(mMonthIndex);
		Date today = CalendarUtil.getDate(CalendarUtil.getCurrentMouth(),
				CalendarUtil.getCurrentDay());
		List<HashMap<String, Object>> days = new ArrayList<HashMap<String, Object>>();
		for (int i = 1; i < dayweek; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("day", "");
			map.put("status", -1);
			days.add(map);
		}
		for (int i = 1; i <= daynum; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("day", i);
			if (this.isSection) {
				map.put("status", -1);
				Date newDay = CalendarUtil.getDate(this.mMonthIndex, i);
				if (dayisSection(newDay)) {
					map.put("status", 0);
					if (this.mMonthIndex == CalendarUtil.getCurrentMouth()
							&& dayisSection(today)) {
						if (i == CalendarUtil.getCurrentDay()) {
							map.put("status", 2);
						}
					} else {
						if (this.mMonthIndex == CalendarUtil
								.getMouthsForDate(this.beginDate)) {
							if (i == CalendarUtil.getDayForDate(this.beginDate)) {
								map.put("status", 1);
							}
						} else {
							if (i == 1) {
								map.put("status", 1);
							}
						}
					}
				} else {
					if (this.mMonthIndex == CalendarUtil.getCurrentMouth()
							&& i == CalendarUtil.getCurrentDay()) {
						map.put("status", -2);
					}
				}
			} else {
				map.put("status", 0);
				if (CalendarUtil.getCurrentDay() > daynum) {
					if (i == 1)
						map.put("status", 1);

				} else {

					if (this.mMonthIndex == CalendarUtil.getCurrentMouth()
							&& i == CalendarUtil.getCurrentDay()) {
						map.put("status", 2);
					} else if (i == CalendarUtil.getCurrentDay()) {
						map.put("status", 1);
					}
				}
			}
			Log.i(TAG, map.toString());
			days.add(map);
		}
		return days;
	}
}
