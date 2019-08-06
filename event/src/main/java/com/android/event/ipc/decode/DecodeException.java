package com.android.event.ipc.decode;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public class DecodeException extends Exception{

    public DecodeException(){}

    public DecodeException(String message){
        super(message);
    }

    public DecodeException(String message,Throwable throwable){
        super(message,throwable);
    }

    public DecodeException(Throwable throwable){
        super(throwable);
    }
}
