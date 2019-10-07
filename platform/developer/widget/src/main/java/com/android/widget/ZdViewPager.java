package com.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.android.widget.adapter.ZdFragmentPagerAdapter;
import com.android.widget.adapter.ZdPagerAdapter;
import com.android.widget.transforms.AccordionTransformer;
import com.android.widget.transforms.BackgroundToForegroundTransformer;
import com.android.widget.transforms.CubeInTransformer;
import com.android.widget.transforms.CubeOutTransformer;
import com.android.widget.transforms.DepthPageTransformer;
import com.android.widget.transforms.DrawerTransformer;
import com.android.widget.transforms.FlipHorizontalTransformer;
import com.android.widget.transforms.FlipVerticalTransformer;
import com.android.widget.transforms.ForegroundToBackgroundTransformer;
import com.android.widget.transforms.RotateDownTransformer;
import com.android.widget.transforms.RotateUpTransformer;
import com.android.widget.transforms.ScaleInOutTransformer;
import com.android.widget.transforms.StackTransformer;
import com.android.widget.transforms.TabletTransformer;
import com.android.widget.transforms.Transformer;
import com.android.widget.transforms.VerticalTransformer;
import com.android.widget.transforms.ZoomInTransformer;
import com.android.widget.transforms.ZoomOutSlideTransformer;
import com.android.widget.transforms.ZoomOutTransformer;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public class ZdViewPager extends ViewPager implements ViewPager.PageTransformer {

    public ZdPagerAdapter mCusPagerAdapter;
    private ZdFragmentPagerAdapter mZdFragmentPagerAdapter;
    private boolean mIsCanScroll = true;
    private boolean mIsVertical = false;
    private boolean mIsAnim = false;
    @SuppressLint("WrongConstant")
    private @Transformer
    int mMode = -1;
    private Class[] mTransformClass = {AccordionTransformer.class, BackgroundToForegroundTransformer.class, CubeInTransformer.class, CubeOutTransformer.class, DepthPageTransformer.class,
            DrawerTransformer.class, FlipHorizontalTransformer.class, FlipVerticalTransformer.class, ForegroundToBackgroundTransformer.class, RotateDownTransformer.class, RotateUpTransformer.class,
            ScaleInOutTransformer.class, StackTransformer.class, TabletTransformer.class, VerticalTransformer.class, ZoomInTransformer.class, ZoomOutSlideTransformer.class, ZoomOutTransformer.class};

    public ZdViewPager(@NonNull Context context) {
        super(context);
    }

    public ZdViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.mIsCanScroll = isCanScroll;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, mIsAnim);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @SuppressLint("WrongConstant")
    public void setMode(boolean isVertical) {
        this.setMode(isVertical, -1);
    }

    public void setMode(boolean isVertical, @Transformer int mode) {
        this.setMode(isVertical, true, mode);
    }

    public void setMode(boolean isVertical, boolean reverseDrawingOrder, @Transformer int mode) {
        this.mIsVertical = isVertical;
        this.mMode = mode;
        try {
            setPageTransformer(reverseDrawingOrder, mode < 0 ? this : (PageTransformer) mTransformClass[mMode].newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        this.invalidate();
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        event.setLocation((event.getY() / height) * width, (event.getX() / width) * height);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mIsCanScroll && super.onInterceptTouchEvent(mIsVertical ? swapTouchEvent(MotionEvent.obtain(event)) : event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mIsCanScroll && super.onTouchEvent(mIsVertical ? swapTouchEvent(MotionEvent.obtain(event)) : event);
    }

    public void setResImg(int... resImgs) {
        List<View> views = new ArrayList<>();
        for (int resImg : resImgs) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(resImg);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            views.add(imageView);
        }
        setViews(views);
    }

    public ZdViewPager setAnim(boolean isAnim) {
        this.mIsAnim = isAnim;
        return this;
    }

    public ZdViewPager setViews(List<View> views) {
        this.setAdapter(mCusPagerAdapter = new ZdPagerAdapter(getContext()).setViews(views));
        return this;
    }

    public ZdViewPager setViews(List<View> views, Boolean isLimitWidth) {
        this.setAdapter(mCusPagerAdapter = new ZdPagerAdapter(getContext()).setViews(views, isLimitWidth));
        return this;
    }

    public void setListener(ZdPagerAdapter.OnPagerItemClickListener listener) {
        if (mCusPagerAdapter != null) {
            mCusPagerAdapter.setListener(listener);
        }
    }

    public ZdFragmentPagerAdapter create(FragmentManager fragmentManager) {
        return mZdFragmentPagerAdapter = new ZdFragmentPagerAdapter(fragmentManager);
    }

    @Override
    public void transformPage(@NonNull View view, float v) {
        float alpha = 0;
        if (0 <= v && v <= 1) {
            alpha = 1 - v;
        } else if (-1 < v && v < 0) {
            alpha = v + 1;
        }
        //view.setAlpha(alpha);
        view.setTranslationX(view.getWidth() * -v);
        float yPosition = v * view.getHeight();
        view.setTranslationY(yPosition);
    }

    public void onRefresh() {
        if (null != mZdFragmentPagerAdapter) {
            mZdFragmentPagerAdapter.onRefresh();
        }
    }
}
