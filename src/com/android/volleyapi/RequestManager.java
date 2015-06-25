package com.android.volleyapi;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

/**
 * Manager for the queue
 * 
 *volley 网络请求框架适合高频率、单次请求数据量不大的网络交互。
 *数据量大的网络请求该框架不适用，如文件下载等。
 */
public class RequestManager {
	
	/**
	 * the queue :-)
	 */
	private static RequestQueue mRequestQueue;

	private RequestManager() {
	 // no instances
	} 

	/**
	 * @param context
	 * 			application context
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * @return
	 * 		instance of the queue
	 * @throws
	 * 		IllegalStatException if init has not yet been called
	 */
	public static RequestQueue getRequestQueue() {
	    if (mRequestQueue != null) {
	        return mRequestQueue;
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}
	
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
	
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
            request.setShouldCache(false);//是否缓存
        }
        mRequestQueue.add(request);
    }
}
