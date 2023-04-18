package com.example.custom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 静态广播, AndroidManifest.xml清单文件中进行声明
 * 开机广播，需配置android:sharedUserId="android.uid.system"才能接收到，即系统应用(拥有系统签名文件)才有权限接收
 */
public class CustomReceiver extends BroadcastReceiver {
    private static final String TAG = "wtx_CustomReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceive, action:" + action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.w(TAG, "ACTION_BOOT_COMPLETED");
        }
    }
}

/**
 * 动态广播注意点
 * 在onResume()注册、onPause()注销是因为onPause()在App死亡前一定会被执行，从而保证广播在App死亡前一定会被注销，从而防止内存泄露。
 * 1. 不在onCreate() & onDestory() 或 onStart() & onStop()注册、注销是因为：
 *      当系统因为内存不足要回收Activity占用的资源时，Activity在执行完onPause()方法后就会被销毁，有些生命周期方法onStop()，onDestory()就不会执行。
 *      当再回到此Activity时，是从onCreate方法开始执行。
 * 2. 假设我们将广播的注销放在onStop()，onDestory()方法里的话，有可能在Activity被销毁后还未执行onStop()，onDestory()方法，即广播仍还未注销，从而导致内存泄露。
 * 3. 但是，onPause()一定会被执行，从而保证了广播在App死亡前一定会被注销，从而防止内存泄露。
 */
