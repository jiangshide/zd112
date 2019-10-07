package com.android.http.okhttp;

import com.android.utils.LogUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest iHttpRequest;

    private long delayTime;
    private int retryCount;
    private boolean isRetry;

    public boolean isRetry() {
        return isRetry;
    }

    public void setRetry(boolean retry) {
        isRetry = retry;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public HttpTask(String url, T requestData, IHttpRequest iHttpRequest, CallbackListener callbackListener) {
        this.iHttpRequest = iHttpRequest;
        LogUtil.e("url:", url);
        iHttpRequest.setUrl(url);
        iHttpRequest.setListener(callbackListener);
        String content = new Gson().toJson(requestData);
        try {
            iHttpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.iHttpRequest.execute();
        } catch (Exception e) {
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}