package com.android.utils;

import android.os.Looper;

/**
 * created by jiangshide on 2016-06-18.
 * email:18311271399@163.com
 */
public final class ThreadUtil {
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
