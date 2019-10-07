package com.android.refresh.runable;

/**
 * created by jiangshide on 2016-07-24.
 * email:18311271399@163.com
 */
public class DelayedRunnable implements Runnable{
    public long delayMillis;
    private Runnable runnable = null;
    public DelayedRunnable(Runnable runnable, long delayMillis) {
        this.runnable = runnable;
        this.delayMillis = delayMillis;
    }
    @Override
    public void run() {
        try {
            if (runnable != null) {
                runnable.run();
                runnable = null;
            }
        } catch (Throwable e) {
            if (!(e instanceof NoClassDefFoundError)) {
                e.printStackTrace();
            }
        }
    }
}
