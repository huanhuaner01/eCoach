package com.huishen.ecoach.ui.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huishen.ecoach.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
		public View getView(int position, View convertView, ViewGroup parent) {
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
					holder.img.setBackgroundColor(getResources().getColor(R.color.color_lighter_gray));
				}
			});
			return convertView;
		}

		private class ViewHolder {
			TextView tv;
			ImageView img;
		}
	}
}
