package com.example.custom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;

public class DataBaseActivity extends AppCompatActivity {
    private Context mContext = DataBaseActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_database);

        Button id_database_sq = findViewById(R.id.btn_database_sq);
        Button id_database_greendao = findViewById(R.id.btn_database_greendao);
        Button id_database_room = findViewById(R.id.btn_database_room);

        id_database_sq.setOnClickListener(mOnClickListener);
        id_database_greendao.setOnClickListener(mOnClickListener);
        id_database_room.setOnClickListener(mOnClickListener);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            Intent intent = new Intent();
            if (R.id.btn_database_sq == viewId) {
                intent.setClassName(mContext, "com.example.custom.db.SQLiteOpenHelperActivity");
            } else if (R.id.btn_database_greendao == viewId) {
                intent.setClassName(mContext, "com.custom.savedb.GreenDaoActivity");
            } else if (R.id.btn_database_room == viewId) {
                intent.setClassName(mContext, "com.example.roomdemo.MainRoomActivity");
            }
            startActivity(intent);
        }
    };

}
