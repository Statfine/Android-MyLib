package com.sj.qq;

import java.util.ArrayList;

import com.sj.mylib.BaseActivity;
import com.sj.mylib.R;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class QQActivity extends BaseActivity implements OnClickListener{

	private static final String SHARE_TO_QZONE_TYPE_IMAGE_TEXT = null;

	//QQ
	private Tencent mTencent;
		
	String mAppid = "1104596493";
	
	private boolean loginFlag = false;
	
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qq_main);
		
		mTencent = Tencent.createInstance(mAppid,this);
		
		initLayout();
	}

	public void initLayout(){
		
		loginButton = (Button)this.findViewById(R.id.qq_login);
		this.findViewById(R.id.qq_share_frends).setOnClickListener((OnClickListener) this);
		this.findViewById(R.id.qq_share_qzone).setOnClickListener((OnClickListener) this);
		
		loginButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qq_login:
			if(loginFlag){
				loginOut();
			}else{
				doLogin();
			}
			break;
		case R.id.qq_share_frends:
			shareTencent();
			break;
		case R.id.qq_share_qzone:
			shareToQzoneTencent();
			break;

		default:
			break;
		}
	}
	
	
	private void doLogin() {
	 	mTencent.login(this, "all", new BaseTencentUiListener());
	}
	private void loginOut(){
		mTencent.logout(this);
		loginButton.setText("登陆");
		Toast.makeText(QQActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
	}
	private class BaseTencentUiListener implements IUiListener {

		@Override
		public void onCancel() {
			System.out.println("onCancel");
		}

		@Override
		public void onComplete(Object arg0) {
			loginFlag = true;
			loginButton.setText("注销");
			Toast.makeText(QQActivity.this, "登陆成功："+arg0.toString(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			System.out.println("onError");	
		}

	}
	
	protected void shareTencent() {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE,"title");
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "description");
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://douban.fm/?start=8508g3c27g-3&cid=-3");
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "sj_Lib");
		String pic = "http://g.hiphotos.baidu.com/image/pic/item/203fb80e7bec54e730a45785bb389b504ec26ae4.jpg";
		Log.i("test", "pic " + pic);
		if (pic != null) {
			ArrayList<String> imageUrls = new ArrayList<String>();
			imageUrls.add(pic);
			params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		}
		mTencent.shareToQQ(this, params, new IBaseUiListener());
	}
	
	protected void shareToQzoneTencent() {
		final Bundle params = new Bundle();
		params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
	    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
	    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
	    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://douban.fm/?start=8508g3c27g-3&cid=-3");//必填
	    String pic = "http://g.hiphotos.baidu.com/image/pic/item/203fb80e7bec54e730a45785bb389b504ec26ae4.jpg";
		Log.i("test", "pic " + pic);
		if (pic != null) {
			ArrayList<String> imageUrls = new ArrayList<String>();
			imageUrls.add(pic);
			params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		}
	    mTencent.shareToQzone(this, params, new IBaseUiListener());
	}
	
	private class IBaseUiListener implements IUiListener {

		@Override
		public void onCancel() {
			Toast.makeText(QQActivity.this, "errcode_cancel", Toast.LENGTH_SHORT).show();
		}


		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			Toast.makeText(QQActivity.this, "onError", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(QQActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
		}
	}
	
}
