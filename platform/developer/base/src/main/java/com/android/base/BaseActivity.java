package com.android.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.android.http.Http;
import com.android.network.NetWork;
import com.android.network.annotation.INetType;
import com.android.network.annotation.NetType;
import com.android.permission.annotation.PermissionResult;
import com.android.permission.model.GrantModel;
import com.android.refresh.Refresh;
import com.android.refresh.header.MaterialHeader;
import com.android.refresh.interfaces.OnLoadMoreListener;
import com.android.refresh.interfaces.OnRefreshListener;
import com.android.skin.Skin;
import com.android.utils.LogUtil;
import com.android.utils.ViewUtils;
import com.android.utils.listener.ZdOnClickListener;
import com.android.widget.ZdSnackbar;
import com.android.widget.ZdTab;
import com.android.widget.ZdToast;
import com.android.zdrouter.ZdRouter;
import com.google.gson.Gson;
import com.zhihu.matisse.MimeType;
import java.util.Set;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class BaseActivity<T> extends AppCompatActivity
    implements ZdOnClickListener, View.OnLongClickListener, OnRefreshListener, OnLoadMoreListener {

  public ZdTab mZdTab;

  protected Refresh mRefresh;

  protected Http http;

  protected final String OBJECT = ZdRouter.OBJECT;
  public static final String ACtivity_FOR_RESULT = "activityForResult";
  public static final int REQUEST_CODE = 23;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(newBase);
    if (null == http) http = Http.INSTANCE;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    //        Skin.getInstance().setFactory(this);//view监听
    super.onCreate(savedInstanceState);
    NetWork.Companion.getInstance().registerObserver(this);//网络监听
    if (null == mZdTab) mZdTab = new ZdTab(this, getSupportFragmentManager());
    ViewUtils.inject(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Skin.getInstance().apply();
  }

  public void setTab(int tab) {
    mZdTab.setTab(tab);
  }

  public void selecteImgs(int selected, Set<MimeType> mimeTypes) {
  }

  @PermissionResult
  public void requestPermissionResult(GrantModel grantModel) {
    LogUtil.e("----------grantModel:", grantModel.isCancel);
  }

  public ZdTab push(Fragment fragment) {
    return push(fragment, "");
  }

  public ZdTab push(Fragment fragment, Bundle bundle) {
    return push(fragment, bundle, null);
  }

  public ZdTab push(Fragment fragment, String tag) {
    return push(fragment, null, tag);
  }

  public ZdTab push(Fragment fragment, Bundle bundle, String tag) {
    return mZdTab.push(fragment, bundle, tag);
  }

  public ZdTab pop() {
    return mZdTab.pop();
  }

  public ZdTab pop(String tag, int flags) {
    return mZdTab.pop(tag, flags);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(Class<T> _class) {
    this.goFragment(null, _class, new Bundle(), false);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(Class<T> _class, boolean isFinissh) {
    this.goFragment(null, _class, new Bundle(), isFinissh);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(String title, Class<T> _class) {
    this.goFragment(title, _class, new Bundle(), false);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(String title, Class<T> _class, boolean isFinish) {
    this.goFragment(title, _class, new Bundle(), isFinish);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(String title, Class _class, Bundle bundle, boolean isFinish) {
    Intent intent = new Intent(this, CommActivity.class);
    if (!TextUtils.isEmpty(title)) {
      bundle.putString("title", title);
    }
    bundle.putSerializable("_class", _class);
    intent.putExtras(bundle);
    startActivity(intent);
    if (isFinish) finish();
  }

  /**
   * 组件间对象传递
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
   * int NONE = -1;
   * int AVAILABLE = 1;
   * int AUTO = 2;
   * int CELLULAR = 3;//cmwap
   * int WIFI = 4;
   * int BLUETOOTH = 5;
   * int ETHERNET = 6;
   * int VPN = 7;
   * int WIFI_AWARE = 8;
   * int LOWPAN = 9;
   */
  @NetType(netType = INetType.AUTO)
  public void netState(@INetType int netType) {
    LogUtil.e("netType:", netType);
    ZdSnackbar.make(findViewById(android.R.id.content), netType == -1 ? "网络已断开" : "网络已连接",
        ZdSnackbar.LENGTH_LONG, ZdSnackbar.STYLE_SHOW_BOTTOM).show();
  }

  /**
   * 主题应用
   */
  public void apply() {
    Skin.getInstance().apply();
  }

  protected View getView(int layout) {
    return LayoutInflater.from(this).inflate(layout, null);
  }

  protected View setView(@LayoutRes int layout) {
    return setView(layout, false, this.getClass().getName());
  }

  protected View setView(@LayoutRes int layout, String tag) {
    return setView(layout, true, tag);
  }

  protected View setView(@LayoutRes int layout, boolean isRefresh) {
    return setView(layout, isRefresh, this.getClass().getName());
  }

  protected View setView(@LayoutRes int layout, boolean isRefresh, String tag) {
    if (isRefresh) {
      mRefresh = new Refresh(this);
      mRefresh.setOnRefreshListener(this);
      mRefresh.setOnLoadMoreListener(this);
      mRefresh.setEnableLoadMore(false);
      mRefresh.addView(LayoutInflater.from(this).inflate(layout, null));
      mRefresh.setRefreshHeader(new MaterialHeader(this));
      return mRefresh;
    } else {
      return getView(layout);
    }
  }

  public View setTitleView(int layout) {
    return setTitleView(layout, "", false, this.getClass().getName());
  }

  public View setTitleView(int layout, String title) {
    return setTitleView(layout, title, false, this.getClass().getName());
  }

  public View setTitleView(int layout, boolean isRefresh) {
    return setTitleView(layout, "", isRefresh, this.getClass().getName());
  }

  public View setTitleView(int layout, String title, boolean isRefresh, String tag) {
    return mZdTab.setTopView(setView(layout, isRefresh, tag), title);
  }

  public void showLoading() {
    mZdTab.showLoading(this).setOnClickListener(this);
  }

  public void showFail() {
    mZdTab.showFail(this).setOnClickListener(this);
  }

  public void hiddle() {
    mZdTab.hiddleTips(this);
  }

  @Override
  public void onRefresh() {
  }

  @Override
  public void onLoadMore() {
  }

  /**
   * 取消刷新操作
   */
  public void cancelRefresh() {
    if (mRefresh != null) {
      mRefresh.finishRefresh();
      mRefresh.finishLoadMore();
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == mZdTab.rootViewId) {
      ZdToast.txt("----v:" + v);
    }
  }

  @Override
  public void onClick(View v, Bundle bundle) {

  }

  @Override public boolean onLongClick(View view) {
    return false;
  }
}
