package com.huishen.ecoach.net;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.huishen.ecoach.util.BitmapUtil;

import android.graphics.Bitmap;
import android.util.LruCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements
		ImageCache {
	
	private static final int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		return cacheSize;
	}

	public LruBitmapCache() {
		this(getDefaultLruCacheSize());
	}

	public LruBitmapCache(int sizeInKiloBytes) {
		super(sizeInKiloBytes);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return BitmapUtil.getBitmapSize(value) / 1024;
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
}