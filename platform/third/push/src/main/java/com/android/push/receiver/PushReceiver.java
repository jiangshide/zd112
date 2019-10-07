package com.android.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.event.ZdEvent;
import com.android.push.Push;
import com.android.push.bean.PushBean;
import com.android.utils.AppUtil;
import com.android.utils.JsonUtil;
import com.android.utils.LogUtil;
import com.android.widget.ZdNotification;

/**
 * created by jiangshide on 2019-09-05.
 * email:18311271399@163.com
 */
public class PushReceiver extends BroadcastReceiver {

  public static String PUSH = "com.android.push";

  @Override public void onReceive(Context context, Intent intent) {
    if (intent == null) return;
    String action = intent.getAction();
    int notificationId = intent.getIntExtra(ZdNotification.NOTIFICATION,1);
    if (TextUtils.isEmpty(action) || !action.equals(PUSH) || !intent.hasExtra(
        Push.PUSH)) {
      return;
    }
    PushBean pushBean = (PushBean) intent.getSerializableExtra(Push.PUSH);
    if (pushBean == null) return;
    pushBean.isInner = AppUtil.isAppForeground();
    Push.getInstance().setPushBean(pushBean);
    if (pushBean.isInner) {
      ZdEvent.Companion.get().with(Push.PUSH).post(pushBean);
    } else {
      AppUtil.goActivity(Push.MAIN);
    }
    ZdNotification.getInstance().clear(notificationId);
  }
}
