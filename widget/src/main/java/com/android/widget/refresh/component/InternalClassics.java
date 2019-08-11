package com.android.widget.refresh.component;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.DimenUtil;
import com.android.widget.R;
import com.android.widget.refresh.ZdRefreshManager;
import com.android.widget.refresh.interfaces.RefreshInternal;
import com.android.widget.refresh.interfaces.RefreshKernel;
import com.android.widget.refresh.interfaces.RefreshLayout;
import com.android.widget.refresh.state.SpinnerStyle;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public abstract class InternalClassics<T extends InternalClassics> extends InternalAbstract implements RefreshInternal {

    public static final byte ID_TEXT_TITLE = 1;
    public static final byte ID_IMAGE_ARROW = 2;
    public static final byte ID_IMAGE_PROGRESS = 3;

    protected TextView mTitleText;
    protected ImageView mArrowView;
    protected ImageView mProgressView;
    protected LinearLayout mCenterLayout;
    protected RefreshKernel mRefreshKernel;
    protected ArrowDrawable mArrowDrawable;
    protected ProgressDrawable mProgressDrawable;
    protected Integer mAccentColor;
    protected Integer mPrimaryColor;
    protected int mBackgroundColor;
    protected int mFinishDuration = 500;
    protected int mPaddingTop = 20;
    protected int mPaddingBottom = 20;

    public InternalClassics(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSpinnerStyle = SpinnerStyle.Translate;
        mArrowView = new ImageView(context);
        mProgressView = new ImageView(context);
        mTitleText = new TextView(context);
        mTitleText.setVisibility(GONE);
        mTitleText.setTextColor(ContextCompat.getColor(context, R.color.gray));
        mCenterLayout = new LinearLayout(context);
        mCenterLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mCenterLayout.setOrientation(LinearLayout.VERTICAL);

        final View thisView = this;
        final ViewGroup thisGroup = this;
        final View arrowView = mArrowView;
        final View titleView = mTitleText;
        final View progressView = mProgressView;
        final ViewGroup centerLayout = mCenterLayout;

        titleView.setId(ID_TEXT_TITLE);
        arrowView.setId(ID_IMAGE_ARROW);
        progressView.setId(ID_IMAGE_PROGRESS);
        centerLayout.setId(android.R.id.widget_frame);

        LinearLayout.LayoutParams lpHeaderText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
//        centerLayout.addView(titleView, lpHeaderText);

        LayoutParams lpHeaderLayout = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpHeaderLayout.addRule(CENTER_IN_PARENT);
        thisGroup.addView(centerLayout, lpHeaderLayout);

        LayoutParams lpArrow = new LayoutParams(DimenUtil.dip2px(20), DimenUtil.dip2px(20));
        lpArrow.addRule(CENTER_VERTICAL);
        lpArrow.addRule(LEFT_OF, android.R.id.widget_frame);
        thisGroup.addView(arrowView, lpArrow);

        LayoutParams lpProgress = new LayoutParams((ViewGroup.LayoutParams) lpArrow);
        lpProgress.addRule(CENTER_IN_PARENT);
        lpProgress.addRule(LEFT_OF, android.R.id.widget_frame);
        progressView.animate().setInterpolator(new LinearInterpolator());
        thisGroup.addView(progressView, lpProgress);

        if (thisView.getPaddingTop() == 0) {
            if (thisView.getPaddingBottom() == 0) {
                thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop = DimenUtil.dip2px(20), thisView.getPaddingRight(), mPaddingBottom = DimenUtil.dip2px(20));
            } else {
                thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop = DimenUtil.dip2px(20), thisView.getPaddingRight(), mPaddingBottom = thisView.getPaddingBottom());
            }
        } else {
            if (thisView.getPaddingBottom() == 0) {
                thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop = thisView.getPaddingTop(), thisView.getPaddingRight(), mPaddingBottom = DimenUtil.dip2px(20));
            } else {
                mPaddingTop = thisView.getPaddingTop();
                mPaddingBottom = thisView.getPaddingBottom();
            }
        }

        if (thisView.isInEditMode()) {
            arrowView.setVisibility(GONE);
        } else {
            progressView.setVisibility(GONE);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final View thisView = this;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            thisView.setPadding(thisView.getPaddingLeft(), 0, thisView.getPaddingRight(), 0);
        } else {
            thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop, thisView.getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            final View arrowView = mArrowView;
            final View progressView = mProgressView;
            arrowView.animate().cancel();
            progressView.animate().cancel();
        }
        final Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        }
    }

    protected T self() {
        return (T) this;
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundFor(this, mBackgroundColor);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        final View progressView = mProgressView;
        if (progressView.getVisibility() != VISIBLE) {
            progressView.setVisibility(VISIBLE);
            Drawable drawable = mProgressView.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            } else {
                progressView.animate().rotation(36000).setDuration(100000);
            }
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        final View progressView = mProgressView;
        Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        } else {
            progressView.animate().rotation(0).setDuration(0);
        }
        progressView.setVisibility(GONE);
        return mFinishDuration;//延迟500毫秒之后再弹回
    }

    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if (colors.length > 0) {
            final View thisView = this;
            if (!(thisView.getBackground() instanceof BitmapDrawable) && mPrimaryColor == null) {
                setPrimaryColor(colors[0]);
                mPrimaryColor = null;
            }
            if (mAccentColor == null) {
                if (colors.length > 1) {
                    setAccentColor(colors[1]);
                } else {
                    setAccentColor(colors[0] == 0xffffffff ? 0xff666666 : 0xffffffff);
                }
                mAccentColor = null;
            }
        }
    }

    public T setProgressDrawable(Drawable drawable) {
        mProgressDrawable = null;
        mProgressView.setImageDrawable(drawable);
        return self();
    }

    public T setProgressResource(@DrawableRes int resId) {
        mProgressDrawable = null;
        mProgressView.setImageResource(resId);
        return self();
    }

    public T setArrowDrawable(Drawable drawable) {
        mArrowDrawable = null;
        mArrowView.setImageDrawable(drawable);
        return self();
    }

    public T setArrowResource(@DrawableRes int resId) {
        mArrowDrawable = null;
        mArrowView.setImageResource(resId);
        return self();
    }

    public T setSpinnerStyle(SpinnerStyle style) {
        this.mSpinnerStyle = style;
        return self();
    }

    public T setPrimaryColor(@ColorInt int primaryColor) {
        mBackgroundColor = mPrimaryColor = primaryColor;
        if (mRefreshKernel != null) {
            mRefreshKernel.requestDrawBackgroundFor(this, mPrimaryColor);
        }
        return self();
    }

    public T setAccentColor(@ColorInt int accentColor) {
        mAccentColor = accentColor;
        mTitleText.setTextColor(accentColor);
        if (mArrowDrawable != null) {
            mArrowDrawable.setColor(accentColor);
        }
        if (mProgressDrawable != null) {
            mProgressDrawable.setColor(accentColor);
        }
        return self();
    }

    public T setPrimaryColorId(@ColorRes int colorId) {
        final View thisView = this;
        setPrimaryColor(ZdRefreshManager.getInstance().getColor(thisView.getContext(), colorId));
        return self();
    }

    public T setAccentColorId(@ColorRes int colorId) {
        final View thisView = this;
        setAccentColor(ZdRefreshManager.getInstance().getColor(thisView.getContext(), colorId));
        return self();
    }

    public T setFinishDuration(int delay) {
        mFinishDuration = delay;
        return self();
    }

    public T setTextSizeTitle(float size) {
        mTitleText.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return self();
    }

    public T setDrawableMarginRight(float dp) {
        final View arrowView = mArrowView;
        final View progressView = mProgressView;
        MarginLayoutParams lpArrow = (MarginLayoutParams) arrowView.getLayoutParams();
        MarginLayoutParams lpProgress = (MarginLayoutParams) progressView.getLayoutParams();
        lpArrow.rightMargin = lpProgress.rightMargin = DimenUtil.dp2px(dp);
        arrowView.setLayoutParams(lpArrow);
        progressView.setLayoutParams(lpProgress);
        return self();
    }

    public T setDrawableSize(float dp) {
        final View arrowView = mArrowView;
        final View progressView = mProgressView;
        ViewGroup.LayoutParams lpArrow = arrowView.getLayoutParams();
        ViewGroup.LayoutParams lpProgress = progressView.getLayoutParams();
        lpArrow.width = lpProgress.width = DimenUtil.dp2px(dp);
        lpArrow.height = lpProgress.height = DimenUtil.dp2px(dp);
        arrowView.setLayoutParams(lpArrow);
        progressView.setLayoutParams(lpProgress);
        return self();
    }

    public T setDrawableArrowSize(float dp) {
        final View arrowView = mArrowView;
        ViewGroup.LayoutParams lpArrow = arrowView.getLayoutParams();
        lpArrow.height = lpArrow.width = DimenUtil.dp2px(dp);
        arrowView.setLayoutParams(lpArrow);
        return self();
    }

    public T setDrawableProgressSize(float dp) {
        final View progressView = mProgressView;
        ViewGroup.LayoutParams lpProgress = progressView.getLayoutParams();
        lpProgress.height = lpProgress.width = DimenUtil.dp2px(dp);
        progressView.setLayoutParams(lpProgress);
        return self();
    }
}
