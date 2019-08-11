package com.android.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;

import com.android.event.LiveEventBus;
import com.android.monitor.Monitor;
import com.android.network.NetWorkApi;
import com.android.skin.Skin;
import com.android.utils.LogUtil;
import com.android.zdplugin.ZdPlugin;
import com.android.zdrouter.ZdRouter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class BaseApplication extends Application {

    public IWXAPI mWxApi;

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        ZdPlugin.getInstance().init(base).fixDex();//todo 热修复待优化
        super.attachBaseContext(base);
//        loop();
        ZdRouter.getInstance().init(this);//路由初始化
        NetWorkApi.Companion.getInstance().init(this);//网络初始化
        Monitor.getInstance().init(this);//性能监控
        LiveEventBus.get().config().supportBroadcast(this).lifecycleObserverAlwaysActive(true);//事件分发初始化
        Skin.getInstance().init(this);//主题初始化

        /**
         * 微信注册
         */
        mWxApi = WXAPIFactory.createWXAPI(this, BuildConfig.WECHAT_APPID);
    }

    private void loop() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Exception e) {
                        LogUtil.e(e);
                    }
                }
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
