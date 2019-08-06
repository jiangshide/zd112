package com.android.event.ipc.encode;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public class EncodeException extends Exception{
    public EncodeException(){}

    public EncodeException(String message){
        super(message);
    }

    public EncodeException(String message,Throwable throwable){
        super(message,throwable);
    }

    public EncodeException(Throwable throwable){
        super(throwable);
    }
}

