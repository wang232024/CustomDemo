package com.example.tablayoutactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void TabLayoutTopActivity(View view) {
        Intent intent = new Intent(this, TabLayoutTopActivity.class);
        startActivity(intent);
    }

    public void TabLayoutBottomActivity(View view) {
        Intent intent = new Intent(this, TabLayoutBottomActivity.class);
        startActivity(intent);
    }

}