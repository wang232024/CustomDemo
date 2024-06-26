package com.example.animation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.KLog;


public class AnimationActivity extends AppCompatActivity {
    private static final String TAG = "wtx_AnimatorActivity";
    private TextView mTextView;
    private Button mButton;
    private ImageView mAnimationImg;
    private AnimatorSet animatorSet;
    boolean switchBtn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animatoractivity);

        mTextView = findViewById(R.id.animation_tv);

        mButton = findViewById(R.id.animation_btn);

        FloatProperty<View> TRANSLATION_X = new FloatProperty<View>("test_identify") {      // 针对任意命名
            private static final int TRANSLATION = 10;

            @Override
            public void setValue(View view, float value) {
                KLog.i("wtx", "setValue:" + value);
                view.setTranslationX(TRANSLATION * value);
            }

            @Override
            public Float get(View view) {
                return view.getTranslationX();
            }
        };

        FloatProperty<View> TRANSLATION_Y = new FloatProperty<View>("test_identify") {      // 针对任意命名
            private static final int TRANSLATION = 10;

            @Override
            public void setValue(View view, float value) {
                KLog.i("wtx", "setValue:" + value);
                view.setTranslationY(TRANSLATION * value);
            }

            @Override
            public Float get(View view) {
                return view.getTranslationY();
            }
        };

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click.");

                if (!switchBtn) {
                    switchBtn = true;
                    ObjectAnimator anim_lt = ObjectAnimator.ofFloat(mButton, TRANSLATION_X, 0, 1);
                    ObjectAnimator anim_rt = ObjectAnimator.ofFloat(mButton, TRANSLATION_Y, 0, 1);
                    ObjectAnimator anim_lb = ObjectAnimator.ofFloat(mButton, TRANSLATION_X, 1, 0);
                    ObjectAnimator anim_rb = ObjectAnimator.ofFloat(mButton, TRANSLATION_Y, 1, 0);
                    anim_lt.setRepeatCount(ValueAnimator.INFINITE);
                    anim_rt.setRepeatCount(ValueAnimator.INFINITE);
                    anim_lb.setRepeatCount(ValueAnimator.INFINITE);
                    anim_rb.setRepeatCount(ValueAnimator.INFINITE);

                    animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(
                            anim_lt,
                            anim_rt,
                            anim_lb,
                            anim_rb
                    );
                    animatorSet.setDuration(100).start();
                } else {
                    switchBtn = false;
                    animatorSet.cancel();
                }
            }
        });

        mAnimationImg = findViewById(R.id.animation_img);
        init();

        // ValueAnimator只能针对值进行动画改变，如果我们需要关联到View的变化，就需要设置监听事件，根据值得变化手动去操作这个View变化。
//        baseValueAnimator();
//        baseValueAnimatorOfObject();

//        baseObjectAnimator();

//        baseAnimatorSet();
        baseAnimatorSetBuild();
    }

    private void init() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationImg.getBackground();
        if (animationDrawable != null) {
            animationDrawable.setOneShot(false);
            animationDrawable.start();
            animationDrawable.selectDrawable(0);
        }
    }

    /**
     * 组合动画
     * with	    设置当前动画与前一个动画一起执行
     * before	设置当前动画在之前所有动画之后执行，也可理解为之前的动画都在这个动画之前执行
     * after	设置当前动画在之前所有动画之前执行，也可理解为之前的所有动画都在这个动画之后执行
     */
    private void baseAnimatorSetBuild() {
        ObjectAnimator alphaAnimator =
                ObjectAnimator.ofFloat(mTextView, "alpha", 1, 0, 1);
        ObjectAnimator rotateAnimator =
                ObjectAnimator.ofFloat(mTextView, "rotation", 0, 360, 0);
        rotateAnimator.setDuration(15000);
        ObjectAnimator scaleAnimator =
                ObjectAnimator.ofFloat(mTextView, "scaleX", 1, 2, 1);
        ObjectAnimator translateAnimator =
                ObjectAnimator.ofFloat(mTextView, "translationX", 0, 100, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator)             // 顺序2
                .with(rotateAnimator)               // 顺序2
                .after(scaleAnimator)               // 顺序1
                .before(translateAnimator);         // 顺序3
        animatorSet.setDuration(2000).start();
    }

    /**
     * playSequentially     顺序进行动画
     * playTogether         同时进行动画
     */
    private void baseAnimatorSet() {
        ObjectAnimator alphaAnimator =
                ObjectAnimator.ofFloat(mTextView, "alpha", 1, 0, 1);
        ObjectAnimator rotateAnimator =
                ObjectAnimator.ofFloat(mTextView, "rotation", 0, 360, 0);
        ObjectAnimator scaleAnimator =
                ObjectAnimator.ofFloat(mTextView, "scaleX", 1, 2, 1);
        ObjectAnimator translateAnimator =
                ObjectAnimator.ofFloat(mTextView, "translationX", 0, 100, 0);

        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playSequentially(alphaAnimator, rotateAnimator, scaleAnimator, translateAnimator);
        animatorSet.playTogether(alphaAnimator, rotateAnimator, scaleAnimator, translateAnimator);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    /**
     * ObjectAnimator继承自ValueAnimator，动画直接作用于View
     */
    private void baseObjectAnimator() {
        /**
         * ObjectAnimator查找第二个参数对应的set方法来设置值。例如传入“alpha”，则它会去TextView中找setAlpha()方法。
         */
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTextView,"alpha", 1.0f, 0.0f, 1.0f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    /**
     * 自定义类变化, Point类坐标变化
     */
    private void baseValueAnimatorOfObject() {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(
                new PointEvaluator(),
                new Point(0, 0),
                new Point(500, 500)
        );
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                Log.i(TAG, "--->" + point);
                mTextView.layout(point.getX(), point.getY(), point.getX() + mTextView.getWidth(),
                        point.getY() + mTextView.getHeight());
            }
        });
        valueAnimator.start();
    }

    /**
     * 基础值变化, int, float, rgb.
     */
    private void baseValueAnimator() {
        /**
         * 设置为ValueAnimator.INFINITE无限重复后，需进行停止，否则会一直进行下去
         */
//        ValueAnimator valueAnimator = ValueAnimator.ofArgb(0xFFFF5454, 0xFF5DDE5D, 0xFF5DBEDE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 500);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();

        /**
         * 监听值的变化
         */
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
//                Log.i(TAG, "onAnimationUpdate, value:" + value);
                mButton.setTranslationX(value);
            }
        });
        /**
         * 监听状态
         */
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "onAnimationStart.");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd.");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG, "onAnimationCancel.");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG, "onAnimationRepeat.");
            }
        });

        UniformSpeed uniformSpeed = new UniformSpeed();
        valueAnimator.setInterpolator(uniformSpeed);
    }
}