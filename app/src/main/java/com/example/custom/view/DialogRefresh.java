package com.example.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.custom.R;


public class DialogRefresh extends View {
    private static final String TAG = "wtx_DialogRefresh";
    private Paint mPaintStroke;
    private Paint mPaintFill;
    private Paint mPaintClear;
    private int paddingTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private float mCapsuleCorner;
    private float mCapsuleWidth;
    private float mCapsuleHeight;
    private float mCapsuleSpacing;
    private float mCapsuleStrkeWidth;
    private float mViewWidth;
    private float mViewHeight;
    private static final int mCapsuleNum = 4;
    private float mMarginStart;
    private float mMarginTop;

    public DialogRefresh(Context context) {
        super(context);
    }

    public DialogRefresh(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
//        String text = ta.getString(R.styleable.CustomTextView_text);

//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Layout_width);
//        String text = ta.getString(R.styleable.CustomTextView_text);

        String xmlns="http://schemas.android.com/apk/res/android";
        int xmlRes = attrs.getAttributeResourceValue(xmlns, "background", -1);
        String xmlText = attrs.getAttributeValue(xmlns, "text");
        float layout_width = attrs.getAttributeFloatValue(xmlns, "layout_width", -1);

        Log.i(TAG, "layout_width:" + layout_width);

        init(context);
    }

    private void init(Context context) {
        mCapsuleCorner = context.getResources().getDimension(R.dimen.capsule_corner);
        mCapsuleWidth = context.getResources().getDimension(R.dimen.capsule_width);
        mCapsuleHeight = context.getResources().getDimension(R.dimen.capsule_height);
        mCapsuleSpacing = context.getResources().getDimension(R.dimen.capsule_spacing);
        mCapsuleStrkeWidth = context.getResources().getDimension(R.dimen.capsule_strke_width);

        mPaintStroke = new Paint();
        mPaintStroke.setColor(Color.BLACK);
        mPaintStroke.setAntiAlias(true);
        mPaintStroke.setStyle(Paint.Style.STROKE);  // 描边
        mPaintStroke.setStrokeWidth(mCapsuleStrkeWidth);

        mPaintFill = new Paint();
        mPaintFill.setColor(Color.BLACK);
        mPaintFill.setAntiAlias(true);
        mPaintFill.setStyle(Paint.Style.FILL);  // 填充
        mPaintFill.setStrokeWidth(mCapsuleStrkeWidth);

        mPaintClear = new Paint();
        mPaintClear.setColor(Color.WHITE);
        mPaintClear.setAntiAlias(true);
        mPaintClear.setStyle(Paint.Style.FILL);  // 填充
        mPaintClear.setStrokeWidth(mCapsuleStrkeWidth);
    }

    public int getRealWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY) {
            return paddingLeft + paddingRight + widthVal;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mPaintStroke.ascent() - mPaintStroke.descent()) + paddingLeft + paddingRight);
        } else {
            return (int) Math.min((Math.abs(mPaintStroke.ascent() - mPaintStroke.descent()) + paddingLeft + paddingRight), widthVal);
        }
    }

    public int getRealHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightVal = MeasureSpec.getSize(heightMeasureSpec);
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        if (heightMode == MeasureSpec.EXACTLY) {
            return paddingTop + paddingBottom + heightVal;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            return (int) (Math.abs(mPaintStroke.ascent() - mPaintStroke.descent()) + paddingTop + paddingBottom);
        } else {
            return (int) Math.min((Math.abs(mPaintStroke.ascent() - mPaintStroke.descent()) + paddingTop + paddingBottom), heightVal);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int usedHeight = getRealHeight(heightMeasureSpec);
        int usedWidth = getRealWidth(widthMeasureSpec);
        setMeasuredDimension(usedWidth, usedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = right - left;
        mViewHeight = bottom - top;

        mMarginStart = (mViewWidth - mCapsuleNum * mCapsuleWidth - (mCapsuleNum - 1) * mCapsuleSpacing) / 2;
        mMarginTop = (mViewHeight - mCapsuleHeight) / 2;
    }

    private int mFillIndex = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawEmpty(canvas, mPaintClear);
        for (int i = 0; i < mCapsuleNum; i++) {
            if (mFillIndex == i) {
                drawCapsule(canvas, mMarginStart + (mCapsuleWidth + mCapsuleSpacing) * i, mMarginTop, mPaintFill);
            } else {
                drawCapsule(canvas, mMarginStart + (mCapsuleWidth + mCapsuleSpacing) * i, mMarginTop, mPaintStroke);
            }
        }
        mFillIndex = mFillIndex + 1;
        mFillIndex = mFillIndex % mCapsuleNum;
        postInvalidateDelayed(1000);
    }

    private void drawEmpty(Canvas canvas, Paint paint) {
        RectF rect = new RectF(0, 0, mViewWidth, mViewHeight);
        canvas.drawRect(rect, paint);
    }

    private void drawCapsule(Canvas canvas, float leftBegin, float TopBegin, Paint paint) {
        Path path = new Path();
        RectF oval1 = new RectF(
                leftBegin + mCapsuleStrkeWidth,
                TopBegin + mCapsuleStrkeWidth,
                leftBegin + mCapsuleStrkeWidth + mCapsuleCorner * 2,
                TopBegin + mCapsuleStrkeWidth + mCapsuleCorner * 2
        );
        path.addArc(oval1, 90, 180);
        path.lineTo(leftBegin + (mCapsuleWidth - mCapsuleCorner) + mCapsuleStrkeWidth, TopBegin + mCapsuleStrkeWidth);
        RectF oval2 = new RectF(
                leftBegin + mCapsuleStrkeWidth + (mCapsuleWidth - mCapsuleCorner * 2),
                TopBegin + mCapsuleStrkeWidth,
                leftBegin + mCapsuleStrkeWidth + mCapsuleWidth,
                TopBegin + mCapsuleStrkeWidth + mCapsuleHeight
        );
        path.addArc(oval2, 270, 180);
        path.lineTo(leftBegin + mCapsuleStrkeWidth + mCapsuleCorner, TopBegin + mCapsuleStrkeWidth + mCapsuleHeight);
        canvas.drawPath(path, paint);
    }

}
