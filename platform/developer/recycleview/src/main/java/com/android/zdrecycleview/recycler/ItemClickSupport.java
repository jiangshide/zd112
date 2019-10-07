package com.android.zdrecycleview.recycler;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.zdrecycleview.adapter.ZdRecyclerAdapter;

/**
 * created by jiangshide on 2017-07-14.
 * email:18311271399@163.com
 */
public class ItemClickSupport {
    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position, long id);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView recyclerView, View view, int position, long id);
    }

    private RecyclerView mRecyclerView;
    private ZdRecyclerAdapter mZdRecyclerAdapter;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public ItemClickSupport(RecyclerView recyclerView, ZdRecyclerAdapter adapter) {
        this.mRecyclerView = recyclerView;
        this.mZdRecyclerAdapter = adapter;
        this.mZdRecyclerAdapter.setItemClickSupport(this);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void onItemClick(View v) {
        if (null != mRecyclerView && null != mZdRecyclerAdapter && null != mOnItemClickListener) {
            final int position = mRecyclerView.getChildLayoutPosition(v);
            if (isMoreOrFooter(position)) {
                return;
            }

            final long id = mZdRecyclerAdapter.getItemId(position);
            mOnItemClickListener.onItemClick(mRecyclerView, v, position - mZdRecyclerAdapter.getCustomHeaderNum(), id);
        }
    }

    public boolean onItemLongClick(View v) {
        if (null != mRecyclerView && null != mZdRecyclerAdapter && null != mOnItemLongClickListener) {
            final int position = mRecyclerView.getChildLayoutPosition(v);
            if (isMoreOrFooter(position)) {
                return false;
            }

            final long id = mZdRecyclerAdapter.getItemId(position);

            return mOnItemLongClickListener.onItemLongClick(mRecyclerView, v, position - mZdRecyclerAdapter.getCustomHeaderNum(), id);
        }

        return false;
    }

    private boolean isMoreOrFooter(int position) {
        return null == mZdRecyclerAdapter.getData()
                || mZdRecyclerAdapter.getData().size() + mZdRecyclerAdapter.getCustomHeaderNum() <= position
                || mZdRecyclerAdapter.getCustomHeaderNum() > position;
    }
}
