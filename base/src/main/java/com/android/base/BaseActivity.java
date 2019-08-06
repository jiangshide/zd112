package com.android.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.android.network.NetWorkApi;
import com.android.network.listener.NetType;
import com.android.network.listener.NetWork;
import com.android.skin.Skin;
import com.android.utils.LogUtil;
import com.android.utils.ViewUtils;
import com.android.utils.listener.ZdOnClickListener;
import com.android.widget.ZdRefresh;
import com.android.widget.ZdTab;
import com.android.widget.refresh.header.MaterialHeader;
import com.android.widget.refresh.interfaces.OnLoadMoreListener;
import com.android.widget.refresh.interfaces.OnRefreshListener;
import com.android.widget.refresh.interfaces.RefreshLayout;
import com.android.zdrouter.ZdRouter;
import com.google.gson.Gson;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class BaseActivity<T> extends AppCompatActivity implements ZdOnClickListener, View.OnLongClickListener, OnRefreshListener, OnLoadMoreListener {

    protected ZdTab mZdTab;

    protected ZdRefresh mZdRefresh;


    protected final String OBJECT = ZdRouter.OBJECT;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        Skin.getInstance().setFactory(this);//监控xml
        super.onCreate(savedInstanceState);
        NetWorkApi.Companion.getInstance().registerObserver(this);//网络监听
        if (null == mZdTab) mZdTab = new ZdTab(this, getSupportFragmentManager());
        ViewUtils.inject(this);
    }

    /**
     * 依赖组件间跳转
     *
     * @param title
     * @param _class
     */
    public void startActivity(String title, Class<T> _class) {
        this.startActivity(title, _class, new Bundle(), false);
    }

    /**
     * 依赖组件间跳转
     *
     * @param title
     * @param _class
     * @param isFinish
     */
    public void startActivity(String title, Class<T> _class, boolean isFinish) {
        this.startActivity(title, _class, new Bundle(), isFinish);
    }

    /**
     * 依赖组件间跳转
     *
     * @param title
     * @param _class
     * @param bundle
     * @param isFinish
     */
    public void startActivity(String title, Class _class, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(this, CommActivity.class);
        bundle.putString("title", title);
        bundle.putSerializable("_class", _class);
        intent.putExtras(bundle);
        startActivity(intent);
        if (isFinish) finish();
    }

    /**
     * 组件间对象传递
     *
     * @param _class
     * @return
     */
    protected T getIntent(Class<T> _class) {
        String json = getIntent().getStringExtra(OBJECT);
        if (TextUtils.isEmpty(json)) return null;
        try {
            return new Gson().fromJson(json, _class);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return null;
    }

    /**
     * 网络状态监控
     */
    @NetWork(netType = NetType.AUTO)
    public void testNet() {
    }

    /**
     * 主题应用
     */
    public void apply() {
        Skin.getInstance().apply();
    }

    protected View addView(int layout) {
        return LayoutInflater.from(this).inflate(layout, null);
    }

    protected View setView(@LayoutRes int layout) {
        return setView(layout, false, null);
    }

    protected View setView(@LayoutRes int layout, String tag) {
        return setView(layout, true, tag);
    }

    protected View setView(@LayoutRes int layout, boolean isRefresh) {
        return setView(layout, isRefresh, null);
    }

    protected View setView(@LayoutRes int layout, boolean isRefresh, String tag) {
        if (isRefresh) {
            mZdRefresh = new ZdRefresh(this);
            mZdRefresh.setOnRefreshListener(this);
            mZdRefresh.setOnLoadMoreListener(this);
            mZdRefresh.setEnableLoadMore(false);
            mZdRefresh.addView(LayoutInflater.from(this).inflate(layout, null));
            mZdRefresh.setRefreshHeader(new MaterialHeader(this));
            return mZdRefresh;
        } else {
            return LayoutInflater.from(this).inflate(layout, null);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    /**
     * 取消刷新操作
     */
    public void cancelRefresh() {
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

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
