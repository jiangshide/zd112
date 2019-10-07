package com.android.tablayout.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.android.tablayout.interfaces.IPagerIndicator;
import com.android.tablayout.interfaces.IPagerTitleView;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public abstract class CommonNavigatorAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public abstract int getCount();

    public abstract IPagerTitleView getTitleView(Context context, int index);

    public abstract IPagerIndicator getIndicator(Context context);

    public float getTitleWeight(Context context, int index) {
        return 1;
    }

    public final void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public final void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public final void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public final void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }
}
