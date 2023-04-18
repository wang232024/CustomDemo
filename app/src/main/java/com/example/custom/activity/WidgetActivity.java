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

        RadioGroup radioGroup = findViewById(R.id.account_check_frequency_rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("wtx", "onCheckedChanged, checkedId:" + checkedId);
            }
        });
    }
}
