package com.android.cache.exception;


import com.android.cache.BuildConfig;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class ProxyCacheException extends Exception{
    private static final String LIBRARY_VERSION = ".Version:"+ BuildConfig.VERSION_NAME;

    public ProxyCacheException(String message){
        super(message+LIBRARY_VERSION);
    }

    public ProxyCacheException(String message,Throwable throwable){
        super(message+LIBRARY_VERSION,throwable);
    }

    public ProxyCacheException(Throwable throwable){
        super("No explanation error:"+LIBRARY_VERSION,throwable);
    }
}
