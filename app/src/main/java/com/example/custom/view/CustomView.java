package com.example.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    private static final String TAG = "wtx_CustomView";
    private Paint mPaint;
    private int paddingTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);  // 填充
        mPaint.setStrokeWidth(5);
    }

    public int getRealWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        Log.w(TAG, "CustomView, paddingLeft:" + paddingLeft + ", paddingRight:" + paddingRight);
        Log.w(TAG, "CustomView, widthVal:" + widthVal + ", widthMode:" + widthMode);

        if (widthMode == MeasureSpec.EXACTLY) {
            return paddingLeft + paddingRight + widthVal;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mPaint.ascent() - mPaint.descent()) + paddingLeft + paddingRight);
        } else {
            return (int) Math.min((Math.abs(mPaint.ascent() - mPaint.descent()) + paddingLeft + paddingRight), widthVal);
        }
    }

    public int getRealHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightVal = MeasureSpec.getSize(heightMeasureSpec);
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        Log.w(TAG, "CustomView, paddingTop:" + paddingTop + ", paddingBottom:" + paddingBottom);
        Log.w(TAG, "CustomView, heightVal:" + heightVal + ", heightMode:" + heightMode);
        if (heightMode == MeasureSpec.EXACTLY) {
            return paddingTop + paddingBottom + heightVal;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mPaint.ascent() - mPaint.descent()) + paddingTop + paddingBottom);
        } else {
            return (int) Math.min((Math.abs(mPaint.ascent() - mPaint.descent()) + paddingTop + paddingBottom), heightVal);
        }
    }

    // TODO ?
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");
        int usedHeight = getRealHeight(heightMeasureSpec);
        int usedWidth = getRealWidth(widthMeasureSpec);
        setMeasuredDimension(usedWidth, usedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged, [" + w + ", " + h + "], [" + oldw + ", " + oldh + "]");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout, [" + left + ", " + top + ", " + right + ", " + bottom + "]");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw");
    }

    /*
    View绘制生命周期
    Activity
        onCreate
            View -> onSizeChanged -> onLayout
        onResume
        onAttachedToWindow
            View -> onMeasure -> onMeasure -> onSizeChanged -> onLayout -> onDraw
        onWindowFocusChanged
    view.layout()方法会触发onSizeChanged和onLayout回调

    代码中动态初始化View的属性
    在Activity的onCreate或者onResume中设置view.layout()会失效：view从xml文件中加载的属性会在此之后触发onSizeChanged和onLayout，从而覆盖掉修改。
    在Activity的onWindowFocusChanged方法中通过LayoutParams重新设置View的位置和宽高可以实现效果。
     */

}
