package com.example.custom.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.custom.R;
import com.example.custom.fragment.FragmentContent;

public class FragmentActivity extends AppCompatActivity {
    private static final String TAG = "wtx_FragmentActivity";
    private Context mContext;
    private FragmentActivity mFragmentActivity;
    private LinearLayout mDrawer;
    private LinearLayout mContent;
    private float mDrawerMin;
    private float mDrawerMax;
    private float mDrawerDis;
    private ConstraintLayout.LayoutParams mDrawerLayoutParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置布局
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_fragmentactivity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_activity_title);

        Log.i(TAG, "onCreate");
        mContext = FragmentActivity.this;
        mFragmentActivity = FragmentActivity.this;

        mDrawer = findViewById(R.id.drawer);

        mDrawerMin = getResources().getDimension(R.dimen.drawer_min);
        mDrawerMax = getResources().getDimension(R.dimen.drawer_max);
        mDrawerDis = mDrawerMax - mDrawerMin;
        mDrawerLayoutParams = (ConstraintLayout.LayoutParams) mDrawer.getLayoutParams();
        Log.i(TAG, "onCreate, drawer:[" + mDrawerMin + ", " + mDrawerMax + "], distance:" + mDrawerDis);
        // [120, 450], 330

//        handler.sendEmptyMessageDelayed(12, 3000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (12 == msg.what) {
                Log.i(TAG, "now commit Fragment.");
                FragmentTransaction transaction =  mFragmentActivity.getSupportFragmentManager().beginTransaction();
                FragmentContent fragmentContent = new FragmentContent();
                transaction.replace(R.id.content, fragmentContent);
                transaction.commit();
            }
        }
    };

    private float beginX = 0;
    private boolean mDrawing = false;
    private boolean isOpen = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beginX = x;
                if (isOpen) {
                    if (mDrawerMax * 0.8 <= beginX && mDrawerMax * 1.05 >= beginX) {
                        mDrawing = true;
                    }
                } else {
                    if (beginX <= mDrawerMin) {
                        mDrawing = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDrawing &&
                        (mDrawerMin <= x && mDrawerMax >= x)) {
                    if (isOpen) {
                        mDrawerLayoutParams.width = (int) (mDrawerMax + (x - beginX));
                        if (mDrawerMax < mDrawerLayoutParams.width) {
                            mDrawerLayoutParams.width = (int) mDrawerMax;
                        }
                        mDrawer.setLayoutParams(mDrawerLayoutParams);
                    } else {
                        mDrawerLayoutParams.width = (int) (mDrawerMin + (x - beginX));
                        if (mDrawerMin > mDrawerLayoutParams.width) {
                            mDrawerLayoutParams.width = (int) mDrawerMin;
                        }
                        mDrawer.setLayoutParams(mDrawerLayoutParams);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mDrawing) {
                    if (isOpen) {
                        if ((mDrawerDis / 2) <= (beginX - x)) {
                            isOpen = false;
                            mDrawerLayoutParams.width = (int) (mDrawerMin);
                        } else {
                            isOpen = true;
                            mDrawerLayoutParams.width = (int) (mDrawerMax);
                        }
                    } else {
                        if ((mDrawerDis / 2) <= (x - beginX)) {
                            isOpen = true;
                            mDrawerLayoutParams.width = (int) (mDrawerMax);
                        } else {
                            isOpen = false;
                            mDrawerLayoutParams.width = (int) (mDrawerMin);
                        }
                    }
                    mDrawing = false;
                    mDrawer.setLayoutParams(mDrawerLayoutParams);
                }
                break;
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, "onWindowFocusChanged");
    }
}
