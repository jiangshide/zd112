package com.android.zdrecycleview.recycler.holder;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * created by jiangshide on 2017-07-14.
 * email:18311271399@163.com
 */
public class AutoViewHolder extends RecyclerView.ViewHolder{

    private SparseArray<View> mViewHolder;
    private View mView;

    public AutoViewHolder(View itemView) {
        super(itemView);
        mViewHolder = new SparseArray<>();
        mView = itemView;
    }

    public <T extends View> T get(int id) {
        View childView = mViewHolder.get(id);
        if (childView == null) {
            childView = mView.findViewById(id);
            mViewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return mView;
    }

    public TextView getTextView(int id) {
        return get(id);
    }

    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

    public void setTextView(int id, int resourceId) {
        getTextView(id).setText(resourceId);
    }
}
