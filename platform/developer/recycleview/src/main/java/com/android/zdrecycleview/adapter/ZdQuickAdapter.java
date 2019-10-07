package com.android.zdrecycleview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.zdrecycleview.recycler.holder.AutoViewHolder;

import java.util.List;

/**
 * created by jiangshide on 2017-07-14.
 * email:18311271399@163.com
 */
public class    ZdQuickAdapter<T> extends ZdRecyclerAdapter<T> {
    private int layoutId;

    public ZdQuickAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    public ZdQuickAdapter(int layoutId, List<T> mData) {
        this.layoutId = layoutId;
        this.mData = null;
        this.mData = mData;
    }

    @Override
    protected RecyclerView.ViewHolder createVHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new AutoViewHolder(view);
    }

    @Override
    protected void showItemView(RecyclerView.ViewHolder holder, int position) {
        bindView((AutoViewHolder) holder, position, getItem(position));
    }

    @Override
    public int getAdapterItemCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public long getAdapterItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return null != mData && 0 < mData.size() && 0 <= position ? mData.get(position) : null;
    }

    public void bindView(AutoViewHolder holder, int position, T model) {
    }
}
