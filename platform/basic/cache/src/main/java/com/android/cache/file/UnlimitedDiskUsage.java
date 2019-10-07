package com.android.cache.file;

import java.io.File;
import java.io.IOException;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class UnlimitedDiskUsage implements DiskUsage{
    @Override
    public void touch(File file) throws IOException {
        //todo do nothing
    }
}
