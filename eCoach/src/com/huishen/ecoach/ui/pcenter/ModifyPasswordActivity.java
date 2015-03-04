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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModifyPasswordActivity extends RightSideParentActivity {
	
	private EditText editOldpwd, editNewpwd, editCfmpwd;
	private Button btnSubmit;
	
	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, ModifyPasswordActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password);
		initWidgets();
	}
	
	private void initWidgets() {
		editCfmpwd = (EditText) findViewById(R.id.modifypwd_edit_cfmpwd);
		editNewpwd = (EditText) findViewById(R.id.modifypwd_edit_newpwd);
		editOldpwd = (EditText) findViewById(R.id.modifypwd_edit_oldpwd);
		btnSubmit = (Button)findViewById(R.id.modifypwd_btn_confirm);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String oldpwd = editOldpwd.getText().toString();
				final String newpwd = editNewpwd.getText().toString();
				final String cfmpwd = editCfmpwd.getText().toString();
				if (oldpwd.equals("") || newpwd.equals("") || cfmpwd.equals("")) {
					Uis.toastShort(ModifyPasswordActivity.this,
							R.string.str_err_requiredinfo_not_completed);
					return;
				}
				if (!cfmpwd.equals(newpwd)) {
					Uis.toastShort(ModifyPasswordActivity.this,
							R.string.str_register_err_pwd_nequal);
					//clear it 
					editNewpwd.setText("");
					editCfmpwd.setText("");
					return ;
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(SRL.Param.PARAM_OLDPWD, oldpwd);
				params.put(SRL.Param.PARAM_NEWPWD, newpwd);
				NetUtil.requestStringData(SRL.Method.METHOD_MODIFY_PASSWORD,
						new ResponseListener() {
							
							@Override
							protected void onSuccess(String arg0) {
								Uis.toastShort(ModifyPasswordActivity.this, R.string.str_operation_success);
							}
							
							@Override
							protected void onReturnBadResult(int errorCode, String arg0) {
								Uis.toastShort(ModifyPasswordActivity.this,R.string.str_modifypwd_error_toast);
							}
						});
			}
		});
	}
}
