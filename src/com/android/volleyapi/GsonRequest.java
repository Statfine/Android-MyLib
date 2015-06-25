package com.android.volleyapi;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by Ho on 2014/6/26.
 */
public class GsonRequest<T> extends Request<T> {

    protected final static String TAG = "GsonRequest";

    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Response.Listener<T> mListener;
    private final Map<String, String> mHeaders;
	private final Map<String, String> mParams;

	
    public GsonRequest( String url, 
    					Class<T> clazz, 
    					Response.Listener<T> listener, 
    					Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, listener, errorListener);
    }

    /*
     * method 方法
     * url 链接
     * clazz 类型
     * headers 头部添加
     * listener 成功回调
     * errorListener 错误回调
     */
    public GsonRequest( int method, 
    					String url, 
    					Class<T> clazz, 
    					Map<String, String> headers, 
    					Response.Listener<T> listener, 
    					Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.mClazz = clazz;
        this.mListener = listener;
        this.mHeaders = headers;
        this.mParams = null;
    }
    
    /*
     * method 方法
     * url 链接
     * clazz 类型
     * listener 成功回调
     * errorListener 错误回调
     * headers 头部添加
     * params 参数
     */
    public GsonRequest( int method, 
    					String url,
    					Class<T> objectClass, 
    					Listener<T> listener, 
    					ErrorListener errorListener,
    					Map<String,String> header,
    					Map<String, String> params) {

		super(method, url, errorListener);
		this.mHeaders = header;
		this.mClazz = objectClass;
		this.mListener = listener;
		this.mParams = params;
	}

	@Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        mListener.onResponse(t);
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return mParams != null ? mParams : super.getParams();
	}
}
