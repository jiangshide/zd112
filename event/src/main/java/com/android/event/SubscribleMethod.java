package com.android.event;

import java.lang.reflect.Method;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public class SubscribleMethod<T> {
    private Method method;
    private ThreadMode threadMode;
    private Class<T> type;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<T> type) {
        this.method = method;
        this.threadMode = threadMode;
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
