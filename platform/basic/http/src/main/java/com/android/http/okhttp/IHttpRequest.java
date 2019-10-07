package com.android.http.okhttp;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public interface IHttpRequest {
    void setUrl(String url);
    void setData(byte[] data);

    void setListener(CallbackListener listener);

    void execute();
}
