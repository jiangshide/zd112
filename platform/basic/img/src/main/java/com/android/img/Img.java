package com.android.img;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.img.dispatcher.BitmapDispatcher;
import com.android.img.listener.IBitmapListener;
import com.android.img.listener.ILoadImgListener;
import com.android.img.request.BitmapRequest;
import com.android.img.transformation.BlurTransformation;
import com.android.img.transformation.ZdCircleTransform;
import com.android.img.transformation.ZdRoundTransform;
import com.android.utils.AppUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * created by jiangshide on 2015-09-12.
 * email:18311271399@163.com
 */
public final class Img {

    private static volatile Img instance;

    private LinkedBlockingQueue<BitmapRequest> requestQueue;

    private BitmapDispatcher[] bitmapDispatchers;

    private static final int DEFAULT_ROUND = 60;

    private static final String[] colors = {"#77B2CF", "#7EA3B0", "#A99DCA", "#7F9DC1", "#98CAAE", "#E2D0EB"};

    private final String CACHE_FILE = "image_manager_disk_cache";

    private Img() {
        this.requestQueue = new LinkedBlockingQueue<>();
        init();
    }

    public static Img getInstance() {
        if (instance == null) {
            synchronized (Img.class) {
                if (instance == null) {
                    instance = new Img();
                }
            }
        }
        return instance;
    }

    public static BitmapRequest with(Context context) {
        return new BitmapRequest(context);
    }

    private void init() {
        stop();
        initDispatch();
    }

    private void initDispatch() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        for (int i = 0; i < threadCount; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue);
            bitmapDispatcher.start();
            bitmapDispatchers[i] = bitmapDispatcher;
        }
    }

    public void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.isInterrupted();
                }
            }
        }
    }

    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest == null) return;
        if (!requestQueue.contains(bitmapRequest)) {
            requestQueue.add(bitmapRequest);
        }
    }

    /**
     * @return
     */
    private static String getRandomColor() {
        return colors[(int) (Math.random() * colors.length)];
    }

    /**
     * 基于Activity为载体加载图片，必须保证Activity没有
     *
     * @param activity
     * @param imgUrl
     * @param imgView
     */
    public static void loadImage(Activity activity, String imgUrl, ImageView imgView) {
        if (!contextAble(activity)) return;

        Glide.with(activity).load(imgUrl).into(imgView);
    }

    /**
     * 加载本地res资源文件
     *
     * @param resId   本地资源文件
     * @param imgView 图片view
     */
    public static void loadImage(int resId, ImageView imgView) {
        if (!contextAble(imgView.getContext())) return;
        Glide.with(imgView.getContext()).load(resId).into(imgView);
    }

    /**
     * @param imgUrl
     * @param imgView
     */
    public static void loadImage(String imgUrl, ImageView imgView) {
        if (!contextAble(imgView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(new ColorDrawable(Color.parseColor(getRandomColor())));
        Glide.with(imgView.getContext()).load(imgUrl)
                .apply(requestOptions).dontAnimate().skipMemoryCache(false)
                .into(imgView);
    }

    /**
     * @param imgUrl
     * @param imgView
     * @param fragment
     */
    public static void loadImage(String imgUrl, ImageView imgView, Fragment fragment) {
        if (!contextAble(imgView.getContext())) return;

        Glide.with(fragment).load(imgUrl).dontAnimate().skipMemoryCache(false).into(imgView);
    }

    /***
     *
     * @param imgUrl
     * @param imgView
     * @param imgDefaultId
     */
    public static void loadImage(String imgUrl, ImageView imgView, int imgDefaultId) {
        if (!contextAble(imgView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(imgDefaultId);
        Glide.with(imgView.getContext()).load(imgUrl).apply(requestOptions).dontAnimate().skipMemoryCache(false).into(imgView);
    }

    /**
     * @param imgUrl
     * @param imgView
     * @param imgDefaultId
     */
    public static void loadImageCircle(String imgUrl, ImageView imgView, int imgDefaultId) {
        if (!contextAble(imgView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(imgDefaultId);
        requestOptions.transform(new ZdCircleTransform());
        Glide.with(imgView.getContext()).load(imgUrl).apply(requestOptions).dontAnimate().skipMemoryCache(false).into(imgView);
    }

    /**
     * @param url
     * @param width
     * @return
     */
    public static String thumbAliOssUrl(String url, int width) {
        if (url != null && url.startsWith("http")) {
            return String.format("%s?x-oss-process=image/resize,w_%d", url, width);
        }
        return url;
    }

    /**
     * @param imgUrl
     * @param imgView
     */
    public static void loadImageCircle(String imgUrl, ImageView imgView) {
        if (!contextAble(imgView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.transform(new ColorDrawable(Color.parseColor(getRandomColor()));
        requestOptions.transform(new ZdCircleTransform());
        Glide.with(imgView.getContext()).load(imgUrl)
                .apply(requestOptions).dontAnimate().skipMemoryCache(false).into(imgView);
    }

    /**
     * @param imgUrl
     * @param imageView
     */
    public static void loadImageRound(String imgUrl, ImageView imageView) {
        loadImageRound(imgUrl, imageView, DEFAULT_ROUND);
    }

    /**
     * @param imgUrl
     * @param imgView
     * @param radiusDp
     */
    public static void loadImageRound(String imgUrl, ImageView imgView, int radiusDp) {
        if (!contextAble(imgView.getContext())) return;
        loadImageRound(imgUrl, imgView, radiusDp, ZdRoundTransform.CornerType.ALL);
    }

    /**
     * @param imgUrl
     * @param imgView
     * @param cornerTypep
     */
    public static void loadImageRound(String imgUrl, ImageView imgView, ZdRoundTransform.CornerType cornerTypep) {
        if (!contextAble(imgView.getContext())) return;
        loadImageRound(imgUrl, imgView, DEFAULT_ROUND, cornerTypep);
    }

    /**
     * @param imgUrl
     * @param imgView
     * @param radiusDp
     * @param cornerType
     */
    public static void loadImageRound(String imgUrl, ImageView imgView, int radiusDp, ZdRoundTransform.CornerType cornerType) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(new ColorDrawable(Color.parseColor(getRandomColor())));
        requestOptions.transform(new ZdRoundTransform(radiusDp, cornerType));
        Glide.with(imgView.getContext()).load(imgUrl)
                .apply(requestOptions).dontAnimate().skipMemoryCache(false).into(imgView);
    }

    /**
     * @param imgUrl
     * @param imgView
     * @param defDrawable
     */
    public static void loadImage(String imgUrl, ImageView imgView, Drawable defDrawable) {
        if (!contextAble(imgView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(defDrawable);
        Glide.with(imgView.getContext()).load(imgUrl).apply(requestOptions).dontAnimate().skipMemoryCache(false).into(imgView);
    }

    /**
     * @param imgUrl
     * @param imageView
     */
    public static void loadImagBlur(String imgUrl, ImageView imageView) {
        if (!contextAble(imageView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new BlurTransformation(imageView.getContext(), 0));
        Glide.with(imageView.getContext()).load(imgUrl)
                .apply(requestOptions).dontAnimate().skipMemoryCache(false)
                .into(imageView);
    }

    /**
     * 加载文件图片，不使用缓存
     *
     * @param picPath 图片在磁盘中的路径
     * @param imgView 图片view
     */
    public static void loadImageFromDisk(String picPath, ImageView imgView) {
        if (!contextAble(imgView.getContext())) return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        Glide.with(imgView.getContext())
                .load(picPath)
                .apply(requestOptions).dontAnimate().skipMemoryCache(false)
                .into(imgView);
    }

    /**
     * @param context
     * @param url
     * @param listener
     */
    public static void loadImage(Context context, String url, final IBitmapListener listener) {
        Glide.with(context).asBitmap().load(url).dontAnimate().skipMemoryCache(false).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (listener != null) {
                    if (resource != null) {
                        listener.onSuccess(resource);
                    } else {
                        listener.onFailure();
                    }
                }
            }
        });
    }

    /**
     * @param context
     * @param imgUrl
     */
    public static void preloadImage(Context context, String imgUrl) {
        if (!contextAble(context)) return;
        Glide.with(context).load(imgUrl).preload();
    }

    /**
     * 同上，主要是为了回调后，将bitmap转换成drawable
     *
     * @param imgResId
     * @param listener
     */
    public static void preLoadImage(Context context, int imgResId, RequestListener listener) {
        if (!contextAble(context)) return;

        Glide.with(context).load(imgResId).listener(listener);
    }

    /**
     * 支持回调，同时支持图片是否缓存到磁盘
     */
    public static void preLoadImage(Context context, String imgUrl, RequestListener listener) {
        if (!contextAble(context)) return;

        Glide.with(context).load(imgUrl).listener(listener);
    }

    /**
     * 需要运行在子线程
     *
     * @param context
     * @param imgUrl
     * @return
     * @throws ExecutionException
     */
    public static File getBitmapFromCache(Context context, String imgUrl) throws ExecutionException, InterruptedException {
        return Glide.with(context).load(imgUrl)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

    /**
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean contextAble(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !activity.isFinishing() && !activity.isDestroyed();
        }

        return true;
    }

    /**
     * @param activity
     * @param smallImgUrl
     * @param bigImgUrl
     * @param view
     * @param loadListener
     */
    public static void loadImage(Activity activity, String smallImgUrl, String bigImgUrl, ImageView view, final ILoadImgListener loadListener) {
        RequestBuilder<Drawable> thumbnailRequest = Glide.with(activity).load(smallImgUrl);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(activity).load(bigImgUrl)
                .thumbnail(thumbnailRequest)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return loadListener.onLoadFailed(e, model, isFirstResource);
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return loadListener.onResourceReady(resource, model, isFirstResource);
                    }
                }).into(view);
    }

    /**
     * 获取Glide磁盘缓存大小
     *
     * @return
     */
    public String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(new File(AppUtil.getApplicationContext().getCacheDir() + "/" + CACHE_FILE)));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
     *
     * @return
     */
    public boolean cleanCatchDisk() {
        return deleteFolderFile(AppUtil.getApplicationContext().getCacheDir() + "/" + CACHE_FILE, true);
    }

    /**
     * 清除图片磁盘缓存，调用Glide自带方法
     *
     * @return
     */
    public boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(AppUtil.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(AppUtil.getApplicationContext()).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 清除Glide内存缓存
     *
     * @return
     */
    public boolean clearCacheMemory() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(AppUtil.getApplicationContext()).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file
     * @return
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 按目录删除文件夹文件方法
     *
     * @param filePath
     * @param deleteThisPath
     * @return
     */
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
