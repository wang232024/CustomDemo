package com.example.common.adapter.list;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.adapter.BaseRecyclerViewAdapter;

import java.util.List;

public class ListRecyclerViewAdapter extends BaseRecyclerViewAdapter<ListItem> {
    public ListRecyclerViewAdapter(Context context, int layout, List<ListItem> list) {
        super(context, layout, list);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new ListViewHolder(view);
    }

    @Override
    public void bindDataView(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            listViewHolder.textView.setText(mList.get(position).getText());
        }
    }
}
