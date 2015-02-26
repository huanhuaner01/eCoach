package com.huishen.ecoach.ui.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentFragmentActivity;

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
import android.widget.ExpandableListAdapter;
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
		mPager = (ViewPager) findViewById(R.id.pager);
		calendartitle = (TextView) findViewById(R.id.calendar_month);
		pre = (ImageButton) findViewById(R.id.calendar_month_pre);
		next = (ImageButton) findViewById(R.id.calendar_month_next);
		expListView = (ExpandableListView)findViewById(R.id.calendar_sub_list);
		//test code begin
		Log.d(LOG_TAG, "adding emulated data...");
		ArrayList<AppointSubject> subjects = new ArrayList<CalendarActivity.AppointSubject>();
		subjects.add(new AppointSubject("科目二", 10, 19));
		subjects.add(new AppointSubject("科目三", 5, 12));
		ArrayList<AppointTable> appointTables = new ArrayList<CalendarActivity.AppointTable>();
		String[] ams = new String[]{"上午","下午","晚上"};
		String[] descs = new String[]{"8:00~12:00","13:00~18:00","20:00~22:00"};
		for (int i=0;i<3;i++){
			AppointTable table  = new AppointTable();
			table.setPeriodName(ams[i]);
			table.setPerioidDescription(descs[i]);
			ArrayList<AppointStudent> students = new ArrayList<CalendarActivity.AppointStudent>();
			for (int j=0; j<5; j++){
				students.add(new AppointStudent("韩梅梅", "15989897820"));
			}
			table.setStudentList(students);
			appointTables.add(table);
		}
		Log.d(LOG_TAG, "adding emulated data...100%.");
		expListView.setAdapter(new AppointmentListAdapter(subjects, appointTables));
	}

	/**
	 * 初始化组件
	 */
	private void init() {
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
		Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
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
			holder.tvName.setText(student.getName());
			holder.tvPhone.setText(student.getPhone());
			holder.imgCall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(LOG_TAG, "calling" + student.getPhone() + "...");
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+student.getPhone()));
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
	private final class AppointmentListAdapter implements ExpandableListAdapter{
		
		private ArrayList<AppointSubject> subjects;		//科目数
		private ArrayList<AppointTable> appointTables;	//对应的约课列表

		AppointmentListAdapter(ArrayList<AppointSubject> subjects,
				ArrayList<AppointTable> appointTables) {
			if (subjects==null || appointTables==null){
				throw new NullPointerException("params cannot be null");
			}
			this.subjects = subjects;
			this.appointTables = appointTables;
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return appointTables.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder = null;
			AppointTable table = appointTables.get(childPosition);
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
			holder.tvPeriodName.setText(table.getPeriodName());
			holder.tvPeriodDescription.setText(table.getPerioidDescription());
			holder.lvStudents.setAdapter( new StudentListAdapter(table.getStudentList()));
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return appointTables.size();
		}

		@Override
		public long getCombinedChildId(long groupId, long childId) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getCombinedGroupId(long groupId) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
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
			holder.tvLimits.setText(buildLimitString(subject.getCurrentAppoint(), subject.getLimits()));
			if (isExpanded){
				holder.imgIndicator.setBackgroundResource(R.drawable.icon_calendar_appoint_group_expanded);
			}
			else {
				holder.imgIndicator.setBackgroundResource(R.drawable.icon_calendar_appoint_group_normal);
			}
			return convertView;
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
				Log.d(LOG_TAG, "start=" + start + ", end=" + end);
				ssb.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			return ssb;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub	
			return false;
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGroupExpanded(int groupPosition) {
			// TODO Auto-generated method stub
			
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
	private final class AppointSubject{
		private final String name;
		private final int currentAppoint;
		private final int limits;
		AppointSubject(String name, int currentAppoint, int limits) {
			super();
			this.name = name;
			this.currentAppoint = currentAppoint;
			this.limits = limits;
		}
		public String getName() {
			return name;
		}
		public int getCurrentAppoint() {
			return currentAppoint;
		}
		public int getLimits() {
			return limits;
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
		public String getName() {
			return name;
		}
		public String getPhone() {
			return phone;
		}
	}
	
	//预约表实体类
	private final class AppointTable {
		private String periodName;
		private String perioidDescription;
		private ArrayList<AppointStudent> studentList;
		public String getPeriodName() {
			return periodName;
		}
		public void setPeriodName(String periodName) {
			this.periodName = periodName;
		}
		public String getPerioidDescription() {
			return perioidDescription;
		}
		public void setPerioidDescription(String perioidDescription) {
			this.perioidDescription = perioidDescription;
		}
		public ArrayList<AppointStudent> getStudentList() {
			return studentList;
		}
		public void setStudentList(ArrayList<AppointStudent> studentList) {
			this.studentList = studentList;
		}
	}
}
