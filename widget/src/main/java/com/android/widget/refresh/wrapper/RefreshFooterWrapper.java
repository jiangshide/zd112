package com.android.widget.refresh.wrapper;

import android.view.View;

import com.android.widget.refresh.component.InternalAbstract;
import com.android.widget.refresh.interfaces.RefreshFooter;

/**
 * created by jiangshide on 2019-07-24.
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
