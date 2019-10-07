package com.android.cache;

import com.android.cache.exception.ProxyCacheException;

import java.io.ByteArrayInputStream;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class ByteArraySource implements Source{

    private final byte[] data;

    private ByteArrayInputStream byteArrayInputStream;

    public ByteArraySource(byte[] data){
        this.data = data;
    }

    @Override
    public void open(long offset) throws ProxyCacheException {
        this.byteArrayInputStream = new ByteArrayInputStream(this.data);
        this.byteArrayInputStream.skip(offset);
    }

    @Override
    public long length() throws ProxyCacheException {
        return this.data.length;
    }

    @Override
    public int read(byte[] buffer) throws ProxyCacheException {
        return this.byteArrayInputStream.read(buffer,0,buffer.length);
    }

    @Override
    public void close() throws ProxyCacheException {

    }
}
