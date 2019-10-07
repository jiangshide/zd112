package com.android.cache;

import com.android.cache.exception.ProxyCacheException;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public interface Source {
    void open(long offset)throws ProxyCacheException;

    long length() throws ProxyCacheException;
    int read(byte[] buffer)throws ProxyCacheException;
    void close()throws ProxyCacheException;
}
