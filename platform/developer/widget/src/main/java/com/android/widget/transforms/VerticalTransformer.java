package com.android.widget.transforms;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/29.
 * @Emal:18311271399@163.com
 */
public class VerticalTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View view, float v) {
        float alpha = 0;
        if (0 <= v && v <= 1) {
            alpha = 1 - v;
        } else if (-1 < v && v < 0) {
            alpha = v + 1;
        }
        view.setAlpha(alpha);
        view.setTranslationX(view.getWidth() * -v);
        float yPosition = v * view.getHeight();
        view.setTranslationY(yPosition);
    }
}
