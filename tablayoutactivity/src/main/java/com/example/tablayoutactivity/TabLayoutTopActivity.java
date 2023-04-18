package com.example.tablayoutactivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutTopActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentFragmentPagerAdapter mContentFragmentPagerAdapter;

    private List<Fragment> mTabFragments;
    private static final int fragmentNum = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_top);

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
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.gray),
                ContextCompat.getColor(this, R.color.white));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(mTabLayout, 10);
        mTabLayout.setupWithViewPager(mViewPager);
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
