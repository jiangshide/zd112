package com.android.widget.refresh.wrapper;

import android.graphics.PointF;
import android.view.View;

import com.android.widget.refresh.ZdRefreshManager;
import com.android.widget.refresh.interfaces.ScrollBoundaryDecider;

/**
 * created by jiangshide on 2019-07-24.
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
        return ZdRefreshManager.getInstance().canRefresh(content, mActionEvent);
    }

    @Override
    public boolean canLoadMore(View content) {
        if (boundary != null) {
            return boundary.canLoadMore(content);
        }
        return ZdRefreshManager.getInstance().canLoadMore(content, mActionEvent, mEnableLoadMoreWhenContentNotFull);
    }
}
