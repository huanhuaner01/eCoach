package com.huishen.ecoach.ui.parent;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author Muyangmin
 * @create 2015-3-2
 */
public abstract class NewIntentParentActivity extends Activity {
	public void onBackButtonClicked(View v) {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
