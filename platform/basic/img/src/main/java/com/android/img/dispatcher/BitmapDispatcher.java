package com.android.img.dispatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.android.img.request.BitmapRequest;
import com.android.utils.LogUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * created by jiangshide on 2015-09-12.
 * email:18311271399@163.com
 */
public class BitmapDispatcher extends Thread {

    private Handler handler = new Handler(Looper.getMainLooper());

    private LinkedBlockingQueue<BitmapRequest> bitmapRequests;

    public BitmapDispatcher(LinkedBlockingQueue<BitmapRequest> requests) {
        this.bitmapRequests = requests;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            try {
                BitmapRequest bitmapRequest = bitmapRequests.take();
                showLoadingImage(bitmapRequest);
                Bitmap bitmap = findBitmap(bitmapRequest);
                showImageView(bitmapRequest, bitmap);
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }

    private void showImageView(final BitmapRequest bitmapRequest, final Bitmap bitmap) {
        if (bitmap != null && bitmapRequest != null && bitmapRequest.getImageView() != null && bitmapRequest.getUrlMd5().equals(bitmapRequest.getImageView().getTag())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    bitmapRequest.getImageView().setImageBitmap(bitmap);
                    if (bitmapRequest.getRequestListener() != null) {
                        bitmapRequest.getRequestListener().onSuccess(bitmap);
                    }
                }
            });
        } else {
            bitmapRequest.getRequestListener().onFailure();
        }
    }

    private Bitmap findBitmap(BitmapRequest bitmapRequest) {
        return downloadImage(bitmapRequest.getUrl());
    }

    private Bitmap downloadImage(String urlStr) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            LogUtil.e(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
        return bitmap;
    }

    private void showLoadingImage(final BitmapRequest bitmapRequest) {
        if (bitmapRequest != null && bitmapRequest.getResId() > 0 && bitmapRequest.getImageView() != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    bitmapRequest.getImageView().setImageResource(bitmapRequest.getResId());
                }
            });
        }
    }
}
