package com.huishen.ecoach.util;

import java.io.File;

import android.os.Environment;

/**
 * 处理应用内的文件路径和文件操作。
 * 
 * @author Muyangmin
 * @create 2015-2-12
 */
public final class FileUtil {
	// 一级目录
	private static final String ROOT_PATH = Environment
			.getExternalStorageDirectory() + File.separator + "ecoach";
	// 二级目录
	private static final String PATH_TEMP_PHOTO = "photo";

	/**
	 * 获取放置临时照片的文件路径。
	 */
	public static final String getTemporaryPhotoPath() {
		return ROOT_PATH + File.separator + PATH_TEMP_PHOTO;
	}
}
