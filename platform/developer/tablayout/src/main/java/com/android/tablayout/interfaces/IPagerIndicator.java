package com.android.tablayout.interfaces;

import com.android.tablayout.TabPosition;

import java.util.List;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public interface IPagerIndicator {
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onPositionDataProvide(List<TabPosition> dataList);
}
