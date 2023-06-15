package com.example.customutil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.example.util.AssetsUtil;
import com.example.util.ManifestUtil;
import com.example.util.ShareUtil;
import com.example.util.UriUtil;
import com.example.util.Logger;
import com.example.util.ZipUtil;
import java.util.ArrayList;
import java.util.List;

public class UtilActivity extends Activity {
    private static final String TAG = "wtx_MainActivity";
    private Context mContext = UtilActivity.this;
    private BroadcastReceiver mBroadcastReceiver = null;
    private EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);

        initView();
        initData();
        Log.w(TAG, "+++onCreate+++");

        ManifestUtil.initPermission(this);

        AssetsUtil.test(this);

//        ReflectUtils reflectUtils = new ReflectUtils();
//        reflectUtils.test();

//        RegularUtil.test();

        Logger.test(this);

        ZipUtil.test(this, 2);
        ZipUtil.test(this, 0);
        ZipUtil.test(this, 1);
    }

    private void initView() {
        // 保持屏幕常亮，或者布局文件中顶层添加android:keepScreenOn="true"
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button btn_share = findViewById(R.id.btn_share);
        Button btn_test = findViewById(R.id.btn_test);
        Button btn_feed = findViewById(R.id.btn_feed);
        mEtContent = findViewById(R.id.et_content);

        btn_share.setOnClickListener(mOnClickListener);
        btn_test.setOnClickListener(mOnClickListener);
        btn_feed.setOnClickListener(mOnClickListener);
    }

    private void initData() {
        if (null == mBroadcastReceiver) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.w(TAG, "onReceive, action:" + action);
                    switch (action) {
                        case Intent.ACTION_SHUTDOWN:

                            break;
                        case Intent.ACTION_REBOOT:

                            break;
                        case Intent.ACTION_SCREEN_OFF:

                            break;
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SHUTDOWN);      // 关机
            intentFilter.addAction(Intent.ACTION_REBOOT);        // 重启
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);    // 熄屏

            // 注册监听广播
            registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    private int mIndex;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (R.id.btn_share == v.getId()) {
                ShareUtil.test(mContext);
            } else if (R.id.btn_test == v.getId()) {

            } else if (R.id.btn_feed == v.getId()) {

            }
        }
    };

    private void test1() {
        List<String> list = new ArrayList<>();
        list.add(mContext.getFilesDir() + "/test.mp4");
        list.add(mContext.getFilesDir() + "/test1.mp4");
        ShareUtil.shareToApp(mContext, list);
    }

    private void test2() {
        // 打开文件管理器
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");   // 文本
//                intent.setType("video/*");      // 视频
//                intent.setType("audio/*");      // 音频
//                intent.setType("image/*");      // 图片
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 打开多个文件
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ClipData CD = data.getClipData();
            if (CD != null) {
                int count = CD.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = CD.getItemAt(i).getUri();
                    Log.i(TAG, "onActivityResult, multiple, url:" + uri.toString());
                    String string = UriUtil.getFileAbsolutePath(mContext, uri);
                    Log.i(TAG, "onActivityResult, multiple, string:" + string);
                }
            } else {
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程
                Log.i(TAG, "onActivityResult, single, url:" + uri.toString());
                String string = UriUtil.getFileAbsolutePath(mContext, uri);
                Log.i(TAG, "onActivityResult, single, string:" + string);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "---onDestroy---");

        // 注销监听广播
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "+++onStart+++");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "+++onResume+++");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "---onPause---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(TAG, "---onRestart---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "---onStop---");
    }

}