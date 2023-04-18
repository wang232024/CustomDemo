package com.example.customutil;

import android.app.Application;

import com.example.customutil.util.Logger;

public class CrashApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// 清单文件AndroidManifest.xml中需要添加Application的name属性
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		Logger.init(this);
	}
}
