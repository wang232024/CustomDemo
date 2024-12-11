package com.example.gifdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GifActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        mImageView = findViewById(R.id.img);
        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    private void show() {
        AssetManager assetManager = getAssets();
        ImageDecoder.Source source = ImageDecoder.createSource(assetManager, "first_switch_1_app.gif");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Drawable drawable = ImageDecoder.decodeDrawable(source);
                    mImageView.setImageDrawable(drawable);
                    if (drawable instanceof AnimatedImageDrawable){
                        ((AnimatedImageDrawable) drawable).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}