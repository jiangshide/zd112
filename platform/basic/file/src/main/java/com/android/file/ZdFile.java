package com.android.file;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.img.Img;
import com.android.utils.AppUtil;
import com.android.utils.FileUtil;
import com.android.widget.ZdButton;
import com.android.widget.ZdToast;
import java.util.ArrayList;

/**
 * created by jiangshide on 2019-10-05.
 * email:18311271399@163.com
 */
public class ZdFile extends Thread implements Runnable {

  private static class ZdFileHolder {
    private static ZdFile instance = new ZdFile();
  }

  public static ZdFile getInstance() {
    return ZdFileHolder.instance;
  }

  public static final String IMG = "img";
  public static final String GIF = "gif";
  public static final String AUDIO = "audio";
  public static final String VIDEO = "video";
  public String fileType = IMG;

  private final int SUCCESS = 1;
  private final int PROGRESS = 2;
  private final int FAILE = -1;
  private int index;

  public ArrayList<String> list;

  private StringBuffer urlS;

  private OnUploadListener mOnUploadListener;

  @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
    @Override public void handleMessage(@NonNull Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case SUCCESS:
          ZdToast.cancelPop();
          if (mOnUploadListener != null) {
            mOnUploadListener.onSuccess(msg.obj.toString());
          }
          break;
        case PROGRESS:
          if (mOnUploadListener != null) {
            mOnUploadListener.onProgress(msg.arg1);
          }
          break;
        case FAILE:
          if (mOnUploadListener != null) {
            mOnUploadListener.onFaile(msg.obj.toString());
          }
          break;
      }
    }
  };

  public ZdFile upload(ArrayList<String> list) {
    return this.upload(IMG, list, null);
  }

  public ZdFile upload(String type, ArrayList<String> list, OnUploadListener listener) {
    this.fileType = type;
    this.list = list;
    this.mOnUploadListener = listener;
    urlS = null;
    urlS = new StringBuffer();
    return this;
  }

  @Override public void run() {
    super.run();
    request(0);
  }

  private void request(int i) {
    if (i >= list.size()) {
      Message message = new Message();
      message.what = 1;
      String urls = urlS.toString();
      if (!TextUtils.isEmpty(urls) && urls.contains(",")) {
        urls = urls.substring(0, urls.length() - 1);
      }
      message.obj = urls;
      handler.sendMessage(message);
      return;
    }
    index = i;
    String path = list.get(index);
    OSSCredentialProvider credentialProvider =
        new OSSPlainTextAKSKCredentialProvider(BuildConfig.ACCESSKEY_ID,
            BuildConfig.ACCESSKEY_SECRET);
    OSS oss =
        new OSSClient(AppUtil.getApplicationContext(), BuildConfig.ENDPOINT, credentialProvider);
    String name = FileUtil.getFileName(path);
    PutObjectRequest put =
        new PutObjectRequest(BuildConfig.BUCKET, fileType + "/" + name, path);

    put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
      @Override
      public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        int progress = (int) ((currentSize * 100 / totalSize));
        Message message = new Message();
        message.what = 2;
        message.arg1 = progress;
        handler.sendMessage(message);
      }
    });
    @SuppressWarnings("rawtypes")
    OSSAsyncTask
        task =
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
          @Override
          public void onSuccess(PutObjectRequest request, PutObjectResult result) {
            String url = BuildConfig.ENDPOINT + "/" + fileType + "/" + name;
            if (urlS != null) {
              urlS.append(url).append(",");
            }
            request(index + 1);
          }

          @Override
          public void onFailure(PutObjectRequest request, ClientException clientExcepion,
              ServiceException serviceException) {
            Message message = new Message();
            message.what = -1;
            if (clientExcepion != null) {
              message.obj = clientExcepion.getMessage();
            }
            if (serviceException != null) {
              message.obj = serviceException.getErrorCode()
                  + " | "
                  + serviceException.getRequestId()
                  + " | "
                  + serviceException.getHostId()
                  + " | "
                  + serviceException.getRawMessage();
            }
            handler.sendMessage(message);
          }
        });
  }

  public void showProgress(Activity activity, int progress) {
    if (activity == null || list == null) return;
    View view = LayoutInflater.from(activity).inflate(R.layout.default_upload, null);
    ImageView uploadImg = view.findViewById(R.id.uploadImg);
    ProgressBar uploadProgress = view.findViewById(R.id.uploadProgress);
    TextView uploadName = view.findViewById(R.id.uploadName);
    TextView uploadDes = view.findViewById(R.id.uploadDes);
    ZdButton uploadSumbit = view.findViewById(R.id.uploadSumbit);
    String path = list.get(index);
    Img.loadImageFromDisk(path, uploadImg);
    uploadName.setText(FileUtil.getFileName(
        path));
    uploadDes.setText(path);
    uploadProgress.setProgress(progress);
    uploadSumbit.setText("上传中...");
    uploadSumbit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
    ZdToast.fixPop(activity, view, -1);
  }

  public interface OnUploadListener {
    void onProgress(int progress);

    void onSuccess(String urls);

    void onFaile(String msg);
  }
}
