package com.android.network.vm;

import android.support.annotation.NonNull;

/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
public class LiveResult<T> {
    private final Throwable error;
    private final T data;

    public LiveResult(Throwable error, T data) {
        this.error = error;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }

    public static <T> LiveResult<T> success(T data) {
        return new LiveResult<>(null, data);
    }

    public static <T> LiveResult<T> error(@NonNull Throwable throwable) {
        return new LiveResult<>(throwable, null);
    }
}
