package com.example.custom.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * getItemOffsets中的参数(Rect outRect)，是包裹Item的矩形
 * outRect.set(l, t, r, b);分别设置Item到该矩形的左/上/右/下的距离
 * 这里的设置会影响到显示时Item的间距，但是不会影响Item本身的宽高
 */
public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "wtx_ItemSpaceDecoration";
    private final int mSpanCouont;
    private float mSpanWidthValueDes = 0;        // 调整间隔，首尾贴边，等分间隔，数量为(mSpanCouont - 1)个
    private float mSpanWidthValueSrc = 0;     // 原间隔，item左对齐，并且间距数量为mSpanCouont个

    /**
     * type 0:根据recyclerviewWidth, mItemWidth, spanCouont计算间距
     *      1:根据recyclerviewWidth, 最终间距, spanCouont计算间距/条目宽度
     *
     * @param recyclerviewWidth recyclerviewWidth
     * @param value value
     * @param spanCouont spanCouont
     * @param type type
     */
    public ItemSpaceDecoration(float recyclerviewWidth, float value, int spanCouont, int type) {
        mSpanCouont = spanCouont;
        float mItemWidth;
        switch (type) {
            case 0:
                mItemWidth = value;
                mSpanWidthValueDes = (recyclerviewWidth - mItemWidth * spanCouont) / (spanCouont - 1);
                mSpanWidthValueSrc = (recyclerviewWidth - mItemWidth * spanCouont) / spanCouont;
                break;
            case 1:
                mSpanWidthValueDes = value;
                mItemWidth = (recyclerviewWidth - mSpanWidthValueDes * (spanCouont - 1)) / spanCouont;
                mSpanWidthValueSrc = (recyclerviewWidth - mItemWidth * spanCouont) / spanCouont;
            default:
                break;
        }
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
        /**
         * outRect设置的值表示在RecyclerView为每个item分配的Rect的偏移值
         * 分配偏移值后，会作为padding计算入item的宽高中
         * 例如：只设置每行地一个item的bottom值，会导致item的高度增加，其他未设置的item的高度会比第一个大
         */
        int childPosition = parent.getChildAdapterPosition(view);
        int indexLine = childPosition % mSpanCouont;
        if (indexLine == 0) {
            // 第一列左边距为0
            outRect.left = 0;
        } else if (indexLine == mSpanCouont - 1) {
            // 最后一列右移一个原边距
            outRect.left = (int) mSpanWidthValueSrc;
        } else {
            // 其他列右移 (边距差) * 索引
            outRect.left = (int) ((mSpanWidthValueDes - mSpanWidthValueSrc) * indexLine);
        }
        outRect.right = 0;

        int intervalHalf = (int) (mSpanWidthValueDes / 2);
        if (childPosition / mSpanCouont == 0) {
            outRect.top = 0;
        } else {
            outRect.top = intervalHalf;
        }
        outRect.bottom = intervalHalf;
    }
}
