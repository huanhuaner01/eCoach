package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用于注册时上传证件的Fragment。
 * @author Muyangmin
 * @create 2015-2-11
 */
public final class UploadCertifyFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_register_upload, null);
		return view;
	}
}
