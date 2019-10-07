package com.android.cache.file;

import java.io.File;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class TotalCountLruDiskUsage extends LruDiskUsage{

    private final int maxCount;

    public TotalCountLruDiskUsage(int maxCount){
        if(maxCount <= 0){
            throw new IllegalArgumentException("Max count must be positive number!");
        }
        this.maxCount = maxCount;
    }

    @Override
    protected boolean accept(File file, long totalSize, int totalCount) {
        return totalCount <= maxCount;
    }
}
