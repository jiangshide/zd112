package com.android.tablayout.titles;

import android.content.Context;
import android.view.View;

import com.android.tablayout.interfaces.IPagerTitleView;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public class DummyPagerTitleView extends View implements IPagerTitleView {
    public DummyPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }
}
