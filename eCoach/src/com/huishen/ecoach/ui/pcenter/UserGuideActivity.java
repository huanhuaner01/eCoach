package com.huishen.ecoach.ui.pcenter;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

//用户指南界面
public class UserGuideActivity extends RightSideParentActivity {
	
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, UserGuideActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_guide);
		initWidgets();
	}

	private final void initWidgets() {
		View headerView = LayoutInflater.from(this).inflate(
				R.layout.listitem_userguide_punish, null);
		TextView time = (TextView) headerView.findViewById(R.id.listitem_userguide_tv_times);
		time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);	//avoid using getDimension().
		time.setTextColor(getResources().getColor(android.R.color.black));
		time.setText(R.string.str_userguide_list_header_times);
		TextView solu = (TextView) headerView.findViewById(R.id.listitem_userguide_tv_solutions);
		time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		solu.setTextColor(getResources().getColor(android.R.color.black));
		solu.setText(R.string.str_userguide_list_header_solutions);
		ListView listView = (ListView) findViewById(R.id.userguide_listview);
		listView.addHeaderView(headerView);
		listView.setAdapter(new PunishAdapter());
	}

	private class PunishAdapter extends BaseAdapter {

		private String[] times; // 次数
		private String[] solutions; // 对应的处罚方案

		private PunishAdapter() {
			times = getResources().getStringArray(
					R.array.str_userguide_list_times);
			solutions = getResources().getStringArray(
					R.array.str_userguide_list_solutions);
			if (times.length != solutions.length) {
				throw new IllegalArgumentException(
						"These 2 array should be the same length.");
			}
		}

		@Override
		public int getCount() {
			return times.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(UserGuideActivity.this)
						.inflate(R.layout.listitem_userguide_punish, null);
				holder = new ViewHolder();
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.listitem_userguide_tv_times);
				holder.tvSolution = (TextView) convertView
						.findViewById(R.id.listitem_userguide_tv_solutions);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTime.setText(times[position % getCount()]);
			holder.tvSolution.setText(solutions[position % getCount()]);
			return convertView;
		}

		private class ViewHolder {
			protected TextView tvTime;
			protected TextView tvSolution;
		}
	}
}
