package com.huishen.ecoach.ui.login;

import java.io.File;
import java.io.IOException;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.net.UploadResponseListener;
import com.huishen.ecoach.util.BitmapUtil;
import com.huishen.ecoach.util.FileUtil;
import com.huishen.ecoach.util.Uris;
import com.huishen.ecoach.widget.RoundImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
 * 
 * @author Muyangmin
 * @create 2015-2-10
 */
public final class ProfileFragment extends Fragment implements OnClickListener {

	private static final String LOG_TAG = "ProfileFragment";
	private NextStepListener nsListener;
	private RoundImageView imgAvatar;
	private EditText editName, editSchool, editCarno, editCardno;
	private Button btnNextStep;
	private static final int REQUEST_CODE_TAKE_PHOTO = 0x2001;
	private static final int REQUEST_CODE_FROM_ALBUM = 0x2101;
	private static final int REQUEST_CODE_CROP_PHOTO = 0x2201;

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

	private final void initWidgets(View view) {
		imgAvatar = (RoundImageView) view
				.findViewById(R.id.register_fragp_img_avatar);
		editName = (EditText) view.findViewById(R.id.register_fragp_edit_name);
		editSchool = (EditText) view
				.findViewById(R.id.register_fragp_edit_school);
		editCarno = (EditText) view
				.findViewById(R.id.register_fragp_edit_carno);
		editCardno = (EditText) view
				.findViewById(R.id.register_fragp_edit_cardno);
		btnNextStep = (Button) view.findViewById(R.id.register_fragp_btn_next);
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
			takePhotoOrFromAlbum();
			break;
		default:
			break;
		}
	}

	// 下一步按钮逻辑：要检查所有的控件。
	private void nextStep() {
		final String name = editName.getText().toString();
		final String school = editSchool.getText().toString();
		final String carno = editCarno.getText().toString();
		final String cardno = editCardno.getText().toString();
		// 暂时不检查照片
		if ((name.length() <= 0) || (school.length() <= 0)
				|| (carno.length() <= 0) || (cardno.length() <= 0)) {
			Toast.makeText(getActivity(), "请填写所有信息", Toast.LENGTH_SHORT).show();
			return;
		}
		// 检查教练证位数
		if (cardno.length() != getResources().getInteger(
				R.integer.coach_certify_valid_length)) {
			Toast.makeText(getActivity(), "教练证号码长度可能不正确", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// TODO 添加网络操作请求并在成功后才调用以下代码。
		if (nsListener != null) {
			Log.d(LOG_TAG, "Step verify-phone completed.");
			nsListener.onFillProfileStepCompleted(name, school, carno, cardno);
		}
	}

	private final void cropPhoto(File imgFile) {
		Uri mUri = Uri.fromFile(imgFile);
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX",
				getResources().getInteger(R.integer.avatar_width));// 输出图片大小
		intent.putExtra("outputY",
				getResources().getInteger(R.integer.avatar_height));
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}

	private final void takePhotoOrFromAlbum() {
		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.str_register_photo_select_source)
				.setItems(R.array.str_register_photo_source,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) { // 拍照
									// 将拍得的照片先保存在本地，指定照片保存路径（SD卡）
									Uri imageUri = Uri
											.fromFile(getAvatarFile());
									Intent openCameraIntent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									openCameraIntent.putExtra(
											MediaStore.EXTRA_OUTPUT, imageUri);
									startActivityForResult(openCameraIntent,
											REQUEST_CODE_TAKE_PHOTO);
								} else { // 从相册选择
									Intent openAlbumIntent = new Intent(
											Intent.ACTION_GET_CONTENT);
									openAlbumIntent.setType("image/*");
									startActivityForResult(openAlbumIntent,
											REQUEST_CODE_FROM_ALBUM);
								}
							}
						}).create().show();

	}

	private final File getAvatarFile() {
		File target = new File(FileUtil.getTemporaryPhotoPath(), "avatar.jpg");
		if (!target.exists()) {
			Log.d(LOG_TAG, "target doesnot exist.create it.");
			target.getParentFile().mkdirs(); // 避免将末尾的文件名命名为文件夹
			try {
				target.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	private final void uploadPhoto(File file) {
		NetUtil.requestUploadFile(file, SRL.METHOD_UPLOAD_AVATAR,
				new UploadResponseListener() {

					@Override
					public void onSuccess(String str) {
						Log.i(LOG_TAG, "cert upload completed." + str);
					}

					@Override
					public void onError(int httpCode) {
						Log.e(LOG_TAG, "upload fail!");
					}

					@Override
					public void onProgressChanged(int hasFinished) {
						Log.d(LOG_TAG, "uploading...finished " + hasFinished
								+ "%");
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "requestCode=" + requestCode + ", resultCode="
				+ resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			// 统一处理
			case REQUEST_CODE_TAKE_PHOTO:
				File origin = getAvatarFile();
				cropPhoto(origin);
				break;
			case REQUEST_CODE_FROM_ALBUM:
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				origin = new File(Uris.getImageFileRealPath(getActivity(),
						originalUri));
				cropPhoto(origin);
				break;
			case REQUEST_CODE_CROP_PHOTO:
				// 拿到剪切数据
				Bitmap cropedBitmap = data.getParcelableExtra("data");
				// 显示剪切的图像
				imgAvatar.setImageBitmap(cropedBitmap);
				// 图像保存到文件中
				BitmapUtil.saveBitmapToFile(getAvatarFile().getAbsolutePath(),
						cropedBitmap);
				File cropedFile = getAvatarFile();
				uploadPhoto(cropedFile);
				break;
			default:
				break;
			}
		}
	}

	protected static interface NextStepListener {
		/**
		 * 当填写资料步骤完成时调用。
		 * 
		 * @param name
		 *            姓名
		 * @param school
		 *            驾校
		 * @param carno
		 *            车号
		 * @param certno
		 *            证件号
		 */
		void onFillProfileStepCompleted(String name, String school,
				String carno, String certno);
	}

}
