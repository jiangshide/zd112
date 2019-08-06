package com.android.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.base.BaseActivity;
import com.android.base.BuildConfig;
import com.android.login.vm.LoginVM;
import com.android.login.vm.data.UserData;
import com.android.network.vm.LiveResult;
import com.android.utils.LogUtil;
import com.android.zdannotations.BindPath;
import com.android.zdrouter.ZdRouter;


/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
@BindPath("login/login")
public class LoginActivity extends BaseActivity {

    private LoginVM loginVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loginWxL).setOnClickListener(this);
        loginVM = ViewModelProviders.of(this).get(LoginVM.class);
        loginVM.getLoginResult().observe(this, new Observer<LiveResult<UserData>>() {
            @Override
            public void onChanged(@Nullable LiveResult<UserData> userDataLiveResult) {
                ZdRouter.getInstance().go("main/main").build(LoginActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.loginWxL) {
            loginVM.wechatlogin(this, BuildConfig.WECHAT_APPID);
        }
    }
}
