package com.example.custom;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.example.common.KLog;

/**
 * 可用于调试接口
 * adb shell settings put system mySettings 1
 */
public class SettingsContentObserver extends ContentObserver {
    private Handler mHandler;
    private Context mContext;

    public SettingsContentObserver(Handler handler, Context context) {
        super(handler);
        mHandler = handler;
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        KLog.i("wtx", "onChange, selfChange:" + selfChange);
        Uri mySettingsUri = Settings.System.getUriFor(CustomApplication.SettingsContentObserver_VALUE);
        try {
            int SettingsContentObserver_VALUE = Settings.System.getInt(
                    mContext.getContentResolver(),
                    CustomApplication.SettingsContentObserver_VALUE
            );
            KLog.i("wtx", "onChange, mySettingsUri:" + mySettingsUri);
            KLog.i("wtx", "onChange, SettingsContentObserver_VALUE:" + SettingsContentObserver_VALUE);
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
