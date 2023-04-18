package com.example.custom.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.custom.R;
import com.example.custom.activity.IntentActivity;

public class CustomService extends IntentService {
    private Context mContext = CustomService.this;
//    private static final String TAG = "WTX_CustomService";
    private static final String TAG = "wtx";
    private static final String NAME = "CustomService";
    private static final String FOREGROUND_SERVICE_CHANNEL_ID = "FOREGROUND_SERVICE_CHANNEL_ID";

    public CustomService() {
        super("CustomService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CustomService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.w(TAG, "onBind");
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.w(TAG, "onHandleIntent, intent:" + intent);

        startForeground(1, getNotification(IntentActivity.class));
        // android.app.RemoteServiceException$ForegroundServiceDidNotStartInTimeException: Context.startForegroundService() did not then call Service.startForeground(): ServiceRecord

        try {
            Log.i(TAG, "---------0");
            Thread.sleep(3000);
            Log.i(TAG, "---------1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Notification getNotification(Class<?> cls) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String ID = getPackageName();       //这里的id里面输入自己的项目的包的路径
        Intent intent = new Intent(mContext, cls);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        NotificationCompat.Builder builder;     //创建服务对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ID, NAME, manager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(mContext, FOREGROUND_SERVICE_CHANNEL_ID);
            builder.setChannelId(ID);
        } else {
            builder = new NotificationCompat.Builder(mContext, FOREGROUND_SERVICE_CHANNEL_ID);
        }
        builder.setContentTitle("title")
                .setContentText("content")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();

        return builder.build();
    }

}
