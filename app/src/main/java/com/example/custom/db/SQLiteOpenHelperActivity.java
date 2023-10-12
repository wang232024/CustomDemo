package com.example.custom.db;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;

public class SQLiteOpenHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_database);

        Button btn_database_insert = findViewById(com.custom.savedb.R.id.btn_database_insert);
        Button btn_database_delete = findViewById(com.custom.savedb.R.id.btn_database_delete);
        Button btn_database_update = findViewById(com.custom.savedb.R.id.btn_database_update);
        Button btn_database_query = findViewById(com.custom.savedb.R.id.btn_database_query);

        btn_database_insert.setOnClickListener(mOnClickListener);
        btn_database_delete.setOnClickListener(mOnClickListener);
        btn_database_update.setOnClickListener(mOnClickListener);
        btn_database_query.setOnClickListener(mOnClickListener);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (com.example.roomdemo.R.id.btn_database_insert == viewId) {

            } else if (com.example.roomdemo.R.id.btn_database_delete == viewId) {

            } else if (com.example.roomdemo.R.id.btn_database_update == viewId) {

            } else if (com.example.roomdemo.R.id.btn_database_query == viewId) {

            }
        }
    };

}
