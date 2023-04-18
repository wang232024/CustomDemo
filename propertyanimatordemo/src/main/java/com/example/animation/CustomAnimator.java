package com.example.animation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.Property;
import android.view.View;

import com.example.animation.interpolator.JellyInterpolator;

public class CustomAnimator {

    private static final String[] PROPERTYNAMES = {
            "backgroundColor",
            "color",
    };

    /**
     * 方法一
     * @param view
     */
    public static void changeBackgroundColor(View view) {
        // 颜色
        ValueAnimator animatorColor = ObjectAnimator.ofInt(view, PROPERTYNAMES[0],
                0xFFFF0000, 0xFF00FF00, 0xFF0000FF);
        animatorColor.setDuration(3000);
        animatorColor.setEvaluator(new ArgbEvaluator());
        animatorColor.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        animatorColor.setRepeatCount(0);
        animatorColor.setRepeatMode(ValueAnimator.REVERSE);
        animatorColor.start();

        // 宽度，自定义包装类
        ViewWrapper viewWrapper = new ViewWrapper(view);
        ObjectAnimator animatorWidth = ObjectAnimator.ofInt(viewWrapper, "customWidth", 300);
        animatorWidth.setDuration(3000);
        animatorWidth.start();

        // 动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorColor, animatorWidth);
        // 设置插值器
        animatorSet.setInterpolator(new JellyInterpolator());
        animatorSet.setDuration(3000);
        animatorSet.start();
    }

    /**
     * 方法二
     * @param view
     */
    public static void changeByProperty(View view) {
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(
                        view,
                        // 这里必须使用有get和set方法的属性，如visibility，然后在覆写方法中修改和获取color，实际修改的是背景色。
                        new Property<View, Integer>(Integer.TYPE, "visibility") {
                            @Override
                            public Integer get(View view) {
                                ColorDrawable drawable = (ColorDrawable) view.getBackground();
                                return drawable.getColor();
                            }

                            @Override
                            public void set(View view, Integer value) {
                                view.setBackgroundColor(value);
                            }
                        },
                        0xFFFF0000,
                        0xFF00FF00,
                        0xFF0000FF
                )
                .setDuration(3000);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.start();
    }

    /**
     * 方法三
     * @param view
     */
    private int mValue;
    private static Property<CustomAnimator, Integer> VALUE = new Property<CustomAnimator, Integer> (Integer.TYPE, "value") {

        @Override
        public Integer get(CustomAnimator anim) {
            Log.i("wtx", "get:" + anim.mValue);
            return anim.mValue;
        }

        @Override
        public void set(CustomAnimator anim, Integer value) {
            anim.mValue = value;
            Log.i("wtx", "set:" + anim.mValue);
        }
    };

    public CustomAnimator(int mValue) {
        this.mValue = mValue;
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(
                        this,
                        VALUE,
                        0xFFFF0000,
                        0xFF00FF00,
                        0xFF0000FF
                )
                .setDuration(3000);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.start();
    }

}
