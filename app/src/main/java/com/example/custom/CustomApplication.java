package com.example.custom;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.provider.Settings;

public class CustomApplication extends Application {
    public static final String SettingsContentObserver_VALUE = "mySettings";
    // 是否开启StrictMode严苛模式
    private static final boolean DEVELOPER_MODE = false;

    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
            );
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
//                .penaltyDeath()   // 一旦StrictMode消息被写到LogCat后应用就会崩溃?
                .build()
            );
        }
        /**
        开启StrictMode需要进行两方面的设置：ThreadPolicy和VmPolicy。
        两种策略中以“detect”开头命名的方法代表需要检测的问题，以“penalty”开头命名地 方法代表探测到问题后的处理方式。
        ThreadPolicy为线程方面使用的策略，包括磁盘读写检测，网络访问检测等。
        VmPolicy为VM相关的策略，用于检测内存泄露，未释放的对象等。
         */

        super.onCreate();

        // 注册监听监听特定的Settings URI (在合适的时候注销监听)
        Uri mySettingsUri = Settings.System.getUriFor(SettingsContentObserver_VALUE);
        // 获取ContentResolver
        ContentResolver contentResolver = getContentResolver();

        // 创建Handler并与主线程绑定
        HandlerThread handlerThread = new HandlerThread("SettingsContentObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        // 创建ContentObserver实例并注册
        SettingsContentObserver settingsContentObserver = new SettingsContentObserver(handler, this);
        contentResolver.registerContentObserver(mySettingsUri, false, settingsContentObserver);
    }
}
