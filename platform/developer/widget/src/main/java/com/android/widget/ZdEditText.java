package com.android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.HashMap;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class ZdEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener  {
    private Drawable mClearDrawable;
    private boolean mHasFoucs;
    private int mRequestType;
    private int mCacheType;
    private String mAction;
    private HashMap<String, String> mParams;
    private int mLength;
    public final int MOBILE = 1, ID_CARD = 2, BRAND_CARD = 3;
    private int mStart, mEnd;
    private int mFormat;
    private boolean mIsDelete;
    private CusEditListener mCusEditListener;

    public ZdEditText(Context context) {
        this(context, null);
    }

    public ZdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ZdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CusEditText, 0, 0);
        if (array != null) {
            mFormat = array.getInteger(R.styleable.CusEditText_format, 0);
            mStart = array.getInteger(R.styleable.CusEditText_formatStart, 0);
            mEnd = array.getInteger(R.styleable.CusEditText_formatEnd, 0);
            mIsDelete = array.getBoolean(R.styleable.CusEditText_isDelete, true);
            array.recycle();
        }
        if (mFormat > 0) {
            format(mFormat);
        } else {
            format(mStart, mEnd);
        }
        init();
        setTransformationMethod(new AsteriskPasswordTransformationMethod());
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void format(int format) {
        format(format, 0, 0);
    }

    public void format(int start, int end) {
        format(0, start, end);
    }

    private void format(int format, int start, int end) {
        switch (format) {
            case MOBILE:
                mStart = 2;
                mEnd = 7;
                break;
            case ID_CARD:
                mStart = 3;
                mEnd = 11;
                break;
            case BRAND_CARD:
                mStart = 2;
                mEnd = 14;
                break;
            default:
                mStart = start;
                mEnd = end;
                break;
        }
    }

    public ZdEditText setRequestSize(int length) {
        this.mLength = length;
        return this;
    }

    public ZdEditText setListener(CusEditListener listener) {
        this.mCusEditListener = listener;
        return this;
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.input_delete);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());

        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /* @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     * getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     * distance 删除图标顶部边缘到控件顶部边缘的距离
     * distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.mHasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    public void setClearIconVisible(boolean visible) {
        Drawable right = visible && mIsDelete ? mClearDrawable : null;
//        setCompoundDrawables(getCompoundDrawables()[0],
//                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public void setClearVisible(){
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (mHasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > mLength) {
            //todo:net for call
        }
        if (null != mCusEditListener) {
            mCusEditListener.afterTextChanged(s,s.toString());
        }
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        if(getContext() instanceof Activity){
            int screenHeight = ((Activity)getContext()).getWindow().getDecorView().getRootView().getHeight();
            int heightDifference = screenHeight-rect.bottom;
            if(heightDifference == 0){
                losePoint();
            }
        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                this.mSource = source;
            }

            @Override
            public int length() {
                return mSource.length();
            }

            @Override
            public char charAt(int index) {
                return (index > mStart && index < mEnd) ? '*' : mSource.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }

    public void searchPoint(){
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        setFocusable(true);
        setCursorVisible(true);
        setFocusableInTouchMode(true);
        requestFocus();
        findFocus();
        mInputMethodManager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }

    public void losePoint(){
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        setFocusable(false);
        setCursorVisible(false);
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public interface CusEditListener {
        public void afterTextChanged(Editable s, String input);
    }
}
