package com.huishen.ecoach.ui.appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

/**
 * 日历计算工具
 * 
 * @author zhanghuan
 * 
 */
public class CalendarUtil {
	/*
	 * 此日历记录的最大年份，也就是只能显示的最大的年份
	 */
	public static int MAX_YEAR = 2100;
	/*
	 * 此日历记录的最小年份，也是最小显示的年份
	 */
	public static int MIN_YEAR = 1970;

	/*
	 * 此日历使用的字符串日历格式，所有的转换都只能用这个格式（格式为：yyyy-MM-dd）
	 */
	public static String DATE_FORMAT = "yyyy-MM-dd";

	public CalendarUtil() {
	}

	/**
	 * 获得1970年到2100年的月份数
	 * 
	 * @return
	 */
	public static int getAllMonth() {
		int year = MAX_YEAR - MIN_YEAR + 1;

		return year * 12;
	}

	/**
	 * 获得指定月份的第一天的时间
	 * 
	 * @param index
	 *            指定月份（最最小年份到最大年份中间的索引月份）
	 * @return
	 */
	public static Date getDate(int index) {
		int year = MIN_YEAR + index / 12;
		int month = index % 12;
		Calendar date = new GregorianCalendar(year, month, 1);
		Date d = date.getTime();
		return d;
	}

	/**
	 * 将指定格式的字符串转换成Date格式的日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDate(String dateStr) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		Date date = null;
		try {
			date = sf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将指定月和日的日期转换成Date日期格式
	 * 
	 * @param mouths
	 *            指定月份（最最小年份到最大年份中间的索引月份）
	 * @param day
	 *            这个月的第day天
	 * @return
	 */
	public static Date getDate(int mouths, int day) {
		int year = MIN_YEAR + mouths / 12;
		int month = mouths % 12;
		Calendar date = new GregorianCalendar(year, month, day);
		Date d = date.getTime();
		return d;
	}

	/**
	 * 获得指定月份的天数
	 * 
	 * @param index
	 *            fragment的显示的月份
	 * @return
	 */
	public static int getDayNum(int index) {
		int year = MIN_YEAR + index / 12;
		int month = index % 12;
		Calendar date = new GregorianCalendar(year, month, 1);
		int daynum = date.getActualMaximum(Calendar.DAY_OF_MONTH);
		return daynum;
	}

	/**
	 * 获得指定月的第一天是所在的星期几
	 * 
	 * @param index
	 * @return
	 */
	public static int getDayWeek(int index){
		int year = MIN_YEAR+index/12;
		int month = index%12;
		Calendar date = new GregorianCalendar(year, month,1);
		int dayweek = date.get(Calendar.DAY_OF_WEEK);
		return dayweek ;
	}

	/**
	 * 获得目前日期所在的月份
	 * 
	 * @return
	 */
	public static int getCurrentMouth() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int mouth = cal.get(Calendar.MONTH);
		int currentIndex = (year - 1970) * 12 + mouth;
		return currentIndex;
	}

	/**
	 * 获取目前日期所在月份的第几天
	 * 
	 * @return
	 */
	public static int getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		return day;
	}

	/**
	 * 获得日期Date所在的月份是从最小年到现在的第几月
	 * 
	 * @param date
	 * @return
	 */
	public static int getMouthsForDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int mouths = (year - 1970) * 12 + month;
		return mouths;
	}

	/**
	 * 获得日期dateStr所在的月份是从最小年到现在的第几月
	 * 
	 * @param String
	 *            string类型的日期格式 例如：2015-01-21
	 * @return
	 */
	public static int getMouthsForDate(String dateStr) {
		if (dateStr.equals("")) {
			Log.i("CalendarUtil", "date is null");
			return -1;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date date;
		int mouths = -1;
		try {
			date = format.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			mouths = (year - 1970) * 12 + month;
			Log.i("CalendarUtil", date.toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.i("CalendarUtil", mouths + "");
		return mouths;
	}

	/**
	 * 获得指定格式字符串日期所在月份的第几天
	 * 
	 **/
	public static int getDayForDate(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date date;
		int day = 0;
		try {
			date = format.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			day = cal.get(Calendar.DAY_OF_MONTH);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 获得Date格式的日期所在月份的第几天
	 * 
	 * @param Date
	 * @return
	 */
	public static int getDayForDate(Date date) {
		int day = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}

}
