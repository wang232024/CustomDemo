package com.example.customutil.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.customutil.download.breakpoint.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import com.example.customutil.download.breakpoint.MulThreadBreakpointManager;
import com.example.customutil.download.multhread.MulThreadDownload;
import com.example.common.util.FileUtil;

import com.example.customutil.R;

public class DownloadActivity extends Activity {
    private static final String TAG = "wtx_DownloadActivity";
    private Context mContext = DownloadActivity.this;
    private boolean downloadServiceRegistered = false;
    private TextView tv;
    private ProgressBar progressbar;
    private TextView progress_tv;

    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);

        initPermission(DownloadActivity.this, mPermissions);

        tv = findViewById(R.id.tv);
        progress_tv = findViewById(R.id.progress_tv);
        progressbar = findViewById(R.id.progressbar);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2();
            }
        });

        Intent intent = new Intent(mContext, DownLoadService.class);
        downloadServiceRegistered = bindService(intent, downloadServiceConnection, Service.BIND_AUTO_CREATE);

        mDatabaseManager = new DatabaseManager(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadServiceRegistered) {
            unbindService(downloadServiceConnection);
            downloadServiceRegistered = false;
        }
    }

    private ServiceConnection downloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w(TAG, "downloadServiceConnection onServiceConnected.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w(TAG, "downloadServiceConnection onServiceDisconnected.");
        }
    };

    private void btn1() {
        breakpointTestStart();
    }

    private void btn2() {
        breakpointTestStop();
    }

    private void mulThreadDownloadTest() {
        MulThreadDownload mulThreadDownload = new MulThreadDownload(null);
        mulThreadDownload.mulThreadDownloadTest();
    }

    private MulThreadBreakpointManager mulThreadBreakpointManager;

    private void breakpointTestStop() {
        mulThreadBreakpointManager.stopDownload();
    }

    private void breakpointTestStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.safeMakeDir("/sdcard/download");
                mulThreadBreakpointManager = new MulThreadBreakpointManager(mContext, "http://192.168.1.84:8080/wtx/wtx.exe", "/sdcard/download", 2);
                mulThreadBreakpointManager.setOnDownloadListener(new MulThreadBreakpointManager.OnDownloadListener() {
                    @Override
                    public void onStart(final long fileLength, final long downloadLength) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setMax((int) fileLength);
                                progressbar.setProgress((int) downloadLength);
                            }
                        });
                    }

                    @Override
                    public void onProcess(final long length) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setProgress((int) length);
                            }
                        });
                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onError(int errorCode) {

                    }

                    @Override
                    public void onFailed(int threadId, int errorCode) {

                    }
                });
                mulThreadBreakpointManager.startDownload();
            }
        }).start();
    }

    private void fileTest() {
        boolean res = false;

//        res = FileUtil.deleteDir("/sdcard", true, true, true);
        long info = System.currentTimeMillis();

        res = FileUtil.appendFile("/sdcard/1/2/1.txt", "------------------------------------" + info + "\n", true);
        for (int i = 0; i < 1000; i++) {
            res = FileUtil.appendFile("/sdcard/1/2/1.txt", "i:" + i + ", info:" + info + "\n", true);
        }

        Log.e(TAG, "res:" + res);
    }

    private void showTv(final String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String pre = tv.getText().toString();
                tv.setText(pre + "\n" + info);
            }
        });
    }

    public static void initPermission(Activity activity, String[] permissions) {
        List<String> toApplyList = new ArrayList<String>();
        // 将没有获取的权限加入ArrayList中,动态请求.
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
                Log.e("ManifestUtil", "perm: " + perm);
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), 123);
        }
    }

    private static final String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
    };

}
