package com.example.bugreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.common.adapter.list.ListItem;
import com.example.common.adapter.list.ListRecyclerViewAdapter;
import com.example.common.KLog;
import com.example.common.adapter.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class BugreportActivity extends AppCompatActivity {
    private static final String TAG = "BugreportActivity";
    private Context mContext = BugreportActivity.this;
    private RecyclerView mBugreportRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bugreport);

        List<ListItem> mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ListItem listItem = new ListItem("test" + i);
            mList.add(listItem);
        }
        ListRecyclerViewAdapter listRecyclerViewAdapter = new ListRecyclerViewAdapter(
                mContext, android.R.layout.simple_list_item_1, mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mBugreportRecyclerView = findViewById(R.id.bugreportRecyclerView);
        mBugreportRecyclerView.setAdapter(listRecyclerViewAdapter);
        mBugreportRecyclerView.setLayoutManager(linearLayoutManager);
        listRecyclerViewAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder holder, int position) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KLog.i(TAG, "" + position);
                    }
                });
            }
        });
    }

}