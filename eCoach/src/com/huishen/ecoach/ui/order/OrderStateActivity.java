package com.huishen.ecoach.ui.order;

import com.huishen.ecoach.R;
import com.huishen.ecoach.util.Uis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class OrderStateActivity extends Activity {

	private static final String LOG_TAG = "OrderStateActivity";
	private static final String EXTRA_ORDER = "order";
	private Order mOrder;

	private TextView tvTitle, tvStuPosition, tvStudyPosition; // common
	private TextView tvCancelReason; // state:cancel
	private TextView tvDate, tvTime, tvPhone, tvContent; // state:evaluated

	public static final Intent getIntent(Context context, Order order) {
		Intent intent = new Intent(context, OrderStateActivity.class);
		intent.putExtra(EXTRA_ORDER, order);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_state);
		try {
			mOrder = (Order) getIntent().getSerializableExtra(EXTRA_ORDER);
		} catch (NullPointerException e) {
			throw new RuntimeException("No order found.");
		}
		initWidgets();
	}

	private void initWidgets() {
		tvTitle = (TextView) findViewById(R.id.orderstate_tv_title);
		tvStuPosition = (TextView) findViewById(R.id.orderstate_tv_stu_postiion);
		tvStudyPosition = (TextView) findViewById(R.id.orderstate_tv_coach_postiion);
		tvCancelReason = (TextView) findViewById(R.id.orderstate_tv_cancel_reason);
		tvDate = (TextView) findViewById(R.id.orderstate_tv_evaldate);
		tvTime = (TextView) findViewById(R.id.orderstate_tv_evaltime);
		tvPhone = (TextView) findViewById(R.id.orderstate_tv_evalphone);
		tvContent = (TextView) findViewById(R.id.orderstate_tv_content);
		
		tvStuPosition.setText(mOrder.getStudentPosition());
		tvStudyPosition.setText(mOrder.getStudyPosition());
		
		if (mOrder.isCancelled()) {
			tvTitle.setText(R.string.str_orderstate_cancelled);
			Uis.setVisibleInvisible(findViewById(R.id.orderstate_ll_cancelled),
					findViewById(R.id.orderstate_ll_evaluated));
			tvCancelReason.setText("");//FIXME finish reason.
		} else if (mOrder.isEvaluated()) {
			tvTitle.setText(R.string.str_orderstate_evaluated);
			Uis.setVisibleInvisible(findViewById(R.id.orderstate_ll_evaluated),
					findViewById(R.id.orderstate_ll_cancelled));
			//TODO finish evaluation.
		}
		else {
			throw new RuntimeException("what state should this order be ?");
		}
	}
}
