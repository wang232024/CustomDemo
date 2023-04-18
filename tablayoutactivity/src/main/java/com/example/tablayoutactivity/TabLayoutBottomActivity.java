package com.example.tablayoutactivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutBottomActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentFragmentPagerAdapter mContentFragmentPagerAdapter;
    private List<Fragment> mTabFragments;
    private static final int fragmentNum = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_bottom);

        initViews();
        initDatas();
    }

    private void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initDatas() {
        mTabFragments = new ArrayList<>();
        for (int i = 0; i < fragmentNum; i++) {
            mTabFragments.add(TabContentFragment.newInstance("Fragment" + i));
        }
        mContentFragmentPagerAdapter = new ContentFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mContentFragmentPagerAdapter);

        // TabLayout.MODE_FIXED TabLayout.MODE_SCROLLABLE
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        ViewCompat.setElevation(mTabLayout, 10);
        mTabLayout.setupWithViewPager(mViewPager);

        // add bottom item tab
        for (int i = 0; i < fragmentNum; i++) {
            TabLayout.Tab itemTab = mTabLayout.getTabAt(i);
            if (itemTab != null) {
                itemTab.setCustomView(R.layout.item_tab_layout_custom);
                TextView itemTv = itemTab.getCustomView().findViewById(R.id.item_tab_layout_custom_tv);
                itemTv.setText(i + "");
            }
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    private class ContentFragmentPagerAdapter extends FragmentPagerAdapter {
        public ContentFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mTabFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTabFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + "";
        }
    }

}
