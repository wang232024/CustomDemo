package com.example.custom.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceViewHolder;

import com.example.custom.R;

public class CustomPreference extends EditTextPreference {
    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Button btn1 = (Button) holder.findViewById(R.id.btn1);
        Button btn2 = (Button) holder.findViewById(R.id.btn2);
        btn1.setOnClickListener(mOnClickListener);
        btn2.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    Log.i("wtx", "btn1");
                    break;
                case R.id.btn2:
                    Log.i("wtx", "btn2");
                    break;
            }
        }
    };

}
