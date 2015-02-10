package com.huishen.ecoach.ui.parent;

import com.huishen.ecoach.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * 实现Activity的共同功能，例如导航条、切换动画等。
 * 
 * @author Muyangmin
 * @create 2015-2-10
 */
public abstract class RightSideParentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.ac_slide_right_in,
				R.anim.ac_slide_left_out);
	}

	public void onBackButtonClicked(View v) {
		finishWithAnimRightOut();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finishWithAnimRightOut();
		}
		return super.onKeyDown(keyCode, event);
	}

	private final void finishWithAnimRightOut() {
		finish();
		overridePendingTransition(R.anim.ac_slide_left_in,
				R.anim.ac_slide_right_out);
	}

}
