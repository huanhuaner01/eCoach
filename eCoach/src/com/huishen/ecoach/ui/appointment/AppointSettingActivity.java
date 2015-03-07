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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * TODO 由于出于可扩展性和界面自适应性的考虑采用了ListView（而不是硬布局科目二、科目三），
 * 使得该页的逻辑比较复杂，在提交保存请求的时候较难获取到数据。一个可能的方案是使用 监听器
 * 将数值的改变反应到ListView的数据集中，另一个方案是丢弃现有的结构，采用硬布局。
 * @author Muyangmin
 * @create 2015-3-7
 */
public class AppointSettingActivity extends RightSideParentActivity {
	
	
	protected static final Intent getIntent(Context context){
		Intent intent = new Intent(context, AppointSettingActivity.class);
		return intent;
	}
	
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
					ArrayList<SubjectCfg> list = new ArrayList<SubjectCfg>();
					list.add(new SubjectCfg("科目二", json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2AMLIMIT),
							json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2PMLIMIT),
							json.optInt(SRL.Param.PARAM_APPOINTCFG_KM2NTLIMIT),
							json.optString(SRL.Param.PARAM_APPOINTCFG_AMPERIOD),
							json.optString(SRL.Param.PARAM_APPOINTCFG_PMPERIOD),
							json.optString(SRL.Param.PARAM_APPOINTCFG_NTPERIOD)));
					list.add(new SubjectCfg("科目三", json.optInt(SRL.Param.PARAM_APPOINTCFG_KM3AMLIMIT),
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
				Uis.toastShort(AppointSettingActivity.this, R.string.str_appointsetting_err_noconfig);
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
		SubjectCfg km2 = ((SubjectAdapter)lvSubjects.getAdapter()).list.get(0);
		SubjectCfg km3 = ((SubjectAdapter)lvSubjects.getAdapter()).list.get(1);
		params.put(SRL.Param.PARAM_APPOINTCFG_KM2AMLIMIT, String.valueOf(km2.amCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM2PMLIMIT, String.valueOf(km2.pmCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM2NTLIMIT, String.valueOf(km2.ntCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM3AMLIMIT, String.valueOf(km3.amCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM3PMLIMIT, String.valueOf(km3.pmCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_KM3NTLIMIT, String.valueOf(km3.ntCount));
		params.put(SRL.Param.PARAM_APPOINTCFG_AMPERIOD, km2.amtime);
		params.put(SRL.Param.PARAM_APPOINTCFG_PMPERIOD, km2.pmtime);
		params.put(SRL.Param.PARAM_APPOINTCFG_NTPERIOD, km2.nttime);
		NetUtil.requestStringData(SRL.Method.METHOD_APPOINT_CONFIG, params, new ResponseListener() {
			
			@Override
			protected void onSuccess(String arg0) {
				Uis.toastShort(AppointSettingActivity.this, "修改成功");
			}
			
			@Override
			protected void onReturnBadResult(int errorCode, String arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//原有的设计是将每个科目都当成一个完整独立的对象来看待，因此对于当前的API来说可能比较难以适配。
	private class SubjectAdapter extends BaseAdapter{
		
		private ArrayList<SubjectCfg> list;

		SubjectAdapter(ArrayList<SubjectCfg> list) {
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

		@SuppressLint("ViewHolder")
		@Override
		public View getView (int position, View convertView, ViewGroup parent){
			ViewHolder holder;
//			convertView=null;		//force reallocate to avoid dumplicate UI update bug
//			if (convertView == null) {
				convertView = LayoutInflater.from(AppointSettingActivity.this)
						.inflate(R.layout.listitem_booksetting_subject, null);
				holder = new ViewHolder();
				holder.editAmCount = (EditText) convertView
						.findViewById(R.id.listitem_booksetting_edit_amnum);
				holder.editPmCount = (EditText) convertView
						.findViewById(R.id.listitem_booksetting_edit_pmnum);
				holder.editNightCount = (EditText) convertView
						.findViewById(R.id.listitem_booksetting_edit_nightnum);
				holder.tvSubjectName = (TextView) convertView
						.findViewById(R.id.listitem_booksetting_subject);
				holder.tvAmPeriod = (TextView)convertView
						.findViewById(R.id.listitem_booksetting_tv_amtime);
				holder.tvPmPeriod = (TextView)convertView
						.findViewById(R.id.listitem_booksetting_tv_pmtime);
				holder.tvNtPeriod = (TextView)convertView
						.findViewById(R.id.listitem_booksetting_tv_nighttime);
				convertView.setTag(holder);
//			}
//			else{
//				holder = (ViewHolder) convertView.getTag();
//			}
			SubjectCfg subject = list.get(position);
			holder.tvSubjectName.setText(subject.name);
			holder.editAmCount.setText(String.valueOf(subject.amCount));
			holder.editPmCount.setText(String.valueOf(subject.pmCount));
			holder.editNightCount.setText(String.valueOf(subject.ntCount));
			holder.tvAmPeriod.setText(SubjectCfg.parseTimeString(subject.amtime));
			holder.tvPmPeriod.setText(SubjectCfg.parseTimeString(subject.pmtime));
			holder.tvNtPeriod.setText(SubjectCfg.parseTimeString(subject.nttime));
			addListeners(holder, position);
			return convertView;
		}
		
		private void addListeners(ViewHolder holder, final int position){
			holder.editAmCount.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length()>0){
						Log.d("AppointSetting", "position="+position+", s="+s.toString());
						list.get(position).setAmCount(Integer.parseInt(s.toString()));
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			holder.editPmCount.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length()>0){
						list.get(position).setPmCount(Integer.parseInt(s.toString()));
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			holder.editNightCount.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length()>0){
						list.get(position).setNtCount(Integer.parseInt(s.toString()));
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		}
		
		private class ViewHolder {
			protected TextView tvSubjectName;
			protected EditText editAmCount;
			protected EditText editPmCount;
			protected EditText editNightCount;
			protected TextView tvAmPeriod;
			protected TextView tvPmPeriod;
			protected TextView tvNtPeriod;
		}
	}
}
