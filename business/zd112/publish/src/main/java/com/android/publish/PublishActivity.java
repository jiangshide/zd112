package com.android.publish;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.android.base.BaseActivity;
import com.android.zdannotations.BindPath;

/**
 * created by jiangshide on 2019-10-02.
 * email:18311271399@163.com
 */

@BindPath("blog/blog")
public class PublishActivity extends BaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    push(new PublishFragment());
  }
}
