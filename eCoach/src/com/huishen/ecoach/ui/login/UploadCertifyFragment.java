package com.huishen.ecoach.ui.login;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.net.UploadResponseListener;
import com.huishen.ecoach.util.BitmapUtil;
import com.huishen.ecoach.util.FileUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用于注册时上传证件的Fragment。
 * 
 * @author Muyangmin
 * @create 2015-2-11
 */
public final class UploadCertifyFragment extends Fragment {

	private static final String LOG_TAG = "UploadCertifyFragment";
	private Button btnSubmit;
	private GridView gridCertifies;
	private NextStepListener nsListener;
	
	private ImageView currentImageView;	//当前正在请求拍摄的ImageView
	private String currentCertName;		//当前正在请求拍摄的证件名称
	
	private static final int REQUEST_CODE_TAKE_PHOTO = 1024;
//	private static final int REQUEST_CODE_FROM_ALBUM = 2048;

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
		View view = inflater.inflate(R.layout.frag_register_upload, null);
		btnSubmit = (Button) view.findViewById(R.id.register_fragu_btn_submit);
		gridCertifies = (GridView) view.findViewById(R.id.register_fragu_grid);
		String[] names = getResources().getStringArray(
				R.array.str_register_fragu_certnames);
		gridCertifies.setAdapter(new CertificateGridAdapter(Arrays
				.asList(names)));
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO 添加网络操作请求并在成功后才调用以下代码。
				if (nsListener != null){
					Log.d(LOG_TAG, "Step verify-phone completed.");
					nsListener.onUploadCertifyStepCompleted();
				}	
			}
		});
		return view;
	}
	
	protected static interface NextStepListener {
		/**
		 * 当上传证件步骤完成时调用。
		 */
		void onUploadCertifyStepCompleted();
	}
	
	private final class CertificateGridAdapter extends BaseAdapter {

		private ArrayList<String> certnames;

		CertificateGridAdapter(List<String> certnames) {
			this.certnames = new ArrayList<String>(certnames);
		}

		@Override
		public int getCount() {
			return certnames.size();
		}

		@Override
		public Object getItem(int arg0) {
			return certnames.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.griditem_register_certify, null);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView
						.findViewById(R.id.griditem_tv_certify);
				holder.img = (ImageView) convertView
						.findViewById(R.id.griditem_img_certify);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(certnames.get(position));
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String certname = certnames.get(position);
					Log.d(LOG_TAG, "Now ready to take " + certname);
					currentImageView = holder.img;
					currentCertName = certname;
					takePhoto(certname);
				}
			});
			return convertView;
		}

		protected class ViewHolder {
			TextView tv;
			ImageView img;
		}
	}
	private final void takePhoto(String certname){
		//将拍得的照片先保存在本地，指定照片保存路径（SD卡）
		Uri imageUri = Uri.fromFile(getImageFile(certname));
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, REQUEST_CODE_TAKE_PHOTO);
	}
	
	private final File getImageFile(String certname){
		File target = new File(FileUtil.getTemporaryPhotoPath(), certname+".jpg");
		if (!target.exists()){
			Log.d(LOG_TAG, "target doesnot exist.create it.");
			target.getParentFile().mkdirs();	//避免将末尾的文件名命名为文件夹
			try {
				target.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return target;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "requestCode="+requestCode+", resultCode="+resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_TAKE_PHOTO:
				File origin = getImageFile(currentCertName);
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(origin.getAbsolutePath());
				int parentw = ((ViewGroup) currentImageView.getParent()).getWidth();
				int parenth = ((ViewGroup) currentImageView.getParent()).getHeight();
				Log.i(LOG_TAG,
						"now ready to display the fucking picture!width="
								+ parentw + ",height=" + parenth);
				Bitmap newBitmap = BitmapUtil.scaleBitmap(bitmap, parentw,
						parenth);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				// 将处理过的图片显示在界面上
				//FIXME 完成图片的圆角操作
				currentImageView.setImageBitmap(newBitmap);
				//上传原图 or 压缩图？
				NetUtil.requestUploadFile(origin, SRL.METHOD_UPLOAD_CERTIFICATES, new UploadResponseListener() {
					
					@Override
					public void onSuccess(String str) {
						Log.i(LOG_TAG, "cert upload completed."+str);
					}
					
					@Override
					public void onError(int httpCode) {
						Log.e(LOG_TAG, "upload fail!");
					}

					@Override
					public void onProgressChanged(int hasFinished) {
						Log.d(LOG_TAG, "uploading...finished " + hasFinished+"%");
					}
				});
				break;
			default:
				break;
			}
		}
	}
}
