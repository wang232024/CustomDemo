package com.example.custom.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * getItemOffsets中的参数(Rect outRect)，是包裹Item的矩形
 * outRect.set(l, t, r, b);分别设置Item到该矩形的左/上/右/下的距离
 * 这里的设置会影响到显示时Item的间距，但是不会影响Item本身的宽高
 */
public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "wtx_ItemSpaceDecoration";
    private int mSpanCouont;
    private float mSpanWidthValue = 0;        // 调整间隔，首尾贴边，等分间隔，数量为(mSpanCouont - 1)个
    private float mSpanWidthValueRaw = 0;     // 原间隔，item左对齐，并且间距数量为mSpanCouont个
    private float mRecyclerviewWidth = 0;
    private float mRecyclerviewHeight = 0;
    private float mItemWidth = 0;
    private float mItemHeight = 0;

    public ItemSpaceDecoration(int spanCouont, float width, float height) {
        super();
        mSpanCouont = spanCouont;
        mRecyclerviewWidth = width;
        mRecyclerviewHeight = height;
    }

    public ItemSpaceDecoration(int spanCouont, float width, float height, float itemWidth, float itemHeight) {
        super();
        mSpanCouont = spanCouont;
        mRecyclerviewWidth = width;
        mRecyclerviewHeight = height;
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //绘制的内容在itemview下层
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //绘制的内容在itemview上层
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (0 == mItemWidth) {          // 未给出比例宽高时，使用api传入的计算值(int型，有精度下降)
            // 此处只能获取到item的宽高，无法获取RecyclerView的宽高(需初始化时传入)
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            Log.d(TAG, "getItemOffsets, [" + layoutParams.width + ", " + layoutParams.height + "]");
            mItemWidth = layoutParams.width;
            mItemHeight = layoutParams.height;
        }

        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
//        Log.d(TAG, "getItemOffsets, childPosition:" + childPosition + ", itemCount:" + itemCount);
        if (null != manager) {
            if (manager instanceof GridLayoutManager) {
                setGridOffset(childPosition, itemCount, outRect);
            } else if (manager instanceof LinearLayoutManager) {
                // manager为LinearLayoutManager时
                ;
            }
        }
    }

    /**
     * outRect设置的值表示在RecyclerView为每个item分配的Rect的偏移值
     * 分配偏移值后，会作为padding计算入item的宽高中
     * 例如：只设置每行地一个item的bottom值，会导致item的高度增加，其他未设置的item的高度会比第一个大
     * @param position
     * @param count
     * @param outRect
     */
    private void setGridOffset(int position, int count, Rect outRect) {
        if (1 < mSpanCouont) {
            if (0 == mSpanWidthValue) {
                mSpanWidthValue = (mRecyclerviewWidth - mItemWidth * mSpanCouont) / (mSpanCouont - 1);
                mSpanWidthValueRaw = (mRecyclerviewWidth - mItemWidth * mSpanCouont) / mSpanCouont;
                Log.i(TAG, "setGridOffset, mSpanWidthValue:" + mSpanWidthValue + ", mSpanWidthValueRaw:" + mSpanWidthValueRaw);
            }

            if (0 == position % mSpanCouont) {
                // 每行第一个item不右移，下移1个调整间隔
                outRect.set(0, 0, 0, (int) mSpanWidthValue);
            } else if (mSpanCouont - 1 == position % mSpanCouont) {
                // 每行最后一个item右移1个原间隔，下移1个调整间隔
                outRect.set((int) mSpanWidthValueRaw, 0, 0, (int) mSpanWidthValue);
            } else {
                // 每行其他item右移(调整间隔 - 原间隔)，下移1个调整间隔
                outRect.set((int) (mSpanWidthValue - mSpanWidthValueRaw), 0, 0, (int) mSpanWidthValue);
            }
        } else {
            // 网格布局每行只有一个item，暂不考虑
            return;
        }
    }

}
