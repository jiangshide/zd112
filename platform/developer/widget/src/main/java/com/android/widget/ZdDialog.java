package com.android.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.android.utils.DateUtil;
import com.android.utils.ScreenUtil;
import com.android.utils.SystemUtil;
import com.android.widget.adapter.ZdListAdapter;
import com.android.widget.anim.Anim;
import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class ZdDialog extends Dialog
    implements AdapterView.OnItemClickListener, ZdWheel.WheelOnItemSelectedListener {
  private List<String> mDataList;
  private List<Integer> mDataColorList;
  private View mView, mDialogLine;
  private static PopupWindow mPopupWindow;
  private DialogViewListener mDialogViewListener;
  private String mTitle, mEditContent, mEditHint, mContent, mRightStr, mLeftStr;
  private ListView mDefaultDialogList;
  private Button mDefaultDialogCancel;
  private TextView mTitleView, mContentView;
  private LinearLayout dialogProgressBarL;
  private ProgressBar dialogProgressBar;
  private TextView dialogProgressBarTxt;
  private ZdEditText mDialogEdit;
  private ZdButton mDialogLeft, mDialogRight;
  private List<ZdWheel> mWheelList;
  private DialogOnClickListener mDialogOnClickListener;
  private AdapterView.OnItemClickListener mOnItemClickListener;
  private DialogViewWheelListener mDialogViewWheelListener;
  private DialogViewWheelBirthdayListener mDialogViewWheelBirthdayListener;
  private boolean mIsReturn;
  private boolean mIsFull = false;
  private boolean mIsEdit = false;
  private boolean mIsOnlyContent = false;
  private boolean mIsOnlySure = false;
  private int defaultEditLength = 12;
  private int mContentSize = 12;
  private boolean mIsWheel;
  private List<List<String>> mWheelDatas;
  private List<ZdWheel.WheelOnItemSelectedListener> mWheelListeners;
  private int mDefaultWheelIndex = 18;
  private int mDefaultWheelTextSize = 16;
  private boolean mIsBirthday = false;
  private static int mIsBirthdayArea = 50;
  private boolean mIsRelation = false;
  private String mBirthday;

  private static ZdDialog mZdDialog;

  public static ZdDialog create(Context context) {
    create(context, R.style.DialogTheme);
    return mZdDialog;
  }

  public static ZdDialog create(Context context, int style) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, style);
  }

  public static ZdDialog createView(Context context, int layout, DialogViewListener l) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, layout, l);
  }

  public static ZdDialog createList(Context context, List<String> dataList) {
    createList(context, R.style.DialogTheme, dataList);
    return mZdDialog;
  }

  public static ZdDialog createList(Context context, List<String> dataList,
      List<Integer> dataColorList) {
    createList(context, R.style.DialogTheme, dataList, dataColorList);
    return mZdDialog;
  }

  public static ZdDialog createList(Context context, int style, List<String> dataList) {
    createList(context, style, dataList, null);
    return mZdDialog;
  }

  public static ZdDialog createList(Context context, int style, List<String> dataList,
      List<Integer> dataColorList) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, style, dataList, dataColorList);
  }

  public static ZdDialog createWheelBirthday(Context context) {
    return createWheelBirthday(context, R.style.DialogTheme);
  }

  public static ZdDialog createWheelBirthday(Context context, int style) {
    List<List<String>> arrLists = new ArrayList<>();
    int year = DateUtil.getYear() - 11;
    List<String> years = new ArrayList<>();
    for (int i = year; i > year - mIsBirthdayArea; i--) {
      if (i < 10) break;
      years.add(i + "");
    }
    arrLists.add(years);
    List<String> months = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      months.add(i + "");
    }
    arrLists.add(months);
    List<String> days = new ArrayList<>();
    int day = DateUtil.getDay(years.get(0), months.get(0));
    for (int i = 1; i <= day; i++) {
      days.add(i + "");
    }
    arrLists.add(days);
    return createWheel(context, style, arrLists, true);
  }

  public static ZdDialog createWheel(Context context, List<List<String>> dataList) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, R.style.DialogTheme, dataList, false, true);
  }

  public static ZdDialog createWheel(Context context, List<List<String>> dataList,
      boolean isBirthday) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, R.style.DialogTheme, dataList, isBirthday, true);
  }

  public static ZdDialog createWheel(Context context, int style, List<List<String>> dataList) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, style, dataList, false, true);
  }

  public static ZdDialog createWheel(Context context, int style, List<List<String>> dataList,
      boolean isBirthday) {
    cancelDialog();
    return mZdDialog = new ZdDialog(context, style, dataList, isBirthday, true);
  }

  private static ZdDialog cancelDialog() {
    if (null != mZdDialog) {
      mZdDialog.dismiss();
      mZdDialog = null;
    }
    return mZdDialog;
  }

  public ZdDialog(@NonNull Context context, int style) {
    super(context, style);
    mView = null;
  }

  public ZdDialog(@NonNull Context context, List<String> dataList) {
    super(context, R.style.DialogTheme);
    initList(context, dataList, null);
  }

  public ZdDialog(@NonNull Context context, List<String> dataList, List<Integer> dataColorList) {
    super(context, R.style.DialogTheme);
    initList(context, dataList, dataColorList);
  }

  public ZdDialog(@NonNull Context context, int style, List<String> dataList) {
    super(context, style);
    initList(context, dataList, null);
  }

  public ZdDialog(@NonNull Context context, int style, List<String> dataList,
      List<Integer> dataColorList) {
    super(context, style);
    initList(context, dataList, dataColorList);
  }

  private void initList(Context context, List<String> dataList, List<Integer> dataColorList) {
    this.mDataList = dataList;
    this.mDataColorList = dataColorList;
    mView = LayoutInflater.from(context).inflate(R.layout.default_dialog_list, null);
  }

  public ZdDialog(@NonNull Context context, List<List<String>> dataList, boolean isBirthday,
      boolean isWheel) {
    super(context, R.style.DialogTheme);
    this.mIsBirthday = isBirthday;
    initWheel(context, dataList, isWheel);
  }

  public ZdDialog(@NonNull Context context, int style, List<List<String>> dataList,
      boolean isBirthday, boolean isWheel) {
    super(context, style);
    this.mIsBirthday = isBirthday;
    initWheel(context, dataList, isWheel);
  }

  private void initWheel(Context context, List<List<String>> dataList, boolean isWheel) {
    this.mIsWheel = isWheel;
    this.mWheelDatas = dataList;
    mView = LayoutInflater.from(context).inflate(R.layout.default_dialog_wheel, null);
  }

  public ZdDialog(@NonNull Context context, int style, int layout) {
    super(context, style);
    this.mView = LayoutInflater.from(context).inflate(layout, null);
  }

  public ZdDialog(@NonNull Context context, int style, View view) {
    super(context, style);
    this.mView = view;
  }

  public ZdDialog(@NonNull Context context, View view, DialogViewListener dialogViewListener) {
    super(context, R.style.DialogTheme);
    this.mView = view;
    this.mDialogViewListener = dialogViewListener;
  }

  public ZdDialog(@NonNull Context context, int style, View view,
      DialogViewListener dialogViewListener) {
    super(context, style);
    this.mView = view;
    this.mDialogViewListener = dialogViewListener;
  }

  public ZdDialog(@NonNull Context context, int layout, DialogViewListener dialogViewListener) {
    super(context, R.style.DialogTheme);
    this.mView = LayoutInflater.from(context).inflate(layout, null);
    this.mDialogViewListener = dialogViewListener;
  }

  public ZdDialog(@NonNull Context context, int style, int layout,
      DialogViewListener dialogViewListener) {
    super(context, style);
    this.mView = LayoutInflater.from(context).inflate(layout, null);
    this.mDialogViewListener = dialogViewListener;
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mView != null) {
      setContentView(mView);
      if (mIsWheel) {
        defailtWheel();
      } else if ((null != mDataList && mDataList.size() > 0)) {
        defailtViewList();
      }
    } else {
      setContentView(R.layout.default_dialog);
      defaultView();
    }
    if (mView != null) {
      ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
      layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
      if (mIsFull) {
        layoutParams.height = getContext().getResources().getDisplayMetrics().heightPixels;
      }
      mView.setLayoutParams(layoutParams);
    }
    if (mDialogViewListener != null) {
      mDialogViewListener.onView(mView);
    }
  }

  public ZdDialog setFull(boolean isFull) {
    this.mIsFull = isFull;
    return this;
  }

  private void defailtWheel() {
    LinearLayout defaultDialogWheelL = findViewById(R.id.defaultDialogWheelL);
    mDialogLeft = findViewById(R.id.defaultDialogWheelLeft);
    mDialogRight = findViewById(R.id.defaultDialogWheelRight);
    if (null == mWheelDatas) return;
    LinearLayout linearLayout = new LinearLayout(getContext());
    linearLayout.setBackgroundResource(R.drawable.bg_dialog_radius_top);
    mWheelList = new ArrayList<>();
    int i = 0;
    String[] birthday = null;
    if (mIsBirthday && !TextUtils.isEmpty(mBirthday)) {
      if (mBirthday.contains("-")) {
        birthday = mBirthday.split("-");
      } else if (mBirthday.length() == 8) {
        birthday = new String[3];
        birthday[0] = mBirthday.substring(0, 4);
        birthday[1] = mBirthday.substring(4, 6);
        birthday[2] = mBirthday.substring(6, 8);
      }
    }
    for (List<String> list : mWheelDatas) {
      ZdWheel zdWheel = new ZdWheel(getContext());
      zdWheel.setId(i);
      linearLayout.setOrientation(LinearLayout.HORIZONTAL);
      linearLayout.addView(zdWheel,
          new LinearLayout.LayoutParams(0, ScreenUtil.getScreenHeight(getContext()) / 3, 1));
      zdWheel.setItems(list);
      zdWheel.setTextSize(mDefaultWheelTextSize);
      if (null != birthday && birthday.length >= i) {
        zdWheel.setCurrentPosition(birthday[i]);
      } else {
        zdWheel.setCurrentPosition(mDefaultWheelIndex);
      }
      zdWheel.setListener(null != mWheelListeners ? mWheelListeners.get(i) : this);
      i++;
      mWheelList.add(zdWheel);
    }
    defaultDialogWheelL.addView(linearLayout);
    setGravity(Gravity.BOTTOM);
    setAnim(R.style.bottomAnim);
    mDialogLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isCancel = isCancel(mDialogLeft);
        if (mDialogViewWheelListener != null) {
          //                    if (!isCancel) {
          //                    }
          mDialogViewWheelListener.onDialogWheelClick(isCancel, mWheelList);
        } else if (mIsBirthday && mDialogViewWheelBirthdayListener != null) {
          if (!isCancel) {
            String year = mWheelList.get(0).getSelectedItem().string;
            String month = mWheelList.get(1).getSelectedItem().string;
            String day = mWheelList.get(2).getSelectedItem().string;
            mDialogViewWheelBirthdayListener.OnDialogWheelBirthday(isCancel, year, month, day);
          } else {
            mDialogViewWheelBirthdayListener.OnDialogWheelBirthday(isCancel, null, null, null);
          }
        }
        dismiss();
      }
    });
    mDialogRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isCancel = isCancel(mDialogRight);
        if (mDialogViewWheelListener != null) {
          //                    if (!isCancel) {
          //                    }
          mDialogViewWheelListener.onDialogWheelClick(isCancel, mWheelList);
        } else if (mIsBirthday && mDialogViewWheelBirthdayListener != null) {
          if (!isCancel) {
            String year = mWheelList.get(0).getSelectedItem().string;
            String month = mWheelList.get(1).getSelectedItem().string;
            String day = mWheelList.get(2).getSelectedItem().string;
            mDialogViewWheelBirthdayListener.OnDialogWheelBirthday(isCancel, year, month, day);
          } else {
            mDialogViewWheelBirthdayListener.OnDialogWheelBirthday(isCancel, null, null, null);
          }
        }
        dismiss();
      }
    });
  }

  public ZdDialog setWheelListListener(List<ZdWheel.WheelOnItemSelectedListener> listeners) {
    this.mWheelListeners = listeners;
    return this;
  }

  private void defailtViewList() {
    mDefaultDialogList = findViewById(R.id.defaultDialogList);
    mDefaultDialogCancel = findViewById(R.id.defaultDialogCancel);
    mDefaultDialogCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
        if (mDialogOnClickListener != null) {
          mDialogOnClickListener.onDialogClick(true, null);
        }
      }
    });

    mDefaultDialogList.setAdapter(
        new ZdListAdapter<String>(getContext(), mDataList, R.layout.default_dialog_list_item) {
          @Override
          protected void convertView(int position, View item, String s) {
            TextView textView = ((TextView) get(item, R.id.defaultDialogListItem));
            if (null != mDataColorList && mDataColorList.size() - 1 >= position) {
              textView.setTextColor(
                  ContextCompat.getColor(getContext(), mDataColorList.get(position)));
            }
            textView.setText(s);
          }
        });
    mDefaultDialogList.setOnItemClickListener(this);
    setGravity(Gravity.BOTTOM);
    setAnim(R.style.bottomAnim);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    dismiss();
    if (null != mOnItemClickListener) mOnItemClickListener.onItemClick(parent, view, position, id);
  }

  private void defaultView() {
    mTitleView = this.findViewById(R.id.title);
    if (!TextUtils.isEmpty(mTitle)) mTitleView.setText(mTitle);
    if (mIsOnlyContent) mTitleView.setVisibility(View.GONE);
    mContentView = this.findViewById(R.id.dialogContent);
    dialogProgressBarL = this.findViewById(R.id.dialogProgressBarL);
    dialogProgressBar = this.findViewById(R.id.dialogProgressBar);
    dialogProgressBarTxt = this.findViewById(R.id.dialogProgressBarTxt);
    if (mContentSize > 0) mContentView.setTextSize(mContentSize);
    mDialogEdit = this.findViewById(R.id.dialogEdit);
    mDialogEdit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(defaultEditLength) });
    if (!TextUtils.isEmpty(mContent)) mContentView.setText(mContent);
    if (!TextUtils.isEmpty(mEditContent)) mDialogEdit.setText(mEditContent);
    if (!TextUtils.isEmpty(mEditHint)) mDialogEdit.setHint(mEditHint);
    mDialogEdit.setVisibility(mIsEdit ? View.VISIBLE : View.GONE);
    mContentView.setVisibility(mIsEdit ? View.GONE : View.VISIBLE);
    mDialogLeft = this.findViewById(R.id.dialogLeft);
    mDialogRight = this.findViewById(R.id.dialogRight);
    mDialogLine = this.findViewById(R.id.dialogLine);
    if (!TextUtils.isEmpty(mRightStr)) mDialogRight.setText(mRightStr);
    if (!TextUtils.isEmpty(mLeftStr)) mDialogLeft.setText(mLeftStr);
    if (mIsOnlySure) {
      mDialogRight.setCurrCorner(0, 0,
          getContext().getResources().getDimension(R.dimen.default_corner),
          getContext().getResources().getDimension(R.dimen.default_corner));
      mDialogLine.setVisibility(View.GONE);
      mDialogLeft.setVisibility(View.GONE);
    }
    mDialogLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mDialogOnClickListener != null) {
          boolean isCancel = isCancel(mDialogLeft);
          String editStr = null;
          if (!isCancel && mIsEdit && null != mDialogEdit) {
            editStr = mDialogEdit.getEditableText().toString().trim();
            if (TextUtils.isEmpty(editStr)) {
              mDialogEdit.setHint(getContext().getString(R.string.no_content));
              Anim.shakeView(mDialogEdit);
              return;
            }
          }
          mDialogOnClickListener.onDialogClick(isCancel, editStr);
        }
        dismiss();
      }
    });
    mDialogRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mDialogOnClickListener != null) {
          boolean isCancel = isCancel(mDialogRight);
          String editStr = null;
          if (!isCancel && mIsEdit && null != mDialogEdit) {
            editStr = mDialogEdit.getEditableText().toString().trim();
            if (TextUtils.isEmpty(editStr)) {
              mDialogEdit.setHint(getContext().getString(R.string.no_content));
              Anim.shakeView(mDialogEdit);
              return;
            }
          }
          mDialogOnClickListener.onDialogClick(isCancel, editStr);
        }
        dismiss();
      }
    });
  }

  private boolean isCancel(ZdButton zdButton) {
    String btnStr = zdButton.getText().toString().trim();
    if (!TextUtils.isEmpty(btnStr) && (btnStr.contains(getContext().getString(R.string.cancel))
        || btnStr.contains(getContext().getString(R.string.off)))) {
      return true;
    }
    return false;
  }

  public ZdDialog setAttr() {
    Window window = getWindow();
    WindowManager.LayoutParams params = window.getAttributes();
    params.gravity = Gravity.CENTER;
    window.setAttributes(params);
    return this;
  }

  public ShapeDrawable createRoundCornerShapeDrawable(float radius, float borderLength,
      int borderColor) {
    float[] outerRadii = new float[8];
    float[] innerRadii = new float[8];
    for (int i = 0; i < 8; i++) {
      outerRadii[i] = radius + borderLength;
      innerRadii[i] = radius;
    }
    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(outerRadii,
        new RectF(borderLength, borderLength, borderLength, borderLength), innerRadii));
    shapeDrawable.getPaint().setColor(borderColor);
    return shapeDrawable;
  }

  public ZdDialog setOnlySure() {
    mIsOnlySure = true;
    return this;
  }

  public ZdDialog setOnlySure(String rightTxtStr) {
    this.mRightStr = rightTxtStr;
    return this.setOnlySure();
  }

  public ZdDialog setOnlySure(int rightTxt) {
    return this.setOnlySure(getContext().getString(rightTxt));
  }

  public ZdDialog setGravity(int gravity) {
    getWindow().setGravity(gravity);
    return this;
  }

  public ZdDialog setAnim(int anim) {
    getWindow().setWindowAnimations(anim);
    return this;
  }

  public ZdDialog setTitles(String title) {
    this.mTitle = title;
    return this;
  }

  public ZdDialog setTitles(int title) {
    return this.setTitles(getContext().getString(title));
  }

  public ZdDialog setContent(String content) {
    this.mContent = content;
    return this;
  }

  public ZdDialog setContent(int content) {
    return this.setContent(getContext().getString(content));
  }

  public ZdDialog setContentOnly(String content) {
    mIsOnlyContent = true;
    return this.setContent(content);
  }

  public ZdDialog setContentOnly(int content) {
    mIsOnlyContent = true;
    return this.setContent(content);
  }

  public ZdDialog setContentSize(int size) {
    mContentSize = size;
    return this;
  }

  public ZdDialog setEdit(boolean isEdit) {
    this.mIsEdit = isEdit;
    return this;
  }

  public ZdDialog setEditContent(String editContent) {
    this.mEditContent = editContent;
    if (mDialogEdit != null) {
      mDialogEdit.setHint(mEditContent);
    }
    return this;
  }

  public ZdDialog setEditContent(int editContent) {
    return this.setEditContent(getContext().getString(editContent));
  }

  public ZdDialog setEditMaxLength(int maxLength) {
    this.defaultEditLength = maxLength;
    return this;
  }

  public ZdDialog setEditHint(String editHint) {
    this.mEditHint = editHint;
    if (mDialogEdit != null) {
      mDialogEdit.setHint(editHint);
    }
    return this;
  }

  public ZdDialog setEditHint(int editHint) {
    return this.setEditHint(getContext().getString(editHint));
  }

  public ZdDialog setLeftTxtColor(int color) {
    if (null != mDialogLeft) mDialogLeft.setTextColor(ContextCompat.getColor(getContext(), color));
    return this;
  }

  public ZdDialog setRightTxtColor(int color) {
    if (null != mDialogRight) {
      mDialogRight.setTextColor(ContextCompat.getColor(getContext(), color));
    }
    return this;
  }

  public ZdDialog setContentPosition(int position) {
    if (mContentView != null) {
      mContentView.setGravity(position);
    }
    return this;
  }

  public ZdDialog setProgress() {
    if (dialogProgressBarL == null) return this;
    dialogProgressBarL.setVisibility(View.VISIBLE);
    dialogProgressBar.setMax(100);
    return this;
  }

  public ZdDialog setProgress(int progress) {
    if (dialogProgressBar == null) return this;
    dialogProgressBar.setProgress(progress);
    dialogProgressBarTxt.setText(progress + "%");
    mDialogRight.setVisibility(progress == 100 ? View.VISIBLE : View.GONE);
    mDialogLeft.setVisibility(progress == 100 ? View.VISIBLE : View.GONE);
    return this;
  }

  public ZdDialog setRightTxt(String rightTxtStr) {
    this.mRightStr = rightTxtStr;
    if (mDialogRight != null) {
      mDialogRight.setText(mRightStr);
    }
    return this;
  }

  public ZdDialog setRightTxt(int rightTxt) {
    return this.setRightTxt(getContext().getString(rightTxt));
  }

  public ZdDialog setLeftTxt(String leftTxtStr) {
    this.mLeftStr = leftTxtStr;
    if (mDialogLeft != null) {
      mDialogLeft.setText(mLeftStr);
    }
    return this;
  }

  public ZdDialog setLeftTxt(int leftTxt) {
    return this.setLeftTxt(getContext().getString(leftTxt));
  }

  public ZdDialog setReturn(boolean isReturn) {
    this.mIsReturn = isReturn;
    return this;
  }

  public ZdDialog setOnWheelListener(DialogViewWheelListener listener) {
    this.mDialogViewWheelListener = listener;
    return this;
  }

  public ZdDialog setOnWheelBirthdayListener(DialogViewWheelBirthdayListener listener) {
    this.mDialogViewWheelBirthdayListener = listener;
    return this;
  }

  public ZdDialog setWheelTxtSize(int txtSize) {
    this.mDefaultWheelTextSize = txtSize;
    return this;
  }

  public ZdDialog setWheelPosition(int wheelIndex) {
    this.mDefaultWheelIndex = wheelIndex;
    return this;
  }

  public ZdDialog setBirthdayArea(int birthdayArea) {
    this.mIsBirthdayArea = birthdayArea;
    return this;
  }

  public ZdDialog setRelation(boolean isRelation) {
    this.mIsRelation = isRelation;
    return this;
  }

  public ZdDialog setOnItemListener(AdapterView.OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
    return this;
  }

  public ZdDialog setListener(DialogOnClickListener l) {
    this.mDialogOnClickListener = l;
    return this;
  }

  public ZdDialog setOutsideClose(boolean isClose) {
    this.setCanceledOnTouchOutside(isClose);
    return this;
  }

  public ZdDialog setBirthday(String birthday) {
    this.mBirthday = birthday;
    return this;
  }

  @Override
  public void onItemSelected(ZdWheel zdWheel, ZdWheel.IndexString indexString) {
    int index = zdWheel.getId();
    int size = mWheelList.size() - 1;
    if (mIsBirthday && index <= size) {
      String year = mWheelList.get(0).getSelectedItem().string;
      String month = mWheelList.get(1).getSelectedItem().string;
      int day = DateUtil.getDay(year, month);
      List<String> days = new ArrayList<>();
      for (int i = 1; i <= day; i++) {
        days.add(i + "");
      }
      mWheelList.get(size).setItems(days);
    } else if (mIsRelation && index < size) {
      index = index + 1;
      mWheelList.get(index).setItems(mWheelDatas.get(index));
    }
  }

  public static void popuWindow(Context context, View view, int layout) {
    popuWindow(context, view, layout, null);
  }

  public static void popuWindow(Context context, View view, int layout, OnPopuListener listener) {
    View rootView = LayoutInflater.from(context).inflate(layout, null);
    cancelPopu();
    mPopupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, true);
    mPopupWindow.setTouchable(true);
    mPopupWindow.setOutsideTouchable(true);
    view.getHeight();
    int[] location = new int[2];
    view.getLocationInWindow(location);
    boolean isBottom = false;
    if (ScreenUtil.getScreenHeight(context)
        - SystemUtil.getStatusBarHeight((Activity) context)
        - location[1] < ScreenUtil.dip2px(context, 80)) {
      mPopupWindow.showAsDropDown(view,
          ScreenUtil.getScreenWidth(context) / 4 - (int) ScreenUtil.dip2px(context, 40),
          -view.getHeight() - (int) ScreenUtil.dip2px(context, 80));
      isBottom = true;
    } else {
      mPopupWindow.showAsDropDown(view,
          ScreenUtil.getScreenWidth(context) / 4 - (int) ScreenUtil.dip2px(context, 40), 0);
      isBottom = false;
    }
    if (listener != null) {
      listener.onView(rootView, isBottom);
    }
  }

  public static void cancelPopu() {
    if (mPopupWindow != null) {
      mPopupWindow.dismiss();
    }
    mPopupWindow = null;
  }

  public interface OnPopuListener {
    void onView(View view, boolean isBottom);
  }

  public interface DialogViewWheelListener extends DialogListener {
    void onDialogWheelClick(boolean isCancel, List<ZdWheel> wheelViews);
  }

  public interface DialogViewWheelBirthdayListener extends DialogListener {
    void OnDialogWheelBirthday(boolean isCancel, String year, String month, String day);
  }

  public interface DialogViewListener extends DialogListener {
    void onView(View view);
  }

  public interface DialogOnClickListener extends DialogListener {
    void onDialogClick(boolean isCancel, String editMessage);
  }

  public interface DialogListener {

  }

  @Override
  public void onBackPressed() {
    if (mDialogOnClickListener != null) {
      mDialogOnClickListener.onDialogClick(false, null);
      return;
    }
    if (!mIsReturn) {
      super.onBackPressed();
    }
  }
}
