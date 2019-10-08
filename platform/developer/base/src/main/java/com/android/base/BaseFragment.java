package com.android.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.http.Http;
import com.android.refresh.Refresh;
import com.android.refresh.header.MaterialHeader;
import com.android.refresh.interfaces.OnLoadMoreListener;
import com.android.refresh.interfaces.OnRefreshListener;
import com.android.tablayout.ZdTabLayout;
import com.android.utils.LogUtil;
import com.android.utils.SystemUtil;
import com.android.utils.listener.ZdOnClickListener;
import com.android.widget.ZdButton;
import com.android.widget.ZdRecycleView;
import com.android.widget.ZdTab;
import com.android.widget.ZdToast;
import com.android.widget.ZdViewPager;
import com.zhihu.matisse.MimeType;
import java.util.List;
import java.util.Set;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class BaseFragment extends Fragment
    implements ZdOnClickListener, AdapterView.OnItemClickListener, OnRefreshListener,
    OnLoadMoreListener, Refresh.OnTouchEventListener {

  protected Refresh mRefresh;

  public Http http;

  public BaseActivity mActivity;

  public ZdRecycleView recyclerView;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (null == http) http = Http.INSTANCE;
    mActivity = (BaseActivity) getActivity();
  }

  @Override public void onDetach() {
    super.onDetach();
    mActivity = null;
  }

  public void setTab(int tab) {
    mActivity.setTab(tab);
  }

  public void selecteImgs(int selected, Set<MimeType> mimeTypes) {
    mActivity.selecteImgs(selected, mimeTypes);
  }

  protected View setView(@LayoutRes int layout) {
    return setView(layout, false, false, getTag());
  }

  protected View setView(@LayoutRes int layout, String tag) {
    return setView(layout, true, false, tag);
  }

  protected View setView(@LayoutRes int layout, boolean isRefresh) {
    return setView(layout, isRefresh, false, getTag());
  }

  protected View setView(@LayoutRes int layout, boolean isRefresh, boolean isMore) {
    return setView(layout, isRefresh, isMore, getTag());
  }

  public View setView(@LayoutRes int layout, boolean isRefresh, boolean isMore, String tag) {
    return setView(LayoutInflater.from(getContext()).inflate(layout, null), isRefresh, isMore,
        tag);
  }

  protected View setView(View view, boolean isRefresh, boolean isMore, String tag) {
    recyclerView = view.findViewById(R.id.recycleView);
    if (!isRefresh) return view;
    mRefresh = new Refresh(getContext());
    mRefresh.setOnRefreshListener(this)
        .setOnLoadMoreListener(this)
        .setEnableLoadMore(isMore)
        .setRefreshHeader(new MaterialHeader(getActivity())).setOnTouchListener(this)
        .addView(view);
    if (TextUtils.isEmpty(tag)) {
      mRefresh.setTag(tag);
    }
    return mRefresh;
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

  public View setTitleView(int layout, String title, boolean isRefresh) {
    return setTitleView(layout, title, isRefresh, this.getClass().getName());
  }

  public View setTitleView(int layout, String title, boolean isRefresh, String tag) {
    return mActivity.mZdTab.setTopView(setView(layout, isRefresh, false, tag), title);
  }

  public void showLoading() {
    mActivity.mZdTab.showLoading(mActivity).setOnClickListener(this);
  }

  public void showFail() {
    mActivity.mZdTab.showFail(mActivity).setOnClickListener(this);
  }

  public void hiddle() {
    mActivity.mZdTab.hiddleTips(mActivity);
  }

  @Override public void onMove(float scrollY) {
    SystemUtil.hideKeyboard(mActivity, mRefresh);
  }

  @Override public void onUp(float scrollY) {

  }

  @Override public void onPause() {
    mActivity.mZdTab.hiddleTips(mActivity);
    super.onPause();
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return setView(R.layout.default_tab_layout);
  }

  protected ZdViewPager createTabLayout(View view, boolean isTabBtn, List<String> titles,
      List<Fragment> fragments, Bundle savedInstanceState) {
    ZdTabLayout tabTitle = view.findViewById(R.id.tabTitle);
    ImageView dotImg = view.findViewById(R.id.dotImg);
    ZdViewPager tabView = view.findViewById(R.id.tabView);
    ZdButton tabBtn = view.findViewById(R.id.tabBtn);
    tabBtn.setVisibility(isTabBtn ? View.VISIBLE : View.GONE);
    tabView.setAdapter(tabView.create(getChildFragmentManager())
        .setTitles(
            titles
        )
        .setFragment(
            fragments
        )
        .initTabs(getActivity(), tabTitle, tabView)
        .setLinePagerIndicator(getColor(R.color.yellow)));
    onViewCreated(view, tabTitle, dotImg, tabView, tabBtn, savedInstanceState);
    return tabView;
  }

  protected void onViewCreated(View view, ZdTabLayout tabTitle, ImageView dotImg,
      ZdViewPager tabView, ZdButton tabBtn, Bundle savedInstanceState) {
  }

  public ZdTab push(Fragment fragment) {
    return push(fragment, this.getTag());
  }

  public ZdTab push(Fragment fragment, Bundle bundle) {
    return push(fragment, bundle, this.getTag());
  }

  public ZdTab push(Fragment fragment, String tag) {
    return push(fragment, null, tag);
  }

  public ZdTab push(Fragment fragment, Bundle bundle, String tag) {
    return ZdTab.instance.push(fragment, bundle, tag);
  }

  public ZdTab pop() {
    return ZdTab.instance.pop();
  }

  public ZdTab pop(String tag, int flags) {
    return ZdTab.instance.pop(tag, flags);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(Class _class) {
    this.goFragment(null, _class, new Bundle(), false);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(String title, Class _class) {
    this.goFragment(title, _class, new Bundle(), false);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(String title, Class _class, boolean isFinish) {
    this.goFragment(title, _class, new Bundle(), isFinish);
  }

  /**
   * 依赖组件间跳转
   */
  public void goFragment(String title, Class _class, Bundle bundle, boolean isFinish) {
    ((BaseActivity) getActivity()).goFragment(title, _class, bundle, isFinish);
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
  public void onLoadMore() {
    LogUtil.e("onLoadMore");
  }

  @Override
  public void onRefresh() {
    LogUtil.e("onRefresh");
  }

  public void autoRefresh() {
    LogUtil.e("autoRefresh");
    if (mRefresh != null) {
      mRefresh.autoRefresh();
    }
  }

  public void cancelRefresh() {
    LogUtil.e("mRefresh:", mRefresh);
    if (mRefresh != null) {
      mRefresh.finishRefresh();
      mRefresh.finishLoadMore();
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == mActivity.mZdTab.rootViewId) {
      ZdToast.txt("----v:" + v);
    }
  }

  @Override
  public void onClick(View v, Bundle bundle) {
  }
}
