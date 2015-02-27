package com.huishen.ecoach.ui.pcenter;

import java.util.HashMap;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseListener;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;
import com.huishen.ecoach.util.Uis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends RightSideParentActivity implements OnClickListener{
	
	private EditText editContent;
	private Button btnSubmit;
	
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, FeedbackActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initWidgets();
	}
	
	private final void initWidgets() {
		editContent = (EditText) findViewById(R.id.feedback_edit_content);
		btnSubmit = (Button)findViewById(R.id.feedback_btn_submit);
		btnSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		//assume only submit button.
		final String content = editContent.getText().toString();
		if (content.equals("")){
			Uis.toastShort(this, R.string.str_err_requiredinfo_not_completed);
			return ;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_FEEDBACK_CONTENT, content);
		NetUtil.requestStringData(SRL.Method.METHOD_FEEDBACK, params,
				new ResponseListener() {
					
					@Override
					protected void onSuccess(String arg0) {
						Uis.toastShort(FeedbackActivity.this, R.string.str_feedback_toast_commit);
					}
					
					@Override
					protected void onReturnBadResult(int errorCode, String arg0) {
						Log.w(FeedbackActivity.class.getSimpleName(), "Fail to send feed information.");
						//即使提交失败，也让用户觉得提交成功了
						Uis.toastShort(FeedbackActivity.this, R.string.str_feedback_toast_commit);
					}
				});
	}
	
}
