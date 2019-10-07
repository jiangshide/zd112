package com.android.http.okhttp;

import com.android.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * created by jiangshide on 2019-06-27.
 * email:18311271399@163.com
 */
public class JsonHttpRequest implements IHttpRequest{

    private String url;
    private byte[] data;
    private CallbackListener callbackListener;
    private HttpURLConnection urlConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener listener) {
        this.callbackListener = listener;
    }

    @Override
    public void execute() {
        URL url = null;
        try {
            url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json:charset=UTF-8");
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data);
            bufferedOutputStream.flush();
            outputStream.close();
            bufferedOutputStream.close();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = urlConnection.getInputStream();
                callbackListener.onSuccess(inputStream);
            }else{
                throw new RuntimeException("请求失败!");
            }
        }catch (Exception e){
            LogUtil.e(e);
            throw new RuntimeException("请求失败!");
        }finally {
            urlConnection.disconnect();
        }
    }
}
