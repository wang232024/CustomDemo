package com.example.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.custom.R;
import com.example.custom.fragment.WelcomeFragment;

public class WelcomActivity extends AppCompatActivity {
    private static final String TAG = "wtx_WelcomeActivity";
    private ViewPager mViewPager;
    private int[] layouts = new int[] {
            R.layout.fragment_welcome_one,
            R.layout.fragment_welcome_two,
            R.layout.fragment_welcome_three,
            R.layout.fragment_welcome_four,
            R.layout.fragment_welcome_five
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcom);

        initViews();
    }

    private void initViews() {
        mViewPager = findViewById(R.id.welcome_viewpager);
        mViewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        // 为viewpager的滑动添加自定义的动画效果
        mViewPager.setPageTransformer(true, new CrossfadePageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled, position:" + position + ", positionOffset:" +
                        positionOffset + ", positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.w(TAG, "onPageSelected, position:" + position);
                if (position == layouts.length - 1) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WelcomeFragment.newInstance(layouts[position]);
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem: " + position);
            return super.instantiateItem(container, position);
        }
    }

    private class CrossfadePageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            Log.d(TAG, "transformPage, position" + position);

            View backgroundView = page.findViewById(R.id.welcome_fragment);
            View text_head = page.findViewById(R.id.heading);
            View text_content = page.findViewById(R.id.content);

            View object1 = page.findViewById(R.id.a000);
            View object2 = page.findViewById(R.id.a001);
            View object3 = page.findViewById(R.id.a002);

            View object4 = page.findViewById(R.id.a003);
            View object5 = page.findViewById(R.id.a004);
            View object6 = page.findViewById(R.id.a005);
            View object7 = page.findViewById(R.id.a006);

            View object10 = page.findViewById(R.id.a011);
            View object8 = page.findViewById(R.id.a008);
            View object11 = page.findViewById(R.id.a007);

            View object9 = page.findViewById(R.id.a010);
            View object12 = page.findViewById(R.id.a012);
            View object13 = page.findViewById(R.id.a013);

            if (0 <= position && position < 1) {
                /**
                 * [0, 1]右侧page处理,抵消page本身的滑动动画
                 * pageWidth * -position 结果为负 右侧向左
                 */
                page.setTranslationX(pageWidth * (-position));
            }

            if (-1 < position && position < 0) {
                /**
                 * [-1 , 0)左侧page处理,抵消page本身的滑动动画
                 * pageWidth * -position 结果为正 左侧向右
                 */
                page.setTranslationX(pageWidth * (-position));
            }

            if (position <= -1.0f || position >= 1.0f) {
                /**
                 * (-& ~ -1),(1 ~ +&)不可见部分不作处理
                 */
            } else if (position == 0.0f) {
                /**
                 * 当前页面
                 */
            } else {
                /**
                 * [-1.1] 此时 postion为相反数
                 * 针对具体的View,移动产生视差
                 */
                if (backgroundView != null) {
                    backgroundView.setAlpha(1.0f - Math.abs(position));
                }

                if (text_head != null) {
                    text_head.setTranslationX(pageWidth * position);
                    text_head.setAlpha(1.0f - Math.abs(position));
                }

                if (text_content != null) {
                    text_content.setTranslationX(pageWidth * position);
                    text_content.setAlpha(1.0f - Math.abs(position));
                }

                if (object1 != null) {
                    object1.setTranslationX(pageWidth * position);
                }

                // parallax effect
                if (object2 != null) {
                    object2.setTranslationX(pageWidth * position);
                }

                if (object4 != null) {
                    object4.setTranslationX(pageWidth / 2 * position);
                }
                if (object5 != null) {
                    object5.setTranslationX(pageWidth / 2 * position);
                }
                if (object6 != null) {
                    object6.setTranslationX(pageWidth / 2 * position);
                }
                if (object7 != null) {
                    object7.setTranslationX(pageWidth / 2 * position);
                }

                if (object8 != null) {
                    object8.setTranslationX((float) (pageWidth / 1.5 * position));
                }

                if (object9 != null) {
                    object9.setTranslationX(pageWidth / 2 * position);
                }

                if (object10 != null) {
                    object10.setTranslationX(pageWidth / 2 * position);
                }

                if (object11 != null) {
                    object11.setTranslationX((float) (pageWidth / 1.2 * position));
                }

                if (object12 != null) {
                    object12.setTranslationX((float) (pageWidth / 1.3 * position));
                }

                if (object13 != null) {
                    object13.setTranslationX((float) (pageWidth / 1.8 * position));
                }

                if (object3 != null) {
                    object3.setTranslationX((float) (pageWidth / 1.2 * position));
                }
            }

        }
    }

}
