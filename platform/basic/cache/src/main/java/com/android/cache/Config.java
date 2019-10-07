package com.android.cache;

import com.android.cache.file.DiskUsage;
import com.android.cache.file.FileNameGenerator;
import com.android.cache.headers.HeaderInjector;
import com.android.cache.sourcestorage.SourceInfoStorage;

import java.io.File;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class Config {
    public final File cacheRoot;
    public final FileNameGenerator fileNameGenerator;
    public final DiskUsage diskUsage;
    public final SourceInfoStorage sourceInfoStorage;
    public final HeaderInjector headerInjector;

    Config(File cacheRoot,FileNameGenerator fileNameGenerator,DiskUsage diskUsage,SourceInfoStorage sourceInfoStorage,HeaderInjector headerInjector){
        this.cacheRoot = cacheRoot;
        this.fileNameGenerator = fileNameGenerator;
        this.diskUsage = diskUsage;
        this.sourceInfoStorage = sourceInfoStorage;
        this.headerInjector = headerInjector;
    }

    File generateCacheFile(String url){
        String name = fileNameGenerator.generate(url);
        return new File(cacheRoot,name);
    }
}
