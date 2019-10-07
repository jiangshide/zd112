package com.android.http.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.event.ZdEvent;
import com.android.utils.SPUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.android.utils.Constant.UPDATE_APK;

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */
public class Download {

    private final OkHttpClient okHttpClient;
    private OnDownloadListener listener;
    public boolean isDownling = false;

    public Download() {
        okHttpClient = new OkHttpClient();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (listener != null) {
                switch (msg.what) {
                    case -1:
                        listener.onDownloadFailed((Exception) msg.obj);
                        break;
                    case 1:
                        listener.onDownloading(msg.arg1);
                        break;
                    case 2:
                        listener.onDownloadSuccess((File) msg.obj);
                        break;
                }
            }
        }
    };

    /**
     * todo:temp
     *
     * @param url
     * @param destFileDir
     * @param destFileName
     */
    public void download(String url, String destFileDir, String destFileName) {
        download(url, destFileDir, destFileName, null);
    }

    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */
    public void download(final String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) {
        this.listener = listener;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        isDownling = false;
                        if (listener != null) {
                            Message message = new Message();
                            message.what = -1;
                            message.obj = e;
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(dir, destFileName);
                        try {
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                isDownling = true;
                                fos.write(buf, 0, len);
                                if (listener != null) {
                                    sum += len;
                                    int progress = (int) (sum * 1.0f / total * 100);
                                    Message message = new Message();
                                    message.what = 1;
                                    message.arg1 = progress;
                                    handler.sendMessage(message);
                                }
                            }
                            fos.flush();
                            isDownling = false;
                            if (listener != null) {
                                Message message = new Message();
                                message.what = 2;
                                message.obj = file;
                                handler.sendMessage(message);
                            } else {
                                SPUtil.putString(UPDATE_APK, file.getAbsolutePath());
                                ZdEvent.Companion.get().with(UPDATE_APK).post(UPDATE_APK);
                            }
                        } catch (Exception e) {
                            if (listener != null) {
                                Message message = new Message();
                                message.what = -1;
                                message.obj = e;
                                handler.sendMessage(message);
                            }
                        } finally {
                            isDownling = false;
                            try {
                                if (is != null)
                                    is.close();
                            } catch (IOException e) {
                            }
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                });
            }
        });
    }

    public interface OnDownloadListener {
        /**
         * @param file 下载成功后的文件
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * @param e 下载异常信息
         */
        void onDownloadFailed(Exception e);
    }
}
