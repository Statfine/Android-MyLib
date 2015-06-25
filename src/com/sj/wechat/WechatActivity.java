package com.sj.wechat;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sj.mylib.BaseActivity;
import com.sj.mylib.R;
import com.sj.mylib.wxapi.WeixinManager;

public class WechatActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_main);
		
		initLayout();
	}
	
	public void initLayout(){
		
		this.findViewById(R.id.wechat_login).setOnClickListener((OnClickListener) this);
		this.findViewById(R.id.wechat_share_frends).setOnClickListener((OnClickListener) this);
		this.findViewById(R.id.wechat_share_qzone).setOnClickListener((OnClickListener) this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wechat_login:
			
			break;
		case R.id.wechat_share_frends:
			WeixinManager.getInstance().shareToWXTimeline("title", "des", "shareUrl", "http://g.hiphotos.baidu.com/image/pic/item/203fb80e7bec54e730a45785bb389b504ec26ae4.jpg", 1, this);
			break;
		case R.id.wechat_share_qzone:
			WeixinManager.getInstance().shareToWXSession("title", "des", "shareUrl", "http://g.hiphotos.baidu.com/image/pic/item/203fb80e7bec54e730a45785bb389b504ec26ae4.jpg", 1, this);
			break;
		default:
			break;
		}
	}
	
}
