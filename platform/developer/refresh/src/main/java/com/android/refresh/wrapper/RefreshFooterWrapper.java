package com.android.refresh.wrapper;

import android.view.View;

import com.android.refresh.component.InternalAbstract;
import com.android.refresh.interfaces.RefreshFooter;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter {
    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return mWrapperView instanceof RefreshFooter && ((RefreshFooter) mWrapperView).setNoMoreData(noMoreData);
    }
}
