package com.android.jsbridge.listener;

/**
 * created by jiangshide on 2019-08-13.
 * email:18311271399@163.com
 */
public interface WebViewJavascriptBridge {
    void send(String data);

    void send(String data, CallBackFunction responseCallback);
}
