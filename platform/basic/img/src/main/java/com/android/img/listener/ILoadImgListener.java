package com.android.img.listener;

import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

/**
 * created by jiangshide on 2015-09-13.
 * email:18311271399@163.com
 */
public interface ILoadImgListener {
    boolean onLoadFailed(
            @Nullable Exception e, Object model, boolean isFirstResource);

    boolean onResourceReady(
            Drawable resource, Object model, boolean isFirstResource);
}
