package com.android.refresh.wrapper;

import android.graphics.PointF;
import android.view.View;

import com.android.refresh.interfaces.ScrollBoundaryDecider;
import com.android.refresh.manager.Manager;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public class ScrollBoundaryDeciderAdapter implements ScrollBoundaryDecider {
    public PointF mActionEvent;
    public ScrollBoundaryDecider boundary;
    public boolean mEnableLoadMoreWhenContentNotFull = true;

    @Override
    public boolean canRefresh(View content) {
        if (boundary != null) {
            return boundary.canRefresh(content);
        }
        return Manager.getInstance().canRefresh(content, mActionEvent);
    }

    @Override
    public boolean canLoadMore(View content) {
        if (boundary != null) {
            return boundary.canLoadMore(content);
        }
        return Manager.getInstance().canLoadMore(content, mActionEvent, mEnableLoadMoreWhenContentNotFull);
    }
}
