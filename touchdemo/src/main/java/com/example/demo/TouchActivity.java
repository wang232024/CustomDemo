package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 1. 控件clickable属性决定其是否默认消耗点击事件。能点击时将消耗掉点击事件，onTouchEvent返回true;否则不消耗点击事件，onTouchEvent返回false，事件继续向上层传递，后续事件由上层接管。
 * 2. onTouch和onTouchEvent做同样的事，默认走onTouchEvent，当设置setOnTouchListener时，走onTouch。
 *    这样设计的好处是，当自定义控件时，直接重写onTouchEvent比较好;而设置setOnTouchListener可覆盖掉onTouchEvent中的功能，常用于非自定义控件的触摸监听（可不用动非自定义控件的代码）。
 * 3. 父容器内的子容器是否对触摸事件进行拦截和消耗，决定事件是否继续向上传递给父容器的onTouchEvent中。
 * 4. 最终事件的处理和消耗位于onTouchEvent中。
 */
public class TouchActivity extends AppCompatActivity {
    private static final String TAG = "wtx";
    private LinearLayout mContainerOut;
    private LinearLayout mContainerIn;

    private float mElevation = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        mContainerOut = findViewById(R.id.container_out);
        mContainerIn = findViewById(R.id.container_in);
        View view = findViewById(R.id.view);
        view.setClickable(true);

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

        mContainerOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "ContainerOut onTouch");
                return false;
            }
        });
        mContainerIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.w(TAG, "ContainerIn onTouch");
                return false;
//                return true;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "view onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "view onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "view onTouch ACTION_UP");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.i(TAG, "view onTouch ACTION_CANCEL");
                        break;
                }
                return true;
            }
        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "onClick");
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"activity, dispatchTouchEvent_ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"activity, dispatchTouchEvent_ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"activity, dispatchTouchEvent_ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"activity, onTouchEvent_ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"activity, onTouchEvent_ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"activity, onTouchEvent_ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (R.id.touch_add == v.getId()) {

            } else if (R.id.touch_del == v.getId()) {

            }
        }
    };

}