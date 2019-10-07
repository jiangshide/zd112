package com.android.cache;

import com.android.cache.exception.ProxyCacheException;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class ByteArrayCache implements Cache{

    private volatile byte[] data;
    private volatile boolean completed;

    public ByteArrayCache(){
        this(new byte[0]);
    }

    public ByteArrayCache(byte[] data){
        this.data = Preconditions.checkNotNull(data);
    }

    @Override
    public long available() throws ProxyCacheException {
        return this.data.length;
    }

    @Override
    public int read(byte[] buffer, long offset, int length) throws ProxyCacheException {
        if(offset >= this.data.length){
            return -1;
        }
        if(offset > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Too long offset for memory cache "+offset);
        }
        return new ByteArrayInputStream(this.data).read(buffer,(int)offset,length);
    }

    @Override
    public void append(byte[] newData, int length) throws ProxyCacheException {
        Preconditions.checkNotNull(this.data);
        Preconditions.checkArgument(length >= 0 && length <= newData.length);
        byte[] appendedData = Arrays.copyOf(this.data,this.data.length+length);
        System.arraycopy(newData,0,appendedData,this.data.length,length);
        this.data = appendedData;
    }

    @Override
    public void close() throws ProxyCacheException {

    }

    @Override
    public void complete() throws ProxyCacheException {
        this.completed = true;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }
}
