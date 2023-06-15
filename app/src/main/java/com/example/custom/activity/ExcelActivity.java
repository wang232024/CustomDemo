package com.example.custom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import com.example.util.AssetsUtil;
import com.example.util.ExcelUtils;
import com.example.custom.R;

public class ExcelActivity extends Activity {
    private static final String TAG = "wtx_ExcelActivity";
    private Context mContext = ExcelActivity.this;
    private TextView mTvExcel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_excel);
        Log.i(TAG, "onCreate");

        mTvExcel = findViewById(R.id.tv_excel);

        initData();
    }

    private void initData() {
        File fileYanglao = new File(getExternalCacheDir().getPath() + "/yanglao.xls");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 写入Excel
                    ExcelUtils.testWriteExcel(getExternalCacheDir().getPath() + "/write.xls");

                    if (!fileYanglao.exists()) {
                        AssetsUtil.copyAssetFile(mContext, "yanglao/yanglao.xls", fileYanglao.getPath());
                        Log.d(TAG, "fileYanglao:" + fileYanglao + ", " + fileYanglao.exists());
                    } else {
                        Log.d(TAG, fileYanglao.getPath() + " already exist.");
                    }

                    // 读取Excel
                    StringBuilder stringBuffer = ExcelUtils.readExcel(new FileInputStream(fileYanglao.getPath()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvExcel.setText(stringBuffer.toString());
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Exception:" + e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
