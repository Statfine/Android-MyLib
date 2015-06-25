package com.sj.mylib.wxapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.sj.mylib.MyApplication;
import com.sj.util.NetworkUtil;
import com.sj.util.WeChatConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeixinManager{
	private IWXAPI wxapi;
	private static final String API_ID = "wxe7711f35717d6b82";
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
//	private static final int THUMB_SIZE = 150;
//	private AsyncTask<String, Long, Boolean> task;
	
	
	protected WeixinManager() {
		wxapi = WXAPIFactory.createWXAPI(MyApplication.getAppContext(), API_ID);
		wxapi.registerApp(API_ID); //ע��Ӧ�õ�΢��
	}
	
	private static class SingletonHolder{
		private static WeixinManager instance = new WeixinManager();
	}
	
	public static WeixinManager getInstance(){
		return SingletonHolder.instance;
	}
	
	/**
	 * ΢����Ȩ
	 * @param context
	 */
	public void loginWeixin(Context context) {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "com.hiho.weixin.auth";
		wxapi.sendReq(req);
	}

	/**
	 * ����΢������Ȧ
	 * @param title ����
	 * @param des  ������Ϣ
	 * @param shareUrl  ��������
	 * @param bitmap  ����ͼ
	 * @param type  ��ý����Ϣ����  1:��ҳ�� 2:���֣�3:��Ƶ
	 * @param context
	 */
	public void shareToWXTimeline(String title, String des, String shareUrl, Bitmap bitmap, int type, Context context){
		
		if(!initWX(type, context)) return;
		
		WXEntryActivity.actionWXEntryActivity(context, type, bitmap, shareUrl, title, des, WeChatConstants.WXSceneTimeline, null);
	}
	
	/**
	* ����΢������Ȧ
	 * @param title ����
	 * @param des  ������Ϣ
	 * @param shareUrl  ��������
	 * @param bitmapUrl  ����ͼ����
	 * @param type  ��ý����Ϣ����  1:��ҳ�� 2:���֣�3:��Ƶ
	 * @param context
	 */
	public void shareToWXTimeline(String title, String des, String shareUrl, String bitmapUrl, int type, Context context){
		
		if(!initWX(type, context)) return;
		
		WXEntryActivity.actionWXEntryActivity(context, type, bitmapUrl, shareUrl, title, des, WeChatConstants.WXSceneTimeline, null);
	}
	
	public void shareToWXTimeline(String title, String des, String shareUrl, String bitmapUrl, int type, Context context, String invokeUrl){
		
		if(!initWX(type, context)) return;
		
		WXEntryActivity.actionWXEntryActivity(context, type, bitmapUrl, shareUrl, title, des, WeChatConstants.WXSceneTimeline, invokeUrl);
	}
	/**
	 * ����΢������
	 * @param title ����
	 * @param des  ������Ϣ
	 * @param shareUrl  ��������
	 * @param bitmap  ����ͼ
	 * @param type  ��ý����Ϣ����  1:��ҳ�� 2:���֣�3:��Ƶ
	 * @param context
	 */
	public void shareToWXSession(String title, String des, String shareUrl, Bitmap bitmap, int type, Context context){
		
		if(!initWX(type, context)) return;
		
		WXEntryActivity.actionWXEntryActivity(context, type, bitmap, shareUrl, title, des, WeChatConstants.WXSceneSession, null);
	}
	
	/**
	* ����΢������
	 * @param title ����
	 * @param des  ������Ϣ
	 * @param shareUrl  ��������
	 * @param bitmapUrl  ����ͼ����
	 * @param type  ��ý����Ϣ����  1:��ҳ�� 2:���֣�3:��Ƶ
	 * @param context
	 */
	public void shareToWXSession(String title, String des, String shareUrl, String bitmapUrl, int type, Context context){
		
		if(!initWX(type, context)) return;
		
		WXEntryActivity.actionWXEntryActivity(context, type, bitmapUrl, shareUrl, title, des, WeChatConstants.WXSceneSession, null);
	}
	
	public void shareToWXSession(String title, String des, String shareUrl, String bitmapUrl, int type, Context context, String invokeUrl){
		
		if(!initWX(type, context)) return;
		
		WXEntryActivity.actionWXEntryActivity(context, type, bitmapUrl, shareUrl, title, des, WeChatConstants.WXSceneSession, invokeUrl);
	}
	
	private boolean initWX(int type, Context context) {
		if (!NetworkUtil.isNetworkAvailable(MyApplication.getAppContext())) {// ���粻ͨ
			Toast.makeText(MyApplication.getAppContext(),"���粻ͨ",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(!wxapi.isWXAppInstalled()){
			Toast.makeText(MyApplication.getAppContext(), "δ��װ΢��", Toast.LENGTH_SHORT).show();
			Intent intent= new Intent(Intent.ACTION_VIEW);        
		    Uri content_url = Uri.parse("http://weixin.qq.com");   
		    intent.setData(content_url);  
		    try {
				context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		if(type == WeChatConstants.WXSceneTimeline){
			if(wxapi.getWXAppSupportAPI() < TIMELINE_SUPPORTED_VERSION){
				Toast.makeText(MyApplication.getAppContext(), "�汾̫�Ͳ�֧�ַ�������Ȧ��", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		return true;
	}

	public boolean isWXAppInstalled(){
		return wxapi.isWXAppInstalled();
	}
	
}
