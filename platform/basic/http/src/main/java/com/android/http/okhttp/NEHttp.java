package com.android.http.okhttp;

/**
 * created by jiangshide on 2019-06-28.
 * email:18311271399@163.com
 */
public class NEHttp {

    public static <T, M> void sendJsonRequest(String url, T requestData, Class<M> response, JsonDataTransformListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(response, listener);
        HttpTask httpTask = new HttpTask(url, requestData, httpRequest, callbackListener);
        httpTask.setRetry(false);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
