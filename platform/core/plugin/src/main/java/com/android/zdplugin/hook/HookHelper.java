package com.android.zdplugin.hook;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;

import com.android.utils.ReflectUtil;
import com.android.zdplugin.proxy.IActivityManagerProxy;
import com.android.zdplugin.proxy.InstrumentationProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * created by jiangshide on 2019-08-12.
 * email:18311271399@163.com
 */
public class HookHelper {
    public static final String TARGET_INTENT = "target_intent";

    public static void hookAMS() throws Exception {
        Object singleton;
        if (Build.VERSION.SDK_INT >= 26) {
            Class<?> clazz = Class.forName("android.app.ActivityManager");
            singleton = ReflectUtil.getObject(clazz, null, "IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            singleton = ReflectUtil.getObject(activityManagerNativeClass, null, "gDefault");
        }
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Method method = singletonClass.getMethod("get");
        Object iActivityManager = method.invoke(singleton);
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iActivityManagerClass}, new IActivityManagerProxy(iActivityManager));
        ReflectUtil.setField(singletonClass, singleton, "mInstance", proxy);
    }

    public static void hookInstrumentation(Context context) throws Exception {
        Class<?> contextImpl = Class.forName("android.app.ContextImpl");
        Object activityThread = ReflectUtil.getObject(contextImpl, context, "mMainThread");
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Object mInstrumentation = ReflectUtil.getObject(activityThreadClass, activityThread, "mInstrumentation");
        ReflectUtil.setField(activityThreadClass, activityThread, "mInstrumentation", new InstrumentationProxy((Instrumentation) mInstrumentation, context.getPackageManager()));
    }
}
