package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.util.KLog;

/**
 * 1. 控件clickable属性决定其是否默认消耗点击事件。能点击时将消耗掉点击事件，onTouchEvent返回true;否则不消耗点击事件，onTouchEvent返回false，事件继续向上层传递，后续事件由上层接管。
 * 2. onTouch和onTouchEvent做同样的事，默认走onTouchEvent，当设置setOnTouchListener时，走onTouch。
 *    这样设计的好处是，当自定义控件时，直接重写onTouchEvent比较好;而设置setOnTouchListener可覆盖掉onTouchEvent中的功能，常用于非自定义控件的触摸监听（可不用动非自定义控件的代码）。
 * 3. 父容器内的子容器是否对触摸事件进行拦截和消耗，决定事件是否继续向上传递给父容器的onTouchEvent中。
 * 4. 最终事件的处理和消耗位于onTouchEvent中。
 */
public class TouchActivity extends AppCompatActivity {
    private static final String TAG = "wtx";
    private ContainerOut mContainerOut;
    private ContainerIn mContainerIn;
    private CustomView mCustomView;

    private float mElevation = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        // AppCompatActivity 隐藏Toolbar
        getSupportActionBar().hide();

        mContainerOut = findViewById(R.id.container_out);
        mContainerIn = findViewById(R.id.container_in);
        mCustomView = findViewById(R.id.customview);
//        mCustomView.setClickable(true);
        KLog.i(TAG, "isClickable:" + mCustomView.isClickable());

        Button touch_add = findViewById(R.id.touch_add);
        Button touch_del = findViewById(R.id.touch_del);

        touch_add.setOnClickListener(mOnClickListener);
        touch_del.setOnClickListener(mOnClickListener);

        // 父容器裁剪
        mContainerOut.setClipToOutline(true);
        mContainerOut.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(
                        0,
                        0,
                        view.getWidth(),
                        view.getHeight(),
                        50
                );
                outline.setAlpha(0);
            }
        });

//        mContainerOut.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                KLog.v(TAG, "ContainerOut onTouch");
//                return false;
//            }
//        });
//        mContainerIn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                KLog.d(TAG, "ContainerIn onTouch");
//                return false;
////                return true;
//            }
//        });
//
        mCustomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KLog.i(TAG, "mCustomView, onTouch, getAction:" + event.getAction());
                boolean performClick = v.performClick();
                KLog.e(TAG, "performClick:" + performClick);
                return true;
            }
        });
        KLog.i(TAG, "isClickable:" + mCustomView.isClickable());

        mContainerIn.post(new Runnable() {
            @Override
            public void run() {
                int expand = 30;
                Rect bounds = new Rect();
                // 获取View2区域在mContainerIn中的相对位置，这里因为mContainerIn是View2的直接父View，所以使用getHitRect()
                mCustomView.getHitRect(bounds);
                // 计算扩展后的区域Bounds相对于mContainerIn的位置，left、top、right、bottom分别为view在各个方向上的扩展范围
                bounds.left -= expand;
                bounds.top -= expand;
                bounds.right += expand;
                bounds.bottom += expand;
                // 创建TouchDelegate
                TouchDelegate touchDelegate = new TouchDelegate(bounds, mCustomView);
                // 为mContainerIn设置TouchDelegate
                mContainerIn.setTouchDelegate(touchDelegate);

                mContainerIn.setBounds(bounds);
                mContainerIn.postInvalidate();
            }
        });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        boolean dispatchTouchEvent = super.dispatchTouchEvent(event);
//        KLog.d(TAG, "dispatchTouchEvent:" + dispatchTouchEvent + ", getAction:" + event.getAction());
//        return dispatchTouchEvent;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean onTouchEvent = super.onTouchEvent(event);
//        KLog.d(TAG, "onTouchEvent:" + onTouchEvent + ", getAction:" + event.getAction());
//        return onTouchEvent;
//    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (R.id.touch_add == v.getId()) {

            } else if (R.id.touch_del == v.getId()) {

            }
        }
    };

}