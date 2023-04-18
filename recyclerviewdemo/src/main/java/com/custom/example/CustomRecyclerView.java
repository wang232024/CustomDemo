package com.custom.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView extends RecyclerView {
    private static final String TAG = "wtx";
    private float initY;
    private OnTitleHeadHeightChangedListener onTitleHeadHeightChangedListener;
    private float mContainerTitleHeightBig;
    private float mContainerTitleHeightSmall;

    public interface OnTitleHeadHeightChangedListener {
        void onTitleLengthDecrease(float offset, float getY);
        void onTitleLengthIncrease(float offset, float getY);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnTitleHeadHeightChangedListener(OnTitleHeadHeightChangedListener listener) {
        onTitleHeadHeightChangedListener = listener;
    }

    public void setTitleInfo(float big, float small, float density) {
        mContainerTitleHeightBig = big;
        mContainerTitleHeightSmall = small;
        Log.i(TAG, "setTitleInfo, [" + small + ", " + big + "]");
    }

    // 判断是否RecyclerView中Item是否已处于顶部位置
    private boolean isRecyclerViewOnTop() {
        View firstChild = getChildAt(0);
        if (null != firstChild) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (firstVisiblePosition == 0 && firstChild.getTop() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        boolean intercept = super.onInterceptTouchEvent(e);
//        return intercept;
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        BoeVideoRecyclerViewAdapter adapter = (BoeVideoRecyclerViewAdapter) getAdapter();
//        if (BoeVideoRecyclerViewAdapter.MODE_SELECT == adapter.getMode()) {
//            Log.d(TAG, "MODE_SELECT.");
//            return super.onTouchEvent(e);
//        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 一旦RecyclerView.Adapter中进行了触摸监听，这里将进不来。
                Log.w(TAG, "CustomRecyclerView onTouch ACTION_DOWN");
                initY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = e.getRawY();
                 float offset = currentY - initY;
//                Log.d(TAG, "currentY:" + currentY + ", initY:" + initY);
//                Log.d(TAG, "#### onTouchEvent ACTION_MOVE offset:." + offset + ",getY():" + getY() + ", currentY:" + currentY);
//                Log.d(TAG, "getTop:" + getTop() + ", getHeight:" + getHeight());
//                Log.d(TAG, "--->" + (mContainerTitleHeightSmall));
//                Log.d(TAG, "+++>" + (mContainerTitleHeightBig));
                //手指上滑
                if (offset < 0 && getY() > (mContainerTitleHeightSmall)) {
                    if (onTitleHeadHeightChangedListener != null) {
                        onTitleHeadHeightChangedListener.onTitleLengthDecrease(offset, getY());
                    }
                    initY = currentY;
//                    return false;
                }
                //手指下滑
                if (offset > 0 && isRecyclerViewOnTop() && getY() < (mContainerTitleHeightBig)) {
                    if (onTitleHeadHeightChangedListener != null) {
                        onTitleHeadHeightChangedListener.onTitleLengthIncrease(offset, getY());
                    }
                    initY = currentY;
//                    return false;
                }
                initY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "CustomRecyclerView onTouch ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "CustomRecyclerView onTouch ACTION_CANCEL");
                break;
        }

        boolean touchEvent = super.onTouchEvent(e);
//        Log.e(TAG, "touchEvent:" + touchEvent);
        return touchEvent;
    }

    // 由于事件被cancel，此时拿不到MotionEvent.ACTION_DOWN，故采用这种方式。
    public void onActionCancel(MotionEvent e) {
//        Log.e(TAG, "#### onTouchEvent onActionCancel -> ACTION_DOWN....");
        initY = e.getRawY();
    }

}
