package com.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * created by jiangshide on 2019-09-02.
 * email:18311271399@163.com
 */
public class ZdListView extends ListView {
    public ZdListView(Context context) {
        super(context);
    }

    public ZdListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZdListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
