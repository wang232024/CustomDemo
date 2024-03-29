package com.example.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * isPkgInstalled查询不到已安装的包：
 *  方法1.清单中添加权限(查询所有)
 *      <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
 *         tools:ignore="android.permission.QueryAllPackagesPermission" />
 *  方法2.清单中添加权限(指定包名)
 *      <queries>
 *          <package android:name="com.tpcdroid.retail" />
 *      </queries>
 */
public class PkgUtil {
    private static final String TAG = "PkgUtil";

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName   包名
     * @return
     */
    public static boolean isPkgInstalled(Context context, String packageName) {
        long time1 = System.currentTimeMillis();
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
//        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(
//            PackageManager.GET_ACTIVITIES |
//                    PackageManager.GET_SERVICES |
//                    PackageManager.GET_RECEIVERS |
//                    PackageManager.GET_PROVIDERS
//        );
        List<String> packageNames = new ArrayList<String>();

        if (packageInfos != null) {
            Log.d(TAG, "packageInfos.size():" + packageInfos.size());
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
                Log.d(TAG, "packName:" + packName);
            }
        }
        long time2 = System.currentTimeMillis();
        Log.d(TAG, "waste:" + (time2 - time1));

        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

}
