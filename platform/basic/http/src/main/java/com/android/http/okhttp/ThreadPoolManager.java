package com.android.http.okhttp;

import com.android.utils.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * created by jiangshide on 2019-06-27.
 * email:18311271399@163.com
 */
public class ThreadPoolManager {
    private static volatile ThreadPoolManager instance;

    private ThreadPoolExecutor threadPoolExecutor;

    //创建延迟队列
    private DelayQueue<HttpTask> delayQueue = new DelayQueue<>();

    //将失败的请求添加到延迟队列
    public void addDelayTask(HttpTask httpTask) {
        if (httpTask != null && httpTask.isRetry()) {
            httpTask.setDelayTime(3000);//设置延迟时间为3秒
            delayQueue.offer(httpTask);
        }
    }

    private ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                addTask(r);
            }
        });
        threadPoolExecutor.execute(coreThread);
        threadPoolExecutor.execute(delayThread);
    }

    //核心线程
    public Runnable coreThread = new Runnable() {
        Runnable runnable = null;

        @Override
        public void run() {
            while (true) {
                try {
                    runnable = linkedBlockingQueue.take();
                } catch (InterruptedException e) {
                    LogUtil.e(e);
                }
                threadPoolExecutor.execute(runnable);
            }
        }
    };

    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    private LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();

    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                linkedBlockingQueue.put(runnable);
            } catch (InterruptedException e) {
                LogUtil.e(e);
            }
        }
    }

    //延迟线程
    public Runnable delayThread = new Runnable() {
        HttpTask httpTask;

        @Override
        public void run() {
            while (true) {
                try {
                    httpTask = delayQueue.take();
//                    if(httpTask.isRetry()){
//                        return;
//                    }
                    if (httpTask.getRetryCount() < 3) {
                        threadPoolExecutor.execute(httpTask);
                        httpTask.setRetryCount(httpTask.getRetryCount() + 1);
                        LogUtil.e("==重试机制==i:" + httpTask.getRetryCount() + " time:" + System.currentTimeMillis());
                    } else {
                        LogUtil.e("==重试机制==i:" + httpTask.getRetryCount() + " much more times!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
