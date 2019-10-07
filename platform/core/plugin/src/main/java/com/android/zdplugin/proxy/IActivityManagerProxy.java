package com.android.zdplugin.proxy;

import android.content.Intent;

import com.android.zdplugin.hook.HookHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * created by jiangshide on 2019-08-12.
 * email:18311271399@163.com
 */
public class IActivityManagerProxy implements InvocationHandler {

    private Object mActivityManager;

    public IActivityManagerProxy(Object activityManager) {
        this.mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Intent intent;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) args[index];
            Intent stubIntent = new Intent();
            String pkgName = "com.android.zdplugin";
            stubIntent.setClassName(pkgName, pkgName + ".StubActivity");
            stubIntent.putExtra(HookHelper.TARGET_INTENT, intent);
            args[index] = stubIntent;
        }
        return method.invoke(mActivityManager, args);
    }
}
