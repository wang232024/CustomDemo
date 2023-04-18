package com.example.glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class GlideActivity extends AppCompatActivity {
    private static final String TAG = "wtx_MainActivity";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);

        ManifestUtil.initPermission(this);

        Button btn = findViewById(R.id.btn);
        mImageView = findViewById(R.id.img);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGlideLoad(0);
            }
        });
    }

    /**
     * 不要在非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext(or You cannot start a load for a destroyed activity)
     * .override()      // 只会将图片加载成指定大小的像素
     * .crossFade()     // 淡入淡出
     * .dontAnimate()   // 不要动画
     * .asBitmap()      // 只允许加载静态图
     * .asGif()         // 只允许加载动态图
     * .fitCenter()     // 让Image完全显示，尺寸不对时，周围会留白
     * .centerCrop()    // 周围不会留白
     * .diskCacheStrategy(DiskCacheStrategy.NONE)   // 禁用缓存功能
     * @param index
     */
    private void testGlideLoad(int index) {
        switch (index) {
            case 0:
                String url= "https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg";
                Glide.with(GlideActivity.this)
                        .load(url)
//                        .load(R.drawable.shinianrenjian)
                        .fallback(R.drawable.icon)         //加载空指针的时候, url=null时
                        .error(R.drawable.no_photo)         // 异常占位图
                        .placeholder(R.drawable.ic_baseline_account_circle_24)       // 加载占位图
                        .listener(mRequestListener)
                        .into(mImageView);
                break;
            case 1:
                // 加载本地图片
                File file = new File(getExternalCacheDir() + "/demo.jpg");
                Glide.with(this).load(file).into(mImageView);
                break;
            case 2:
                // 加载应用资源
//                Glide.with(this).load(R.drawable.setting).into(mImageView);
                Glide.with(this).load(R.drawable.setting).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }
                });
                break;
//            case 3:
//                // 加载二进制流
//                byte[] image = getImageBytes();
//                Glide.with(this).load(image).into(mImageView);
//                break;
//            case 4:
//                // 加载Uri对象
//                Uri imageUri = getImageUri();
//                Glide.with(this).load(imageUri).into(mImageView);
//                break;
//            case 5:
//                Glide.with(this).load(R.drawable.shinianrenjian)
//                        //模糊
//                        .bitmapTransform(new BlurTransformation(this))
//                        //圆角
//                        .bitmapTransform(new RoundedCornersTransformation(this, 24, 0, RoundedCornersTransformation.CornerType.ALL))
//                        //遮盖
//                        .bitmapTransform(new MaskTransformation(this, R.mipmap.ic_launcher))
//                        //灰度
//                        .bitmapTransform(new GrayscaleTransformation(this))
//                        //圆形
//                        .bitmapTransform(new CropCircleTransformation(this))
//                        .into(mImageView);
//                break;
        }
    }

    RequestListener<Drawable> mRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Log.e(TAG, "onLoadFailed");
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            Log.e(TAG, "onResourceReady");
            return false;
        }
    };

}