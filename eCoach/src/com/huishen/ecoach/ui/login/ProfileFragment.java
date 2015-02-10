package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 填写资料的Fragment。
 * @author Muyangmin
 * @create 2015-2-10
 */
public final class ProfileFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_register_profile, container);
		return view;
	}
}
