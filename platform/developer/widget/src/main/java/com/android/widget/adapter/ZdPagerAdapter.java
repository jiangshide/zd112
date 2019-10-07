package com.android.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * created by jiangshide on 2019/2/13.
 * email:18311271399@163.com
 */
public class ZdPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private Context mContext;
    private List<View> mViews;
    private boolean mIsLimitWidth = false;
    private OnPagerItemClickListener mOnPagerItemClickListener;

    public ZdPagerAdapter(Context context) {
        this.mContext = context;
    }

    public ZdPagerAdapter setViews(List<View> views) {
        return this.setViews(views, mIsLimitWidth);
    }

    public ZdPagerAdapter setViews(List<View> views, Boolean isLimitWidth) {
        this.mViews = views;
        this.mIsLimitWidth = isLimitWidth;
        return this;
    }

    public void setListener(OnPagerItemClickListener listener) {
        this.mOnPagerItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public float getPageWidth(int position) {
        return mIsLimitWidth ? (float) 0.8 : super.getPageWidth(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        view.setId(position);
        view.setOnClickListener(this);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
//        ((ViewPager) container).removeView(mViews.get(position));
    }

    @Override
    public void onClick(View v) {
        if (mOnPagerItemClickListener != null) {
            mOnPagerItemClickListener.onItemClick(v, v.getId());
        }
    }

    public interface OnPagerItemClickListener {
        void onItemClick(View view, int position);
    }
}
