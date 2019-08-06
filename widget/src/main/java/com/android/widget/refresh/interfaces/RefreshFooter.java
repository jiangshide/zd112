package com.android.widget.refresh.interfaces;

import android.support.annotation.RestrictTo;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;
import static android.support.annotation.RestrictTo.Scope.SUBCLASSES;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
//@RestrictTo({LIBRARY,LIBRARY_GROUP,SUBCLASSES})
public interface RefreshFooter extends RefreshInternal{
    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     * @param noMoreData 是否有更多数据
     * @return true 支持全部加载完成的状态显示 false 不支持
     */
    boolean setNoMoreData(boolean noMoreData);
}
