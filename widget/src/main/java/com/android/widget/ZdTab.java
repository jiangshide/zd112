package com.android.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.utils.LogUtil;
import com.android.utils.listener.ZdOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class ZdTab extends ZdTabHost implements FragmentManager.OnBackStackChangedListener{
    private FragmentManager fragmentManager;
    private ZdTabTop mZdTabTop;
    private ZdTabHost fragmentTabHost;
    private FrameLayout frameLayout;
    private LinearLayout defaultCmmTipsL;
    private ImageView defaultCommExceptionImg;
    private TextView defaultCommExceptionTxt;
    private LinearLayout defaultTabBarL;
    private Fragment[] mFragments;
    private String[] mTitles;
    private String[] mTags;
    private int[] mIcons;
    private OnTabViewListener mOnTabViewListener;
    private OnTabClickListener mOnTabClickListener;
    private OnLongClickListener mOnLongClickListener;
    private final int MIN_CLICK_DELAY_TIME = 200;
    private long mLastClickTime = 0;
    private List<TextView> mRedDotList;
    private boolean isVisibleTabBar = true;

    public ZdTab(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.fragmentManager.addOnBackStackChangedListener(this);
        init();
    }

    public ZdTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.default_tab, this, true);
        mZdTabTop = view.findViewById(R.id.defaultTopView);
        fragmentTabHost = view.findViewById(android.R.id.tabhost);
        frameLayout = view.findViewById(R.id.frameLayout);
//        defaultCmmTipsL = view.findViewById(R.id.defaultCmmTipsL);
//        defaultCommExceptionImg = view.findViewById(R.id.defaultCommExceptionImg);
//        defaultCommExceptionTxt = view.findViewById(R.id.defaultCommExceptionTxt);
        defaultTabBarL = view.findViewById(R.id.defaultTabBarL);
    }

    public ZdTab setVisibleTabBar(boolean isVisible) {
        this.isVisibleTabBar = isVisible;
        return this;
    }

    public ZdTabTop getTopView() {
        return mZdTabTop;
    }

    public ZdTab setVisibleTop(boolean isVisible) {
        mZdTabTop.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ZdTab setTitle(int resId) {
        mZdTabTop.setTitle(getResources().getString(resId));
        return setVisibleTop(true);
    }

    public ZdTab setTitle(String title) {
        mZdTabTop.setTitle(title);
        return setVisibleTop(true);
    }

    public ZdTab setLeftName(String name) {
        mZdTabTop.setLeftName(name);
        return setVisibleTop(true);
    }

    public ZdTab setRightName(String name) {
        mZdTabTop.setRightName(name);
        return setVisibleTop(true);
    }

    public ZdTab setOnLeftClick(ZdOnClickListener listener) {
        mZdTabTop.setOnLeftClick(listener);
        return setVisibleTop(true);
    }

    public ZdTab setOnRightClick(ZdOnClickListener listener) {
        mZdTabTop.setOnRightClick(listener);
        return setVisibleTop(true);
    }

    public ZdTab setCommTips(int layout) {
        return setCommTips(LayoutInflater.from(getContext()).inflate(layout, null));
    }

    public ZdTab setCommTips(View view) {
//        defaultCmmTipsL.removeAllViews();
//        defaultCmmTipsL.addView(view);
        return this;
    }

    public ZdTab setVisibleCommTips(boolean isVisible) {
//        defaultCmmTipsL.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return this;
    }

    public LinearLayout getCommTips() {
        return defaultCmmTipsL;
    }

    public ZdTab setCommTipsImg(int resImg) {
        defaultCommExceptionImg.setImageResource(resImg);
        return this;
    }

    public ImageView getCommTipsImg() {
        return defaultCommExceptionImg;
    }

    public ZdTab setCommTipsTxt(int resTxt) {
        return setCommTipsTxt(getResources().getString(resTxt));
    }

    public ZdTab setCommTipsTxt(String txt) {
        defaultCommExceptionTxt.setText(txt);
        return this;
    }

    public ZdTab setCommTipsTxtColor(@ColorRes int resTxt) {
        defaultCommExceptionTxt.setTextColor(getResources().getColor(resTxt));
        return this;
    }

    public ZdTab setCommTipsTxtSize(int resTxt) {
        defaultCommExceptionTxt.setTextSize(resTxt);
        return this;
    }

    public TextView getCommTipsTxt() {
        return defaultCommExceptionTxt;
    }

    public ZdTab init(FragmentManager fragmentManager) {
        fragmentTabHost.setup(getContext(), fragmentManager, android.R.id.tabcontent);
        return this;
    }

    public ZdTab setFragments(Fragment... fragments) {
        this.mFragments = fragments;
        return this;
    }

    public ZdTab setTitles(String... titles) {
        this.mTitles = titles;
        return this;
    }

    public ZdTab setIcons(int... icons) {
        this.mIcons = icons;
        return this;
    }

    public ZdTab setTags(String... tags) {
        this.mTags = tags;
        return this;
    }

    public ZdTab setTabViewListener(OnTabViewListener listener) {
        this.mOnTabViewListener = listener;
        return this;
    }

    public ZdTab setTabOnClickListener(OnTabClickListener listener) {
        this.mOnTabClickListener = listener;
        return this;
    }

    public ZdTab setTabLongClickListener(OnLongClickListener listener) {
        this.mOnLongClickListener = listener;
        return this;
    }

    public ZdTab setTab(int tabId) {
        if (fragmentTabHost != null) {
            fragmentTabHost.setCurrentTab(tabId);
        }
        return this;
    }

    public ZdTab create() {
        mRedDotList = new ArrayList<>();
        int i = 0;
        for (Fragment fragment : mFragments) {
            String title = i + "";
            if (null != mTitles) {
                title = mTitles[i];
            }
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(title).setIndicator(null != mOnTabViewListener ? mOnTabViewListener.view(i) : getDefaultView(i));
            fragmentTabHost.addTab(tabSpec, fragment.getClass(), fragment.getArguments());
            fragmentTabHost.getTabWidget().getChildTabViewAt(i).setId(i);
            fragmentTabHost.getTabWidget().getChildTabViewAt(i).setTag(mTags[i]);
            fragmentTabHost.getTabWidget().getChildTabViewAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = System.currentTimeMillis();
                    int tabId = v.getId();
                    if (currentTime - mLastClickTime > MIN_CLICK_DELAY_TIME) {
                        mLastClickTime = currentTime;
                        if (!mTags[tabId].contains("no")) {
                            fragmentTabHost.setCurrentTab(v.getId());
                        }
                        mOnTabClickListener.onTab(tabId);
                    } else {
                        mOnTabClickListener.DoubleOnClick(tabId, mTags[tabId]);
                    }
                }
            });
            if (null != mOnLongClickListener) {
                fragmentTabHost.getTabWidget().getChildTabViewAt(i).setOnLongClickListener(mOnLongClickListener);
            }
            i++;
        }
        return this;
    }

    private View getDefaultView(int index) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.default_tab_item, null);
        if (null != mIcons) {
            ImageView imageView = view.findViewById(R.id.tabIcon);
            imageView.setImageResource(mIcons[index]);
            imageView.setVisibility(VISIBLE);
        }
        if (null != mTitles) {
            TextView textView = view.findViewById(R.id.tabTxt);
            textView.setText(mTitles[index]);
            textView.setVisibility(VISIBLE);
        }
        mRedDotList.add((TextView) view.findViewById(R.id.tabRedDot));
        return view;
    }

    public void push(Fragment fragment) {
        push(fragment, null);
    }

    public void push(Fragment fragment, String tag) {
        push(fragment, null, tag);
    }

    public void push(Fragment fragment, Bundle bundle, String tag) {
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
        frameLayout.setVisibility(View.VISIBLE);
    }

    public void pop() {
        fragmentManager.popBackStack();
    }

    /**
     * tag可以为null或者相对应的tag，flags只有0和1(POP_BACK_STACK_INCLUSIVE)两种情况
     * 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment。
     * 如果tag为null ，flags为1时，弹出回退栈中所有fragment。
     * 如果tag不为null，那就会找到这个tag所对应的fragment，flags为0时，弹出该
     * fragment以上的Fragment，如果是1，弹出该fragment（包括该fragment）以
     * 上的fragment
     *
     * @param tag
     * @param flags:
     */
    public void pop(String tag, int flags) {
        fragmentManager.popBackStackImmediate(tag, flags);
    }

    public ZdTab showDot(int id, boolean isShow) {
        return this.showDot(id, -1, isShow);
    }

    public ZdTab showDot(int id, int count, boolean isShow) {
        if (count >= 10) {
            mRedDotList.get(id).setPadding(15, 10, 15, 10);
        } else if (count > 0 && count < 10) {
            mRedDotList.get(id).setPadding(15, 5, 15, 5);
        } else {
            mRedDotList.get(id).setPadding(10, 2, 10, 2);
        }

        mRedDotList.get(id).setVisibility(isShow ? VISIBLE : GONE);
        mRedDotList.get(id).setText(count > 99 ? "99+" : count > 0 ? count + "" : "");
        LogUtil.e("--------id:", id, " | count:", count, " | isShow:", isShow);
        return this;
    }

    @Override
    public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            frameLayout.setVisibility(View.VISIBLE);
            defaultTabBarL.setVisibility(GONE);
        } else {
            frameLayout.setVisibility(View.GONE);
            defaultTabBarL.setVisibility(VISIBLE);
        }
    }

    public interface OnTabClickListener {
        void onTab(int position);

        void DoubleOnClick(int tabId, String tag);
    }

    public interface OnTabViewListener {
        View view(int position);
    }
}
