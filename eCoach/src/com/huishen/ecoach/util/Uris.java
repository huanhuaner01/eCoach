package com.huishen.ecoach.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 放置与Uri相关的一些操作。
 * 
 * @author Muyangmin
 * @create 2015-2-13
 */
public final class Uris {
	/**
	 * 获取图片文件的实际路径
	 * @param context 上下文信息
	 * @param contentUri 包含图片文件信息的Uri
	 * @return 返回解析的路径
	 */
	public static final String getImageFileRealPath(Context context,
			Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}
}
