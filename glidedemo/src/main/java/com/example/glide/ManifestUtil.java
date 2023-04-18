package com.example.glide;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ManifestUtil {
    private static final String TAG = "ManifestUtils";
    public static final int CODE_REQUEST_PERMISSION = 0x9001;

    public static void initPermission(Activity activity) {
        initPermission(activity, mPermissions);
    }

    /**
     * android 6.0 以上需要动态申请权限
     * permissions: 需要请求的所有权限
     * onRequestPermissionsResult: android 6.0以上动态授权的回调,在Activity中获取
     * 所有授权完成之后才会回调。
     * 在android 6.0及以后，WRITE_SETTINGS权限的保护等级已经由原来的dangerous升级为signature，
     * 这意味着我们的APP需要用系统签名或者成为系统预装软件才能够申请此权限，
     * 并且还需要提示用户跳转到修改系统的设置界面去授予此权限，动态获取不能成功。
     */
    public static void initPermission(Activity activity, String[] permissions) {
        // 如果当前版本号 >= 23(即android6.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> toApplyList = new ArrayList<>();
            // 将没有获取的权限加入ArrayList中,动态请求.
            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                    toApplyList.add(perm);
                    //进入到这里代表没有权限，需要用户手动进行授权。
                    Log.e(TAG, "perm: " + perm);
                }
            }
            String[] tmpList = new String[toApplyList.size()];
            if (!toApplyList.isEmpty()) {
                ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), CODE_REQUEST_PERMISSION);
            }
        }
    }

    private static final String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
    };

}