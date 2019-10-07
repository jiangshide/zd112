package com.android.cache.sourcestorage;


import com.android.cache.SourceInfo;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class NoSourceInfoStorage implements SourceInfoStorage{
    @Override
    public SourceInfo get(String url) {
        return null;
    }

    @Override
    public void put(String url, SourceInfo sourceInfo) {

    }

    @Override
    public void release() {

    }
}
