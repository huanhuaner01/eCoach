package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ComplaintActivity extends RightSideParentActivity {

	private static final String LOG_TAG = "ComplaintActivity";

	private static final String EXTRA_ORDERID = "orderid";

	private long orderid;

	private ListView listView;
	private ReasonSelectListener reasonSelectListener;
	private EditText editOtherReason;

	/**
	 * 获取启动该Activity的Intent。
	 * 
	 * @param context
	 *            上下文信息
	 * @param order
	 *            要启动投诉的订单，该订单的id必须是一个正数。
	 * @return 返回指定的Intent。
	 */
	public static final Intent getIntent(Context context, Order order) {
		Intent intent = new Intent(context, ComplaintActivity.class);
		if (order.getId() <= 0) {
			throw new IllegalArgumentException(
					"This order should have a positive id.");
		}
		intent.putExtra(EXTRA_ORDERID, order.getId());
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaint);
		orderid = getIntent().getLongExtra(EXTRA_ORDERID, 0);
		Log.d(LOG_TAG, "complaint order id : " + orderid);
		if (orderid <= 0) {
			throw new IllegalArgumentException(
					"This order should have a positive id.");
		}
		listView = (ListView) findViewById(R.id.complaint_listview);
		final View footer = LayoutInflater.from(this).inflate(
				R.layout.listfooter_complaint_reason, null);
		editOtherReason = (EditText) footer
				.findViewById(R.id.listfooter_edit_other_reason);
		editOtherReason.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.d(LOG_TAG, "onFocusChange:"+hasFocus);
				if (hasFocus) {
					reasonSelectListener.clearSelection();
				}
			}
		});
		listView.addFooterView(footer);
		listView.setAdapter(new ComplaintReasonAdaptr(getResources()
				.getStringArray(R.array.str_complaint_reasons)));
		reasonSelectListener = new ReasonSelectListener();
		listView.setOnItemClickListener(reasonSelectListener);
	}

	private final class ReasonSelectListener implements OnItemClickListener {

		private static final int INVALID_POSITION = -1;
		private int selectedPosition = INVALID_POSITION;

		protected final void clearSelection() {
			if (selectedPosition==INVALID_POSITION){
				return ;	// do nothing.
			}
			ViewHolder before = (ViewHolder) listView.getChildAt(
					selectedPosition).getTag();
			before.img.setVisibility(View.INVISIBLE);
			selectedPosition = INVALID_POSITION;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//清除Footer的焦点
			if (editOtherReason.hasFocus()){
				editOtherReason.clearFocus();
				//隐藏已经打开的软键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			if (selectedPosition == position) {
				return; // the same, do nothing
			} else if (selectedPosition != INVALID_POSITION) {
				ViewHolder before = (ViewHolder) listView.getChildAt(
						selectedPosition).getTag();
				before.img.setVisibility(View.INVISIBLE);
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			selectedPosition = position;
			holder.img.setVisibility(View.VISIBLE);
		}
	}

	private final class ComplaintReasonAdaptr extends BaseAdapter {

		private String[] reasons;

		ComplaintReasonAdaptr(String[] reasons) {
			this.reasons = reasons;
		}

		@Override
		public int getCount() {
			return reasons.length;
		}

		@Override
		public Object getItem(int arg0) {
			return reasons[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(ComplaintActivity.this)
						.inflate(R.layout.listitem_complaint_reason, null);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView
						.findViewById(R.id.listitem_complaint_reason_tv);
				holder.img = (ImageView) convertView
						.findViewById(R.id.listitem_complaint_reason_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(reasons[position]);
			return convertView;
		}

	}

	private final class ViewHolder {
		protected TextView tv;
		protected ImageView img;
	}
}