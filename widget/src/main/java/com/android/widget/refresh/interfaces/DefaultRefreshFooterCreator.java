package com.android.widget.refresh.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public interface DefaultRefreshFooterCreator {
    @NonNull
    RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout);
}
