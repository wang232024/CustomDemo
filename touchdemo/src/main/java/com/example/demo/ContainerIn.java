package com.example.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.common.KLog;

public class ContainerIn extends LinearLayout {
    private static final String TAG = "ContainerIn";
    private Paint mPaint = new Paint();
    private Rect mBounds;

    public ContainerIn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // 初始化分隔线画笔
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
    }

    /**
     * 1. 拦截点击事件
     * @param ev
     * @return  true:拦截
     *          false:不拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        KLog.d(TAG, "onInterceptTouchEvent:" + onInterceptTouchEvent + ", getAction:" + ev.getAction());
        return onInterceptTouchEvent;
    }

    /**
     * 2. 处理点击事件
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
     * 分发点击事件
     * 当前控件的super.dispatchTouchEvent由其onTouchEvent和onInterceptTouchEvent决定
     * 当前控件只拦截不处理，dispatchTouchEvent为false，父容器后续事件不再分发过来
     * 当前控件不拦截只处理，dispatchTouchEvent为true，父容器后续事件继续分发
     * 总结:
     *      1. 父容器是否继续分发点击事件过来，由dispatchTouchEvent的返回值决定，只有返回true，后续事件才会分发过来。
     *      2. dispatchTouchEvent的返回值最好不要直接修改，而是由onTouchEvent和onInterceptTouchEvent的返回值进行控制。
     *      3. 如果对事件进行了拦截，那么给出对应的处理。否则不作拦截和处理，继续分发给子控件。
     * @param ev
     * @return  事件处理结果，true:当前控件/子控件已对事件进行处理
     *                    false:当前控件/子控件均未对事件进行处理
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
        KLog.w(TAG, "dispatchTouchEvent:" + dispatchTouchEvent + ", getAction:" + ev.getAction());
        return dispatchTouchEvent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null != mBounds) {
            canvas.drawRect(mBounds, mPaint);
        }
    }

    public void setBounds(Rect rect) {
        mBounds = rect;
    }

//    @Override
//    public boolean performClick() {
//        boolean perform = super.performClick();
////        KLog.w(TAG, "ContainerIn performClick:" + perform);
//        return perform;
//    }

}
