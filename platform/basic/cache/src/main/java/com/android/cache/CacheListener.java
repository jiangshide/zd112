package com.android.cache;

import java.io.File;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public interface CacheListener {
    void onCacheAvailable(File cacheFIle, String url, int percentsAvailable);
}
