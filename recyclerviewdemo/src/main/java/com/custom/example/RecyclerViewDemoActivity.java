package com.custom.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDemoActivity extends AppCompatActivity {
    private static final String TAG = "wtx";
    private Context mContext = RecyclerViewDemoActivity.this;
    private TextView mTitle;
    private CustomRecyclerView mCustomRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private List<CustomBean> mList = new ArrayList<>();
    private int mWidth;
    private int mHeight;
    private float mDensity;
    private float mTitleHeightBig;
    private float mTitleHeightSmall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerviewdemo);

        mTitle = findViewById(R.id.title_recyclerviewdemo);
        mCustomRecyclerView = findViewById(R.id.custom_recyclerview);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        CustomRecyclerViewAdapter mCustomRecyclerViewAdapter = new CustomRecyclerViewAdapter(mContext, mList, R.layout.recyclerviewdemo_item);
        mCustomRecyclerViewAdapter.setOnClickListener(mOnClickListener);
        mCustomRecyclerViewAdapter.setRecyclerView(mCustomRecyclerView);

        mCustomRecyclerView.setLayoutManager(mGridLayoutManager);
        mCustomRecyclerView.setAdapter(mCustomRecyclerViewAdapter);
        mCustomRecyclerView.setOnTitleHeadHeightChangedListener(mOnTitleHeadHeightChangedListener);

        DisplayMetrics metrics2 = getResources().getDisplayMetrics();
        mWidth = metrics2.widthPixels;
        mHeight = metrics2.heightPixels;
        mDensity = metrics2.density;
        Log.e(TAG, "width=" + mWidth + "; height=" + mHeight + ", density:" + mDensity);
        mTitleHeightBig = 100 * mDensity;
        mTitleHeightSmall = 50 * mDensity;
        mCustomRecyclerView.setTitleInfo(mTitleHeightBig, mTitleHeightSmall, mDensity);

        for (int i = 0; i < 100; i++) {
            CustomBean bean = new CustomBean();
            bean.setIndex(i);
            bean.setName("name" + i);
            bean.setPath("testpath");
            mList.add(bean);
        }
    }

    private CustomRecyclerViewAdapter.OnClickListener mOnClickListener = new CustomRecyclerViewAdapter.OnClickListener() {
        @Override
        public void onClickShort(int position) {
            Log.i(TAG, "onClickShort:" + position);
        }

        @Override
        public void onClickLong(int position) {
            Log.i(TAG, "onClickLong:" + position);
        }
    };

    // 标题栏高度变化
    CustomRecyclerView.OnTitleHeadHeightChangedListener mOnTitleHeadHeightChangedListener = new CustomRecyclerView.OnTitleHeadHeightChangedListener() {
        @Override
        public void onTitleLengthDecrease(float offset, float getY) {
            Log.d(TAG, "onTitleLengthDecrease offset:" + offset + ", getY:" + getY);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mTitle.getLayoutParams();
            layoutParams.height = (int) (layoutParams.height + offset);
            if (layoutParams.height <= mTitleHeightSmall) {
                layoutParams.height = (int) mTitleHeightSmall;
            }
            mTitle.setLayoutParams(layoutParams);
            mTitle.invalidate();
        }

        @Override
        public void onTitleLengthIncrease(float offset, float getY) {
            Log.d(TAG, "onTitleLengthIncrease offset:" + offset + ", getY:" + getY);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mTitle.getLayoutParams();
            layoutParams.height = (int) (layoutParams.height + offset);
            if (layoutParams.height >= mTitleHeightBig) {
                layoutParams.height = (int) mTitleHeightBig;
            }
            mTitle.setLayoutParams(layoutParams);
            mTitle.invalidate();
        }
    };

}