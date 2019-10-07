package com.android.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.img.listener.IProgressListener;
import com.android.img.progress.GlideApp;
import com.android.img.progress.GlideRequest;
import com.android.img.progress.ProgressManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;

/**
 * created by jiangshide on 2017-08-25.
 * email:18311271399@163.com
 */
public class ZdImageLoader {
    protected static final String ANDROID_RESOURCE = "android.resource://";
    protected static final String FILE = "file://";
    protected static final String SEPARATOR = "/";

    private String url;
    private WeakReference<ImageView> imageViewWeakReference;
    private GlideRequest<Drawable> glideRequest;

    public static ZdImageLoader create(ImageView imageView) {
        return new ZdImageLoader(imageView);
    }

    private ZdImageLoader(ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
        glideRequest = GlideApp.with(getContext()).asDrawable();
    }

    public ImageView getImageView() {
        if (imageViewWeakReference != null) {
            return imageViewWeakReference.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public GlideRequest getGlideRequest() {
        if (glideRequest == null) {
            glideRequest = GlideApp.with(getContext()).asDrawable();
        }
        return glideRequest;
    }

    protected Uri resId2Uri(@DrawableRes int resId) {
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resId);
    }

    public ZdImageLoader load(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
//        return loadImage(resId2Uri(resId), placeholder, transformation);
        return null;
    }

    protected GlideRequest<Drawable> loadImage(Object obj) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        return glideRequest.load(obj);
    }


    public ZdImageLoader loadImage(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        glideRequest = loadImage(obj);
        if (placeholder != 0) {
            glideRequest = glideRequest.placeholder(placeholder);
        }

        if (transformation != null) {
            glideRequest = glideRequest.transform(transformation);
        }

        glideRequest.into(new GlideImageViewTarget(getImageView()));
        return this;
    }

    public ZdImageLoader listener(Object obj, IProgressListener iProgressListener) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        ProgressManager.addListener(url, iProgressListener);
        return this;
    }

    private class GlideImageViewTarget extends DrawableImageViewTarget {
        GlideImageViewTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            IProgressListener iProgressListener = ProgressManager.getProgressListener(getUrl());
            if (iProgressListener != null) {
                iProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(getUrl());
            }
            super.onLoadFailed(errorDrawable);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            IProgressListener iProgressListener = ProgressManager.getProgressListener(getUrl());
            if (iProgressListener != null) {
                iProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(getUrl());
            }
            super.onResourceReady(resource, transition);
        }
    }
}
