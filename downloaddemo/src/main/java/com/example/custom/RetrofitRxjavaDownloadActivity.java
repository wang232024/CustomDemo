package com.example.custom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.download.DownloadInfo;
import com.example.custom.download.DownloadManager;
import com.example.custom.utils.KLog;


public class RetrofitRxjavaDownloadActivity extends AppCompatActivity {
    private static final String TAG = RetrofitRxjavaDownloadActivity.class.getName();
    private ProgressBar pb_progress;
    private TextView tv_progress;
    private DownloadManager downloadManager;
    private int i = 0;
    private Button btn_start;
    private Button btn_stop;
    private Button btn_pasuse;
    private Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ManifestUtil.initPermission(this);

        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        tv_progress = (TextView) findViewById(R.id.tv_progress);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_pasuse = (Button) findViewById(R.id.btn_pause);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_start.setOnClickListener(mOnClickListener);
        btn_stop.setOnClickListener(mOnClickListener);
        btn_pasuse.setOnClickListener(mOnClickListener);
        btn_continue.setOnClickListener(mOnClickListener);
        updateUi(true, false, false, false);

        downloadManager = new DownloadManager();
        downloadManager.setOnDownloadListener(mOnDownloadListener);
    }

    private void updateUi(boolean e1, boolean e2, boolean e3, boolean e4) {
        btn_start.setEnabled(e1);
        btn_stop.setEnabled(e2);
        btn_pasuse.setEnabled(e3);
        btn_continue.setEnabled(e4);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_start) {
//                downloadManager.start("http://gdown.baidu.com/data/wisegame/dc8a46540c7960a2/baidushoujizhushou_16798087.apk");
//                downloadManager.start("https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg");
                downloadManager.downloadStart("http://hbfile.huabanimg.com/android/huaban-android.apk");
            } else if (id == R.id.btn_stop) {
                downloadManager.downloadStop();
            } else if (id == R.id.btn_pause) {
                downloadManager.downloadPause();
            } else if (id == R.id.btn_continue) {
                downloadManager.downloadContinue();
            }
        }
    };

    private DownloadManager.OnDownloadListener mOnDownloadListener = new DownloadManager.OnDownloadListener() {
        @Override
        public void onStart(DownloadInfo downloadInfo) {
            KLog.w("onStart");
            updateUi(false, true, true, false);
        }

        @Override
        public void onPause(DownloadInfo downloadInfo) {
            KLog.w("onPause");
            updateUi(false, true, false, true);
        }

        @Override
        public void onContinue(DownloadInfo downloadInfo) {
            KLog.w("onContinue");
            updateUi(false, true, true, false);
        }

        @Override
        public void onStop(DownloadInfo downloadInfo) {
            KLog.w("onStop");
            pb_progress.setProgress(0);
            tv_progress.setText("0%");
            updateUi(true, false, false, false);
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            KLog.w("onFinish");
            updateUi(true, false, false, false);
        }

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            double progress = (100.0 * downloadInfo.getReadLength() / downloadInfo.getContentLength());
            pb_progress.setProgress((int) progress);
            tv_progress.setText(progress + "%");
        }

        @Override
        public void onError(DownloadInfo downloadInfo) {
            KLog.e("onError:" + downloadInfo.getThrowable());
            updateUi(true, false, false, false);
            Toast.makeText(getApplicationContext(), "" + downloadInfo.getThrowable(), Toast.LENGTH_SHORT).show();
        }

    };

}
