package com.huishen.ecoach.ui.msg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.huishen.ecoach.R;
import com.huishen.ecoach.db.DbManager;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MessageActivity extends RightSideParentActivity {

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, MessageActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initWidgets();
	}

	private void initWidgets() {
		ListView listView = (ListView) findViewById(R.id.message_listview);
		IMsgStorage source = new DbManager(this);
		ArrayList<AppMessage> list = source.getAppMessages();
		MsgAdapter adapter = new MsgAdapter(list);
		listView.setAdapter(adapter);
	}

	private class MsgAdapter extends BaseAdapter {

		private ArrayList<AppMessage> list;
		// 为减小对象创建开销而引入的两个字段。
		private SimpleDateFormat dateSdf = new SimpleDateFormat("MM-dd",
				Locale.US);
		private SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm",
				Locale.US);

		MsgAdapter(ArrayList<AppMessage> list) {
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
				convertView = LayoutInflater.from(MessageActivity.this)
						.inflate(R.layout.listitem_msg_list, null);
				holder = new ViewHolder();
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.listitem_msg_tv_content);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.listitem_msg_tv_date);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.listitem_msg_tv_time);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.listitem_msg_tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AppMessage msg = list.get(position);
			holder.tvTitle.setText(msg.getTitle());
			holder.tvContent.setText(msg.getContent());
			holder.tvDate.setText(dateSdf.format(new Date(msg.getTime())));
			holder.tvTime.setText(timeSdf.format(new Date(msg.getTime())));
			return convertView;
		}

		private class ViewHolder {
			protected TextView tvTitle;
			protected TextView tvDate;
			protected TextView tvTime;
			protected TextView tvContent;
		}
	}
}
