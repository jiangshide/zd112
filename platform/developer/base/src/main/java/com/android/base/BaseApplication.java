package com.android.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import com.android.cache.HttpProxyCacheServer;
import com.android.http.Http;
import com.android.location.Location;
import com.android.skin.Skin;
import com.android.utils.FileUtil;
import com.android.utils.LogUtil;
import com.android.utils.SPUtil;
import com.android.zdplugin.ZdPlugin;
import com.android.zdrouter.ZdRouter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.File;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class BaseApplication extends Application {

  public IWXAPI mWxApi;

  private HttpProxyCacheServer httpProxyCacheServer;

  public String UID = "uid";

  public static BaseApplication instance;

  @Override
  protected void attachBaseContext(Context base) {
    MultiDex.install(base);
    ZdPlugin.getInstance().init(base).fixDex();//todo 热修复待优化
    super.attachBaseContext(base);
    instance = this;
    //        loop();
    ZdRouter.getInstance().init(this);//路由初始化
    Http.INSTANCE.init(this);//网络初始化
    //Monitor.getInstance().init(this);//性能监控
    //ZdEvent.get().config().supportBroadcast(this).lifecycleObserverAlwaysActive(true);//事件分发初始化
    Skin.getInstance().init(this);//主题初始化

    /**
     * 微信注册
     */
    mWxApi = WXAPIFactory.createWXAPI(this, BuildConfig.WECHAT_APPID);

    Location.getInstance().startLocation();
  }

  @Override public void onCreate() {
    super.onCreate();
    //Push.getInstance().init(this,this).setChannel("jsd");
    //loop();
    //Intent service = new Intent(getApplicationContext(), BaseService.class);
    //getApplicationContext().startService(service);
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

  public HttpProxyCacheServer getProxy(Context context) {
    return this.getProxy(context, false);
  }

  public HttpProxyCacheServer getProxy(Context context, Boolean isAudo) {
    File cacheFile = FileUtil.getVideoCacheDir(context);
    if (!isAudo) {
      cacheFile = FileUtil.getAudioCacheDir(context);
    }
    if (httpProxyCacheServer == null) {
      httpProxyCacheServer =
          new HttpProxyCacheServer.Builder(this).cacheDirectory(cacheFile).build();
    }
    return httpProxyCacheServer;
  }

  public String getProxyUrl(String url, Boolean isAudio) {
    return getProxy(this, isAudio).getProxyUrl(url);
  }

  public long getUid() {
    return SPUtil.getLong(UID);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }
}
