package com.sj.mylib.wxapi;



import java.io.ByteArrayOutputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sj.mylib.R;
import com.sj.util.Util;
import com.sj.util.WeChatConstants;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXEntryActivity";

	private IWXAPI wxAPI;
	private static final int THUMB_SIZE = 150;
	private AsyncTask<String, Long, Boolean> task;
	private int shareTpye;
	private int wxType; // 微信分享类型 朋友圈；微信朋友
	private String title;
	private String des;
	private String shareUrl;
	private String invokeUrl;
	private String bitmapUrl;
	private static Bitmap mBitmap;
//	private Handler mHandler = new Handler();

	public static void actionWXEntryActivity(Context context, int type,
			Bitmap bitmap, String shareUrl, String title, String des,
			int wxscenetimeline, String invokeUrl) {
		Intent intent = new Intent(context, WXEntryActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("shareUrl", shareUrl);
		intent.putExtra("title", title);
		intent.putExtra("des", des);
		intent.putExtra("WXType", wxscenetimeline);
		mBitmap = bitmap;
		intent.putExtra("invokeUrl", invokeUrl);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "失败",Toast.LENGTH_SHORT).show();
		}
	}

	public static void actionWXEntryActivity(Context context, int type,
			String bitmapUrl, String shareUrl, String title, String des,
			int wxscenetimeline, String invokeUrl) {
		Intent intent = new Intent(context, WXEntryActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("shareUrl", shareUrl);
		intent.putExtra("title", title);
		intent.putExtra("des", des);
		intent.putExtra("bitmapUrl", bitmapUrl);
		intent.putExtra("WXType", wxscenetimeline);
		intent.putExtra("invokeUrl", invokeUrl);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "失败",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		wxAPI.handleIntent(getIntent(), this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		wxAPI = WXAPIFactory.createWXAPI(this, WeChatConstants.APP_ID);
		// 将该app注册到微信
		wxAPI.registerApp(WeChatConstants.APP_ID);
		wxAPI.handleIntent(getIntent(), this);

		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
		// 初始化数据
		initDatas();
		
		if(TextUtils.isEmpty(title) && TextUtils.isEmpty(des) && TextUtils.isEmpty(shareUrl)){
			return;
		}
		
		task = new ShareWXTask(wxType);
		task.execute("");

		/*mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		}, 6000);*/

	}

	private void initDatas() {
		wxType = getIntent().getIntExtra("WXType",
				WeChatConstants.WXSceneTimeline);
		title = getIntent().getStringExtra("title");
		des = getIntent().getStringExtra("des");
		shareUrl = getIntent().getStringExtra("shareUrl");
		bitmapUrl = getIntent().getStringExtra("bitmapUrl");
		shareTpye = getIntent().getIntExtra("type",
				WeChatConstants.WEBPAGE_WX_TYPE);
		invokeUrl = getIntent().getStringExtra("invokeUrl");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
//		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
		}
		mBitmap = null;
		task = null;
		super.onDestroy();
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
		Log.d(TAG, "onReq.....");
		finish();
	}
	
	private void goToGetMsg() {
		Log.d(TAG, "goToGetMsg");
		try {
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(launchIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * hiho://share?from=iPhone&guid=3370c6f2-5f31-11e3-b6ea-00a0d1eaf061&st=121.183&et=132.783
	 * @param showReq
	 */
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		Log.d(TAG, "goToShowMsg");
		
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		
		Log.d(TAG, msg.toString());
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onResp");
		String txt = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			txt = "成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			txt = "取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			break;
		}

		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
		finish();
	}

	private class ShareWXTask extends AsyncTask<String, Long, Boolean> {
		private int type;
		ProgressDialog progress;

		public ShareWXTask(int mtype) {
			type = mtype;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(WXEntryActivity.this);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setMessage("请稍后...");
			progress.setCancelable(true);
			progress.setIndeterminate(true);
			progress.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;

			Log.d("WX", "doInBackground:" + shareUrl);
			WXMediaMessage msg = new WXMediaMessage();
			switch (shareTpye) {
			case WeChatConstants.IMAGE_WX_TYPE:
				WXImageObject imageObject = new WXImageObject();
				imageObject.imageUrl = shareUrl;
				msg.mediaObject = imageObject;
				break;
			case WeChatConstants.MUSIC_WX_TYPE:
				WXMusicObject musicObject = new WXMusicObject();
				musicObject.musicUrl = shareUrl;
				msg.mediaObject = musicObject;
				break;
			case WeChatConstants.VIDEO_WX_TYPE:
				WXVideoObject videoObject = new WXVideoObject();
				videoObject.videoUrl = shareUrl;
				msg.mediaObject = videoObject;
				/*WXAppExtendObject appdata = new WXAppExtendObject();
				appdata.extInfo = title;
				appdata.filePath = shareUrl;
				appdata.fileData = shareUrl.getBytes();
				if(type == WeiXinConstants.WXSceneTimeline){
					msg.mediaObject = videoObject;
				}else {
					msg.mediaObject = appdata;
				}*/
				
				break;
			case WeChatConstants.WEBPAGE_WX_TYPE:
				WXWebpageObject webpageObject = new WXWebpageObject();
				webpageObject.webpageUrl = shareUrl;
				msg.mediaObject = webpageObject;
				break;
			default:
				break;
			}

			msg.title = title;
			msg.description = des;
			Bitmap thumbBmp = null;
			
			try {
				if (mBitmap != null) {
					thumbBmp = Bitmap.createScaledBitmap(mBitmap, THUMB_SIZE,
							THUMB_SIZE, true);
				} else if (bitmapUrl != null) {
					Log.i(TAG, "bitmapUrl:" + bitmapUrl);
					Bitmap bmp = BitmapFactory.decodeStream(new URL(bitmapUrl).openStream());
					thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
					bmp.recycle();
					
				} else {
					thumbBmp = Bitmap.createScaledBitmap(BitmapFactory
							.decodeResource(WXEntryActivity.this.getResources(), R.drawable.ic_launcher),
							THUMB_SIZE, THUMB_SIZE, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				thumbBmp = Bitmap.createScaledBitmap(BitmapFactory
						.decodeResource(WXEntryActivity.this.getResources(), R.drawable.ic_launcher),
						THUMB_SIZE, THUMB_SIZE, true);
			}
			

			msg.setThumbImage(thumbBmp);
			//msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("webpage");
			req.message = msg;
			req.scene = type == WeChatConstants.WXSceneTimeline ? SendMessageToWX.Req.WXSceneTimeline
					: SendMessageToWX.Req.WXSceneSession;
			result = wxAPI.sendReq(req);

			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				if(!isFinishing() && progress != null && progress.isShowing()){
					progress.dismiss();
				}
				if (!result) {
					Toast.makeText(WXEntryActivity.this, "分享失败",
							Toast.LENGTH_SHORT).show();
					finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			finish();
			break;
		}

		return super.onTouchEvent(event);
	}
}