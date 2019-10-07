package com.android.push.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.android.push.Push;
import com.android.push.bean.PushBean;
import com.android.push.getui.service.GeTuiIntentService;
import com.android.utils.AppUtil;
import com.android.utils.LogUtil;

/**
 * created by jiangshide on 2019-09-06.
 * email:18311271399@163.com
 */
public class PushActivity extends Activity {

  /**
   * the push key with content
   */
  private final String CONTENT = "content";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (!intent.hasExtra(CONTENT)) return;
    String data = intent.getStringExtra(CONTENT);
    if (TextUtils.isEmpty(data)) return;
    LogUtil.e("push~data:", data);
    PushBean pushBean = GeTuiIntentService.parseData(this, data);
    if (pushBean == null) return;
    //pushBean.isInner = AppUtil.isAppForeground();
    pushBean.isInner = false;
    Push.getInstance().setPushBean(pushBean);
    if (pushBean.show_type) {
      AppUtil.wakeUpAndUnlock();
    }
    AppUtil.goActivity(Push.MAIN);
    finish();
  }
}
