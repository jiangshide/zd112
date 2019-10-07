package com.android.refresh.interfaces;

/**
 * created by jiangshide on 2016-07-24.
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
