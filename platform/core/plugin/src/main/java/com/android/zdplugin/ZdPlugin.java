package com.android.zdplugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.utils.AppUtil;
import com.android.utils.LogUtil;
import com.android.zdplugin.fix.FixDex;

import java.lang.reflect.Method;

/**
 * 处理插件话基础逻辑,以及热修复相关逻辑
 * created by jiangshide on 2019-08-06.
 * email:18311271399@163.com
 */
public final class ZdPlugin {

    private Context mContext;

    private ZdPlugin() {
    }

    public ZdPlugin init(Context context) {
        this.mContext = context;
        return this;
    }

    private static class ZdPluginHolder {
        private static ZdPlugin instance = new ZdPlugin();
    }

    public static ZdPlugin getInstance() {
        return ZdPluginHolder.instance;
    }

    /**
     * 热修复调用实现
     */
    public void fixDex() {
        new FixDex().fixDex(mContext);
    }

    /**
     * 插件加载调用实现
     */
    public void loadPlugin() {
        //todo 待实现
    }

    public void initApplication(String path) {
        this.initApplication(path, "onCreate");
    }

    public void initApplication(String path, String methodName) {
        try {
            Class clazz = Class.forName(AppUtil.getApplicationInfo(path).name, true, mContext.getClassLoader());
            Method method = clazz.getMethod(methodName);
            method.invoke(clazz.newInstance());
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void startActivity(String classPath) {
        this.startActivity(classPath, null);
    }

    public void startActivity(String classPath, Bundle bundle) {
        Class clazz = null;
        try {
            clazz = Class.forName(classPath);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (null == clazz) {
            LogUtil.e("clazz is null!");
            return;
        }
        Intent intent = new Intent(mContext, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(new Intent());
    }
}
