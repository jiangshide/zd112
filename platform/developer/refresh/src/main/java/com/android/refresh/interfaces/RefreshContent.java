package com.android.refresh.interfaces;

import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public interface RefreshContent {
    @NonNull
    View getView();
    @NonNull
    View getScrollableView();

    void onActionDown(MotionEvent e);

    void setUpComponent(RefreshKernel kernel, View fixedHeader, View fixedFooter);
    void setScrollBoundaryDecider(ScrollBoundaryDecider boundary);

    void setEnableLoadMoreWhenContentNotFull(boolean enable);

    void moveSpinner(int spinner, int headerTranslationViewId, int footerTranslationViewId);

    boolean canRefresh();
    boolean canLoadMore();

    ValueAnimator.AnimatorUpdateListener scrollContentWhenFinished(int spinner);
}
