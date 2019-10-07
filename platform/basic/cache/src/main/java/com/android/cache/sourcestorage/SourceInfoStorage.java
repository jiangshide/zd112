package com.android.cache.sourcestorage;

import com.android.cache.SourceInfo;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public interface SourceInfoStorage {
    SourceInfo get(String url);

    void put(String url, SourceInfo sourceInfo);
    void release();
}
