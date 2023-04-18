package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ContainerIn extends LinearLayout {
    private static final String TAG = "wtx";

    public ContainerIn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "ContainerIn dispatchTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "ContainerIn dispatchTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "ContainerIn dispatchTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "ContainerIn dispatchTouchEvent, ACTION_CANCEL");
                break;
        }
        boolean dispatch = super.dispatchTouchEvent(ev);
        Log.w(TAG, "ContainerIn dispatchTouchEvent, dispatch:" + dispatch);
        return dispatch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "ContainerIn onInterceptTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "ContainerIn onInterceptTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "ContainerIn onInterceptTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "ContainerIn onInterceptTouchEvent, ACTION_CANCEL");
                break;
        }
//        boolean intercept = super.onInterceptTouchEvent(ev);
//        Log.w(TAG, "ContainerIn onInterceptTouchEvent, intercept:" + intercept);
//        return intercept;
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "ContainerIn onTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "ContainerIn onTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "ContainerIn onTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "ContainerIn onTouchEvent, ACTION_CANCEL");
                break;
        }
//        boolean touch = super.onTouchEvent(event);
//        Log.w(TAG, "ContainerIn onTouchEvent, touch:" + touch);
//        return touch;
        return true;
    }

//    @Override
//    public boolean performClick() {
//        boolean perform = super.performClick();
////        Log.w(TAG, "ContainerIn performClick:" + perform);
//        return perform;
//    }

}
