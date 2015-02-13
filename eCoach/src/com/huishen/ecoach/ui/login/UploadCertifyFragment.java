package com.huishen.ecoach.ui.login;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.net.UploadResponseListener;
import com.huishen.ecoach.util.BitmapUtil;
import com.huishen.ecoach.util.FileUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
	//存储数据和控件的列表，便于控制UI
	private ArrayList<CertEntity> entities = new ArrayList<CertEntity>();
	private ArrayList<ViewHolder> holders = new ArrayList<ViewHolder>();
	
	//拍照和从相册取图的请求码，注意：因请求类型多而复杂，当用户操作较快时容易出现混乱，
	//使用给两种请求码加position的方式区分证件位置，结合上面两个列表进行UI控制。
	private static final int REQUEST_CODE_TAKE_PHOTO = 0x1001;
	private static final int REQUEST_CODE_FROM_ALBUM = 0x1101;
	
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
		for (String name: names){
			entities.add(new CertEntity(name, false));
		}
		gridCertifies.setAdapter(new CertificateGridAdapter(entities));
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
	
	//与Activity进行通信的接口
	protected static interface NextStepListener {
		/**
		 * 当上传证件步骤完成时调用。
		 */
		void onUploadCertifyStepCompleted();
	}
	
	private final class CertEntity{
		String certname;		//标记该位置的证件名称
		boolean hasPhoto;		//标记该位置已经有照片
		CertEntity(String certname, boolean hasPhoto) {
			this.certname = certname;
			this.hasPhoto = hasPhoto;
		}
	}
	//GridView的ViewHolder。
	private class ViewHolder {
//		ProgressBar prg;
		TextView tv;
		ImageView img;
	}
	private final class CertificateGridAdapter extends BaseAdapter {
	
		private ArrayList<CertEntity> list;

		CertificateGridAdapter(ArrayList<CertEntity> list) {
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
				holders.add(holder);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(list.get(position).certname);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final CertEntity certEntity = list.get(position);
					String certname = certEntity.certname;
					if (certEntity.hasPhoto){	//已有照片，显示或重传
						
					}
					else{
						Log.d(LOG_TAG, "Now ready to take " + certname);
						takePhoto(certname, position);						
					}
				}
			});
			return convertView;
		}

	}

	private final void takePhoto(final String certname,final int position) {
		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.str_register_photo_select_source)
				.setItems(R.array.str_register_photo_source,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) { // 拍照
									// 将拍得的照片先保存在本地，指定照片保存路径（SD卡）
									Uri imageUri = Uri.fromFile(getImageFile(certname));
									Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
									startActivityForResult(openCameraIntent,
											REQUEST_CODE_TAKE_PHOTO + position);
								} else { // 从相册选择

								}
							}
						}).create().show();

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
	
	private final void uploadPhoto(File file, final ViewHolder holder){
		NetUtil.requestUploadFile(file, SRL.METHOD_UPLOAD_CERTIFICATES, new UploadResponseListener() {
			
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
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "requestCode="+requestCode+", resultCode="+resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			//统一处理
			case REQUEST_CODE_TAKE_PHOTO:
			case REQUEST_CODE_TAKE_PHOTO+1:
			case REQUEST_CODE_TAKE_PHOTO+2:
			case REQUEST_CODE_TAKE_PHOTO+3:
				final int position = requestCode - REQUEST_CODE_TAKE_PHOTO;
				File origin = getImageFile(entities.get(position).certname);
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(origin.getAbsolutePath());
				int parentw = ((ViewGroup) holders.get(position).img.getParent()).getWidth();
				int parenth = ((ViewGroup) holders.get(position).img.getParent()).getHeight();
				Log.i(LOG_TAG,
						"now ready to display the fucking picture!width="
								+ parentw + ",height=" + parenth);
				Bitmap newBitmap = BitmapUtil.scaleBitmap(bitmap, parentw,
						parenth);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				// 将处理过的图片显示在界面上
				//FIXME 完成图片的圆角操作
				holders.get(position).img.setImageBitmap(newBitmap);
				//上传原图 or 压缩图？
				uploadPhoto(origin, holders.get(position));
				break;
			default:
				break;
			}
		}
	}
}
