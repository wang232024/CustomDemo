package com.example.selectdelete;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 长按唤出栏(退出/全选/删除)列表模板
 * TemplateRecyclerViewAdapter      适配器
 * TemplateRecyclerViewBean         元素(数据属性 + isSelect)
 * layout_recyclerview_item         条目布局
 * layout_recyclerview_toolbar      唤出栏布局
 */
public class TemplateRecyclerViewAdapter extends RecyclerView.Adapter<TemplateRecyclerViewAdapter.TemplateViewHolder> {
    private Context mContext;
    private int mLayout;
    protected List<TemplateRecyclerViewBean> mList;
    private int mMode;
    private int mCheckboxVisible = View.INVISIBLE;
    private int mContainerVisible = View.GONE;

    private OnRecyclerViewClickListener mOnRecyclerViewClickListener;
    public interface OnRecyclerViewClickListener {
        void onSingleClickListener(View view, int position);
        void onLongClickListener(View view, int position);
    }
    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener listener) {
        mOnRecyclerViewClickListener = listener;
    }

    private View mLongClickContainer;
    public void setLongClickView(View view) {
        mLongClickContainer = view;
        View cancel = view.findViewById(R.id.recyclerview_longclick_cancel);
        View delete = view.findViewById(R.id.recyclerview_longclick_delete);
        View all = view.findViewById(R.id.recyclerview_longclick_all);
        if (null != cancel) {
            cancel.setOnClickListener(OnContainerClickListener);
        }
        if (null != delete) {
            delete.setOnClickListener(OnContainerClickListener);
        }
        if (null != all) {
            all.setOnClickListener(OnContainerClickListener);
        }
    }

    public void setMode(int mode) {
        Log.i("wtx", "setMode:" + mode);
        mMode = mode;
        if (0 == mMode) {
            mCheckboxVisible = View.INVISIBLE;
            mContainerVisible = View.GONE;
        } else if (1 == mMode) {
            mCheckboxVisible = View.VISIBLE;
            mContainerVisible = View.VISIBLE;
        }
        if (null != mLongClickContainer) {
            mLongClickContainer.setVisibility(mContainerVisible);
        }
        notifyDataSetChanged();
    }

    public TemplateRecyclerViewAdapter(Context context, int layout, List<TemplateRecyclerViewBean> list) {
        mContext = context;
        mLayout = layout;
        mList = list;
        Log.i("wtx", "list.size:" + list.size());
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        return new TemplateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder holder, int p) {
        final int position = p;
        View view = holder.itemView;
        Log.i("wtx", "p:" + p + ", view:" + view + ", getVisibility:" + view.getVisibility());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnRecyclerViewClickListener) {
                    mOnRecyclerViewClickListener.onSingleClickListener(holder.itemView, position);
                    TemplateRecyclerViewBean templateRecyclerViewBean = mList.get(position);
                    templateRecyclerViewBean.setSelect(!templateRecyclerViewBean.isSelect());
                    notifyDataSetChanged();
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnRecyclerViewClickListener) {
                    mOnRecyclerViewClickListener.onLongClickListener(holder.itemView, position);

                    if (null != mLongClickContainer) {
                        TemplateRecyclerViewBean templateRecyclerViewBean = mList.get(position);
                        templateRecyclerViewBean.setSelect(!templateRecyclerViewBean.isSelect());
                        setMode(1);
                    }
                }
                return true;
            }
        });

        TemplateRecyclerViewBean templateRecyclerViewBean = mList.get(position);
        if (null != holder.textView) {
            holder.textView.setText(templateRecyclerViewBean.getName());
        }
        if (null != holder.checkbox) {
            holder.checkbox.setVisibility(mCheckboxVisible);
            holder.checkbox.setClickable(false);
            holder.checkbox.setChecked(templateRecyclerViewBean.isSelect());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TemplateViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView textView;

        public TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.recyclerview_item_checkbox);
            textView = itemView.findViewById(R.id.recyclerview_item_textview);
        }
    }

    View.OnClickListener OnContainerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.recyclerview_longclick_cancel) {
                setMode(0);
            } else if (id == R.id.recyclerview_longclick_delete) {
                if (1 == mMode) {
                    // 列表逆向删除
                    for (int i = mList.size() - 1; 0 <= i; i--) {
                        TemplateRecyclerViewBean templateRecyclerViewBean = mList.get(i);
                        if (templateRecyclerViewBean.isSelect()) {
                            mList.remove(i);
                        }
                    }
                    setMode(0);
                }
            } else if (id == R.id.recyclerview_longclick_all) {
                boolean allSelect = true;
                for (int i = 0; i < mList.size(); i++) {
                    TemplateRecyclerViewBean templateRecyclerViewBean = mList.get(i);
                    if (!templateRecyclerViewBean.isSelect()) {
                        allSelect = false;
                        break;
                    }
                }

                for (int i = 0; i < mList.size(); i++) {
                    TemplateRecyclerViewBean templateRecyclerViewBean = mList.get(i);
                    templateRecyclerViewBean.setSelect(!allSelect);
                }
                notifyDataSetChanged();
            }
        }
    };

}
