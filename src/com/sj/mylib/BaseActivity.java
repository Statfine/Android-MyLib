package com.sj.mylib;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	public static List<Activity> baseList = new ArrayList<Activity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MobclickAgent.setDebugMode(true);
		MobclickAgent.updateOnlineConfig(this);
		baseList.add(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		baseList.remove(this);
	}
	
	public static void close() {
		for (Activity activity : baseList) {
			activity.finish();
			activity = null;
		}
		baseList.clear();
	}
	
}
