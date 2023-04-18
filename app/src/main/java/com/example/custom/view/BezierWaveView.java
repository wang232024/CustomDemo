package com.example.custom.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class BezierWaveView extends View {
    private static final String TAG = "wtx_BezierWaveView";
    private Paint mPaint;
    private Path mPath;
    private int mWidth;
    private int mHeight;
    private int mBaseHeight;
    private Point[] mPoints = new Point[5];
    private Point mControlPoint = new Point();

    public BezierWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mBaseHeight = mHeight / 2;

        int step = w / 2;
        int stepHeight = step / 4;

        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i] = new Point();
            mPoints[i].set(step * (i - 2), mBaseHeight);
        }

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);  // 绘制
        mPaint.setStyle(Paint.Style.FILL);  // 填充
        mPaint.setStrokeWidth(5);

        mPath = new Path();

        setAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.WHITE);
        canvas.drawRect(-mWidth, 0, mWidth, mHeight, mPaint);

        mPath.reset();      // 路径重置
        mPath.moveTo(mPoints[0].x, mPoints[0].y);

        for (int i = 0; i < mPoints.length - 1; i++) {
            int x = (mPoints[i].x + mPoints[i + 1].x) / 2;
            int y = mPoints[i + 1].y + 100 * (0 == i % 2 ? 1 : -1);
            mPath.quadTo(x + offset, y, mPoints[i + 1].x + offset, mPoints[i + 1].y);
        }

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(-mWidth, mHeight);
        mPath.lineTo(-mWidth, 0);

        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.BLUE);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x + offset, mPoints[i].y, 3, mPaint);
        }
    }

    private int offset = 300;
    private void setAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mWidth);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();        // 更新偏移值
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

}
