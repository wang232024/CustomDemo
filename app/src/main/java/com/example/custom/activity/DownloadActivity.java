package com.example.custom.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;
import com.example.custom.download.DownloadContant;
import com.example.custom.download.breakpoint.MulThreadBreakpointManager;
import com.example.custom.download.multhread.MulThreadDownloadManager;
import com.example.util.FileUtil;
import com.example.util.Logger;

import java.io.File;
import java.util.Arrays;

public class DownloadActivity extends AppCompatActivity {
    private static final String TAG = "wtx_DownloadActivity";
    private Context mContext;
    private long mDownloadId;   // 下载ID
    private DownloadManager mDownloadManager;
    private final DownloadManagerReceiver mDownloadManagerReceiver = new DownloadManagerReceiver();
    private MulThreadDownloadManager mulThreadDownloadManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download_activity);

        mContext = this;

        Button btn1 = findViewById(R.id.download1);
        Button btn2 = findViewById(R.id.download2);
        Button btn3 = findViewById(R.id.download3);
        Button btn4 = findViewById(R.id.download4);
        Button btn5 = findViewById(R.id.download5);
        Button btn_insert = findViewById(R.id.btn_insert);
        Button btn_delete = findViewById(R.id.btn_delete);
        Button btn_update = findViewById(R.id.btn_update);
        Button btn_query = findViewById(R.id.btn_query);
        btn1.setOnClickListener(mOnClickListener);
        btn2.setOnClickListener(mOnClickListener);
        btn3.setOnClickListener(mOnClickListener);
        btn4.setOnClickListener(mOnClickListener);
        btn5.setOnClickListener(mOnClickListener);
        btn_insert.setOnClickListener(mOnClickListener);
        btn_delete.setOnClickListener(mOnClickListener);
        btn_update.setOnClickListener(mOnClickListener);
        btn_query.setOnClickListener(mOnClickListener);

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        mulThreadDownloadManager = new MulThreadDownloadManager(mulThreadDownloadListener);

        // 测试数据库用
        mulThreadBreakpointManager = new MulThreadBreakpointManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mDownloadManagerReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mDownloadManagerReceiver);
    }

    private class DownloadManagerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
                Log.i(TAG, "用户点击了通知");
                // 点击下载进度通知时, 对应的下载ID以数组的方式传递
                long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                Log.i(TAG, "ids: " + Arrays.toString(ids));
            } else if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.i(TAG, "下载完成");
                /*
                 * 获取下载完成对应的下载ID, 这里下载完成指的不是下载成功, 下载失败也算是下载完成,
                 * 所以接收到下载完成广播后, 还需要根据 id 手动查询对应下载请求的成功与失败.
                 */
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                Log.i(TAG, "id: " + id);
                // 根据获取到的ID，使用上面第3步的方法查询是否下载成功

                Log.i(TAG, "mDownloadId:" + mDownloadId);
                // 创建一个查询对象
                DownloadManager.Query query = new DownloadManager.Query();
                // 根据 下载ID 过滤结果
                query.setFilterById(mDownloadId);
                // 还可以根据状态过滤结果
                // query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
                // 执行查询, 返回一个 Cursor (相当于查询数据库)
                Cursor cursor = mDownloadManager.query(query);
                Logger.logCursor(TAG, cursor);
                cursor.close();

                Uri uri = mDownloadManager.getUriForDownloadedFile(id);
                Log.i(TAG, "uri:" + uri);   // content://downloads/all_downloads/16     // 下载所用uri
            }
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.download1:
                    try {
                        /**
                         * 系统浏览器下载，下载位置/sdcard/Download
                         */
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(DownloadContant.DOWNLOAD_SRC[0]));
                        startActivity(intent);
                    } catch (Exception e) {
                        // No Activity found to handle Intent { act=android.intent.action.VIEW cat=[android.intent.category.BROWSABLE] }
                        Log.e(TAG, e.toString());
                        e.printStackTrace();
                    }
                    break;
                case R.id.download2:
                    // DownloadManager下载
                    download("/sdcard/Download/DownloadManager下载.jpeg");
                    break;
                case R.id.download3:
                    try {
                        Log.i(TAG, "多线程下载");
                        // 多线程下载
                        File mulThreadDownloadFile = new File("/sdcard/Download/MulThreadDownloadManager.jpeg");
                        mulThreadDownloadManager.download(
                                DownloadContant.DOWNLOAD_SRC[1],
                                8,
                                mulThreadDownloadFile
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Exception:" + e);
                    }
                    break;
                case R.id.download4:
                    breakpointTestStart();
                    break;
                case R.id.download5:
                    breakpointTestStop();
                    break;
                case R.id.btn_insert:
                    mulThreadBreakpointManager.breakpointInsert();
                    break;
                case R.id.btn_delete:
                    mulThreadBreakpointManager.breakpointDelete();
                    break;
                case R.id.btn_update:
                    mulThreadBreakpointManager.breakpointUpdate();
                    break;
                case R.id.btn_query:
                    mulThreadBreakpointManager.breakpointQuery();
                    break;
            }
        }
    };

    // 封装下载请求（Request），加入下载队列
    private void download(String downloadPath) {
        /*
         * 1. 封装下载请求
         */
        // http 下载链接（该链接为 CSDN APP 的下载链接，仅做参考）
        // 创建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadContant.DOWNLOAD_SRC[0]));
        /*
         * 设置在通知栏是否显示下载通知(下载进度), 有 3 个值可选:
         *    VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
         *    VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
         *    VISIBILITY_HIDDEN:                    始终不显示通知
         */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 设置通知的标题和描述
        request.setTitle("通知标题XXX");
        request.setDescription("对于该请求文件的描述");
        /*
         * 设置允许使用的网络类型, 可选值:
         *     NETWORK_MOBILE:      移动网络
         *     NETWORK_WIFI:        WIFI网络
         *     NETWORK_BLUETOOTH:   蓝牙网络
         * 默认为所有网络都允许
         */
        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 添加请求头
        // request.addRequestHeader("User-Agent", "Chrome Mozilla/5.0");
        // 设置下载文件的保存位置
        File saveFile = new File(downloadPath);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        request.setDestinationUri(Uri.fromFile(saveFile));

        /*
         * 2. 添加下载任务
         */
        // 将下载请求加入下载队列, 返回一个下载ID
        mDownloadId = mDownloadManager.enqueue(request);
        // 如果中途想取消下载, 可以调用remove方法, 根据返回的下载ID取消下载, 取消下载后下载保存的文件将被删除
        // manager.remove(downloadId);
    }

    private MulThreadDownloadManager.MulThreadDownloadManagerListener mulThreadDownloadListener = new MulThreadDownloadManager.MulThreadDownloadManagerListener() {
        @Override
        public void onStart(long length) {
            Log.w(TAG, "onStart, length:" + length);
        }

        @Override
        public void onProcess(long len, long length) {
            Log.d(TAG, "onProcess, len:" + len + ", length:" + length);
        }

        @Override
        public void onFinish(long sum, int responseCode, long timeWaste) {
            Log.w(TAG, "onFinish, sum:" + sum + ", responseCode:" + responseCode + ", timeWaste:" + timeWaste);
        }

        @Override
        public void onFailed(int responseCode) {
            Log.e(TAG, "onFailed, responseCode:" + responseCode);
        }
    };

    private MulThreadBreakpointManager mulThreadBreakpointManager;

    private void breakpointTestStop() {
        mulThreadBreakpointManager.stopDownload();
    }

    private void breakpointTestStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File("/sdcard/Download/qmui.apk");
                mulThreadBreakpointManager = new MulThreadBreakpointManager(
                        DownloadActivity.this,
                        DownloadContant.DOWNLOAD_SRC[2],
                        "",
                        file,
                        5
                );
                mulThreadBreakpointManager.setOnDownloadListener(new MulThreadBreakpointManager.OnDownloadListener() {
                    @Override
                    public void onStart(final long fileLength, final long downloadLength) {

                    }

                    @Override
                    public void onProcess(final long length) {

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

}
