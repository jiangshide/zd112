package com.android.img.listener;

import android.graphics.Bitmap;

/**
 * created by jiangshide on 2015-09-12.
 * email:18311271399@163.com
 */
public interface IBitmapListener {
    boolean onSuccess(Bitmap bitmap);

    boolean onFailure();
}
