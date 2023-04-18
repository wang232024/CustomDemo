package com.example.custom.recyclerview;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ItemTouchHelper是谷歌提供的用于实现Recyclerview 拖拽效果的帮助类
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private static final String TAG = "wtx_ItemTouchHelperCallback";
    private IMoveAndSwipeCallback mIMoveAndSwipeCallback;

    public interface IMoveAndSwipeCallback {
        void onMove(int prePosition, int postPosition);
        void onSwiped(int position);
    }

    public void setiMoveAndSwipeCallback(IMoveAndSwipeCallback iMoveAndSwipeCallback) {
        mIMoveAndSwipeCallback = iMoveAndSwipeCallback;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //支持上下拖拽
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        //item支持左滑
        final int swipeFlags = ItemTouchHelper.RIGHT;

//        return makeMovementFlags(dragFlags, swipeFlags);
        return makeMovementFlags(dragFlags, 0);
    }

    /**
     * 长按后拖拽结束后（手指抬起）会回调的方法
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        Log.i(TAG, "onMove, first:" + viewHolder.getAdapterPosition() + ", second:" + target.getAdapterPosition());
        if (mIMoveAndSwipeCallback != null) {
            mIMoveAndSwipeCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    /**
     * 快速点击拖动侧滑回调
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.w(TAG, "onSwiped, direction:" + direction);
//        if (iMoveAndSwipeCallback != null) {
//            iMoveAndSwipeCallback.onSwiped(viewHolder.getAdapterPosition());
//        }
    }
}
