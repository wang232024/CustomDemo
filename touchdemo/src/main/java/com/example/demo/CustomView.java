package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.common.KLog;

public class CustomView extends View {
    private static final String TAG = "CustomView";

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 如果设置了控件的setClickable(true)，那么super.onTouchEvent(event)将返回true
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = super.onTouchEvent(event);
        KLog.i(TAG, "onTouchEvent:" + onTouchEvent + ", getAction:" + event.getAction());
        return onTouchEvent;
    }

    /**
     * 父容器根据子控件的dispatchTouchEvent值来判断是否继续向下分发点击事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
        KLog.w(TAG, "dispatchTouchEvent:" + dispatchTouchEvent + ", getAction:" + ev.getAction());
        return dispatchTouchEvent;
    }

    @Override
    public boolean performClick() {
        boolean perform = super.performClick();
        KLog.i(TAG, "performClick:" + perform);
        return perform;
    }

}
