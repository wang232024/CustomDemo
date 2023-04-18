package com.example.basedesignlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseDesignLayoutActivity extends AppCompatActivity {
    private String[] mLists;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basedesignlayout);

        initViews();
    }

    private void initViews() {
        mLists = new String[50];
        for (int i = 0; i < 50; i++) {
            mLists[i] = "" + i;
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseDesignLayoutActivity.this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(BaseDesignLayoutActivity.this).inflate(R.layout.recyclerview_item, parent, false);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view){};
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String string = mLists[position];

                View view = holder.itemView;
                TextView tv = view.findViewById(R.id.recyclerview_item_tv);
                tv.setText(string);
            }

            @Override
            public int getItemCount() {
                return mLists.length;
            }
        });
    }
}