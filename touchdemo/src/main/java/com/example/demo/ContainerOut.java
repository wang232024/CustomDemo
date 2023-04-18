package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ContainerOut extends LinearLayout {
    private static final String TAG = "wtx";

    public ContainerOut(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ContainerOut dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "ContainerOut dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ContainerOut dispatchTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "ContainerOut dispatchTouchEvent ACTION_CANCEL");
                break;
        }
        boolean dispatch = super.dispatchTouchEvent(ev);
        Log.e(TAG, "ContainerOut dispatchTouchEvent, dispatch:" + dispatch);
        return dispatch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ContainerOut onInterceptTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "ContainerOut onInterceptTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ContainerOut onInterceptTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "ContainerOut onInterceptTouchEvent, ACTION_CANCEL");
                break;
        }
        boolean intercept = super.onInterceptTouchEvent(ev);
        Log.e(TAG, "ContainerOut onInterceptTouchEvent, intercept:" + intercept);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ContainerOut onTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "ContainerOut onTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ContainerOut onTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "ContainerOut onTouchEvent, ACTION_CANCEL");
                break;
        }
        boolean touch = super.onTouchEvent(event);
        Log.e(TAG, "ContainerOut onTouchEvent, touch:" + touch);
        return touch;
    }

//    @Override
//    public boolean performClick() {
//        boolean perform = super.performClick();
////        Log.e(TAG, "ContainerOut performClick:" + perform);
//        return perform;
//    }

}

