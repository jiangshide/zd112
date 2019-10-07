package com.android.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.utils.LogUtil;

/**
 * created by jiangshide on 2019-10-01.
 * email:18311271399@163.com
 */
public class BaseReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    LogUtil.e("--------action:", intent.getAction());
    Intent service = new Intent(context, BaseService.class);
    context.startService(service);
  }
}
