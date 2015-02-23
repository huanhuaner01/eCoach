package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ComplaintActivity extends RightSideParentActivity {
	
	public static final String LOG_TAG = "ComplaintActivity";
	
	private static final String EXTRA_ORDERID = "orderid";
	
	/**
	 * 获取启动该Activity的Intent。
	 * @param context 上下文信息
	 * @param order 要启动投诉的订单，该订单的id必须是一个正数。
	 * @return 返回指定的Intent。
	 */
	public static final Intent getIntent(Context context, Order order){
		Intent intent = new Intent(context, ComplaintActivity.class);
		if (order.getId() < 0){
			throw new IllegalArgumentException("This order should have a positive id.");
		}
		intent.putExtra(EXTRA_ORDERID, order.getId());
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaint);
	}
}