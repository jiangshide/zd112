package com.android.cache.exception;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class InterruptedProxyCacheException extends ProxyCacheException{

    public InterruptedProxyCacheException(String message){
       super(message);
    }

    public InterruptedProxyCacheException(String message,Throwable throwable){
        super(message,throwable);
    }

    public InterruptedProxyCacheException(Throwable throwable){
        super(throwable);
    }
}
