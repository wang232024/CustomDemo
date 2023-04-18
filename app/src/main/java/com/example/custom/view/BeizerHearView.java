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

public class BeizerHearView extends View {
    private static final String TAG = "wtx_BeizerHearView";
    private Paint mPaint;
    private Path mPath;
    private int realWidth;
    private int realHeight;
    private int paddingTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;

    public BeizerHearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);  // 绘制
        mPaint.setStyle(Paint.Style.FILL);  // 填充
        mPaint.setStrokeWidth(5);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int usedHeight = getRealHeight(heightMeasureSpec);
        int usedWidth = getRealWidth(widthMeasureSpec);
        setMeasuredDimension(usedWidth, usedHeight);
    }

    public int getRealHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightVal = MeasureSpec.getSize(heightMeasureSpec);
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        Log.w(TAG, "paddingTop:" + paddingTop + ", paddingBottom:" + paddingTop);
        Log.w(TAG, "heightVal:" + heightVal);
        if (heightMode == MeasureSpec.EXACTLY) {
            return paddingTop + paddingBottom + heightVal;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mPaint.ascent() - mPaint.descent()) + paddingTop + paddingBottom);
        } else {
            return (int) Math.min((Math.abs(mPaint.ascent() - mPaint.descent()) + paddingTop + paddingBottom), heightVal);
        }
    }

    public int getRealWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        if (widthMode == MeasureSpec.EXACTLY) {
            return paddingLeft + paddingRight + widthVal;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mPaint.ascent() - mPaint.descent()) + paddingLeft + paddingRight);
        } else {
            return (int) Math.min((Math.abs(mPaint.ascent() - mPaint.descent()) + paddingLeft + paddingRight), widthVal);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        realWidth = w;
        realHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.moveTo(0.5f * realWidth, 0.17f * realHeight);

        mPath.cubicTo(
                0.15f * realWidth, -0.35f * realHeight,
                -0.4f * realWidth, 0.45f * realHeight,
                0.5f * realWidth, realHeight);

        mPath.moveTo(0.5f * realWidth, realHeight);

        mPath.cubicTo(
                realWidth + 0.4f * realWidth, 0.45f * realHeight,
                realWidth - 0.15f * realWidth, -0.35f * realHeight,
                0.5f * realWidth, 0.17f * realHeight);

        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

}
