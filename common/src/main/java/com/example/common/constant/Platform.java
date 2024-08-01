package com.example.common.constant;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

public class Platform {

    public static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getAppVersion(Context context) {
        String versionName = "";
        try {
            //获取PackageManager实例
            PackageManager packageManager = context.getPackageManager();
            //getPackageName()是当前类的包名，0表示获取版本信息
            PackageInfo packeInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionName = packeInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取产品Model
     * @return
     */
    public static String getSku() {
        return Build.MODEL;
    }

    /**
     * 获取系统固件版本号
     * @return
     */
    public static String getFirmwareVersion() {
        return Build.DISPLAY;
    }

}
