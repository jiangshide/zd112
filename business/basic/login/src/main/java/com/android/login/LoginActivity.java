package com.android.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.android.base.BaseActivity;
import com.android.base.BuildConfig;
import com.android.http.vm.LiveResult;
import com.android.login.vm.LoginVM;
import com.android.login.vm.data.UserData;
import com.android.zdannotations.BindPath;


/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
@BindPath("login/login")
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loginWxL).setOnClickListener(this);

        LoginVM.of(this, LoginVM.class).getLoginResult().observe(this, new Observer<LiveResult<UserData>>() {
            @Override
            public void onChanged(@Nullable LiveResult<UserData> userDataLiveResult) {
//                ZdRouter.getInstance().go("main/main").build(LoginActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.loginWxL) {
            LoginVM.of(this, LoginVM.class).wechatlogin(this, BuildConfig.WECHAT_APPID);
        }
    }
}
