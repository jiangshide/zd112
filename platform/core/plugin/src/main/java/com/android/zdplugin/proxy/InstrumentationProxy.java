package com.android.zdplugin.proxy;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.android.utils.LogUtil;
import com.android.zdplugin.hook.HookHelper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * created by jiangshide on 2019-08-12.
 * email:18311271399@163.com
 */
public class InstrumentationProxy extends Instrumentation {

    private Instrumentation mInstrumentation;
    private PackageManager mPackageManager;

    public InstrumentationProxy(Instrumentation instrumentation, PackageManager packageManager) {
        this.mInstrumentation = instrumentation;
        this.mPackageManager = packageManager;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        List<ResolveInfo> infos = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        if (null == infos || infos.size() == 0) {
            intent.putExtra(HookHelper.TARGET_INTENT, intent.getComponent().getClassName());
            intent.setClassName(who, "com.android.zdplugin.StubActivity");
        }
        try {
            Method execMethod = Instrumentation.class.getDeclaredMethod("execStartActivity", Context.class, IBinder.class,
                    IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);
            return (ActivityResult) execMethod.invoke(mInstrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return null;
    }

    public Activity newActivity(ClassLoader classLoader, String className, Intent intent) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        String name = intent.getStringExtra(HookHelper.TARGET_INTENT);
        if (!TextUtils.isEmpty(name)) {
            return super.newActivity(classLoader, name, intent);
        }
        return super.newActivity(classLoader, className, intent);
    }
}
