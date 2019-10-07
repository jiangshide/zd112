package com.android.widget;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.android.utils.AppUtil;
import com.android.utils.LogUtil;

/**
 * created by jiangshide on 2019-09-05.
 * email:18311271399@163.com
 */
public class ZdNotification {

  private static class ZdNotificationHolder {
    private static ZdNotification instance = new ZdNotification();
  }

  public static ZdNotification getInstance() {
    return ZdNotificationHolder.instance;
  }

  public static int notificationId = 1;//默认通知Id
  /**
   * 默认通道id
   */
  private String channelId = "1";

  public static final String NOTIFICATION = "notification";

  /**
   * 默认通道名称
   */
  public String channelName = "default";
  private Context context;
  public static int REQUEST = 1;
  public int iconId = AppUtil.getIcon();
  public boolean isClear;

  public int lightColor = 0xffff0000;//blue
  public int lightTime = 500;
  public int unLightTime = 100;

  public int vibrate = 1;//the vibrate with time second

  public static final String ACTION = "com.android.notification";
  public static final String NOTIFICATION_MSG = "notificationMsg";

  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder;

  private MediaPlayer mediaPlayer;

  public ZdNotification build() {
    notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel
          channel =
          new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
      channel.enableLights(true); //是否在桌面icon右上角展示小红点
      channel.setLightColor(Color.RED); //小红点颜色
      channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
      notificationManager.createNotificationChannel(channel);
      builder.setChannelId(channelId);
    }
    Notification notification = builder.build();
    notification.flags = isClear ? Notification.FLAG_AUTO_CANCEL
        : Notification.FLAG_NO_CLEAR | Notification.FLAG_SHOW_LIGHTS;
    builder.setPublicVersion(notification);//锁屏时显示通知
    notificationManager.notify(notificationId, notification);
    notificationId++;
    return this;
  }

  public Notification getNotification() {
    return create().build().builder.build();
  }

  public ZdNotification create() {
    return create("1");
  }

  public ZdNotification create(String channelID) {
    this.context = AppUtil.getApplicationContext();
    builder = new NotificationCompat.Builder(context, channelID);
    return this;
  }

  public ZdNotification setView(int layout) {
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
    builder.setCustomBigContentView(remoteViews)//设置通知的布局
        .setCustomHeadsUpContentView(remoteViews)//设置悬挂通知的布局
        .setCustomContentView(remoteViews);
    return this;
  }

  public ZdNotification setClass(Class clazz) {
    return setClass(null, clazz);
  }

  public ZdNotification setClass(Bundle bundle, Class clazz) {
    return setClass(ACTION, bundle, clazz);
  }

  public ZdNotification setClass(String action, Bundle bundle, Class clazz) {
    Intent intent = new Intent(context, clazz);
    intent.setAction(action);
    intent.putExtra(NOTIFICATION, notificationId);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    PendingIntent pendingIntent =
        PendingIntent.getBroadcast(context, REQUEST, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    builder.setAutoCancel(true);
    builder.setContentIntent(pendingIntent);
    //builder.setDeleteIntent(pendingIntent);
    //builder.setOnlyAlertOnce(true);
    //builder.setUsesChronometer(true);
    builder.setOngoing(false);
    builder.setLocalOnly(true);
    builder.setSmallIcon(iconId);
    //builder.setDefaults(NotificationCompat.DEFAULT_ALL);
    return this;
  }

  public ZdNotification setIcon(int icon) {
    this.iconId = icon;
    return this;
  }

  public ZdNotification setTitle(String title) {
    builder.setContentTitle(title);
    return this;
  }

  public ZdNotification setContent(String content) {
    builder.setContentText(content);
    return this;
  }

  public ZdNotification setVibrate(boolean isVibrate) {
    if (isVibrate) {
      //builder.setVibrate(new long[] { 0, 1000, 1000, 1000 });
      vibrate(vibrate);
    } else {
      //builder.setVibrate(new long[] { 0 });
      virateCancle();
    }
    return this;
  }

  /**
   * 让手机振动milliseconds毫秒
   */
  public ZdNotification vibrate(int time) {
    Vibrator vib =
        (Vibrator) AppUtil.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    if (vib != null && vib.hasVibrator()) {
      vib.vibrate(1000 * time);
    }
    return this;
  }

  /**
   * 设定的pattern[]模式振动
   *
   * @param pattern long pattern[] = {1000, 20000, 10000, 10000, 30000};
   * @param repeat -1以禁用重复,否则重复或者指定位置重复
   */
  public ZdNotification vibrate(long[] pattern, int repeat) {
    Vibrator vib =
        (Vibrator) AppUtil.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    if (vib != null && vib.hasVibrator()) {
      vib.vibrate(pattern, repeat);
    }
    return this;
  }

  /**
   * 取消震动
   */
  public ZdNotification virateCancle() {
    Vibrator vib =
        (Vibrator) AppUtil.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    if (vib != null) {
      vib.cancel();
    }
    return this;
  }

  /**
   * 播放系统铃声
   */
  public ZdNotification ring() {
    Context context = AppUtil.getApplicationContext();
    try {
      cancelRing();
      mediaPlayer = MediaPlayer.create(context, R.raw.hott);
      mediaPlayer.setVolume(1f, 1f);
      mediaPlayer.start();
    } catch (Exception e) {
      LogUtil.e(e);
    }
    return this;
  }

  /**
   * 取消系统铃声
   */
  public ZdNotification cancelRing() {
    if (mediaPlayer != null) {
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
        mediaPlayer.release();
      }
    }
    mediaPlayer = null;
    return this;
  }

  public ZdNotification setIsRing(boolean isRing) {
    //Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "raw/hott.wav");
    //if (uri != null && isRing) {
    //  builder.setSound(uri);
    //}
    //if (isRing) {
    //  ring();
    //} else {
    //  cancelRing();
    //}
    return this;
  }

  public ZdNotification setIsClear(boolean isClear) {
    this.isClear = isClear;
    return this;
  }

  public ZdNotification setLights(boolean isLight) {
    if (isLight) {
      builder.setLights(lightColor, lightTime, unLightTime);
    }
    return this;
  }

  public ZdNotification setProgress(int max, int progress, boolean indeterminate) {
    builder.setProgress(max, progress, indeterminate);
    return this;
  }

  public ZdNotification setSubText(String subText) {
    builder.setSubText(subText);
    return this;
  }

  public ZdNotification setTicker(String ticker) {
    builder.setTicker(ticker);
    return this;
  }

  public ZdNotification setWhen(long when) {
    builder.setWhen(when);
    return this;
  }

  public ZdNotification setLocalOnly(boolean b) {
    builder.setLocalOnly(b);
    return this;
  }

  public ZdNotification setShowWhen(boolean showWhen) {
    builder.setShowWhen(showWhen);
    return this;
  }

  public ZdNotification setChannelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  public ZdNotification setChannelName(String channelName) {
    this.channelName = channelName;
    return this;
  }

  public void clear(int notificationId) {
    if (notificationManager != null) {
      notificationManager.cancel(notificationId);
    }
  }
}
