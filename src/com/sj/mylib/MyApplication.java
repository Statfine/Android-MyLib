package com.sj.mylib;


import com.adnroid.volleyapi.image.ImageCacheManager;
import com.adnroid.volleyapi.image.ImageCacheManager.CacheType;
import com.android.volleyapi.RequestManager;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

public class MyApplication extends Application {

	private static Context mContext;
	
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100; // PNG is lossless so quality is ignored but must be provided 
	
	public static Context getAppContext(){
		return mContext;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();
		
		RequestManager.init(this);
		createImageCache();
	}
	
	/**
	 * ³õÊ¼»¯Í¼Æ¬»º´æÈÝÆ÷
	 */
	private void createImageCache() {
		Long memoryCacheSize = Runtime.getRuntime().freeMemory() / 2;
		
		ImageCacheManager.getInstance().init(this, memoryCacheSize.intValue(),
				DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY,
				CacheType.MEMORY_DISK);
	}
	
}
