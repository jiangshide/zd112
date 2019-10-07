package com.android.monitor;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.android.img.Img;

/**
 * 监控系统基础信息,应用基础信息,OOM,ANR等相关信息
 * created by jiangshide on 2019-08-06.
 * email:18311271399@163.com
 */
public final class Monitor implements Application.ActivityLifecycleCallbacks {

    private Context mContext;

    private Monitor() {
    }

    private static class MonitorHolder {
        private static Monitor instsance = new Monitor();
    }

    public void init(Application application) {
        this.mContext = application;
        ((Application) this.mContext).registerActivityLifecycleCallbacks(this);
    }

    public static Monitor getInstance() {
        return MonitorHolder.instsance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
