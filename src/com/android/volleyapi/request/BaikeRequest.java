package com.android.volleyapi.request;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.entity.BaiKe;
import com.android.entity.Weatherinfo;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volleyapi.GsonRequest;
import com.android.volleyapi.RequestManager;

public class BaikeRequest {

	private static final String TAG = BaikeRequest.class.getSimpleName();

	public static BaikeRequest getInstance(){
		return SingleHolder.instance;
	}
	
	private static class SingleHolder{
		private static BaikeRequest instance = new BaikeRequest(); 
	}
	
	protected BaikeRequest(){
		
	}
	
	//Url直接获取
	public void postWeatherinfo(Listener<Weatherinfo.RequestData> listener,
			ErrorListener errorListener) {
		
		String uri = "http://www.weather.com.cn/data/cityinfo/101110101.html";
		
		GsonRequest<Weatherinfo.RequestData> request = new GsonRequest<Weatherinfo.RequestData>(Method.GET
				, uri
				, Weatherinfo.RequestData.class
				, null
				, listener
				, errorListener);
		
		request.setTag(TAG);
		//request.setShouldCache(false);//是否缓存
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}
	
	//Url获取String
	public void postpostWeatherString(Listener<String> listener,ErrorListener errorListener) {
		
		//String uri = String.format(RequestUrl.BAIKE_URL, keyword);
		String uri = "http://www.weather.com.cn/data/cityinfo/101110101.html";
		
		//返回String
		StringRequest request = new StringRequest(Method.GET, uri, listener, errorListener);
		
		request.setTag(TAG);
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}
	
	//params 带参获取
	public void postpostWeatherParams(Listener<BaiKe> listener,ErrorListener errorListener,String key) {

		Map<String, String> mapHaeder = new HashMap<String, String>();
		
		//String uri = "http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=%E5%85%B3%E9%94%AE%E8%AF%8D&bk_length=600";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("scope", "103");
		params.put("format", "json");
		params.put("appid", "379020");
		params.put("bk_key", key);
		params.put("bk_length", "600");
		
		GsonRequest<BaiKe> request = new GsonRequest<BaiKe>(
				Method.POST
				, "http://baike.baidu.com/api/openapi/BaikeLemmaCardApi"
				, BaiKe.class
				, listener
				, errorListener
				, null
				, params);
		
		request.setTag(TAG);
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}
	
	/**
	 * 取消网络请求
	 */
	public void cancleRequest() {
		RequestManager.getRequestQueue().cancelAll(TAG);
	}
	
}
