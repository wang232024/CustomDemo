package com.example.custom;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.custom.example.RecyclerViewDemoActivity;
import com.custom.savedb.GreenDaoActivity;
import com.example.animation.AnimationActivity;
import com.example.animation.PropertyAnimatorActivity;
import com.example.basedesignlayout.BaseDesignLayoutActivity;
import com.example.custom.activity.BezierActivity;
import com.example.custom.activity.CustomViewActivity;
import com.example.custom.activity.DealWithActivity;
import com.example.custom.activity.DownloadActivity;
import com.example.custom.activity.EditTextActivity;
import com.example.custom.activity.ExcelActivity;
import com.example.custom.activity.FragmentActivity;
import com.example.custom.activity.ImmersionActivity;
import com.example.custom.activity.IntentActivity;
import com.example.custom.activity.LockPatternActivity;
import com.example.custom.activity.LoginActivity;
import com.example.custom.activity.SettingsActivity;
import com.example.custom.activity.StyleActivity;
import com.example.custom.activity.WelcomActivity;
import com.example.custom.activity.WidgetActivity;
import com.example.custom.activity.WindowActivity;
import com.example.custom.recyclerview.ItemBean;
import com.example.custom.recyclerview.ItemSpaceDecoration;
import com.example.roomdemo.MainRoomActivity;
import com.example.util.ManifestUtil;
import com.example.custom.recyclerview.CustomRecyclerViewAdapter;
//import com.example.custom.recyclerview.ItemSpaceDecoration;
import com.example.util.ShareUtil;
import com.example.util.UriUtil;
import com.example.customutil.UtilActivity;
import com.example.demo.TouchActivity;
import com.example.eventbus.EventBusActivity;
import com.example.gifdemo.GifActivity;
import com.example.glide.GlideActivity;
import com.example.selectdelete.SelectDeleteRecyclerViewActivity;
import com.example.tablayoutactivity.TabLayoutActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends Activity {
    private static final String TAG = "wtx_RecyclerViewActivity";
    private Context mContext = RecyclerViewActivity.this;
    private CustomRecyclerViewAdapter mCustomRecyclerViewAdapter;
    private List<ItemBean> mList = new ArrayList<>();
    private float mWidth;
    private float mHeight;
    private float mDensity;
    private float mRecyclerviewWidth;
    private float mRecyclerviewHeight;
    private float mItemWidth;
    private float mItemHeight;
    private int mSpanCouont = 3;    // 比例适配，修改此值须重新计算item参考线的比例值。一旦修改这里，须同时修改RecyclerView布局文件中的item参考线percent值。
    private String[] mActivityNames;
    private RecyclerView mRecyclerView;
    private Guideline guideline_recyclerview_left;
    private Guideline guideline_recyclerview_right;
    private Guideline guideline_recyclerview_top;
    private Guideline guideline_recyclerview_bottom;

    private static final int mActivityNameId = R.array.activity_names;          // 添加/删除Activity须同时修改mActivityCls和R.array.activity_names
    private static final Class<?>[] mActivityCls = {
            LoginActivity.class,                ExcelActivity.class,            DownloadActivity.class,
            WidgetActivity.class,               WindowActivity.class,           EditTextActivity.class,
            ImmersionActivity.class,
            CustomViewActivity.class,           StyleActivity.class,            LockPatternActivity.class,
            WelcomActivity.class,               AnimationActivity.class,         BezierActivity.class,
            DealWithActivity.class,             IntentActivity.class,           FragmentActivity.class,
            SettingsActivity.class,             TabLayoutActivity.class,        BaseDesignLayoutActivity.class,
            RecyclerViewDemoActivity.class,     GifActivity.class,
            GreenDaoActivity.class,             MainRoomActivity.class,
            GlideActivity.class,                EventBusActivity.class,         SelectDeleteRecyclerViewActivity.class,
            UtilActivity.class,                 TouchActivity.class,            PropertyAnimatorActivity.class,
            RetrofitRxjavaDownloadActivity.class,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerviewactivity);

        ManifestUtil.initPermission(this);
        initView();
        initData();
        fillData();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initData();
    }

    private BroadcastReceiver mBroadcastReceiver = null;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void initData() {
        // 目标机器 800 * 1280
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        mDensity = metrics.density;
        Log.i("wtx", "[" + mWidth + " * " + mHeight + "], density:" + mDensity);
        // land:[1280.0 * 740.0], density:1.25
        // tc622 mtk O611   [1200.0 * 1928.0], density:1.5

        mRecyclerviewWidth = (getPercent(guideline_recyclerview_right) - getPercent(guideline_recyclerview_left)) * mWidth;
        mRecyclerviewHeight = (getPercent(guideline_recyclerview_bottom) - getPercent(guideline_recyclerview_top)) * mHeight;

        if (mWidth < mHeight) {
            mItemWidth = 0.30625f * mWidth;         // Item宽高
            mItemHeight = 0.11718751f * mHeight;
        } else {
            mItemWidth = 0.30625f * mHeight;         // Item宽高
            mItemHeight = 0.11718751f * mWidth;
        }
        Log.w(TAG, "ratewidth:" + (getPercent(guideline_recyclerview_right) - getPercent(guideline_recyclerview_left)) +
                ", rateheight:" + (getPercent(guideline_recyclerview_bottom) - getPercent(guideline_recyclerview_top)));
        Log.w(TAG, "RecyclerView, [" + mRecyclerviewWidth + ", " + mRecyclerviewHeight + "]");
        Log.w(TAG, "Item, [" + mItemWidth + ", " + mItemHeight + "]");

        if (null == mBroadcastReceiver) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.w(TAG, "onReceive, action:" + action);
                    switch (action) {
                        case Intent.ACTION_SHUTDOWN:

                            break;
                        case Intent.ACTION_REBOOT:

                            break;
                        case Intent.ACTION_SCREEN_OFF:

                            break;
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SHUTDOWN);      // 关机
            intentFilter.addAction(Intent.ACTION_REBOOT);        // 重启
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);    // 熄屏
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);     // 亮屏
            registerReceiver(mBroadcastReceiver, intentFilter);
        }

        // 适配器
        mCustomRecyclerViewAdapter = new CustomRecyclerViewAdapter(mContext, R.layout.layout_recyclerview_item, mList, mItemWidth, mItemHeight);
        mCustomRecyclerViewAdapter.setOnClickListener(mOnClickListener);
        mRecyclerView.setAdapter(mCustomRecyclerViewAdapter);

        // 布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, mSpanCouont);
        mRecyclerView.setLayoutManager(gridLayoutManager);
//        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

//        //给RecyclerView设置ItemTouchHelper，可拖拽
//        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback();
//        itemTouchHelperCallback.setiMoveAndSwipeCallback(new ItemTouchHelperCallback.IMoveAndSwipeCallback() {
//            @Override
//            public void onMove(int prePosition, int postPosition) {
//                Collections.swap(mList, prePosition, postPosition);
//                if (recyclerView.getAdapter() != null) {
//                    recyclerView.getAdapter().notifyItemMoved(prePosition, postPosition);
//                }
//            }
//
//            @Override
//            public void onSwiped(int position) {
//                mList.remove(position);
//                if (recyclerView.getAdapter() != null) {
//                    recyclerView.getAdapter().notifyItemRemoved(position);
//                }
//            }
//        });
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        // 分隔线
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(mSpanCouont, mRecyclerviewWidth, mRecyclerviewHeight);
        mRecyclerView.addItemDecoration(decoration);
    }

    private void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 保持屏幕常亮，或者布局文件中顶层添加android:keepScreenOn="true"

        mRecyclerView = findViewById(R.id.id_recyclerview);
        // 通过参考线计算宽高
        guideline_recyclerview_left = findViewById(R.id.id_guideline_recyclerview_left);
        guideline_recyclerview_right = findViewById(R.id.id_guideline_recyclerview_right);
        guideline_recyclerview_top = findViewById(R.id.id_guideline_recyclerview_top);
        guideline_recyclerview_bottom = findViewById(R.id.id_guideline_recyclerview_bottom);

        mActivityNames = getResources().getStringArray(mActivityNameId);
    }

    /**
     * 获取参考线比例值
     * @param guideline
     * @return
     */
    private float getPercent(Guideline guideline) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        return params.guidePercent;
    }

    private CustomRecyclerViewAdapter.OnClickListener mOnClickListener = (status, position) -> {
        Intent intent = new Intent(mContext, mActivityCls[position]);
        startActivity(intent);
    };

    private void fillData() {
        for (int i = 0; i < mActivityNames.length; i++) {
            ItemBean itemBean1 = new ItemBean();
            itemBean1.setState(mActivityNames[i]);
            itemBean1.setName(mActivityNames[i]);
            itemBean1.setLocation(mActivityNames[i]);
            mList.add(itemBean1);
        }
        mCustomRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void test1() {
        List<String> list = new ArrayList<>();
        list.add(mContext.getFilesDir() + "/test.mp4");
        list.add(mContext.getFilesDir() + "/test1.mp4");
        ShareUtil.shareToApp(mContext, list);
    }

    private void test2() {
        // 打开文件管理器
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");   // 文本
//                intent.setType("video/*");      // 视频
//                intent.setType("audio/*");      // 音频
//                intent.setType("image/*");      // 图片
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 打开多个文件
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ClipData CD = data.getClipData();
            if (CD != null) {
                int count = CD.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = CD.getItemAt(i).getUri();
                    Log.i(TAG, "onActivityResult, multiple, url:" + uri.toString());
                    String string = UriUtil.getFileAbsolutePath(mContext, uri);
                    Log.i(TAG, "onActivityResult, multiple, string:" + string);
                }
            } else {
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程
                Log.i(TAG, "onActivityResult, single, url:" + uri.toString());
                String string = UriUtil.getFileAbsolutePath(mContext, uri);
                Log.i(TAG, "onActivityResult, single, string:" + string);
            }
        }
    }

}
