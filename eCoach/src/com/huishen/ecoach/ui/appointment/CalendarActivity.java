package com.huishen.ecoach.ui.appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.ResponseParser;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.parent.RightSideParentFragmentActivity;
import com.huishen.ecoach.util.Uis;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

/**
 * 装载日历控件的activity
 * 
 * @author zhanghuan
 * 
 */
public class CalendarActivity extends RightSideParentFragmentActivity implements
		CalendarInterface {

	private static final String LOG_TAG = "CalendarActivity";

	private static final String EXTRA_BEGIN_DATE = "beginDate";
	private static final String EXTRA_END_DATE = "endDate";
	private static final String EXTRA_SECTION_STATUS = "isSection";

	private TextView calendartitle;
	private ImageButton pre, next;
	private ViewPager mPager;
	private ExpandableListView expListView;
	private String beginDate, endDate; // 选区开始时间和结束时间
	private boolean isSection = false; // 是否使用选区
	
	private Button btnSetting;
	private ArrayList<SubjectCfg> subjectCfgs;	//保存科目的时间、限约人数等设置。
	private Map<String, ArrayList<AppointSubject>> cachedDateAppointMap;	//保存每天的数据
	
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
		if (!(beginDate.matches("\\d{4}-\\d{2}-\\d{2}") && endDate.matches("\\d{4}-\\d{2}-\\d{2}"))) {
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
		cachedDateAppointMap = new HashMap<String, ArrayList<AppointSubject>>();
		registView();
		initCalendar();
		getAppointPeriodCfg();
	}
	
	private void getAppointPeriodCfg(){
		NetUtil.requestStringData(SRL.Method.METHOD_QUERY_APPOINTCFG, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				try {
					JSONObject json = new JSONObject(ResponseParser
									.getStringFromResult(arg0,SRL.ReturnField.FIELD_INFO));
					parseAppointConfig(json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//解析预约设置。
	private void parseAppointConfig(JSONObject json) {
		SubjectCfg km2 = subjectCfgs.get(0);
		km2.setName(getString(R.string.str_appointment_subject2));
		km2.setAmCount(json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2AMLIMIT));
		km2.setPmCount(json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2PMLIMIT));
		km2.setNtCount(json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2NTLIMIT));
		km2.setAmtime(SubjectCfg.parseTimeString(json.optString(
				SRL.Param.PARAM_APPOINTCFG_AMPERIOD,
				getString(R.string.str_booksetting_time_am))));
		km2.setPmtime(SubjectCfg.parseTimeString(json.optString(
				SRL.Param.PARAM_APPOINTCFG_PMPERIOD,
				getString(R.string.str_booksetting_time_pm))));
		km2.setNttime(SubjectCfg.parseTimeString(json.optString(
				SRL.Param.PARAM_APPOINTCFG_NTPERIOD,
				getString(R.string.str_booksetting_time_night))));

		SubjectCfg km3 = subjectCfgs.get(1);
		km3.setName(getString(R.string.str_appointment_subject3));
		km3.setAmCount(json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3AMLIMIT));
		km3.setPmCount(json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3PMLIMIT));
		km3.setNtCount(json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3NTLIMIT));
		km3.setAmtime(SubjectCfg.parseTimeString(json.optString(
				SRL.Param.PARAM_APPOINTCFG_AMPERIOD,
				getString(R.string.str_booksetting_time_am))));
		km3.setPmtime(SubjectCfg.parseTimeString(json.optString(
				SRL.Param.PARAM_APPOINTCFG_PMPERIOD,
				getString(R.string.str_booksetting_time_pm))));
		km3.setNttime(SubjectCfg.parseTimeString(json.optString(
				SRL.Param.PARAM_APPOINTCFG_NTPERIOD,
				getString(R.string.str_booksetting_time_night))));
		Log.d(LOG_TAG, subjectCfgs.toString());
	}

	/**
	 * 查询指定日期的预约学员列表并刷新列表数据。
	 * @param year 年
	 * @param month 月（自然月，1，2，3，。。。）
	 * @param day 日
	 */
	private final void refreshAppointTables(int year, int month, int day){
		HashMap<String, String> params = new HashMap<String, String>();
		final String date = String.format(Locale.US, "%1$d-%2$d-%3$d", year, month, day);
		if (cachedDateAppointMap.containsKey(date)){
			Log.d(LOG_TAG, "Specified day["+date+"] already in cached map. skipping net request...");
			((AppointmentListAdapter) expListView
					.getExpandableListAdapter())
					.refreshData(cachedDateAppointMap.get(date));
			return;
		}
		params.put(SRL.Param.PARAM_APPOINT_DATE, date);
		NetUtil.requestStringData(SRL.Method.METHOD_QUERY_APPOINT_STULIST, params, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				try {
					JSONArray array = new JSONArray(ResponseParser
							.getStringFromResult(arg0, SRL.ReturnField.FIELD_INFO));
					if(expListView.getExpandableListAdapter()==null){
						expListView.setAdapter(new AppointmentListAdapter(buildAppointTableData(date, array)));
					}
					else{
						((AppointmentListAdapter) expListView
								.getExpandableListAdapter())
								.refreshData(buildAppointTableData(date,
										array));
					}
				} catch (JSONException e) {
					Uis.toastShort(CalendarActivity.this, "Fuck beautiful");
				}
				
			}
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				
			}
		});
	}
	
	private final ArrayList<AppointSubject> buildAppointTableData(String date,
			JSONArray array) throws JSONException {
		String[] subjects = getResources().getStringArray(
				R.array.str_appointment_subjectlist);
		String[] periods = getResources().getStringArray(
				R.array.str_appointment_periodlist);
		ArrayList<AppointSubject> subjectList = new ArrayList<CalendarActivity.AppointSubject>(
				subjects.length);
		for (int i=0; i<subjects.length; i++){
			ArrayList<AppointPeriod> periodList = new ArrayList<CalendarActivity.AppointPeriod>();
			SubjectCfg cfg = subjectCfgs.get(i);
			Log.d(LOG_TAG, cfg.toString());
			String[] descs = new String[]{cfg.amtime, cfg.pmtime, cfg.nttime};
			for (int j=0; j< periods.length; j++){
				//添加空的列表，之后统一解析和加入列表。
				AppointPeriod period = new AppointPeriod(periods[j], descs[j],
						new ArrayList<CalendarActivity.AppointStudent>());
				periodList.add(period);
			}
			AppointSubject subject = new AppointSubject(subjects[i], cfg.getAppointLimit(), periodList);
			subjectList.add(subject);
		}
		//开始解析数据
		for (int index=0; index<array.length(); index++){
			JSONObject json = array.getJSONObject(index);
			String name = json.optString(SRL.ReturnField.FIELD_APPOINT_STUNAME);
			String phone = json.optString(SRL.ReturnField.FIELD_APPOINT_PHONE);
			int periodIndex = json.optInt(SRL.ReturnField.FIELD_APPOINT_PERIOD)-1;
			int subjectIndex = json.optInt(SRL.ReturnField.FIELD_APPOINT_SUBJECT)-1;
			subjectList.get(subjectIndex).getPeriodList().get(periodIndex).studentList
					.add(new AppointStudent(name, phone));
		}
		cachedDateAppointMap.put(date, subjectList);
		return subjectList;
//		return new AppointmentListAdapter(subjectList);
	}
	

	/**
	 * 注册组件
	 */
	private void registView() {
		mPager = (ViewPager) findViewById(R.id.pager);
		calendartitle = (TextView) findViewById(R.id.calendar_month);
		pre = (ImageButton) findViewById(R.id.calendar_month_pre);
		next = (ImageButton) findViewById(R.id.calendar_month_next);
		expListView = (ExpandableListView)findViewById(R.id.calendar_sub_list);
		btnSetting = (Button)findViewById(R.id.calendar_btn_setting);
		btnSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(AppointSettingActivity.getIntent(CalendarActivity.this));
			}
		});
		//test code begin
//		Log.d(LOG_TAG, "adding emulated data...");
//		ArrayList<AppointSubject> subjects = new ArrayList<CalendarActivity.AppointSubject>();
//		subjects.add(new AppointSubject("科目二", 10, 19));
//		subjects.add(new AppointSubject("科目三", 5, 12));
//		ArrayList<AppointTable> appointTables = new ArrayList<CalendarActivity.AppointTable>();
//		String[] ams = new String[]{"上午","下午","晚上"};
//		String[] descs = new String[]{"8:00~12:00","13:00~18:00","20:00~22:00"};
//		for (int i=0;i<3;i++){
//			AppointTable table  = new AppointTable();
//			table.setPeriodName(ams[i]);
//			table.setPerioidDescription(descs[i]);
//			ArrayList<AppointStudent> students = new ArrayList<CalendarActivity.AppointStudent>();
//			for (int j=0; j<5; j++){
//				students.add(new AppointStudent("韩梅梅", "15989897820"));
//			}
//			table.setStudentList(students);
//			appointTables.add(table);
//		}
//		Log.d(LOG_TAG, "adding emulated data...100%.");
//		expListView.setAdapter(new AppointmentListAdapter(subjects, appointTables));
		subjectCfgs = new ArrayList<SubjectCfg>();
		subjectCfgs.add(new SubjectCfg());
		subjectCfgs.add(new SubjectCfg());
	}

	/**
	 * 初始化组件
	 */
	private void initCalendar() {
		CalendarPagerAdapter mPagerAdapter = new CalendarPagerAdapter(
				getSupportFragmentManager(), this);

		/************** 选区功能 *********************/
		if (isSection) {
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
		pre.setOnClickListener(new OnClickListener() {

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

		next.setOnClickListener(new OnClickListener() {

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

		if (isSection) {
			int begin = CalendarUtil.getMouthsForDate(beginDate);
			int end = CalendarUtil.getMouthsForDate(endDate);
			if (index >= begin && index <= end) {
				mPager.setCurrentItem(index);
			} else {
				mPager.setCurrentItem(begin);
			}
		} else {
			mPager.setCurrentItem(index);
		}
	}

	/**
	 * 返回监听
	 */
	@Override
	public void resultdata(String data) {
//		Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
//		refreshAppointTables(year, month, day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US); //Hacking
		try {
			Date date = sdf.parse(data);
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);
			refreshAppointTables(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH) + 1,
					calendar.get(Calendar.DAY_OF_MONTH));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 扩展BaseAdapter,实现学员具体信息的显示。
	 * @author Muyangmin
	 * @create 2015-2-26
	 */
	private final class StudentListAdapter extends BaseAdapter{
		
		private ArrayList<AppointStudent> list;

		StudentListAdapter(ArrayList<AppointStudent> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView==null){
				convertView = LayoutInflater.from(CalendarActivity.this)
						.inflate(R.layout.listitem_calendar_student, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.listitem_calendar_tv_stuname);
				holder.tvPhone = (TextView) convertView.findViewById(R.id.listitem_calendar_tv_stuphone);
				holder.imgCall = (ImageButton)convertView.findViewById(R.id.lisitem_calendar_imgbtn_call);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			final AppointStudent student = list.get(position);
			holder.tvName.setText(student.name);
			holder.tvPhone.setText(student.phone);
			holder.imgCall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(LOG_TAG, "calling" + student.phone + "...");
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+student.phone));
					CalendarActivity.this.startActivity(intent);
				}
			});
			return convertView;
		}
		private final class ViewHolder{
			protected TextView tvName;
			protected TextView tvPhone;
			protected ImageButton imgCall;
		}
	}
	
	/**
	 * 实现可折叠列表的适配器，主要显示科目、时间等信息。
	 * @author Muyangmin
	 * @create 2015-2-26
	 */
	private final class AppointmentListAdapter extends BaseExpandableListAdapter{
		
		private ArrayList<AppointSubject> subjects;		//科目数

		AppointmentListAdapter(ArrayList<AppointSubject> subjects) {
			if (subjects==null){
				throw new NullPointerException("params cannot be null");
			}
			this.subjects = subjects;
		}
		
		//刷新数据
		public void refreshData(ArrayList<AppointSubject> subjects){
			if (subjects!=null){
			this.subjects = subjects;
				notifyDataSetChanged(); 
			}
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return subjects.get(groupPosition).getPeriodList().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder = null;
			AppointPeriod table = (AppointPeriod) getChild(groupPosition, childPosition);
			if (convertView==null){
				convertView = LayoutInflater.from(CalendarActivity.this)
						.inflate(R.layout.listitem_calendar_periods, null);
				holder = new ChildViewHolder();
				holder.lvStudents = (ListView) convertView
						.findViewById(R.id.listitem_calendar_studentlist);
				holder.tvPeriodDescription = (TextView) convertView
						.findViewById(R.id.listitem_calendar_perioddesc);
				holder.tvPeriodName = (TextView) convertView
						.findViewById(R.id.listitem_calendar_periodname);
				convertView.setTag(holder);
			}
			else{
				holder = (ChildViewHolder)convertView.getTag();
			}
			holder.tvPeriodName.setText(table.periodName);
			holder.tvPeriodDescription.setText(table.perioidDescription);
			holder.lvStudents.setAdapter( new StudentListAdapter(table.studentList));
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return subjects.get(groupPosition).periodList.size();
		}

		@Override
		public long getCombinedChildId(long groupId, long childId) {
			return 0;
		}

		@Override
		public long getCombinedGroupId(long groupId) {
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public int getGroupCount() {
			return subjects.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(CalendarActivity.this)
						.inflate(R.layout.listitem_calendar_subject, null);
				holder = new GroupViewHolder();
				holder.imgIndicator = (ImageView) convertView
						.findViewById(R.id.listitem_calendar_img_subject_indicator);
				holder.tvLimits = (TextView) convertView
						.findViewById(R.id.listitem_calendar_tv_subject_limits);
				holder.tvSubject = (TextView) convertView
						.findViewById(R.id.listitem_calendar_tv_subject);
				convertView.setTag(holder);
			}
			else{
				holder = (GroupViewHolder) convertView.getTag();
			}
			AppointSubject subject = subjects.get(groupPosition);
			holder.tvSubject.setText(subject.getName());
			holder.tvLimits.setText(buildLimitString(getCurrentAppoint(), subject.getLimits()));
			if (isExpanded){
				holder.imgIndicator.setBackgroundResource(R.drawable.icon_calendar_appoint_group_expanded);
			}
			else {
				holder.imgIndicator.setBackgroundResource(R.drawable.icon_calendar_appoint_group_normal);
			}
			return convertView;
		}
		
		private int getCurrentAppoint(){
			int count =0;
			for (int i=0; i<subjects.size(); i++){
				count+= subjects.get(i).periodList.get(i).studentList.size();
			}
			return count;
		}
		
		//构造限选字符串并设置样式。
		private final CharSequence buildLimitString(int current, int limit){
			String str = String.format(getResources().getString(R.string.str_calendar_subject_limits),
					current, limit);
			SpannableStringBuilder ssb = new SpannableStringBuilder(str);
			Pattern num = Pattern.compile("\\d+");
			Matcher matcher = num.matcher(str);
			while (matcher.find()){
				final ForegroundColorSpan span = new ForegroundColorSpan(
						getResources().getColor(R.color.color_theme_orange));
				int start = matcher.start();
				int end = matcher.end();
//				Log.d(LOG_TAG, "start=" + start + ", end=" + end);
				ssb.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			return ssb;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			
		}

		@Override
		public void onGroupExpanded(int groupPosition) {
			
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			
		}
		private final class ChildViewHolder {
			protected TextView tvPeriodName;
			protected TextView tvPeriodDescription;
			protected ListView lvStudents;
		}
		private final class GroupViewHolder {
			protected TextView tvSubject;
			protected TextView tvLimits;
			protected ImageView imgIndicator;
		}
	}
	//预约科目实体类
	private class AppointSubject{
		private String name;
		private int limits;
		private ArrayList<AppointPeriod> periodList;
		AppointSubject(String name, int limits,
				ArrayList<AppointPeriod> periodList) {
			super();
			this.name = name;
			this.limits = limits;
			this.periodList = periodList;
		}
		public String getName() {
			return name;
		}
		public int getLimits() {
			return limits;
		}
		public ArrayList<AppointPeriod> getPeriodList() {
			return periodList;
		}
	}
	
	//预约学员实体类
	private final class AppointStudent{
		private final String name;
		private final String phone;
		AppointStudent(String name, String phone) {
			this.name = name;
			this.phone = phone;
		}
	}
	
	//预约表实体类
	private final class AppointPeriod {
		private String periodName;
		private String perioidDescription;
		private ArrayList<AppointStudent> studentList;
		AppointPeriod(String periodName, String perioidDescription,
				ArrayList<AppointStudent> studentList) {
			super();
			this.periodName = periodName;
			this.perioidDescription = perioidDescription;
			this.studentList = studentList;
		}
	}
}
