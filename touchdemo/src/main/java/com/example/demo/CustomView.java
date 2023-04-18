package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class CustomView extends AppCompatButton {
    private static final String TAG = "wtx";

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "CustomView dispatchTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "CustomView dispatchTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "CustomView dispatchTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "CustomView dispatchTouchEvent, ACTION_CANCEL");
                break;
        }
        boolean dispatch = super.dispatchTouchEvent(ev);
        Log.i(TAG, "CustomView dispatchTouchEvent, dispatch:" + dispatch);
        return dispatch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "CustomView onTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "CustomView onTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "CustomView onTouchEvent, ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "CustomView onTouchEvent, ACTION_CANCEL");
                break;
        }
        boolean touch = super.onTouchEvent(event);
        Log.i(TAG, "CustomView onTouchEvent, touch:" + touch);
        return touch;
    }

//    @Override
//    public boolean performClick() {
//        boolean perform = super.performClick();
////        Log.i(TAG, "CustomView performClick:" + perform);
//        return perform;
//    }

}
