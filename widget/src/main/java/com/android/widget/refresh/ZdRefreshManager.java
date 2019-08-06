package com.android.widget.refresh;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class ZdRefreshManager {

    private ZdRefreshManager() {
    }

    private static class ZdRefreshManagerHolder {
        private static final ZdRefreshManager instance = new ZdRefreshManager();
    }

    public static ZdRefreshManager getInstance() {
        return ZdRefreshManagerHolder.instance;
    }

    /**
     * 判断内容是否可以刷新
     *
     * @param targetView 内容视图
     * @param touch      按压事件位置
     * @return 是否可以刷新
     */
    public boolean canRefresh(@NonNull View targetView, PointF touch) {
        if (canScrollUp(targetView) && targetView.getVisibility() == View.VISIBLE) {
            return false;
        }
        if (targetView instanceof ViewGroup && touch != null) {
            ViewGroup viewGroup = (ViewGroup) targetView;
            final int childCount = viewGroup.getChildCount();
            PointF point = new PointF();
            for (int i = childCount; i > 0; i--) {
                View child = viewGroup.getChildAt(i - 1);
                if (isTransformedTouchPointInView(viewGroup, child, touch.x, touch.y, point)) {
                    touch.offset(point.x, point.y);
                    boolean can = canRefresh(child, touch);
                    touch.offset(-point.x, -point.y);
                    return can;
                }
            }
        }
        return true;
    }

    /**
     * 判断内容视图是否可以加载更多
     *
     * @param targetView  内容视图
     * @param touch       按压事件位置
     * @param contentFull 内容是否填满页面 (未填满时，会通过canScrollUp自动判断)
     * @return 是否可以刷新
     */
    public boolean canLoadMore(@NonNull View targetView, PointF touch, boolean contentFull) {
        if (canScrollDown(targetView) && targetView.getVisibility() == View.VISIBLE) {
            return false;
        }
        //touch == null 时 canLoadMore 不会动态递归搜索
        if (targetView instanceof ViewGroup && touch != null) {
            ViewGroup viewGroup = (ViewGroup) targetView;
            final int childCount = viewGroup.getChildCount();
            PointF point = new PointF();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                if (isTransformedTouchPointInView(viewGroup, child, touch.x, touch.y, point)) {
                    touch.offset(point.x, point.y);
                    boolean can = canLoadMore(child, touch, contentFull);
                    touch.offset(-point.x, -point.y);
                    return can;
                }
            }
        }
        return (contentFull || canScrollUp(targetView));
    }

    public boolean canScrollUp(@NonNull View targetView) {
        if (Build.VERSION.SDK_INT < 14) {
            if (targetView instanceof AbsListView) {
                final ViewGroup viewGroup = (ViewGroup) targetView;
                final AbsListView absListView = (AbsListView) targetView;
                return viewGroup.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0
                        || viewGroup.getChildAt(0).getTop() < targetView.getPaddingTop());
            } else {
                return targetView.getScrollY() > 0;
            }
        } else {
            return targetView.canScrollVertically(-1);
        }
    }

    public boolean canScrollDown(@NonNull View targetView) {
        if (Build.VERSION.SDK_INT < 14) {
            if (targetView instanceof AbsListView) {
                final ViewGroup viewGroup = (ViewGroup) targetView;
                final AbsListView absListView = (AbsListView) targetView;
                final int childCount = viewGroup.getChildCount();
                return childCount > 0 && (absListView.getLastVisiblePosition() < childCount - 1
                        || viewGroup.getChildAt(childCount - 1).getBottom() > targetView.getPaddingBottom());
            } else {
                return targetView.getScrollY() < 0;
            }
        } else {
            return targetView.canScrollVertically(1);
        }
    }

    public boolean isTransformedTouchPointInView(@NonNull View group, @NonNull View child, float x, float y, PointF outLocalPoint) {
        if (child.getVisibility() != View.VISIBLE) {
            return false;
        }
        final float[] point = new float[2];
        point[0] = x;
        point[1] = y;
//        transformPointToViewLocal(group, child, point);
        point[0] += group.getScrollX() - child.getLeft();
        point[1] += group.getScrollY() - child.getTop();
//        final boolean isInView = pointInView(child, point[0], point[1], 0);
        final boolean isInView = point[0] >= 0 && point[1] >= 0
                && point[0] < (child.getWidth())
                && point[1] < ((child.getHeight()));
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(point[0] - x, point[1] - y);
        }
        return isInView;
    }

    public int getColor(@NonNull Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorId);
        }
        return context.getResources().getColor(colorId);
    }

    public int measureViewHeight(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        int childHeightSpec;
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        if (p.height > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(p.height, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
        return view.getMeasuredHeight();
    }

    public void scrollListBy(@NonNull AbsListView listView, int y) {
        if (Build.VERSION.SDK_INT >= 19) {
            listView.scrollListBy(y);
        } else if (listView instanceof ListView) {
            final int firstPosition = listView.getFirstVisiblePosition();
            if (firstPosition == ListView.INVALID_POSITION) {
                return;
            }

            final ViewGroup listGroup = listView;
            final View firstView = listGroup.getChildAt(0);
            if (firstView == null) {
                return;
            }

            final int newTop = firstView.getTop() - y;
            ((ListView) listView).setSelectionFromTop(firstPosition, newTop);
        } else {
            listView.smoothScrollBy(y, 0);
        }
    }

    public boolean isScrollableView(View view) {
        return view instanceof AbsListView
                || view instanceof ScrollView
                || view instanceof ScrollingView
                || view instanceof WebView
                || view instanceof ViewPager
                || view instanceof NestedScrollingChild
                || view instanceof NestedScrollingParent;
    }

    public void fling(View scrollableView, int velocity) {
        if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocity);
        } else if (scrollableView instanceof AbsListView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((AbsListView) scrollableView).fling(velocity);
            }
        } else if (scrollableView instanceof WebView) {
            ((WebView) scrollableView).flingScroll(0, velocity);
        } else if (scrollableView instanceof NestedScrollView) {
            ((NestedScrollView) scrollableView).fling(velocity);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocity);
        }
    }
}
