package com.android.user;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.android.base.BaseActivity;
import com.android.zdannotations.BindPath;

/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
@BindPath("user/user")
public class UserActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    goFragment("user", UserFragment.class, true);
  }
}
