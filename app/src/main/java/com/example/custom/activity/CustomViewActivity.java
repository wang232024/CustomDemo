package com.example.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;

import com.example.custom.R;
import com.example.custom.view.CustomView;

public class CustomViewActivity extends Activity {
    private static final String TAG = "wtx_CustomViewActivity";
    private CustomView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customview);

        mView = findViewById(R.id.view);

        Log.i(TAG, "onCreate");

        mView.setClickable(true);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click");
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, "onWindowFocusChanged");

        // 通过LayoutParams重新设置View的位置和宽高
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mView.getLayoutParams();
        lp.leftMargin = 100;
        lp.topMargin = 300;
        lp.width = 150;
        lp.height = 250;
        mView.setLayoutParams(lp);
    }

}
