package com.android.zdrecycleview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.zdrecycleview.R;
import com.android.zdrecycleview.recycler.ItemClickSupport;
import com.android.zdrecycleview.recycler.holder.EmptyViewHolder;
import com.android.zdrecycleview.recycler.holder.HeaderViewHolder;
import com.android.zdrecycleview.recycler.holder.LoadMoreViewHolder;

import java.util.List;

/**
 * created by jiangshide on 2017-07-14.
 * email:18311271399@163.com
 */
public abstract class ZdRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    public final static int HEADER_TYPE = Integer.MIN_VALUE;
    public final static int FOOTER_TYPE = Integer.MAX_VALUE - 1;
    public final static int MORE_TYPE = Integer.MAX_VALUE;
    public final static int EMPTY_TYPE = Integer.MAX_VALUE - 2;

    private View mCustomLoadMoreView;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private View mDefaultView;
    private View mBadNetView;
    private View mEmptyHolderView;

    private boolean mIsHeadViewEmpty = false;
    private boolean mIsFootViewEmpty = false;
    private boolean mIsEmptyViewEnable = false;

    @LayoutRes
    private int mLoadMoreResId;

    private boolean mHasMore;
    private boolean mHasHeader;
    private boolean mHasFooter;

    protected List<T> mData;

    private ItemClickSupport mItemClickSupport;

    /**
     * Create view holder according to view types.
     *
     * @param parent   the parent view group
     * @param viewType the view type
     * @return the created view holder
     */
    protected abstract RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType);

    /**
     * Show item view.
     *
     * @param holder   the view holder
     * @param position the position of the view holder
     */
    protected abstract void showItemView(RecyclerView.ViewHolder holder, int position);

    /**
     * Get adpater item id by position.
     *
     * @param position item position
     * @return adapter item id
     */
    public abstract long getAdapterItemId(int position);

    /**
     * Get the count of adapter items.
     *
     * @return the count of adapter items
     */
    public int getAdapterItemCount() {
        return null != mData ? mData.size() : 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case MORE_TYPE:
                holder = getMoreViewHolder(parent);
                break;
            case HEADER_TYPE:
                holder = getHeaderViewHolder(parent);
                break;
            case FOOTER_TYPE:
                holder = new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.default_recycler_empty, parent, false));
                break;
            case EMPTY_TYPE:
                holder = new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.default_recycler_empty, parent, false));
                break;
            default:
                holder = createVHolder(parent, viewType);
                break;
        }
        return holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = holder.getItemViewType();
        switch (viewType) {
            case MORE_TYPE:
                holder.itemView.setVisibility(getItemCount() > getCustomsNum() && mHasMore ? View.VISIBLE : View.GONE);
                break;
            case HEADER_TYPE:
                holder.itemView.setVisibility(mHasHeader ? View.VISIBLE : View.GONE);
                break;
            case FOOTER_TYPE:
                if (mHasFooter && !mHasMore) {
                    ((EmptyViewHolder) holder).mContainer.removeAllViews();
                    ((EmptyViewHolder) holder).mContainer.getLayoutParams().height = mFooterView.getLayoutParams().height;
                    ((EmptyViewHolder) holder).mContainer.getLayoutParams().width = mFooterView.getLayoutParams().width;
                    ((EmptyViewHolder) holder).mContainer.addView(mFooterView);
                } else {
                    ((EmptyViewHolder) holder).mContainer.removeAllViews();
                    ((EmptyViewHolder) holder).mContainer.getLayoutParams().height = 0;
                    ((EmptyViewHolder) holder).mContainer.getLayoutParams().width = 0;
                }
                break;
            case EMPTY_TYPE:
                ((EmptyViewHolder) holder).mContainer.removeAllViews();
                if (null == mEmptyHolderView.getLayoutParams()) {
                    mEmptyHolderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                ((EmptyViewHolder) holder).mContainer.getLayoutParams().height = mEmptyHolderView.getLayoutParams().height;
                ((EmptyViewHolder) holder).mContainer.getLayoutParams().width = mEmptyHolderView.getLayoutParams().width;
                ((EmptyViewHolder) holder).mContainer.addView(mEmptyHolderView);
                break;
            default:
                holder.itemView.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(this);
                showItemView(holder, mHasHeader ? position - 1 : position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int customTypeCount = 0;
        if (mHasMore && null != mData && 0 < mData.size()) {
            customTypeCount++;
        }
        if (mHasHeader) {
            customTypeCount++;
        }
        if (mHasFooter) {
            customTypeCount++;
        }

        if ((null == mData || 0 == mData.size()) && mIsEmptyViewEnable) {
            customTypeCount = 1;
            if (!mIsFootViewEmpty && mHasFooter) {
                customTypeCount++;
            }
            if (!mIsHeadViewEmpty && mHasHeader) {
                customTypeCount++;
            }
        }

        return getAdapterItemCount() + customTypeCount;
    }

    @Override
    public long getItemId(int position) {
        if (mHasMore && 0 != position && getItemCount() - 1 == position) {
            return -1;
        }
        return getAdapterItemId(mHasHeader ? position - 1 : position);
    }

    public T getItem(int position) {
        return null != mData && position <= mData.size() - 1 ? mData.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {

        boolean isDataEmpty = null == mData || 0 == mData.size();

        if (mIsEmptyViewEnable && isDataEmpty) {

            if (!mIsHeadViewEmpty && mHasHeader && 0 == position) {
                return HEADER_TYPE;
            }

            if (!mIsFootViewEmpty && mHasFooter && getItemCount() - 1 == position) {
                return FOOTER_TYPE;
            }

            return EMPTY_TYPE;
        } else {

            if (mHasHeader && 0 == position) {
                return HEADER_TYPE;
            }

            if (mHasFooter && getItemCount() - 1 == position) {
                return FOOTER_TYPE;
            }

            if (mHasMore && !isDataEmpty) {
                if (!mHasFooter && getItemCount() - 1 == position) {
                    return MORE_TYPE;
                } else if (mHasFooter && getItemCount() - 2 == position) {
                    return MORE_TYPE;
                }
            }
        }

        return getAttackItemViewType(mHasHeader ? position - 1 : position);
    }

    /**
     * 设置emptyView
     * 优先级 default empty badNet
     *
     * @param isHeadViewEmpty is headerview empty that show emptyview
     * @param isFootViewEmpty is footerview empty that show emptyview
     * @param emptyView       custom empty view
     */
    public void setEmptyView(boolean isHeadViewEmpty, boolean isFootViewEmpty, View emptyView) {
        mEmptyView = emptyView;
        initEmptyParams(isHeadViewEmpty, isFootViewEmpty);
    }

    /**
     * 设置emptyView isHeadViewEmpty and isFootViewEmpty is true
     * 优先级 default empty badNet
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(true, true, emptyView);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void showEmptyView() {
        mEmptyHolderView = mEmptyView;
        notifyDataSetChanged();
    }

    /**
     * 设置defaultView
     * 优先级 default empty badNet
     *
     * @param isHeadViewEmpty
     * @param isFootViewEmpty
     * @param defaultView
     */
    public void setDefaultView(boolean isHeadViewEmpty, boolean isFootViewEmpty, View defaultView) {
        mDefaultView = defaultView;
        initEmptyParams(isHeadViewEmpty, isFootViewEmpty);
    }

    /**
     * 设置defaultView
     * 优先级 default empty badNet
     *
     * @param defaultView
     */
    public void setDefaultView(View defaultView) {
        setDefaultView(true, true, defaultView);
    }

    public View getDefaultView() {
        return mDefaultView;
    }

    public void showDefaultView() {
        mEmptyHolderView = mDefaultView;
        notifyDataSetChanged();
    }

    /**
     * 设置badNetView
     * 优先级 default empty badNet
     *
     * @param isHeadViewEmpty
     * @param isFootViewEmpty
     * @param badNetView
     */
    public void setBadNetView(boolean isHeadViewEmpty, boolean isFootViewEmpty, View badNetView) {
        mBadNetView = badNetView;
        initEmptyParams(isHeadViewEmpty, isFootViewEmpty);
    }

    /**
     * 设置badNetView
     * 优先级 default empty badNet
     *
     * @param badNetView
     */
    public void setBadNetView(View badNetView) {
        setBadNetView(true, true, badNetView);
    }

    public View getBadNetView() {
        return mBadNetView;
    }

    public void showBadNetView() {
        mEmptyHolderView = mBadNetView;
        notifyDataSetChanged();
    }

    private void initEmptyParams(boolean isHeadViewEmpty, boolean isFootViewEmpty) {
        mIsHeadViewEmpty = isHeadViewEmpty;
        mIsFootViewEmpty = isFootViewEmpty;
        mIsEmptyViewEnable = true;
        initEmptyHolderView();
    }

    private void initEmptyHolderView() {
        if (null != mDefaultView) {
            mEmptyHolderView = mDefaultView;
        } else if (null != mEmptyView) {
            mEmptyHolderView = mEmptyView;
        } else if (null != mBadNetView) {
            mEmptyHolderView = mBadNetView;
        }
    }

    /**
     * 使用TitanRecyclerView专用getItemViewType
     *
     * @param position
     * @return
     */
    protected int getAttackItemViewType(int position) {
        return 0;
    }

    protected RecyclerView.ViewHolder getMoreViewHolder(ViewGroup parent) {
        if (null != mCustomLoadMoreView) {
            return new LoadMoreViewHolder(mCustomLoadMoreView);
        }
        if (0 != mLoadMoreResId) {
            mCustomLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(mLoadMoreResId, parent, false);
        } else {
            mCustomLoadMoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.defailt_recycler_more, parent, false);
        }
        return new LoadMoreViewHolder(mCustomLoadMoreView);
    }

    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup parent) {

        if (null == mHeaderView.getLayoutParams()) {
            setViewGroupLp(mHeaderView);
        }
        return new HeaderViewHolder(mHeaderView);
    }

    public void setCustomLoadMoreView(View customView) {
        mLoadMoreResId = 0;
        mCustomLoadMoreView = customView;
    }

    public void setCustomLoadMoreView(int resourceId) {
        mCustomLoadMoreView = null;
        mLoadMoreResId = resourceId;
    }

    /**
     * use TitanRecyclerView as root(ViewGroup)
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        this.mHasHeader = true;
    }

    public void removeHeaderView() {
        this.mHeaderView = null;
        this.mHasHeader = false;
        notifyDataSetChanged();
    }

    /**
     * use TitanRecyclerView as root(ViewGroup)
     *
     * @param footerView
     */
    public void setFooterView(View footerView) {
        setFooterView(footerView, LinearLayout.VERTICAL);
    }

    public void setFooterView(View footerView, int orientation) {
        if (null == footerView.getLayoutParams()) {
            if (LinearLayout.VERTICAL == orientation) {
                footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        this.mFooterView = footerView;
        this.mHasFooter = true;
    }

    public void removeFooterView() {
        this.mFooterView = null;
        this.mHasFooter = false;
        notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        this.mHasMore = hasMore;
        notifyDataSetChanged();
    }

    public boolean hasMore() {
        return this.mHasMore;
    }

    /**
     * 获取自定义的数量
     * notifyItemInserted等一系列的操作
     * 都是针对recyclerview的所有item
     * 只针对mData操作时需考虑自定义数
     *
     * @return
     */
    public int getCustomHeaderNum() {
        int customHeaderNum = 0;
        if (mHasHeader) {
            ++customHeaderNum;
        }
        return customHeaderNum;
    }

    /**
     * 获取所有自定义数量
     *
     * @return
     */
    private int getCustomsNum() {
        int customs = 0;
        customs = mHasFooter ? ++customs : customs;
        customs = mHasHeader ? ++customs : customs;
        customs = mHasMore ? ++customs : customs;
        if ((null == mData || 0 == mData.size()) && mIsEmptyViewEnable) {
            customs = 1;
            customs = mHasFooter && !mIsFootViewEmpty ? ++customs : customs;
            customs = mHasHeader && !mIsHeadViewEmpty ? ++customs : customs;
        }
        return customs;
    }

    public void addDataEnd(T data) {
        if (data != null && null != mData) {
            int startIndex = this.mData.size();
            this.mData.add(data);
            filterData(this.mData);
            notifyItemInserted(startIndex + getCustomHeaderNum());
        }
    }

    public void addData(T data, int index) {
        if (null != data && null != mData && 0 <= index && getAdapterItemCount() >= index) {
            this.mData.add(index, data);
            filterData(mData);
            notifyItemInserted(index + getCustomHeaderNum());
        }
    }

    public void addDataEnd(List<T> data) {
        if (null == mData) {
            setData(data);
        } else if (data != null && data.size() > 0 && data != this.mData) {
            int startIndex = this.mData.size();
            this.mData.addAll(data);
            filterData(this.mData);
            notifyItemRangeInserted(startIndex + getCustomHeaderNum(), data.size());
        }
    }

    public void addDataTop(List<T> data) {
        if (null == mData) {
            setData(data);
        } else if (null != data && 0 < data.size() && data != this.mData) {
            this.mData.addAll(0, data);
            filterData(mData);
            notifyItemRangeInserted(getCustomHeaderNum(), data.size());
        }
    }

    public void addDataTop(T data) {
        if (null != data && null != mData) {
            this.mData.add(0, data);
            filterData(mData);
            notifyItemInserted(getCustomHeaderNum());
        }
    }

    public void remove(T data) {
        if (data != null && null != mData) {
            if (mData.contains(data)) {
                int startIndex = this.mData.indexOf(data);
                mData.remove(data);
                if (startIndex != -1) {
                    filterData(this.mData);
                    notifyItemRemoved(startIndex + getCustomHeaderNum());
                }

            }
        }
    }

    public void remove(int index) {
        if (null != mData && 0 <= index && getAdapterItemCount() >= index) {
            mData.remove(index);
            filterData(mData);
            notifyItemRemoved(index + getCustomHeaderNum());
        }
    }

    public void update(T data) {
        if (data != null && null != mData) {
            int startIndex = this.mData.indexOf(data);
            if (startIndex != -1) {
                mData.set(startIndex, data);
            }
            notifyItemChanged(startIndex + getCustomHeaderNum());
        }
    }

    public void update(int index, T data) {
        if (null != mData && index >= 0 && index < mData.size() && data != null) {
            mData.set(index, data);
            filterData(this.mData);
            notifyItemChanged(index + getCustomHeaderNum());
        }
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(@NonNull List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (null != mData) {
            this.mData.clear();
            notifyDataSetChanged();
        }
    }

    public void setItemClickSupport(ItemClickSupport itemClickSupport) {
        this.mItemClickSupport = itemClickSupport;
    }

    public boolean hasHeader() {
        return mHasHeader;
    }

    public boolean hasFooter() {
        return mHasFooter;
    }

    public void filterData(List<T> data) {
        // Dummy
    }

    @Override
    public void onClick(View v) {
        if (null != mItemClickSupport) {
            mItemClickSupport.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != mItemClickSupport) {
            return mItemClickSupport.onItemLongClick(v);
        }
        return false;
    }

    private void setViewGroupLp(View view) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
    }
}
