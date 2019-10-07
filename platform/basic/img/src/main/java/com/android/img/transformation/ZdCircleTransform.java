package com.android.img.transformation;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * created by jiangshide on 2016-03-13.
 * email:18311271399@163.com
 */
public class ZdCircleTransform extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.ohdance.framework.image.ZdCircleTransform." + VERSION;

    @Override
    public String toString() {
        return "ZdCircleTransform()";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ZdCircleTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID).getBytes(CHARSET));
    }
}
