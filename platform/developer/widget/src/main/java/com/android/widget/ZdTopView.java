package com.android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class ZdTopView extends LinearLayout implements View.OnClickListener {

  private int mBgColor;

  private String mLeftName;
  private int mLeftTextColor;
  private int mLeftTextSize;

  private String mTitleName;
  private int mTitleTextColor;
  private int mTitleTextSize;

  private String mTitleSmallName;
  private int mTitleSmallTextColor;
  private int mTitleSmallTextSize;

  private String mRightName;
  private int mRightTextColor;
  private int mRightTextSize;

  private List<String> mDataList;

  private OnClickListener mOnLeftClickListener, mOnRightClickListener;

  private AdapterView.OnItemClickListener mOnItemClickListener;

  public ZdTopView(Context context) {
    this(context, null);
  }

  public ZdTopView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ZdTopView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZdTopView, 0, 0);
    if (array != null) {
      mBgColor = array.getColor(R.styleable.ZdTopView_bgColor,
          getResources().getColor(R.color.colorPrimaryDark));
      mLeftName = array.getString(R.styleable.ZdTopView_leftName);
      mLeftTextColor = array.getColor(R.styleable.ZdTopView_leftTextColor,
          getResources().getColor(R.color.black));
      mLeftTextSize = array.getInteger(R.styleable.ZdTopView_leftTextSize, 16);
      //            leftBg = array.getInteger(R.styleable.NavigationTopView_leftBg, 0);

      mTitleName = array.getString(R.styleable.ZdTopView_titleName);
      mTitleTextColor = array.getInteger(R.styleable.ZdTopView_titleTextColor,
          getResources().getColor(R.color.black));
      mTitleTextSize = array.getInteger(R.styleable.ZdTopView_titleTextSize, 18);

      mTitleSmallName = array.getString(R.styleable.ZdTopView_titleName);
      mTitleSmallTextColor = array.getInteger(R.styleable.ZdTopView_titleTextColor,
          getResources().getColor(R.color.black));
      mTitleSmallTextSize = array.getInteger(R.styleable.ZdTopView_titleTextSize, 13);

      mRightName = array.getString(R.styleable.ZdTopView_rightName);
      mRightTextColor = array.getColor(R.styleable.ZdTopView_rightTextColor,
          getResources().getColor(R.color.black));
      mRightTextSize = array.getInteger(R.styleable.ZdTopView_rightTextSize, 16);
      //            rightBg = array.getInteger(R.styleable.NavigationTopView_rightBg, 0);
      array.recycle();
    }
    init();
  }

  private LinearLayout topL;
  private ZdButton topLeftBtn;
  private TextView topTitle;
  private TextView topTitleSmall;
  private ZdButton topRightBtn;

  private void init() {
    addView(LayoutInflater.from(getContext()).inflate(R.layout.default_top, null),
        new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    topL = findViewById(R.id.topL);
    topLeftBtn = findViewById(R.id.topLeftBtn);
    topTitle = findViewById(R.id.topTitle);
    topTitleSmall = findViewById(R.id.topTitleSmall);
    topRightBtn = findViewById(R.id.topRightBtn);

    topL.setBackgroundColor(mBgColor);

    topLeftBtn.setTextColor(mLeftTextColor);
    topLeftBtn.setTextSize(mLeftTextSize);
    topLeftBtn.setOnClickListener(this);
    if (!TextUtils.isEmpty(mLeftName)) {
      setLeft(mLeftName);
    }

    topTitle.setText(TextUtils.isEmpty(mTitleName) ? "" : mTitleName);
    topTitle.setTextColor(mTitleTextColor);
    topTitle.setTextSize(mTitleTextSize);

    topTitleSmall.setText(TextUtils.isEmpty(mTitleSmallName) ? "" : mTitleSmallName);
    topTitleSmall.setTextColor(mTitleSmallTextColor);
    topTitleSmall.setTextSize(mTitleSmallTextSize);

    topRightBtn.setTextColor(mRightTextColor);
    topRightBtn.setTextSize(mRightTextSize);
    topRightBtn.setOnClickListener(this);
    if (!TextUtils.isEmpty(mRightName)) {
      setRight(mRightName);
    }
  }

  public ZdTopView showView(boolean isShow) {
    topL.setVisibility(isShow ? View.VISIBLE : View.GONE);
    return this;
  }

  public ZdTopView setBg(int color) {
    topL.setBackgroundColor(color);
    return this;
  }

  public ZdTopView setTitle(Object object) {
    if (object == null) return showView(false);
    String title = "";
    if (object instanceof String) {
      title = (String) object;
    } else if (object instanceof Integer) {
      title = getResources().getString((Integer) object);
    }
    if (TextUtils.isEmpty(title)) return showView(false);
    topTitle.setText(title);
    topTitle.setVisibility(VISIBLE);
    return showView(true);
  }

  public ZdTopView setSmallTitle(Object object) {
    if (object == null) return showView(false);
    String title = "";
    if (object instanceof String) {
      title = (String) object;
    } else if (object instanceof Integer) {
      title = getResources().getString((Integer) object);
    }
    if (TextUtils.isEmpty(title)) return showView(false);
    topTitleSmall.setText(title);
    topTitleSmall.setVisibility(VISIBLE);
    return showView(true);
  }

  public ZdTopView setLeftRes(int res) {
    return setLeft(getResources().getString(res));
  }

  public ZdTopView setLeft(Object object) {
    if (object == null) return showView(false);
    if (object instanceof String) {
      topLeftBtn.setText((String) object);
      topLeftBtn.drawableLeft(R.drawable.alpha);
      topLeftBtn.setVisibility(VISIBLE);
      return this.showView(true);
    } else if (object instanceof Integer) {
      topLeftBtn.drawableLeft((Integer) object);
      topLeftBtn.setText("");
      return this.showView(true);
    } else if (object instanceof Boolean) {
      topLeftBtn.setVisibility((Boolean) object ? View.VISIBLE : View.GONE);
    }
    return this;
  }

  public ZdTopView setRightRes(int res) {
    return setRight(getResources().getString(res));
  }

  public ZdTopView setRight(Object object) {
    if (object == null) return showView(false);
    if (object instanceof String) {
      topRightBtn.setText((String) object);
      topRightBtn.drawableRight(R.drawable.alpha);
      topRightBtn.setVisibility(VISIBLE);
      return showView(true);
    } else if (object instanceof Integer) {
      topRightBtn.drawableLeft((Integer) object);
      topRightBtn.setText("");
      topRightBtn.setVisibility(VISIBLE);
      return showView(true);
    } else if (object instanceof Boolean) {
      topRightBtn.setVisibility((Boolean) object ? View.VISIBLE : View.GONE);
    }
    return this;
  }

  public ZdTopView setDataList(List<String> dataList) {
    this.mDataList = dataList;
    return this;
  }

  public ZdTopView setOnLeftClick(OnClickListener listener) {
    this.mOnLeftClickListener = listener;
    return this;
  }

  public ZdTopView setOnRightClick(OnClickListener listener) {
    this.mOnRightClickListener = listener;
    return this;
  }

  public ZdTopView setOnItemListener(AdapterView.OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
    return this;
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.topLeftBtn) {
      if (mOnLeftClickListener != null) {
        mOnLeftClickListener.onClick(v);
      } else {
        ((Activity) getContext()).finish();
      }
    } else if (id == R.id.topRightBtn) {
      if (mDataList != null && mDataList.size() > 0) {
        ZdDialog zdDialog = ZdDialog.createList(getContext(), mDataList);
        if (mOnItemClickListener != null) {
          zdDialog.setOnItemListener(mOnItemClickListener);
        }
        zdDialog.show();
      } else if (mOnRightClickListener != null) {
        mOnRightClickListener.onClick(v);
      }
    }
  }
}
