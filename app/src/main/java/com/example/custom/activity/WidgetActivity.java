package com.example.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.custom.R;

public class WidgetActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simple_widget);

        /**
            RadioButton设置图片大小(xml文件中无法直接调整图片大小)
            1. 去掉默认的选择图标
            android:button="@null"

            2. 代码中设置：
            //设置图片
            Drawable drawable_trans = getResources().getDrawable(R.drawable.selector_radio_btn);
            //设置大小
            drawable_trans.setBounds(new Rect(0, 0, 25, 25));
            //设置出现位置
            radioTrans.setCompoundDrawables(drawable_trans, null, null, null);
        **/

        RadioGroup radioGroup = findViewById(R.id.account_check_frequency_rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("wtx", "onCheckedChanged, checkedId:" + checkedId);
            }
        });
    }
}
