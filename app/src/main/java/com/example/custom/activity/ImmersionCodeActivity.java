package com.example.custom.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;
import com.example.custom.util.PictureUtil;

public class ImmersionCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codeimmersion);
        
        initViews();
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();

            // SYSTEM_UI_FLAG_LAYOUT_STABLE:防止系统栏隐藏时内容区域大小发生变化

//            // 全屏，即不带顶部状态栏
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;

//            // 隐藏顶部状态栏和底部导航栏
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            //顶部状态栏透明
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            decorView.setSystemUiVisibility(option);

            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();        // 去掉默认的标题栏
        actionBar.hide();

        ImageView imgImmersionBg = findViewById(R.id.img_immersion_bg);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
//        Bitmap bmp = doBlur(bitmap, 0, false);
//        imgImmersionBg.setImageBitmap(bmp);

        Button btnGaussian = findViewById(R.id.btn_gaussian);
        btnGaussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius += 1;
                if (radius > 100) {
                    radius = 0;
                }
                Log.i("wtx", "radius:" + radius);
                Bitmap bmp = PictureUtil.doBlur(bitmap, radius);
                imgImmersionBg.setImageBitmap(bmp);
            }
        });
    }
    private int radius = 0;

//    /**
//     * 一般用于全屏化的游戏或者视频设置中，即实现沉浸式模式。
//     * @param hasFocus
//     */
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            );
//        }
//    }

}
