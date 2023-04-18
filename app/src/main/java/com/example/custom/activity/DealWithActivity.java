package com.example.custom.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;
import com.example.custom.synchronizeddealwith.AsynchronizedDealwith;
import com.example.custom.synchronizeddealwith.DataWrap;

public class DealWithActivity extends AppCompatActivity {
    private static final String TAG = "wtx";
    private static final String data = "dataString";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final AsynchronizedDealwith dealwith = new AsynchronizedDealwith(new AsynchronizedDealwith.OnDealwithListener() {
            @Override
            public void onDealBegin() {
                Log.e(TAG, "+++onDealBegin+++");
            }

            @Override
            public void onDealwith(Object object) {
                DataWrap dataWrap = (DataWrap) object;
                Log.e(TAG, "onDealwith:" + dataWrap.getData());
            }

            @Override
            public void onDealFinish() {
                Log.e(TAG, "---onDealFinish---");
            }
        });

        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealwith.insertAndDealwith(data);
            }
        });

        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealwith.cleanList();
            }
        });
    }
}
