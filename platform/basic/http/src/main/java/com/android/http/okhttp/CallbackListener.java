package com.android.http.okhttp;

import java.io.InputStream;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailure();
}
