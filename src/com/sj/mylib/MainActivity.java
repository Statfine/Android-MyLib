package com.sj.mylib;

import com.sj.qq.QQActivity;
import com.sj.volley.VolleyActivity;
import com.sj.wechat.WechatActivity;
import com.umeng.update.UmengUpdateAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * @author SJ
 * 
 * ��������Ե�������
 * 
 * QQ
 * ΢�ţ� eclipseӦ��ǩ����������ڲ�ͬeclipse��������Ҫ����key
 * ����
 * 
 */
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UmengUpdateAgent.update(this);

		initLayout();
	}

	public void initLayout(){
		this.findViewById(R.id.qq_jump).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, QQActivity.class));
			}
		});
		
		this.findViewById(R.id.wechat_jump).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, WechatActivity.class));
			}
		});
		
		this.findViewById(R.id.volley_jump).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, VolleyActivity.class));
			}
		});

	}


}
