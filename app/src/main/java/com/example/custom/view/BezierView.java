package com.example.custom.view;

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

import com.example.custom.R;

import java.util.Random;

public class BezierView extends View {
    private static final String TAG = "wtx_BezierView";
    private Point pointControl1;
    private Point pointControl2;
    private Paint mPaint;
    private Path mPath;
    private boolean isBezierLine = true;
    private Point[] mPoints = new Point[6];

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "BezierView, 2");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure, widthMeasureSpec:" + widthMeasureSpec + ", heightMeasureSpec:" + heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged, [" + w + ", " + h + "], [" + oldw + ", " + oldh + "]");

        Random random = new Random();
        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i] = new Point(100 * (i + 1), (1 + random.nextInt(6)) * 100);
        }

        pointControl1 = new Point();
        pointControl2 = new Point();

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout, [" + left + ", " + top + ", " + right + ", " + bottom + "]");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.RED);
        // 坐标点
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 2, mPaint);
        }

        mPath.moveTo(mPoints[0].x, mPoints[0].y);
        if (isBezierLine) {
            for (int i = 0; i < mPoints.length - 1; i++) {
                // 控制点
                int step = (mPoints[i + 1].x - mPoints[i].x) / 2;
                pointControl1.set(mPoints[i].x + step, mPoints[i].y);
                pointControl2.set(mPoints[i].x + step, mPoints[i + 1].y);
                mPaint.setColor(Color.YELLOW);
                canvas.drawCircle(pointControl1.x, pointControl1.y, 1, mPaint);
                // 曲线
                mPath.cubicTo(pointControl1.x, pointControl1.y, pointControl2.x, pointControl2.y, mPoints[i + 1].x, mPoints[i + 1].y);
            }
        } else {
            // 直线
            for (int i = 0; i < mPoints.length - 1; i++) {
                mPath.lineTo(mPoints[i + 1].x, mPoints[i + 1].y);
            }
        }

        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, mPaint);
    }

}
