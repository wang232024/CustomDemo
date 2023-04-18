package com.example.customutil.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DownLoadService extends Service {
    private static final String TAG = "wtx_DownloadService";
    private DownloadBind downloadBind = new DownloadBind();

    public class DownloadBind extends Binder {
        public DownLoadService getService() {
            return DownLoadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "DownLoadService onBind.");

        return downloadBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "DownLoadService onUnbind.");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "DownLoadService onCreate.");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DownLoadService onDestroy.");
    }

}
