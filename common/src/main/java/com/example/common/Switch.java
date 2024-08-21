package com.example.common;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.launcher3.util.SQLiteCacheHelper;

/**
 * 设置/获取开关变量，用于adb控制等场景
 */
public class Switch {
    public static final String TAG = Switch.class.getName();

    public static void test(Context context) {
        /**
         * 1.system：包含各种各样的用户偏好系统设置；
         * adb shell settings put system mySettingsSystem 1
         *
         * 2.global：所有的偏好设置对系统的所有用户公开，第三方APP有读没有写的权限；
         * adb shell settings put global mySettingsGlobal 2
         *
         * 3.secure：安全性的用户偏好系统设置，第三方APP有读没有写的权限。
         * adb shell settings put secure mySettingsSecure 3
         */
        int mySettingsSystem = Settings.System.getInt(context.getContentResolver(), "mySettingsSystem", 0);
        int mySettingsGlobal = Settings.Global.getInt(context.getContentResolver(), "mySettingsGlobal", 0);
        int mySettingsSecure = Settings.Secure.getInt(context.getContentResolver(), "mySettingsSecure", 0);

        // adb shell settings put system mySettingsSystem 7; adb shell settings put global mySettingsGlobal 8; adb shell settings put secure mySettingsSecure 9
        Log.i(TAG, "mySettingsSystem:" + mySettingsSystem);
        Log.i(TAG, "mySettingsGlobal:" + mySettingsGlobal);
        Log.i(TAG, "mySettingsSecure:" + mySettingsSecure);

        // 子模块依赖aar文件 local_lib_aar
        SQLiteCacheHelper sqLiteCacheHelper;
    }

}
