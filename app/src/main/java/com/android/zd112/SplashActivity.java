package com.android.zd112;

import com.android.base.BaseActivity;
import com.android.utils.ShareParamUtil;
import com.android.utils.annotation.ContentView;
import com.android.zdannotations.BindPath;
import com.android.zdrouter.ZdRouter;

@BindPath("splash/splash")
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Override
    protected void onResume() {
        super.onResume();
        boolean isLogin = ShareParamUtil.getBoolean("isLogin");
        ZdRouter.getInstance().go(isLogin ? "main/main" : "login/login").build(this);
        ShareParamUtil.putBoolean("isLogin", true);
    }
}
