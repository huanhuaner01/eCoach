package com.huishen.ecoach.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 处理Bitmap相关的操作（旋转、缩放，etc.）。
 * 
 * @author Muyangmin
 * @create 2015-2-12
 */
public final class BitmapUtil {
	/**
	 * 缩放图片到指定尺寸。
	 * @param bitmap 原图
	 * @param width 目标宽度
	 * @param height 目标高度
	 * @return 缩放后的位图
	 */
	public static final Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
		if (width <=0 || height <=0 ){
			throw new IllegalArgumentException("target size must be positive!");
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}
	
	/**
	 * 缩放图片到指定比例。
	 * @param bitmap 原图
	 * @param scalew 横向缩放比例
	 * @param scaleh 纵向缩放比例
	 * @return 缩放后的位图
	 */
	public static final Bitmap scaleBitmap(Bitmap bitmap, float scalew,
			float scaleh) {
		if (scalew <=0 || scaleh <=0 ){
			throw new IllegalArgumentException("scale rate must be positive!");
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scalew, scaleh);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return newbmp;
	}
}
