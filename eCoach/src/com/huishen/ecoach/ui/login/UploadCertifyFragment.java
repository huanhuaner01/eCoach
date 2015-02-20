package com.huishen.ecoach.ui.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.Response;
import com.huishen.ecoach.Const;
import com.huishen.ecoach.R;
import com.huishen.ecoach.net.NetUtil;
import com.huishen.ecoach.net.ResponseParser;
import com.huishen.ecoach.net.SRL;
import com.huishen.ecoach.net.UploadResponseListener;
import com.huishen.ecoach.util.BitmapUtil;
import com.huishen.ecoach.util.FileUtil;
import com.huishen.ecoach.util.Prefs;
import com.huishen.ecoach.util.Uis;
import com.huishen.ecoach.util.Uris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
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
import android.widget.ProgressBar;
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
	private static final int REQUEST_CODE_TAKE_PHOTO = 0x3001;
	private static final int REQUEST_CODE_FROM_ALBUM = 0x3101;
	
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
				//检查四张证件是否都已上传完毕
				boolean uploadFinished = true;
				for (int i=0; i<entities.size(); i++){
					if (Prefs.getString(getActivity(), getImagePrefname(i))==null){
						uploadFinished = false;
						break;
					}
				}
				if (!uploadFinished){
					Uis.toastShort(getActivity(),
							R.string.str_register_err_cert_upload_not_finished);
					return ;
				}
				submitLastRequest();
			}
		});
		return view;
	}
	
	private final void submitLastRequest(){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SRL.Param.PARAM_MOBILE_NUMBER, Prefs.getString(getActivity(), Const.KEY_VERIFIED_PHONE));
		params.put(SRL.Param.PARAM_USERNAME, Prefs.getString(getActivity(), Const.KEY_COACH_NAME));
		params.put(SRL.Param.PARAM_SCHOOL, Prefs.getString(getActivity(), Const.KEY_COACH_SCHOOL));
		params.put(SRL.Param.PARAM_CARNO, Prefs.getString(getActivity(), Const.KEY_COACH_CARNO));
		params.put(SRL.Param.PARAM_COACH_CERTNO, Prefs.getString(getActivity(), Const.KEY_COACH_CERTNO));
		params.put(SRL.Param.PARAM_PATH_AVATAR, Prefs.getString(getActivity(), Const.KEY_COACH_AVATAR));
		params.put(SRL.Param.PARAM_PATH_CERT1, Prefs.getString(getActivity(), getImagePrefname(0)));
		params.put(SRL.Param.PARAM_PATH_CERT2, Prefs.getString(getActivity(), getImagePrefname(1)));
		params.put(SRL.Param.PARAM_PATH_CERT3, Prefs.getString(getActivity(), getImagePrefname(2)));
		params.put(SRL.Param.PARAM_PATH_CERT4, Prefs.getString(getActivity(), getImagePrefname(3)));
		NetUtil.requestStringData(SRL.Method.METHOD_FINISH_REGISTER, params,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (ResponseParser.isReturnSuccessCode(arg0)) {
							if (nsListener != null) {
								Log.d(LOG_TAG, "Step verify-phone completed.");
								Uis.toastShort(getActivity(),
										R.string.str_register_info_profile_ok);
								for (int i=0; i<4 ; i++){
									Prefs.removeKey(getActivity(), getImagePrefname(i));
								}
								nsListener.onUploadCertifyStepCompleted();
							}
						} else {
							Uis.toastShort(getActivity(),
									R.string.str_register_err_cert_upload_not_finished);
						}
					}
				});
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
		String photoPath;		//如已有照片，该字段用于记忆文件的路径
		CertEntity(String certname, boolean hasPhoto) {
			this.certname = certname;
			this.hasPhoto = hasPhoto;
			this.photoPath = null;
		}
	}
	//GridView的ViewHolder。
	private class ViewHolder {
		ProgressBar prg;
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
				holder.prg = (ProgressBar)convertView.findViewById(R.id.griditem_prg_upload);
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
						Log.d(LOG_TAG, "photo exists, delete or browse?");
						browseOrChooseAnother(certname, position);	
					}
					else{
						Log.d(LOG_TAG, "ready to add photo:" + certname);
						takePhotoOrFromAlbum(certname, position);						
					}
				}
			});
			return convertView;
		}

	}
	
	//处理已有照片时的操作
	private final void browseOrChooseAnother(final String certname,final int position) {
		new AlertDialog.Builder(getActivity())
				.setTitle(R.string.str_register_photo_alreay_added)
				.setItems(R.array.str_register_photo_operations,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) { // 查看大图
									getActivity().startActivity(ImageBrowseActivity.getIntent(
											getActivity(),entities.get(position).photoPath));
								} else { // 重传
									holders.get(position).img.setImageBitmap(null);
									entities.get(position).hasPhoto = false;
									takePhotoOrFromAlbum(certname, position);
								}
							}
						}).create()	.show();

	}

	private final void takePhotoOrFromAlbum(final String certname,final int position) {
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
									Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				                    openAlbumIntent.setType("image/*");
									startActivityForResult(openAlbumIntent,
											REQUEST_CODE_FROM_ALBUM + position);
								}
							}
						}).create()	.show();

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
	
	private final String getImagePrefname(int position){
		return "certimg" + position;
	}
	
	private final void uploadPhoto(File file, final int position){
		NetUtil.requestUploadFile(file, SRL.Method.METHOD_UPLOAD_CERTIFICATES, new UploadResponseListener() {
			
			@Override
			public void onSuccess(String str) {
				//上传不成功时取得的为null值。
				String url = ResponseParser.getStringFromResult(str,
						SRL.ReturnField.FIELD_URI);
				Log.i(LOG_TAG, "avatar upload completed." + url);
				Prefs.setString(getActivity(), getImagePrefname(position), url);
				holders.get(position).prg.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onError(int httpCode) {
				Log.e(LOG_TAG, "upload fail!");
			}

			@Override
			public void onProgressChanged(int hasFinished) {
				Log.d(LOG_TAG, "uploading...finished " + hasFinished+"%");
				holders.get(position).prg.setProgress(hasFinished);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "requestCode="+requestCode+", resultCode="+resultCode);
		if (resultCode == Activity.RESULT_OK) {
			final int position;
			int parentw, parenth;
			Bitmap bitmap, newBitmap;
			switch (requestCode) {
			//统一处理
			case REQUEST_CODE_TAKE_PHOTO:
			case REQUEST_CODE_TAKE_PHOTO + 1:
			case REQUEST_CODE_TAKE_PHOTO + 2:
			case REQUEST_CODE_TAKE_PHOTO + 3:
				position = requestCode - REQUEST_CODE_TAKE_PHOTO;
				File origin = getImageFile(entities.get(position).certname);
				// 将保存在本地的图片取出并缩小后显示在界面上
				bitmap = BitmapFactory.decodeFile(origin.getAbsolutePath());
				parentw = ((ViewGroup) holders.get(position).img.getParent()).getWidth();
				parenth = ((ViewGroup) holders.get(position).img.getParent()).getHeight();
				newBitmap = BitmapUtil.scaleBitmap(bitmap, parentw,
						parenth);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				// 将处理过的图片显示在界面上
				holders.get(position).img.setImageBitmap(newBitmap);
				//置标记位为true
				entities.get(position).hasPhoto = true;
				//记忆图片位置
				entities.get(position).photoPath = origin.getAbsolutePath();
				//上传图片
				uploadPhoto(origin, position);
				break;
			case REQUEST_CODE_FROM_ALBUM:
			case REQUEST_CODE_FROM_ALBUM + 1:
			case REQUEST_CODE_FROM_ALBUM + 2:
			case REQUEST_CODE_FROM_ALBUM + 3:
				position = requestCode - REQUEST_CODE_FROM_ALBUM;
				ContentResolver resolver = getActivity().getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					bitmap = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (bitmap != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						parentw = ((ViewGroup) holders.get(position).img
								.getParent()).getWidth();
						parenth = ((ViewGroup) holders.get(position).img
								.getParent()).getHeight();
						newBitmap = BitmapUtil.scaleBitmap(bitmap, parentw,
								parenth);
						// 释放原始图片占用的内存，防止out of memory异常发生
						bitmap.recycle();
						holders.get(position).img.setImageBitmap(newBitmap);
						//置标记位为true
						entities.get(position).hasPhoto = true;
					}
					//上传图片
					String path = Uris.getImageFileRealPath(getActivity(), originalUri);
					entities.get(position).photoPath = path;
					Log.d(LOG_TAG, "resolved path:" + path);
					uploadPhoto(new File(path), position);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}
}
