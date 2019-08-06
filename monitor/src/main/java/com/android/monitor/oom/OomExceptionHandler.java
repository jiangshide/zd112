package com.android.monitor.oom;

import android.content.Context;
import android.os.Debug;

import java.io.File;
import java.io.IOException;

/**
 * created by jiangshide on 2019-08-06.
 * email:18311271399@163.com
 */
public class OomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private Context context;

    public OomExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler,Context context){
        this.uncaughtExceptionHandler = exceptionHandler;
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(containsOom(e)){
            File heapDumpFile = new File(context.getFilesDir(),"out-of-memory.hprof");
            try {
                Debug.dumpHprofData(heapDumpFile.getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        uncaughtExceptionHandler.uncaughtException(t,e);
    }

    private boolean containsOom(Throwable throwable){
        return true;
    }
}
