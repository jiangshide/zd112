package com.android.refresh.interfaces;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public interface DefaultRefreshFooterCreator {
    @NonNull
    RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout);
}
