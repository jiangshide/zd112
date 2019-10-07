package com.android.cache;

import com.android.cache.exception.ProxyCacheException;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public interface Cache {
    long available() throws ProxyCacheException;

    int read(byte[] buffer, long offset, int length) throws ProxyCacheException;

    void append(byte[] data, int length) throws ProxyCacheException;

    void close() throws ProxyCacheException;

    void complete() throws ProxyCacheException;

    boolean isCompleted();
}
