package com.huishen.ecoach.ui.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class OrderListActivity extends RightSideParentActivity {
	
	public static Intent getIntent(Context context){
		Intent intent = new Intent(context, OrderListActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		initWidgets();
	}

	private void initWidgets() {
		ListView listView = (ListView) findViewById(R.id.orderlist_listview);
		ArrayList<Order> list = new ArrayList<Order>();
		OrderAdapter adapter = new OrderAdapter(list);
		listView.setAdapter(adapter);
	}
	
	private class OrderAdapter extends BaseAdapter{
		private ArrayList<Order> list;
		// 为减小对象创建开销而引入的两个字段。
		private SimpleDateFormat dateSdf = new SimpleDateFormat("M月d日",
				Locale.CHINA);
		private SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm",
				Locale.US);

		OrderAdapter(ArrayList<Order> list) {
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(OrderListActivity.this)
						.inflate(R.layout.listitem_order_list, null);
				holder = new ViewHolder();
				holder.tvStudentPostion = (TextView)findViewById(R.id.listitem_order_tv_student);
				holder.tvDate = (TextView)findViewById(R.id.listitem_order_tv_date);
				holder.tvTime = (TextView)findViewById(R.id.listitem_order_tv_time);
				holder.tvTargetPosition = (TextView)findViewById(R.id.listitem_order_tv_target);
				holder.tvcancel = (TextView)findViewById(R.id.listitem_order_tv_cancelled);
				holder.rtbEvaluation = (RatingBar)findViewById(R.id.listitem_rtb_evaluate);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Order order = list.get(position);
			holder.tvDate.setText(dateSdf.format(new Date(order.getTime())));
			holder.tvTime.setText(timeSdf.format(new Date(order.getTime())));
			holder.tvStudentPostion.setText(order.getStudentPosition());
			holder.tvTargetPosition.setText(order.getStudyPosition());
			if (order.isCancelled()){
				holder.rtbEvaluation.setVisibility(View.INVISIBLE);
				holder.tvcancel.setVisibility(View.VISIBLE);
			}
			else{
				holder.rtbEvaluation.setVisibility(View.VISIBLE);
				holder.tvcancel.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
		private class ViewHolder {
			protected TextView tvStudentPostion;
			protected TextView tvDate;
			protected TextView tvTime;
			protected TextView tvTargetPosition;
			protected TextView tvcancel;
			protected RatingBar rtbEvaluation;
		}
	}
}
