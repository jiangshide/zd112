package com.android.zdrecycleview.recycler.holder;

import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.android.zdrecycleview.R;

/**
 * created by jiangshide on 2017-07-14.
 * email:18311271399@163.com
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder{

    public FrameLayout mContainer;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        mContainer = (FrameLayout) itemView.findViewById(R.id.container);
    }
}
