package com.example.custom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.custom.R;

public class ImmersionActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_immersion);

        Button btn_immersion_code = findViewById(R.id.btn_immersion_code);
        Button btn_immersion_color = findViewById(R.id.btn_immersion_color);
        Button btn_immersion_picture = findViewById(R.id.btn_immersion_picture);
        btn_immersion_code.setOnClickListener(v -> {
            Intent intent = new Intent(ImmersionActivity.this, ImmersionCodeActivity.class);
            startActivity(intent);
        });
        btn_immersion_color.setOnClickListener(v -> {
            Intent intent = new Intent(ImmersionActivity.this, ImmersionColorActivity.class);
            startActivity(intent);
        });
        btn_immersion_picture.setOnClickListener(v -> {
            Intent intent = new Intent(ImmersionActivity.this, ImmersionPictureActivity.class);
            startActivity(intent);
        });
    }

}
