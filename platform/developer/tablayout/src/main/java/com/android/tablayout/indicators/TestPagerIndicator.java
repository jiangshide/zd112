package com.android.tablayout.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.android.tablayout.TabPosition;
import com.android.tablayout.help.FragmentContainerHelper;
import com.android.tablayout.interfaces.IPagerIndicator;

import java.util.List;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public class TestPagerIndicator extends View implements IPagerIndicator {
    private Paint mPaint;
    private int mOutRectColor;
    private int mInnerRectColor;
    private RectF mOutRect = new RectF();
    private RectF mInnerRect = new RectF();

    private List<TabPosition> mPositionDataList;

    public TestPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mOutRectColor = Color.RED;
        mInnerRectColor = Color.GREEN;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mOutRectColor);
        canvas.drawRect(mOutRect, mPaint);
        mPaint.setColor(mInnerRectColor);
        canvas.drawRect(mInnerRect, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        TabPosition current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        TabPosition next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        mOutRect.left = current.mLeft + (next.mLeft - current.mLeft) * positionOffset;
        mOutRect.top = current.mTop + (next.mTop - current.mTop) * positionOffset;
        mOutRect.right = current.mRight + (next.mRight - current.mRight) * positionOffset;
        mOutRect.bottom = current.mBottom + (next.mBottom - current.mBottom) * positionOffset;

        mInnerRect.left = current.mContentLeft + (next.mContentLeft - current.mContentLeft) * positionOffset;
        mInnerRect.top = current.mContentTop + (next.mContentTop - current.mContentTop) * positionOffset;
        mInnerRect.right = current.mContentRight + (next.mContentRight - current.mContentRight) * positionOffset;
        mInnerRect.bottom = current.mContentBottom + (next.mContentBottom - current.mContentBottom) * positionOffset;

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<TabPosition> dataList) {
        mPositionDataList = dataList;
    }

    public int getOutRectColor() {
        return mOutRectColor;
    }

    public void setOutRectColor(int outRectColor) {
        mOutRectColor = outRectColor;
    }

    public int getInnerRectColor() {
        return mInnerRectColor;
    }

    public void setInnerRectColor(int innerRectColor) {
        mInnerRectColor = innerRectColor;
    }
}
