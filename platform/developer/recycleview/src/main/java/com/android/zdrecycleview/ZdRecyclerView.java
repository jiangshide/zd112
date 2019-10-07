package com.android.zdrecycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.zdrecycleview.adapter.ZdRecyclerAdapter;
import com.android.zdrecycleview.recycler.ItemClickSupport;
import com.android.zdrecycleview.recycler.OnEndlessScrollListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
public class ZdRecyclerView extends RecyclerView {

    @IntDef({TOP, BOTTOM, LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public interface OnLayoutChangeListener {
        void onLayoutChange();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnScrollStateChangedListener {
        void onStateChanged(int newState);
    }

    public interface OnScrolledListener {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    private OnEndlessScrollListener mOnEndlessScrollListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnScrolledListener mOnScrolledListener;
    private ZdRecyclerAdapter mZdRecyclerAdapter;
    private OnScrollStateChangedListener mOnScrollStateChangedListener;
    private ItemClickSupport mItemClickSupport;
    private ItemClickSupport.OnItemClickListener mOnItemClickListener;
    private ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;


    @LayoutRes
    protected int mLoadMoreResourceId;
    protected View mCustomLoadMoreView;
    protected int mScrollbarStyle;
    private boolean mHasMore;

    public ZdRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public ZdRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init(context);
    }

    public ZdRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        init(context);
    }

    protected void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        if (mLoadMoreResourceId == 0) {
            mLoadMoreResourceId = R.layout.default_recycler_empty;
        }
        addEndlessScrollListener();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZdRecyclerView);

        try {
            mLoadMoreResourceId = typedArray.getResourceId(R.styleable.ZdRecyclerView_layout_more,
                    R.layout.defailt_recycler_more);
            mScrollbarStyle = typedArray.getInt(R.styleable.ZdRecyclerView_scrollbarStyle, -1);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 滑至底部监听
     * 实现loadmore
     */
    private void addEndlessScrollListener() {
        mOnEndlessScrollListener = new OnEndlessScrollListener() {
            @Override
            public void onLoadMore() {
                if (mOnLoadMoreListener != null && mZdRecyclerAdapter != null
                        && mZdRecyclerAdapter.hasMore()) {
                    mOnLoadMoreListener.onLoadMore();
                }
            }

            @Override
            public void onScrollStateChanged(int newState) {
                if (mOnScrollStateChangedListener != null) {
                    mOnScrollStateChangedListener.onStateChanged(newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mOnScrolledListener) {
                    mOnScrolledListener.onScrolled(recyclerView, dx, dy);
                }
            }
        };
        addOnScrollListener(mOnEndlessScrollListener);
    }


    private void setupAdapter() {
        if (mZdRecyclerAdapter != null) {
            mZdRecyclerAdapter.setHasMore(mHasMore);
            if (null != mCustomLoadMoreView) {
                mZdRecyclerAdapter.setCustomLoadMoreView(mCustomLoadMoreView);
            } else {
                mZdRecyclerAdapter.setCustomLoadMoreView(mLoadMoreResourceId);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof ZdRecyclerAdapter) {
            mZdRecyclerAdapter = (ZdRecyclerAdapter) adapter;
            mItemClickSupport = new ItemClickSupport(this, (ZdRecyclerAdapter) adapter);
            mItemClickSupport.setOnItemClickListener(mOnItemClickListener);
            mItemClickSupport.setOnItemLongClickListener(mOnItemLongClickListener);
            optimizeGridLayout(getLayoutManager());
            setupLoadMore();
            setupAdapter();
            adapter.registerAdapterDataObserver(new AdapterDataObserver() {
                @Override
                public void onChanged() {
                    //bug fixed
                    //如果最后一次数据itemcount==第一次data.size+customview数量，则loadmore失败
                    //这边这个判断为ture，则认为是刷新动作（只判断该bug的刷新动作），初始化mPreviousTotal
                    if (getLayoutManager().getItemCount() == mOnEndlessScrollListener.mPreviousTotal) {
                        mOnEndlessScrollListener.mPreviousTotal = 0;
                    }
                }
            });
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        optimizeGridLayout(layout);
        setupLoadMore();
        setupAdapter();
    }

    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener onLoadMoreListener) {
        mHasMore = true;
        mOnLoadMoreListener = onLoadMoreListener;
        setupAdapter();
    }

    public void setOnScrolledListener(@NonNull OnScrolledListener onScrolledListener) {
        mOnScrolledListener = onScrolledListener;
        addEndlessScrollListener();
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
        setupAdapter();
    }

    public void setCustomLoadMoreView(View customView) {
        mLoadMoreResourceId = 0;
        mCustomLoadMoreView = customView;
        setupAdapter();
    }

    public void setCustomLoadMoreView(int resourceId) {
        this.mCustomLoadMoreView = null;
        this.mLoadMoreResourceId = resourceId;
        setupAdapter();
    }

    public void setOnItemClickListener(ItemClickSupport.OnItemClickListener
                                               onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        if (mItemClickSupport != null) {
            mItemClickSupport.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(ItemClickSupport.OnItemLongClickListener
                                                   onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
        if (mItemClickSupport != null) {
            mItemClickSupport.setOnItemLongClickListener(onItemLongClickListener);
        }
    }

    public void addLayoutChangeListener(@Orientation final int orientation,
                                        @NonNull final OnLayoutChangeListener listener) {

        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                boolean isLayoutChange = false;

                switch (orientation) {
                    case TOP:
                        isLayoutChange = top > oldTop;
                        break;
                    case BOTTOM:
                        isLayoutChange = bottom < oldBottom;
                        break;
                    case LEFT:
                        isLayoutChange = left > oldLeft;
                        break;
                    case RIGHT:
                        isLayoutChange = right < oldRight;
                        break;
                }
                if (isLayoutChange) {
                    // 异步执行，防止attackView中数据不能被操作
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLayoutChange();
                        }
                    }, 100);
                }
            }
        });
    }

    /**
     * 设置滑动时，是否加载数据。
     *
     * @param isNeed true为加载数据，false为不加载。
     */
    public void notifyDataChangeOnScroll(boolean isNeed) {
        if (!isNeed) {
            return;
        }

        mOnEndlessScrollListener.setNeedLoadData(true);
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener listener) {
        mOnScrollStateChangedListener = listener;
        addEndlessScrollListener();
    }

    /**
     * 重写SpanSizeLookup，优化GridLayoutManager Header/Footer/LoadMore 显示
     * 如果用户已重写，则不再重写，用户重写应考虑Header/Footer/LoadMore 显示问题
     *
     * @param layoutManager the layout manager
     */
    private void optimizeGridLayout(@NonNull final RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof GridLayoutManager)) return;

        GridLayoutManager.SpanSizeLookup lookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup();
        if (!(lookup instanceof GridLayoutManager.DefaultSpanSizeLookup)) return;

        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mZdRecyclerAdapter == null) {
                    return 0;
                }
                int viewType = mZdRecyclerAdapter.getItemViewType(position);
                boolean flag = ZdRecyclerAdapter.EMPTY_TYPE == viewType
                        || ZdRecyclerAdapter.FOOTER_TYPE == viewType
                        || ZdRecyclerAdapter.HEADER_TYPE == viewType
                        || ZdRecyclerAdapter.MORE_TYPE == viewType;

                return flag ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
            }
        });
    }

    /**
     * 处理水平loadmoreview
     */
    private void setupLoadMore() {

        boolean isHorizonal = false;

        if (getLayoutManager() instanceof LinearLayoutManager) {
            isHorizonal = ((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL;
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            isHorizonal = ((StaggeredGridLayoutManager) getLayoutManager()).getOrientation() == StaggeredGridLayoutManager.HORIZONTAL;
        }

        mLoadMoreResourceId = R.layout.defailt_recycler_more == mLoadMoreResourceId && isHorizonal ? R.layout.default_recycler_horizontal_more : R.layout.defailt_recycler_more;
    }
}
