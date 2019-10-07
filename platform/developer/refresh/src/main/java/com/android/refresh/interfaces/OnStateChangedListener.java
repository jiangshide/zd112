package com.android.refresh.interfaces;

import androidx.annotation.NonNull;

import com.android.refresh.state.RefreshState;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public interface OnStateChangedListener {
    /**
     * 状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);
}
