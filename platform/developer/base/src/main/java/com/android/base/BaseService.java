package com.android.base;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.android.utils.LogUtil;
import com.android.widget.ZdNotification;
import com.android.widget.ZdToast;
import com.android.widget.manager.CountDown;

/**
 * created by jiangshide on 2019-10-01.
 * email:18311271399@163.com
 */
public class BaseService extends Service implements CountDown.OnCountDownTickListener {

  private NotificationManager notificationManager;
  private String notificationId = "serviceId";
  private String notificationName = "serviceName";

  @Override public void onCreate() {
    super.onCreate();
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel =
          new NotificationChannel(notificationId, notificationName,
              NotificationManager.IMPORTANCE_HIGH);
      notificationManager.createNotificationChannel(notificationChannel);
    }
    startForeground(ZdNotification.notificationId, getNotification());
  }

  private Notification getNotification() {
    Notification.Builder builder = new Notification.Builder(this)
        .setSmallIcon(R.drawable.default_notification)
        .setContentTitle("title")
        .setContentText("content");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      builder.setChannelId(notificationId);
    }
    Notification notification = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
      notification = builder.build();
    }
    return notification;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    test();
    return START_STICKY;
  }

  private void test() {
    CountDown.getInstance().create(10).setListener(this);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onFinish() {
    CountDown.getInstance().create(10).setListener(this);
  }

  @Override public void onTick(long millisUntilFinished) {
  }

  @Override public void onDestroy() {
    super.onDestroy();
    LogUtil.e("---destory");
  }
}
