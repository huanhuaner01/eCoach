package com.huishen.ecoach.ui.login;

import com.huishen.ecoach.R;
import com.huishen.ecoach.ui.parent.RightSideParentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageBrowseActivity extends RightSideParentActivity {

	private static final String EXTRA_IMAGE_FILE = "image";

	/**
	 * 获得启动该Activity的Intent。
	 * 
	 * @param context
	 *            上下文信息
	 * @param path
	 *            要查看的图片文件的路径
	 * @return 返回包装后的Intent。
	 */
	public static Intent getIntent(Context context, String path) {
		if (path == null || path.equals("")) {
			throw new IllegalArgumentException("must specify an image file.");
		}
		Intent intent = new Intent(context, ImageBrowseActivity.class);
		intent.putExtra(EXTRA_IMAGE_FILE, path);
		return intent;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_browse);
		String path = getIntent().getExtras().getString(EXTRA_IMAGE_FILE, null);
		if (path == null || path.equals("")) {
			throw new IllegalArgumentException("must specify an image file.");
		}
		ImageView imageView = (ImageView) findViewById(R.id.image_browse_img);
		imageView.setBackgroundDrawable(new BitmapDrawable(getResources(),
				BitmapFactory.decodeFile(path)));
	}
}
