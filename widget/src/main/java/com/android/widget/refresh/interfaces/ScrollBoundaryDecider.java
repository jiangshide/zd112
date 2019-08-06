package com.android.widget.refresh.interfaces;

import android.view.View;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public interface ScrollBoundaryDecider {
    /**
     * 根据内容视图状态判断是否可以开始下拉刷新
     * @param content 内容视图
     * @return true 将会触发下拉刷新
     */
    boolean canRefresh(View content);
    /**
     * 根据内容视图状态判断是否可以开始上拉加载
     * @param content 内容视图
     * @return true 将会触发加载更多
     */
    boolean canLoadMore(View content);
}
