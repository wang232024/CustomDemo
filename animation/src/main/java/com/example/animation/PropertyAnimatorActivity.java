package com.example.animation;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PropertyAnimatorActivity extends AppCompatActivity {
    private Context mContext = PropertyAnimatorActivity.this;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animator);

        mTextView = findViewById(R.id.textview_color);
//        CustomAnimator.changeBackgroundColor(mTextView);

        CustomAnimator customAnimator = new CustomAnimator(0xFF00FF00);

        Button btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PropertyAnimatorSecondActivity.class);

                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(mContext, R.anim.taskbarview_open_app_enter, R.anim.no_anim);
                Bundle optsBundle = activityOptions.toBundle();
                startActivity(intent, optsBundle);
            }
        });
        Log.i("wtx", "onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("wtx", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("wtx", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("wtx", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("wtx", "onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("wtx", "onNewIntent, intent:" + intent);
    }
}