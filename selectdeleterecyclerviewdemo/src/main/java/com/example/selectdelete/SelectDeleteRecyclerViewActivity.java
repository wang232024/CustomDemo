package com.example.selectdelete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SelectDeleteRecyclerViewActivity extends AppCompatActivity {
    private static final String TAG = "wtx";
    private Context mContext;
    private List<TemplateRecyclerViewBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_delete_recyclerview);

        mContext = this;
        initData();

        ConstraintLayout recyclerviewLongclickContainer = findViewById(R.id.recyclerview_longclick_container);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_sd);

        TemplateRecyclerViewAdapter templateRecyclerViewAdapter = new TemplateRecyclerViewAdapter(mContext, R.layout.layout_recyclerview_item_sd, mList);
        recyclerView.setAdapter(templateRecyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        templateRecyclerViewAdapter.setOnRecyclerViewClickListener(new TemplateRecyclerViewAdapter.OnRecyclerViewClickListener() {
            @Override
            public void onSingleClickListener(View view, int position) {
                Log.i("wtx", "onSingleClickListener, position:" + position);
            }

            @Override
            public void onLongClickListener(View view, int position) {
                Log.i("wtx", "onLongClickListener, position:" + position);
            }
        });
        templateRecyclerViewAdapter.setLongClickView(recyclerviewLongclickContainer);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TemplateRecyclerViewBean templateRecyclerViewBean = new TemplateRecyclerViewBean();
            templateRecyclerViewBean.setIndex(i);
            templateRecyclerViewBean.setName("test" + i);
            mList.add(templateRecyclerViewBean);
        }
    }

}