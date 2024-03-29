package com.example.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private int mLayout;
    protected List<T> mList;
    private OnClickListener mOnClickListener;

    public BaseRecyclerViewAdapter(Context context, int layout, List<T> list) {
        mContext = context;
        mLayout = layout;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindDataView(holder, position);
        if (mOnClickListener != null) {
            mOnClickListener.onClick(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public abstract RecyclerView.ViewHolder createViewHolder(View view);

    public abstract void bindDataView(RecyclerView.ViewHolder holder, int position);
}
