package com.example.customutil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private Context mContext;
    private int mLayout;
    private List<String> mList;

    public CustomerAdapter(Context context, int layout, List<String> list) {
        mContext = context;
        mLayout = layout;
        mList = list;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(mLayout, null, false);
        CustomerViewHolder customerViewHolder = new CustomerViewHolder(view);
        return customerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder customerViewHolder, int i) {
        TextView tv = customerViewHolder.tv;
        tv.setText(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    public void update(RecyclerView recyclerView) {
        notifyDataSetChanged();
        recyclerView.scrollToPosition(mList.size() - 1);
    }

}
