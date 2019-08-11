package com.android.zd112;

import android.Manifest;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.base.BaseActivity;
import com.android.permission.annotation.Permission;
import com.android.permission.annotation.PermissionResult;
import com.android.permission.model.GrantModel;
import com.android.skin.Skin;
import com.android.utils.LogUtil;
import com.android.utils.ShareParamUtil;
import com.android.utils.annotation.ContentView;
import com.android.utils.annotation.InjectView;
import com.android.utils.annotation.OnClick;
import com.android.widget.ZdDialog;
import com.android.zdannotations.BindPath;

@BindPath("splash/splash")
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @InjectView(R.id.textTips)
    private TextView textTips;
    @InjectView(R.id.bgImg)
    private ImageView bgImg;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isLogin = ShareParamUtil.getBoolean("isLogin");
//        ZdRouter.getInstance().go(isLogin ? "main/main" : "login/login").build();
        ShareParamUtil.putBoolean("isLogin", true);
    }


    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 0)
    @OnClick(R.id.test)
    public void test() {
        Skin.getInstance().loadSkinApk(Environment.getExternalStorageDirectory() + "/skin.apk");
        apply();
    }

    @PermissionResult
    public void request(GrantModel grantModel) {
        LogUtil.e("------------request:", grantModel.isCancel);
    }

    @Override
    public void requestPermissionResult(GrantModel grantModel) {
        LogUtil.e("----------grantModel:", grantModel.isCancel);
    }

    @OnClick(R.id.login)
    public void login(View view) {
//        List<String> list = new ArrayList<String>();
//        list.add("java");
//        list.add("android");
//        ZdDialog.createList(this, list).show();

        ZdDialog.create(this).show();
    }
}
