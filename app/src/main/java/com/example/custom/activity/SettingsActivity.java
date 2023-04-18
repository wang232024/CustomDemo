package com.example.custom.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.custom.R;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = "wtx_SettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Exit");
            setListFooter(button);
        }
    }

    private void initToolbar() {
        //找到Activity根布局
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        //获取根布局子View
        View content = rootView.getChildAt(0);
        //加载自定义布局文件
        LinearLayout toolbarLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_custom_toolbar, null);
        //移除根布局所有子view
        rootView.removeAllViews();
        //注意这里一要将前面移除的子View添加到我们自定义布局文件中，否则PreferenceActivity中的Header将不会显示
        toolbarLayout.addView(content);
        //将包含Toolbar的自定义布局添加到根布局中
        rootView.addView(toolbarLayout);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        Log.i(TAG, "isValidFragment, fragmentName:" + fragmentName);
        return true;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_mypreferenceactivity, target);
    }
}
