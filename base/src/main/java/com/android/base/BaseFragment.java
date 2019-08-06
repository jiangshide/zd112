package com.android.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.android.utils.LogUtil;
import com.android.utils.SystemUtil;
import com.android.utils.listener.ZdOnClickListener;
import com.android.widget.ZdRefresh;
import com.android.widget.refresh.header.MaterialHeader;
import com.android.widget.refresh.interfaces.OnLoadMoreListener;
import com.android.widget.refresh.interfaces.OnRefreshListener;
import com.android.widget.refresh.interfaces.RefreshLayout;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class BaseFragment extends Fragment implements ZdOnClickListener, AdapterView.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {

    protected ZdRefresh mZdRefresh;

    protected View setView(@LayoutRes int layout) {
        return setView(layout, false, false, null);
    }

    protected View setView(@LayoutRes int layout, String tag) {
        return setView(layout, true, false, tag);
    }

    protected View setView(@LayoutRes int layout, boolean isRefresh) {
        return setView(layout, isRefresh, false, null);
    }

    protected View setView(@LayoutRes int layout, boolean isRefresh, boolean isMore) {
        return setView(layout, isRefresh, isMore, null);
    }

    protected View setView(@LayoutRes int layout, boolean isRefresh, boolean isMore, String tag) {
        if (isRefresh) {
            mZdRefresh = new ZdRefresh(getContext());
            mZdRefresh.setOnRefreshListener(this);
            mZdRefresh.setOnLoadMoreListener(this);
            mZdRefresh.setEnableLoadMore(isMore);
            mZdRefresh.addView(LayoutInflater.from(getContext()).inflate(layout, null));
            mZdRefresh.setRefreshHeader(new MaterialHeader(getActivity()));
            mZdRefresh.setOnTouchListener(new ZdRefresh.OnTouchEventListener() {
                @Override
                public void onMove(float scrollY) {
                    SystemUtil.hideKeyboard(getActivity(), mZdRefresh);
                }

                @Override
                public void onUp(float scrollY) {

                }
            });
            return mZdRefresh;
        } else {
            return LayoutInflater.from(getContext()).inflate(layout, null);
        }
    }

    protected int getColor(int resColor) {
        return getResources().getColor(resColor);
    }

    protected int getDim(int resDim) {
        return getResources().getDimensionPixelSize(resDim);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        LogUtil.e("----------onLoadMore:",refreshLayout);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        LogUtil.e("----------onRefresh:",refreshLayout);
    }

    public void onRefresh() {
        if (mZdRefresh != null) {
            mZdRefresh.autoRefresh();
        }
    }

    public void cancelRefresh() {
        LogUtil.e("---------mZdRefresh:",mZdRefresh);
        if (mZdRefresh != null) {
            mZdRefresh.finishRefresh();
            mZdRefresh.finishLoadMore();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onClick(View v, Bundle bundle) {

    }
}
