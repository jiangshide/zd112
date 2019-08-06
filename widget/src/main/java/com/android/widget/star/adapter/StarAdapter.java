package com.android.widget.star.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * created by jiangshide on 2018-09-05.
 * email:18311271399@163.com
 */
public class StarAdapter<T> extends AbsStarAdapter<T> {

    private List<T> mData;

//    public StarAdapter() {
////    }

    public StarAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        T t = getItem(position);
        return getView(context, position, parent, t);
    }

    @Override
    public T getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }

    @Override
    public int getPopularity(int position) {
        return 0;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }

    @Override
    protected View getView(Context context, int position, ViewGroup parent, T t) {
        return null;
    }
}
