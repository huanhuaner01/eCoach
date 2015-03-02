package com.huishen.ecoach.ui.appointment;

import java.util.ArrayList;

import com.huishen.ecoach.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BookSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_setting);
		ListView lv = (ListView) findViewById(R.id.booksetting_listview);
		lv.setAdapter(new SubjectAdapter(null));
	}
	
	private final class Subject{
		protected final String name;
		protected final int amCount;
		protected final int pmCount;
		protected final int nightCount;
		Subject(String name, int amCount, int pmCount, int nightCount) {
			super();
			this.name = name;
			this.amCount = amCount;
			this.pmCount = pmCount;
			this.nightCount = nightCount;
		}
	}
	
	private class SubjectAdapter extends BaseAdapter{
		
		private ArrayList<Subject> list;

		SubjectAdapter(ArrayList<Subject> list) {
			super();
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
		public View getView (int position, View convertView, ViewGroup parent){
			ViewHolder holder;
			if (convertView==null){
				convertView = LayoutInflater.from(BookSettingActivity.this).inflate(R.layout.listitem_booksetting_subject, null);
				holder = new ViewHolder();
				holder.tvAmCount = (TextView) convertView.findViewById(R.id.listitem_booksetting_edit_amnum);
				holder.tvPmCount = (TextView) convertView.findViewById(R.id.listitem_booksetting_edit_pmnum);
				holder.tvNightCount = (TextView) convertView.findViewById(R.id.listitem_booksetting_edit_nightnum);
				holder.tvSubjectName = (TextView)convertView.findViewById(R.id.listitem_booksetting_subject);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			Subject subject = list.get(position);
			holder.tvSubjectName.setText(subject.name);
			holder.tvAmCount.setText(subject.amCount);
			holder.tvPmCount.setText(subject.pmCount);
			holder.tvNightCount.setText(subject.nightCount);
			return convertView;
		}
		private class ViewHolder {
			protected TextView tvSubjectName;
			protected TextView tvAmCount;
			protected TextView tvPmCount;
			protected TextView tvNightCount;
			
		}
	}
}
