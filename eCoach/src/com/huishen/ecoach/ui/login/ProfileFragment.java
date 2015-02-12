package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;
import com.huishen.ecoach.widget.RoundImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 用于注册时填写资料的Fragment。
 * @author Muyangmin
 * @create 2015-2-10
 */
public final class ProfileFragment extends Fragment implements OnClickListener{

	private static final String LOG_TAG = "ProfileFragment";
	private NextStepListener nsListener;
	private RoundImageView imgAvatar;
	private EditText editName, editSchool, editCarno, editCardno;
	private Button btnNextStep;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			nsListener = (NextStepListener) activity;
		} catch (ClassCastException cce) {
			Log.e(LOG_TAG, "ERROR: activity must implements NextStepListener!");
			nsListener = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_register_profile, null);
		initWidgets(view);
		return view;
	}
	
	private final void initWidgets(View view){
		imgAvatar = (RoundImageView)view.findViewById(R.id.register_fragp_img_avatar);
		editName = (EditText)view.findViewById(R.id.register_fragp_edit_name);
		editSchool = (EditText)view.findViewById(R.id.register_fragp_edit_school);
		editCarno = (EditText)view.findViewById(R.id.register_fragp_edit_carno);
		editCardno = (EditText)view.findViewById(R.id.register_fragp_edit_cardno);
		btnNextStep = (Button)view.findViewById(R.id.register_fragp_btn_next);
		imgAvatar.setOnClickListener(this);
		btnNextStep.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_fragp_btn_next:
			nextStep();
			break;
		case R.id.register_fragp_img_avatar:
			selectPhoto();
			break;
		default:
			break;
		}
	}
	//下一步按钮逻辑：要检查所有的控件。
	private void nextStep(){
		final String name = editName.getText().toString();
		final String school = editSchool.getText().toString();
		final String carno = editCarno.getText().toString();
		final String cardno = editCardno.getText().toString();
		//暂时不检查照片
		if ((name.length() <= 0) || (school.length() <= 0)
				|| (carno.length() <= 0) || (cardno.length() <= 0)) {
			Toast.makeText(getActivity(), "请填写所有信息", Toast.LENGTH_SHORT).show();
			return ;
		}
		//检查教练证位数
		if (cardno.length()!=getResources().getInteger(R.integer.coach_certify_valid_length)){
			Toast.makeText(getActivity(), "教练证号码长度可能不正确", Toast.LENGTH_SHORT).show();
			return ;
		}
		//TODO 添加网络操作请求并在成功后才调用以下代码。
		if (nsListener != null){
			Log.d(LOG_TAG, "Step verify-phone completed.");
			nsListener.onFillProfileStepCompleted(name, school, carno, cardno);
		}
	}
	
	private final void selectPhoto(){
		CharSequence[] items = {"相册", "相机"};  
	    new AlertDialog.Builder(getActivity())
		    .setTitle("选择图片来源")
		    .setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if( which == 0 ){
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						startActivityForResult(Intent.createChooser(intent, "选择图片"), 1001); 
			        }else{
			        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			        	startActivityForResult(intent, 1002);  
			        }
				}
			})
			.create().show(); 
	}

	protected static interface NextStepListener {
		/**
		 * 当填写资料步骤完成时调用。
		 * @param name 姓名
		 * @param school 驾校
		 * @param carno 车号
		 * @param certno 证件号
		 */
		void onFillProfileStepCompleted(String name, String school,
				String carno, String certno);
	}

}
