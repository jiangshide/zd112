package com.android.widget.recycleview.listener;

/**
 * created by jiangshide on 2019-09-16.
 * email:18311271399@163.com
 */
public interface OnViewPagerListener {
  /*初始化完成*/
  void onInitComplete();

  /*释放的监听*/
  void onPageRelease(boolean isNext, int position);

  /*选中的监听以及判断是否滑动到底部*/
  void onPageSelected(int position, boolean isBottom);
}
