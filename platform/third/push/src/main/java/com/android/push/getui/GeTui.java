package com.android.push.getui;

import android.content.Context;
import android.text.TextUtils;
import com.android.push.Push;
import com.android.push.getui.service.GeTuiIntentService;
import com.android.push.getui.service.GeTuiPushService;
import com.android.push.listener.OnPushListener;
import com.android.utils.AppUtil;
import com.android.utils.LogUtil;
import com.getui.gis.sdk.GInsightManager;
import com.getui.gis.sdk.listener.IGInsightEventListener;
import com.getui.gs.ias.core.GsConfig;
import com.getui.gs.sdk.GsManager;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

/**
 * created by jiangshide on 2019-09-04.
 * email:18311271399@163.com
 */
public class GeTui {

  private static class GeTuiHolder {
    private static GeTui instance = new GeTui();
  }

  public static GeTui getInstance() {
    return GeTuiHolder.instance;
  }

  public GeTuiIntentService mGeTuiIntentService;

  public int pushIcon;

  public GeTui init(Context context, OnPushListener listener) {
    if (context == null) return this;
    PushManager.getInstance().initialize(context, GeTuiPushService.class);
    PushManager.getInstance()
        .registerPushIntentService(context,
            (mGeTuiIntentService = new GeTuiIntentService()).getClass());
    mGeTuiIntentService.setListener(listener);
    GInsightManager.getInstance().init(context, new IGInsightEventListener() {

      @Override public void onSuccess(String userId) {
        Push.getInstance().setUserId(userId);
      }

      @Override public void onError(String s) {
      }
    });
    PushManager.getInstance()
        .registerPushIntentService(AppUtil.getApplicationContext(), GeTuiIntentService.class);
    GsManager.getInstance().init(context);
    return this;
  }

  public GeTui bindAlias(String... aliass) {
    if (aliass == null || aliass.length == 0) return this;
    for (String alias : aliass) {
      boolean isBind = PushManager.getInstance().bindAlias(AppUtil.getApplicationContext(), alias);
      LogUtil.e("isBind:",isBind);
    }
    return this;
  }

  public GeTui unBindAlias(String... aliass) {
    if (aliass != null || aliass.length == 0) return this;
    for (String alias : aliass) {
      PushManager.getInstance().unBindAlias(AppUtil.getApplicationContext(), alias, true);
    }
    return this;
  }

  /**
   * 设置tag
   */
  public GeTui setTag(String... tags) {
    if (tags == null || tags.length == 0) return this;
    int size = tags.length;
    Tag[] tagParams = new Tag[size];
    for (int i = 0; i < size; i++) {
      tagParams[i] = new Tag().setName(tags[i]);
    }
    int tagResult = PushManager.getInstance()
        .setTag(AppUtil.getApplicationContext(), tagParams, System.currentTimeMillis() + "");
    return this;
  }

  /**
   * 设置渠道
   */
  public GeTui setChannel(String channel) {
    if (TextUtils.isEmpty(channel)) return this;
    GInsightManager.getInstance().setInstallChannel(channel);
    GsConfig.setInstallChannel(channel);
    GsConfig.setSessionTimoutMillis(System.currentTimeMillis());//启动应用时长
    //GsConfig.setEnableSmartReporting(true);默认有 WIFI 的情况实时上传，无 WIFI 则间隔 10 分钟上报数据，以及每次启动时上传。开发者也可以手动关闭有 WIFI 情况下实时上传数据的开关。
    return this;
  }

  public GeTui setIcon(int icon){
    this.pushIcon = icon;
    return this;
  }

  public GeTui setListener(OnPushListener listener) {
    if (mGeTuiIntentService != null) {
      mGeTuiIntentService.setListener(listener);
    }
    return this;
  }
}
