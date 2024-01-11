package com.example.custom.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custom.R;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomHolder> {
    private static final String TAG = "wtx_CustomRecyclerViewAdapter";
    private final Context mContext;
    private final int mLayout;
    private final List<ItemBean> mList;
    private final float mItemWidth;
    private final float mItemHeight;
    private int mStatus = 0;     // 0:普通模式   1:删除模式
    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void onClick(int status, int position);
    }

    public CustomRecyclerViewAdapter(Context context, int layout, List<ItemBean> list, float itemWidth, float itemHeight) {
        mContext = context;
        mLayout = layout;
        mList = list;
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) mItemWidth;
        layoutParams.height = (int) mItemHeight;
        view.setLayoutParams(layoutParams);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        ItemBean itemBean = mList.get(position);
        if (null != holder.state) {
            holder.state.setText(itemBean.getState());
        }
        if (null != holder.name) {
            holder.name.setText(itemBean.getName());
        }
        if (null != holder.location) {
            holder.location.setText(itemBean.getLocation());
        }
        holder.itemView.setOnClickListener(v -> {
            if (null != mOnClickListener) {
                mOnClickListener.onClick(mStatus, position);
            }
        });
        if (null != holder.select) {
            if (0 == mStatus) {
                holder.select.setVisibility(View.INVISIBLE);
            } else if (1 == mStatus) {
                holder.select.setVisibility(View.VISIBLE);
            }
            holder.select.setOnCheckedChangeListener(null); // ViewHolder复用时，setChecked会触发OnCheckedChangeListener，这里置为null避免异常触发监听
            holder.select.setChecked(itemBean.isSelect());
            final int p = position;
            holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG, "onCheckedChanged, position:" + p + ", isChecked:" + isChecked);
                    mList.get(p).setSelect(isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomHolder extends RecyclerView.ViewHolder {
        private TextView state;
        private TextView name;
        private TextView location;
        private CheckBox select;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
//            state = itemView.findViewById(R.id.id_recyclerview_item_state);
            name = itemView.findViewById(R.id.id_recyclerview_item_name);
//            location = itemView.findViewById(R.id.id_recyclerview_item_location);
            select = itemView.findViewById(R.id.id_recyclerview_item_select);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
