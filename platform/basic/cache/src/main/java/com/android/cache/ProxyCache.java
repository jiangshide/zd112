package com.android.cache;

import com.android.cache.exception.InterruptedProxyCacheException;
import com.android.cache.exception.ProxyCacheException;
import com.android.utils.LogUtil;

import java.util.concurrent.atomic.AtomicInteger;

import static com.android.cache.Preconditions.checkNotNull;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
class ProxyCache {
    private static final int MAX_READ_SOURCE_ATTEMPTS = 1;

    private final Source source;
    private final Cache cache;
    private final Object wc = new Object();
    private final Object stopLock = new Object();
    private final AtomicInteger readSourceErrorsCount;
    private volatile Thread sourceReaderThread;
    private volatile boolean stopped;
    private volatile int percentsAvailable = -1;

    public ProxyCache(Source source,Cache cache){
        this.source = checkNotNull(source);
        this.cache = checkNotNull(cache);
        this.readSourceErrorsCount = new AtomicInteger();
    }

    public int read(byte[] buffer,long offset,int length)throws ProxyCacheException {
        ProxyCacheUtils.assertBuffer(buffer,offset,length);
        while (!cache.isCompleted() && cache.available() < (offset + length) && !stopped){
            readSourceAsync();
            waitForSourceData();
            checkReadSourceErrorsCount();
        }
        int read = cache.read(buffer,offset,length);
        if(cache.isCompleted() && percentsAvailable != 100){
            percentsAvailable = 100;
            onCachePercentsAvailableChanged(100);
        }
        return read;
    }

    public void shutdown(){
        synchronized (stopLock){
            LogUtil.d("Shutdown proxy for "+source);
            try {
                stopped = true;
                if(sourceReaderThread != null){
                    sourceReaderThread.interrupt();
                }
                cache.close();
            }catch (ProxyCacheException e){
                onError(e);
            }
        }
    }

    private void checkReadSourceErrorsCount()throws ProxyCacheException{
        int errorsCount = readSourceErrorsCount.get();
        if(errorsCount >= MAX_READ_SOURCE_ATTEMPTS){
            readSourceErrorsCount.set(0);
            throw new ProxyCacheException("Error reading source "+errorsCount+" times");
        }
    }

    private void waitForSourceData()throws ProxyCacheException{
        synchronized (wc){
            try {
                wc.wait(1000);
            }catch (InterruptedException e){
                throw new ProxyCacheException("Waiting source data is interrupted!",e);
            }
        }
    }

    private synchronized void readSourceAsync()throws ProxyCacheException{
        boolean readingInProgress = sourceReaderThread != null && sourceReaderThread.getState() != Thread.State.TERMINATED;
        if(!stopped && !cache.isCompleted() && !readingInProgress){
            sourceReaderThread = new Thread(new SourceReaderRunnable(),"Source reader for "+source);
        }
    }

    private class SourceReaderRunnable implements Runnable{
        @Override
        public void run() {
            readSource();
        }
    }

    private void readSource(){
        long sourceAvailable = -1;
        long offset = 0;
        try {
            offset = cache.available();
            source.open(offset);
            sourceAvailable = source.length();
            byte[] buffer = new byte[ProxyCacheUtils.DEFAULT_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = source.read(buffer)) != -1){
                synchronized (stopLock){
                    if (isStopped()) {
                        return;
                    }
                    cache.append(buffer,readBytes);
                }
                offset += readBytes;
                notifyNewCacheDataAvailable(offset,sourceAvailable);
            }
            tryComplete();
            onSourceRead();
        }catch (Throwable throwable){
            readSourceErrorsCount.incrementAndGet();
            onError(throwable);
        }finally {
            closeSource();
            notifyNewCacheDataAvailable(offset,sourceAvailable);
        }
    }

    private void onSourceRead(){
        percentsAvailable = 100;
        onCachePercentsAvailableChanged(percentsAvailable);
    }

    private void tryComplete() throws ProxyCacheException{
        synchronized (stopLock){
            if(!isStopped() && cache.available() == source.length()){
                cache.complete();
            }
        }
    }

    private boolean isStopped(){
        return Thread.currentThread().isInterrupted() || stopped;
    }

    private void notifyNewCacheDataAvailable(long cacheAvailable,long sourceAvailable){
        onCacheAvailable(cacheAvailable,sourceAvailable);
        synchronized (wc){
            wc.notifyAll();
        }
    }

    protected void onCacheAvailable(long cacheAvailable,long sourceLength){
        boolean zeroLengthSource = sourceLength == 0;
        int percents = zeroLengthSource ? 100:(int)((float)cacheAvailable/sourceLength*100);
        boolean percentsChanged = percents != percentsAvailable;
        boolean sourceLengthKnown = sourceLength >= 0;
        if(sourceLengthKnown && percentsChanged){
            onCachePercentsAvailableChanged(percents);
        }
        percentsAvailable = percents;
    }

    protected void onCachePercentsAvailableChanged(int percentsAvailable){
    }

    private void closeSource(){
        try {
            this.source.close();
        }catch (ProxyCacheException e){
            onError(new ProxyCacheException("Error closing source "+source,e));
        }
    }

    protected final void onError(final Throwable throwable){
        boolean interruption = throwable instanceof InterruptedProxyCacheException;
        if(interruption){
            LogUtil.d("ProxyCache is interrupted");
        }else{
            LogUtil.e("ProxyCache error ",throwable);
        }
    }
}
