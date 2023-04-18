package com.example.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.custom.R;

/**
 * 自定义View基本步骤
 * 1.自定义属性。res/values/attrs文件中，添加declare-styleable属性
 * 2.构造方法中获得自定义的属性参数
 * 3.[重写onMesure]
 * 4.重写onDraw
 */
public class SimpleTextView extends View {
    private static final String TAG = "wtx";
    private Paint mPaint;
    private Rect mBound;

    private String mTestName = "testname";
    private String mTitleText = "SimpleTextView";
    private int mTitleTextColor = Color.RED;
    private float mTitleTextSize = 18f;

    public SimpleTextView(Context context) {
        this(context, null, 0);
    }

    public SimpleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParameter(context, attrs, defStyleAttr);
    }

    private void initParameter(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * 属性名空间为xmlns:app="http://schemas.android.com/apk/res-auto"，使用时以app:testname="xxx"方式赋值。
         */
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleTextView);
        Log.i(TAG, "ta:" + ta);
        int n = ta.getIndexCount();
        Log.i(TAG, "SimpleTextView, n:" + n);
        for (int i = 0; i < n; i++)
        {
            int attr = ta.getIndex(i);
            switch (attr)
            {
                // 这里可以指定任意名称与合法格式
                case R.styleable.SimpleTextView_testname:
                    mTestName = ta.getString(attr);
                    break;
                case R.styleable.SimpleTextView_titleText:
                    mTitleText = ta.getString(attr);
                    Log.w(TAG, "mTitleText:" + mTitleText);
                    break;
                case R.styleable.SimpleTextView_titleTextColor:
                    mTitleTextColor = ta.getColor(attr, mTitleTextColor);
                    Log.w(TAG, "mTitleTextColor:" + mTitleTextColor);
                    break;
                case R.styleable.SimpleTextView_titleTextSize:
                    mTitleTextSize = ta.getDimension(attr, mTitleTextSize);
                    Log.w(TAG, "mTitleTextSize:" + mTitleTextSize);
                    break;
            }
        }
        ta.recycle();
        Log.i(TAG, "mTestName:" + mTestName);

        mPaint = new Paint();
        mBound = new Rect();
        if (null != mTitleText) {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.setColor(mTitleTextColor);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i(TAG, "mTitleText:" + mTitleText);
        Log.i(TAG, "mBound:[" + mBound.left + ", " + mBound.top + ", " + mBound.right + ", " + mBound.bottom + "]");
        Log.i(TAG, "[" + mBound.width() + ", " + mBound.height() + "]");
        Log.i(TAG, "[" + getWidth() + ", " + getHeight() + "]");
        Log.i(TAG, "[" + getMeasuredWidth() + ", " + getMeasuredHeight() + "]");
        canvas.drawText(mTitleText, (getWidth() - mBound.width()) / 2.0f, (getHeight() + mBound.height()) / 2.0f, mPaint);
    }

}
