package com.example.custom.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;

/**
 *  1.在values中设置
 *  android:windowTranslucentStatus为false
 *  android:windowTranslucentNavigation为true
 *  android:statusBarColor为@android:color/transparent
 *
 *  2.在清单文件中配置需要沉浸式状态栏的activity加入theme
 *
 *  3.在Activity的布局文件中的跟布局加入android:fitsSystemWindows="true"
 */
public class ImmersionPictureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictureimmersion);

    }
}
