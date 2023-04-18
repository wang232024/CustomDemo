package com.custom.example;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {
    private static final String TAG = "wtx";
    private Context mContext;
    private List<CustomBean> mList;
    private int mItemLayout;
    private long mTouchTime;
    private OnClickListener mOnClickListener;
    private CustomRecyclerView mCustomRecyclerView;

    public void setRecyclerView(CustomRecyclerView recyclerView) {
        mCustomRecyclerView = recyclerView;
    }

    public interface OnClickListener {
        void onClickShort(int position);
        void onClickLong(int position);
    }

    public CustomRecyclerViewAdapter(Context context, List<CustomBean> list, int layout) {
        mContext = context;
        mList = list;
        mItemLayout = layout;
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mItemLayout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final int p = position;
        CustomBean bean = mList.get(position);

        holder.imageView.setBackgroundColor(Color.GRAY);
        holder.textView.setText(bean.getName());

        // 这里设置触摸监听，一旦滑动，将ACTION_DOWN -> ACTION_CANCEL，后续ACTION_MOVE和ACTION_UP将由RecyclerView进行处理
        // 如果不滑动，则直接走这里的ACTION_DOWN -> ACTION_UP
        holder.itemView.setClickable(true);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long touchTime = System.currentTimeMillis();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "CustomRecyclerViewAdapter onTouch ACTION_DOWN");
                        mTouchTime = touchTime;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "CustomRecyclerViewAdapter onTouch ACTION_UP");
                        if (null != mOnClickListener) {
                            if (1000 < (touchTime - mTouchTime)) {
                                mOnClickListener.onClickLong(p);
                            } else {
                                mOnClickListener.onClickShort(p);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "CustomRecyclerViewAdapter onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.i(TAG, "CustomRecyclerViewAdapter onTouch ACTION_CANCEL");
                        if (null != mCustomRecyclerView) {
                            // RecyclerView中接收不到ACTION_DOWN，故此时通知该事件
                            mCustomRecyclerView.onActionCancel(event);
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerview_item_img);
            textView = itemView.findViewById(R.id.recyclerview_item_tv);
        }
    }

}
