package com.android.refresh.interfaces;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;

import com.android.refresh.state.RefreshState;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public interface RefreshKernel {
    @NonNull
    RefreshLayout getRefreshLayout();

    @NonNull
    RefreshContent getRefreshContent();

    RefreshKernel setState(@NonNull RefreshState state);

    /**
     * 结束关闭二极刷新
     *
     * @return RefreshKernel
     */
    RefreshKernel finishTwoLevel();

    /**
     * 移动视图到指定位置
     * moveSpinner 的取名来自 谷歌官方的 {@linkandroidx.swiperefreshlayout.widget.SwipeRefreshLayout}
     *
     * @param spinner    位置 (px)
     * @param isDragging true 手指正在拖动 false 回弹动画执行
     * @return RefreshKernel
     */
    RefreshKernel moveSpinner(int spinner, boolean isDragging);

    /**
     * 执行动画使视图位移到指定的 位置
     * moveSpinner 的取名来自 谷歌官方的 {@link androidx.swiperefreshlayout.widget.SwipeRefreshLayout}
     *
     * @param endSpinner 指定的结束位置 (px)
     * @return ValueAnimator 如果没有执行动画 null
     */
    ValueAnimator animSpinner(int endSpinner);

    /**
     * 指定在下拉时候为 Header 或 Footer 绘制背景
     *
     * @param backgroundColor 背景颜色
     * @return RefreshKernel
     */
    RefreshKernel requestDrawBackgroundFor(RefreshInternal internal, int backgroundColor);

    /**
     * 请求事件
     *
     * @param request 请求
     * @return RefreshKernel
     */
    RefreshKernel requestNeedTouchEventFor(@NonNull RefreshInternal internal, boolean request);

    /**
     * 请求设置默认内容滚动设置
     *
     * @param translation 移动
     * @return RefreshKernel
     */
    RefreshKernel requestDefaultTranslationContentFor(@NonNull RefreshInternal internal, boolean translation);

    /**
     * 请求重新测量 headerHeight 或 footerHeight , 要求 height 高度为 WRAP_CONTENT
     *
     * @return RefreshKernel
     */
    RefreshKernel requestRemeasureHeightFor(@NonNull RefreshInternal internal);

    /**
     * 设置二楼回弹时长
     *
     * @param duration 二楼回弹时长
     * @return RefreshKernel
     */
    RefreshKernel requestFloorDuration(int duration);
}
