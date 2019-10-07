package com.android.http.okhttp;

import android.os.Handler;

import com.android.utils.LogUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
public class JsonCallbackListener<T> implements CallbackListener{

    private Class<T> responseClass;
    private Handler mHandler = new Handler();
    private JsonDataTransformListener listener;

    public JsonCallbackListener(Class<T> responseClass,JsonDataTransformListener listener){
        this.responseClass = responseClass;
        this.listener = listener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String response = getContent(inputStream);
        LogUtil.e("response:",response);
        final T clazz = new Gson().fromJson(response,responseClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(listener != null){
                    listener.onSuccess(clazz);
                }
            }
        });
    }

    private String getContent(InputStream inputStream){
        String content= null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            try {
                while ((line=bufferedReader.readLine()) != null){
                    stringBuilder.append(line+"\n");
                }
            }catch (IOException e){
                LogUtil.e(e);
            }finally {
                try {
                    inputStream.close();
                }catch (IOException e){
                    LogUtil.e(e);
                }
            }
            return stringBuilder.toString();
        }catch (Exception e){
            LogUtil.e(e);
        }
        return content;
    }

    @Override
    public void onFailure() {

    }
}