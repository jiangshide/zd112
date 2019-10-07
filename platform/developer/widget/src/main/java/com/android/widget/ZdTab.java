package com.android.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.utils.LogUtil;
import com.android.widget.anim.Anim;
import com.android.widget.manager.CountDown;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class ZdTab extends ZdTabHost implements FragmentManager.OnBackStackChangedListener,
    View.OnClickListener {
  private FragmentManager fragmentManager;
  private ZdTabHost fragmentTabHost;
  private FrameLayout frameLayout;
  private ZdViewPager zdViewPager;
  private View tabLine;
  private LinearLayout defaultTabBarL;
  private Fragment[] mFragments;
  private String[] mTitles;
  private String[] mTags;
  private int[] mStates;
  private int[] mIcons;
  private int mSelectedColor = Color.parseColor("#ffd200");
  private int mUnSelectedColor = Color.parseColor("#000000");
  private OnTabViewListener mOnTabViewListener;
  private OnTabClickListener mOnTabClickListener;
  private OnLongClickListener mOnLongClickListener;
  private OnRefreshListener mOnRefreshListener;
  private final int MIN_CLICK_DELAY_TIME = 200;
  private long mLastClickTime = 0;
  private List<TextView> mRedDotList;
  private int index;
  private List<ImageView> mImageViews;
  private List<TextView> mTextViews;
  public static ZdTab instance;

  private HashMap<String, Object> hashMap;

  //private ZdTopView zdTopView;

  private final String TITLE = "title";

  private final String SMALL_TITLE = "smallTitle";

  private final String LEFT_BTN = "leftBtn";

  private final String RIGHT_BTN = "rightBtn";

  private final String LIST = "list";

  private final String CLICK_LEFT = "clickLeft";

  private final String CLICK_RIGHT = "clickRight";

  private final String CLICK_ITEM = "clickItem";

  private View rootView;

  public final static int rootViewId = 0x1000000;

  private int refresgId;//刷新Id

  public final static int TAB = 0;//被选中
  public final static int NO_TAB = 1;//不被选中

  private OnClickListener mOnLeftClickListener, mOnRightClickListener;

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
    instance = this;
    View view = LayoutInflater.from(getContext()).inflate(R.layout.default_tab, this, true);
    fragmentTabHost = view.findViewById(android.R.id.tabhost);
    frameLayout = view.findViewById(R.id.frameLayout);
    zdViewPager = view.findViewById(R.id.zdViewPager);
    tabLine = view.findViewById(R.id.tabLine);
    defaultTabBarL = view.findViewById(R.id.defaultTabBarL);
    hashMap = new HashMap<>();
  }

  public ZdTab put(String key, Object object) {
    hashMap.put(key, object);
    return this;
  }

  public ZdTab setTitle(Object title) {
    //if (zdTopView != null) {
    //  zdTopView.setTitle(title);
    //}
    return put(TITLE, title);
  }

  public ZdTab setSmallTitle(Object smallTitle) {
    //if (zdTopView != null) {
    //  zdTopView.setSmallTitle(smallTitle);
    //}
    return put(SMALL_TITLE, smallTitle);
  }

  public ZdTab setLet(Object object) {
    //if (zdTopView != null) {
    //  zdTopView.setLeft(object);
    //}
    return put(LEFT_BTN, object);
  }

  public ZdTab setRight(Object object) {
    //if (zdTopView != null) {
    //  zdTopView.setRight(object);
    //}
    return put(RIGHT_BTN, object);
  }

  public ZdTab setDataList(Object object) {
    //if (zdTopView != null) {
    //  zdTopView.setDataList((List<String>) object);
    //}
    return put(LIST, object);
  }

  public ZdTab setLeftListener(OnClickListener listener) {
    //if (zdTopView != null) {
    //  zdTopView.setOnLeftClick(listener);
    //}
    this.mOnLeftClickListener = listener;
    return put(CLICK_LEFT, listener);
  }

  public ZdTab setRightListener(OnClickListener listener) {
    //if (zdTopView != null) {
    //  zdTopView.setOnRightClick(listener);
    //}
    this.mOnRightClickListener = listener;
    return put(CLICK_RIGHT, listener);
  }

  public ZdTab setOnItemListener(AdapterView.OnItemClickListener itemListener) {
    //if (zdTopView != null) {
    //  zdTopView.setOnItemListener(itemListener);
    //}
    return put(CLICK_ITEM, itemListener);
  }

  public View setTopView(View view, String title) {
    LinearLayout root = new LinearLayout(getContext());
    root.setOrientation(LinearLayout.VERTICAL);
    //if (zdTopView != null) {
    //  zdTopView.removeAllViews();
    //  zdTopView = null;
    //}
    ZdTopView zdTopView = new ZdTopView(getContext());
    zdTopView.setOnLeftClick(this);
    zdTopView.setOnRightClick(this);
    if (hashMap.containsKey(TITLE)) {
      zdTopView.setTitle(hashMap.get(TITLE));
    }
    if (hashMap.containsKey(SMALL_TITLE)) {
      zdTopView.setSmallTitle(hashMap.get(SMALL_TITLE));
    }
    if (hashMap.containsKey(LEFT_BTN)) {
      zdTopView.setLeft(hashMap.get(LEFT_BTN));
    }
    if (hashMap.containsKey(RIGHT_BTN)) {
      zdTopView.setRight(hashMap.get(RIGHT_BTN));
    }
    if (hashMap.containsKey(CLICK_LEFT)) {
      Object t = hashMap.get(CLICK_LEFT);
      if (t instanceof OnClickListener) {
        zdTopView.setOnLeftClick((OnClickListener) t);
      }
    }
    if (hashMap.containsKey(CLICK_RIGHT)) {
      Object t = hashMap.get(CLICK_RIGHT);
      if (t instanceof OnClickListener) {
        zdTopView.setOnRightClick((OnClickListener) t);
      }
    }
    if (hashMap.containsKey(CLICK_ITEM)) {
      Object t = hashMap.get(CLICK_ITEM);
      if (t instanceof AdapterView.OnItemClickListener) {
        zdTopView.setOnItemListener((AdapterView.OnItemClickListener) t);
      }
    }
    if (hashMap.containsKey(LIST)) {
      Object t = hashMap.get(LIST);
      zdTopView.setDataList((List<String>) t);
    }
    if (!TextUtils.isEmpty(title)) {
      zdTopView.setTitle(title);
    }
    root.addView(zdTopView);
    root.addView(view);
    return root;
  }

  public View showLoading(Activity activity) {
    return showTips(activity, R.layout.default_loading);
  }

  public View showFail(Activity activity) {
    return showTips(activity, R.layout.default_fail);
  }

  public View showTips(Activity activity, int layout) {
    rootView = LayoutInflater.from(activity).inflate(layout, null);
    rootView.setId(rootViewId);
    ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
    ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
    );
    ((FrameLayout.LayoutParams) params).gravity = Gravity.CENTER;
    viewGroup.addView(rootView, params);
    return rootView;
  }

  public void hiddleTips(Activity activity) {
    if (rootView == null || activity == null) return;
    ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
    viewGroup.removeView(rootView);
  }

  public ZdTab setLine(boolean isShow) {
    tabLine.setVisibility(isShow ? VISIBLE : GONE);
    return this;
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

  public ZdTab setState(int... states) {
    this.mStates = states;
    return this;
  }

  public ZdTab setUnSelectedColor(int color) {
    this.mUnSelectedColor = color;
    return this;
  }

  public ZdTab setSelectedColor(int color) {
    this.mSelectedColor = color;
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

  public ZdTab setRefreshListener(OnRefreshListener refreshListener) {
    this.mOnRefreshListener = refreshListener;
    return this;
  }

  public ZdTab setTab(int tabId) {
    if (validate(tabId)) return this;
    this.index = tabId;
    if (fragmentTabHost != null) {
      fragmentTabHost.setCurrentTab(index);
      setTextColor(index);
    }
    return this;
  }

  public ZdTab setTabIcon(int icon) {
    if (!mImageViews.isEmpty()) {
      ImageView imageView = mImageViews.get(refresgId);
      imageView.setImageResource(mIcons[refresgId]);
      imageView.clearAnimation();
      if (mOnRefreshListener != null) {
        mOnRefreshListener.onRefresh(false);
      }
      refresgId = fragmentTabHost.getCurrentTab();
      ImageView imageViewNew = mImageViews.get(refresgId);
      imageViewNew.setImageResource(icon);
      Anim.anim(imageViewNew);
      if (mOnRefreshListener != null) {
        mOnRefreshListener.onRefresh(true);
      }
      CountDown.getInstance().setListener(new CountDown.OnCountDownListener() {
        @Override public void onFinish() {
          imageViewNew.setImageResource(mIcons[refresgId]);
          imageViewNew.clearAnimation();
        }
      }).create(5);
    }
    return this;
  }

  public ZdTab create() {
    mImageViews = new ArrayList<>();
    mTextViews = new ArrayList<>();
    mRedDotList = new ArrayList<>();
    int i = 0;
    for (Fragment fragment : mFragments) {
      String title = i + "";
      if (null != mTitles) {
        title = mTitles[i];
      }
      TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(title)
          .setIndicator(
              null != mOnTabViewListener ? mOnTabViewListener.view(i) : getDefaultView(i));
      fragmentTabHost.addTab(tabSpec, fragment.getClass(), fragment.getArguments());
      fragmentTabHost.getTabWidget().getChildTabViewAt(i).setId(i);
      fragmentTabHost.getTabWidget().getChildTabViewAt(i).setTag(mTags[i]);
      fragmentTabHost.getTabWidget()
          .getChildTabViewAt(i)
          .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              long currentTime = System.currentTimeMillis();
              int tabId = v.getId();
              if (validate(tabId)) return;
              if (mOnTabClickListener != null) {
                mOnTabClickListener.onTab(tabId);
              }
              setTextColor(tabId);
              if (currentTime - mLastClickTime > MIN_CLICK_DELAY_TIME) {
                mLastClickTime = currentTime;
                if (!mTags[tabId].contains("no")) {
                  fragmentTabHost.setCurrentTab(v.getId());
                }
                index = tabId;
              } else {
                mOnTabClickListener.DoubleOnClick(tabId, mTags[tabId]);
              }
            }
          });
      if (null != mOnLongClickListener) {
        fragmentTabHost.getTabWidget()
            .getChildTabViewAt(i)
            .setOnLongClickListener(mOnLongClickListener);
      }
      i++;
    }
    setTextColor(index);
    return this;
  }

  private boolean validate(int tabId) {
    for (int i = 0; i < mStates.length; i++) {
      if (i == tabId && mStates[i] == NO_TAB) {
        if (mOnTabClickListener != null) {
          mOnTabClickListener.onNoTab(tabId);
          return true;
        }
      }
    }
    return false;
  }

  private View getDefaultView(int tab) {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.default_tab_item, null);
    if (null != mIcons) {
      ImageView imageView = view.findViewById(R.id.tabIcon);
      imageView.setImageResource(mIcons[tab]);
      imageView.setVisibility(VISIBLE);
      mImageViews.add(imageView);
    }
    if (null != mTitles) {
      TextView textView = view.findViewById(R.id.tabTxt);
      textView.setText(mTitles[tab]);
      textView.setVisibility(TextUtils.isEmpty(mTitles[tab]) ? GONE : VISIBLE);
      mTextViews.add(textView);
    }
    mRedDotList.add(view.findViewById(R.id.tabRedDot));
    return view;
  }

  private ZdTab setTextColor(int tab) {
    if (mTextViews == null || mTextViews.size() == 0) return this;
    for (TextView textView : mTextViews) {
      textView.setTextColor(mUnSelectedColor);
    }
    mTextViews.get(tab).setTextColor(mSelectedColor);
    return this;
  }

  public ZdTab push(Fragment fragment) {
    return push(fragment, null);
  }

  public ZdTab push(Fragment fragment, String tag) {
    return push(fragment, null, tag);
  }

  public ZdTab push(Fragment fragment, Bundle bundle, String tag) {
    fragment.setArguments(bundle);
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.frameLayout, fragment);
    //fragmentTransaction.replace(R.id.frameLayout, fragment);
    fragmentTransaction.addToBackStack(tag);
    fragmentTransaction.commitAllowingStateLoss();
    showFragment(true);
    hashMap.clear();

    return this;
  }

  public ZdTab pop() {
    fragmentManager.popBackStack();
    LogUtil.e("---------pop");
    return this;
  }

  /**
   * tag可以为null或者相对应的tag，flags只有0和1(POP_BACK_STACK_INCLUSIVE)两种情况
   * 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment。
   * 如果tag为null ，flags为1时，弹出回退栈中所有fragment。
   * 如果tag不为null，那就会找到这个tag所对应的fragment，flags为0时，弹出该
   * fragment以上的Fragment，如果是1，弹出该fragment（包括该fragment）以
   * 上的fragment
   */
  public ZdTab pop(String tag, int flags) {
    fragmentManager.popBackStackImmediate(tag, flags);
    return this;
  }

  public ZdTab setDot() {
    return this.setDot(index);
  }

  public ZdTab setDot(int id) {
    return this.setDot(id, true);
  }

  public ZdTab setDot(int id, boolean isShow) {
    return this.setDot(id, -1, isShow);
  }

  public ZdTab setDot(int id, int count, boolean isShow) {
    if (count >= 10) {
      mRedDotList.get(id).setPadding(15, 10, 15, 10);
    } else if (count > 0 && count < 10) {
      mRedDotList.get(id).setPadding(15, 5, 15, 5);
    } else {
      mRedDotList.get(id).setPadding(10, 2, 10, 2);
    }

    mRedDotList.get(id).setVisibility(isShow ? VISIBLE : GONE);
    mRedDotList.get(id).setText(count > 99 ? "99+" : count > 0 ? count + "" : "");
    return this;
  }

  @Override
  public void onBackStackChanged() {
    showFragment(fragmentManager != null && (fragmentManager.getBackStackEntryCount() > 0));
  }

  private void showFragment(boolean isShow) {
    frameLayout.setVisibility(isShow ? VISIBLE : GONE);
    defaultTabBarL.setVisibility(isShow ? GONE : VISIBLE);
  }

  @Override public void onClick(View view) {
    long currentTime = System.currentTimeMillis();
    if (currentTime - mLastClickTime > MIN_CLICK_DELAY_TIME) {
      mLastClickTime = currentTime;
      if (view.getId() == R.id.topLeftBtn) {
        if (mOnLeftClickListener != null) {
          mOnLeftClickListener.onClick(view);
        } else {
          pop();
        }
      } else if (view.getId() == R.id.topRightBtn) {
        if (mOnRightClickListener != null) {
          mOnRightClickListener.onClick(view);
        }
      }
    }
  }

  public interface OnTabClickListener {
    void onTab(int position);

    void onNoTab(int position);

    void DoubleOnClick(int tabId, String tag);
  }

  public interface OnTabViewListener {
    View view(int position);
  }

  public interface OnRefreshListener {
    void onRefresh(boolean isRefresh);
  }
}
