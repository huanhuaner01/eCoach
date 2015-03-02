package com.huishen.ecoach.ui.appointment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.ResponseParser;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.util.Uis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BookSettingActivity extends RightSideParentActivity {
	
//	private static final String LOG_TAG = "BookSettingActivity";
	
	private Button btnSubmit;
	private ListView lvSubjects;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_setting);
		initWidgets();
		NetUtil.requestStringData(SRL.Method.METHOD_QUERY_APPOINTCFG, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				try {
					JSONObject json = new JSONObject(ResponseParser
							.getStringFromResult(arg0, SRL.ReturnField.FIELD_INFO));
					ArrayList<Subject> list = new ArrayList<BookSettingActivity.Subject>();
					list.add(new Subject("科目二", json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2AMLIMIT),
							json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2PMLIMIT),
							json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2NTLIMIT),
							json.optString(SRL.Param.PARAM_APPOINTCFG_AMPERIOD),
							json.optString(SRL.Param.PARAM_APPOINTCFG_PMPERIOD),
							json.optString(SRL.Param.PARAM_APPOINTCFG_NTPERIOD)));
					list.add(new Subject("科目三", json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3AMLIMIT),
							json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3PMLIMIT),
							json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3NTLIMIT),
							json.optString(SRL.Param.PARAM_APPOINTCFG_AMPERIOD),
							json.optString(SRL.Param.PARAM_APPOINTCFG_PMPERIOD),
							json.optString(SRL.Param.PARAM_APPOINTCFG_NTPERIOD)));
					lvSubjects.setAdapter(new SubjectAdapter(list));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				
			}
		});
	}
	
	private void initWidgets() {
		lvSubjects = (ListView) findViewById(R.id.booksetting_listview);
		btnSubmit = (Button) findViewById(R.id.booksetting_btn_submit); 
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				performSubmit();
			}
		});
	}
	
	private final void performSubmit(){
		HashMap<String, String> params = new HashMap<String, String>();
		Subject km2 = ((SubjectAdapter)lvSubjects.getAdapter()).list.get(0);
		Subject km3 = ((SubjectAdapter)lvSubjects.getAdapter()).list.get(1);
		params.put(SRL.Param.PARAM_APPOINTCFG_KM2AMLIMIT, String.valueOf(km2.amCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM2PMLIMIT, String.valueOf(km2.pmCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM2NTLIMIT, String.valueOf(km2.ntCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM3AMLIMIT, String.valueOf(km3.amCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM3PMLIMIT, String.valueOf(km3.pmCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM3NTLIMIT, String.valueOf(km3.ntCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_AMPERIOD, km2.amtime);
		params.put(SRL.Param.PARAM_APPOINTCFG_PMPERIOD, km2.pmtime);
		params.put(SRL.Param.PARAM_APPOINTCFG_NTPERIOD, km2.nttime);
		NetUtil.requestStringData(SRL.Method.METHOD_APPOINT_CONFIG, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				Uis.toastShort(BookSettingActivity.this, "修改成功");
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private final class Subject{
		protected final String name;
		protected final int amCount;
		protected final int pmCount;
		protected final int ntCount;
		protected final String amtime;
		protected final String pmtime;
		protected final String nttime;
		
		Subject(String name, int amCount, int pmCount, int nightCount,
				String amtime, String pmtime, String nttime) {
			super();
			this.name = name;
			this.amCount = amCount;
			this.pmCount = pmCount;
			this.ntCount = nightCount;
			this.amtime = amtime;
			this.pmtime = pmtime;
			this.nttime = nttime;
		}
	}
	
	//原有的设计是将每个科目都当成一个完整独立的对象来看待，因此对于当前的API来说可能比较难以适配。
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

		//调用代码根据该方法的返回值来取得数据。该数据依赖于每次编辑后即更新对象的值。
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
			if (convertView == null) {
				convertView = LayoutInflater.from(BookSettingActivity.this)
						.inflate(R.layout.listitem_booksetting_subject, null);
				holder = new ViewHolder();
				holder.tvAmCount = (TextView) convertView
						.findViewById(R.id.listitem_booksetting_edit_amnum);
				holder.tvPmCount = (TextView) convertView
						.findViewById(R.id.listitem_booksetting_edit_pmnum);
				holder.tvNightCount = (TextView) convertView
						.findViewById(R.id.listitem_booksetting_edit_nightnum);
				holder.tvSubjectName = (TextView) convertView
						.findViewById(R.id.listitem_booksetting_subject);
				holder.tvAmPeriod = (TextView)convertView
						.findViewById(R.id.listitem_booksetting_tv_amtime);
				holder.tvAmPeriod = (TextView)convertView
						.findViewById(R.id.listitem_booksetting_tv_pmtime);
				holder.tvAmPeriod = (TextView)convertView
						.findViewById(R.id.listitem_booksetting_tv_nighttime);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			Subject subject = list.get(position);
			holder.tvSubjectName.setText(subject.name);
			holder.tvAmCount.setText(subject.amCount);
			holder.tvPmCount.setText(subject.pmCount);
			holder.tvNightCount.setText(subject.ntCount);
			holder.tvAmPeriod.setText(getTimeString(subject.amtime));
			holder.tvPmPeriod.setText(getTimeString(subject.pmtime));
			holder.tvNtPeriod.setText(getTimeString(subject.nttime));
			return convertView;
		}
		
		private final String getTimeString(String server){
			return server;
		}
		
		private class ViewHolder {
			protected TextView tvSubjectName;
			protected TextView tvAmCount;
			protected TextView tvPmCount;
			protected TextView tvNightCount;
			protected TextView tvAmPeriod;
			protected TextView tvPmPeriod;
			protected TextView tvNtPeriod;
		}
	}
}
