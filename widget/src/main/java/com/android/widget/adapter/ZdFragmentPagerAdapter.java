package com.android.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.widget.ZdNavigator;
import com.android.widget.ZdTabLayout;
import com.android.widget.ZdViewPager;
import com.android.widget.tablayout.adapter.CommonNavigatorAdapter;
import com.android.widget.tablayout.help.ViewPagerHelper;
import com.android.widget.tablayout.indicators.LinePagerIndicator;
import com.android.widget.tablayout.interfaces.IPagerIndicator;
import com.android.widget.tablayout.interfaces.IPagerTitleView;
import com.android.widget.tablayout.titles.ColorTransitionPagerTitleView;
import com.android.widget.tablayout.titles.SimplePagerTitleView;

import java.util.List;


/**
 * created by jiangshide on 2019/2/13.
 * email:18311271399@163.com
 */
//public class CusFragmentPagerAdapter extends FragmentStatePagerAdapter {
public class ZdFragmentPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private Fragment[] mFragments;
    private String[] mTitles;
    private Bundle mBundle;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    private int mLinePagerIndicator = Color.YELLOW;
    private ZdViewPager mZdViewPager;
    private int mSelected = -1;

    public ZdFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ZdFragmentPagerAdapter setFragment(Fragment... fragments) {
        this.mFragments = fragments;
        return this;
    }

    public ZdFragmentPagerAdapter setFragment(List<Fragment> fragments) {
        int size = fragments.size() - 1;
        mFragments = new Fragment[size];
        for (int i = 0; i < size; i++) {
            mFragments[i] = fragments.get(i);
        }
        return this;
    }

    public ZdFragmentPagerAdapter setTitles(String... titles) {
        this.mTitles = titles;
        return this;
    }

    public ZdFragmentPagerAdapter setBundle(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (null != mTitles && mTitles.length >= 0 && position <= mTitles.length) ? mTitles[position] : null;
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragments[position];
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    public ZdFragmentPagerAdapter initTabs(Context context, ZdTabLayout cusTabLayout, final ZdViewPager viewPager) {
        return initTabs(context, cusTabLayout, viewPager, false);
    }

    public ZdFragmentPagerAdapter initTabs(Context context, ZdTabLayout zdTabLayout, final ZdViewPager viewPager, boolean mode) {
        this.mZdViewPager = viewPager;
//        viewPager.addOnPageChangeListener(this);
        zdTabLayout.setBackgroundColor(Color.WHITE);
        ZdNavigator zdNavigator = new ZdNavigator(context);
        zdNavigator.setAdjustMode(mode);
        zdNavigator.setAdapter(commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return (null != mTitles) ? mTitles.length : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setTextSize(16);
                if (null != mTitles && mTitles.length >= index) {
                    simplePagerTitleView.setText(mTitles[index]);
                    simplePagerTitleView.getPaint().setFakeBoldText(true);
                }
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(mLinePagerIndicator);
                linePagerIndicator.setRoundRadius(5);
                return linePagerIndicator;
            }
        });

        zdTabLayout.setNavigator(zdNavigator);
        ViewPagerHelper.bind(zdTabLayout, viewPager);
        return this;
    }

    public ZdFragmentPagerAdapter setLinePagerIndicator(int color) {
        this.mLinePagerIndicator = color;
        if (null != commonNavigatorAdapter) commonNavigatorAdapter.notifyDataSetChanged();
        return this;
    }

    public void onRefresh() {
        mFragments[mZdViewPager.getCurrentItem()].onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mSelected == -1 && mFragments != null) {
            mFragments[position].onResume();
        }
    }

    @Override
    public void onPageSelected(int position) {
        mSelected = position;
        if (mFragments != null) {
            mFragments[position].onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
