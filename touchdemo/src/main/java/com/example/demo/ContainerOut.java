package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

import com.example.util.KLog;

/**
 *  如果传入的点击事件ACTION_DOWN未被处理，那么后续的点击事件(ACTION_MOVE, ACTION_UP)都不会再往下传递。
 */
public class ContainerOut extends LinearLayout {
    private static final String TAG = "ContainerOut";

    public ContainerOut(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        KLog.d(TAG, "onInterceptTouchEvent:" + onInterceptTouchEvent + ", getAction:" + ev.getAction());
        return onInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = super.onTouchEvent(event);
        KLog.i(TAG, "onTouchEvent:" + onTouchEvent + ", getAction:" + event.getAction());
        return onTouchEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
        KLog.w(TAG, "dispatchTouchEvent:" + dispatchTouchEvent + ", getAction:" + ev.getAction());
        return dispatchTouchEvent;
    }

//    @Override
//    public boolean performClick() {
//        boolean perform = super.performClick();
////        KLog.e(TAG, "ContainerOut performClick:" + perform);
//        return perform;
//    }

}

