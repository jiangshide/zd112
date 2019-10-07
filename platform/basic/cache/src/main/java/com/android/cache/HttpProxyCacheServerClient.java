package com.android.cache;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.cache.exception.ProxyCacheException;
import com.android.cache.file.FileCache;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.android.cache.Preconditions.checkNotNull;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class HttpProxyCacheServerClient {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final String url;
    private volatile HttpProxyCache httpProxyCache;
    private final List<CacheListener>  cacheListeners = new CopyOnWriteArrayList<>();
    private final CacheListener cacheListener;
    private final Config config;

    public HttpProxyCacheServerClient(String url,Config config){
        this.url = checkNotNull(url);
        this.config = checkNotNull(config);
        this.cacheListener = new UIHandler(url,cacheListeners);
    }

    public void processRequest(GetRequest getRequest, Socket socket)throws ProxyCacheException, IOException {
        startProcessRequest();
        try {
            atomicInteger.incrementAndGet();
            httpProxyCache.processRequest(getRequest,socket);
        }finally {
            finishProcessRequest();
        }
    }

    public int getClientCount(){
        return atomicInteger.get();
    }

    private synchronized void finishProcessRequest(){
        if(atomicInteger.decrementAndGet() <= 0){
            httpProxyCache.shutdown();//todo shutdown()
            httpProxyCache = null;
        }
    }

    public void shutdown(){
        cacheListeners.clear();
        if(httpProxyCache != null){
            httpProxyCache.registerCacheListener(null);
            httpProxyCache.shutdown();
            httpProxyCache = null;
        }
        atomicInteger.set(0);
    }

    public void registerCacheListener(CacheListener cacheListener){
        this.cacheListeners.add(cacheListener);
    }

    public void unregisterCacheListener(CacheListener cacheListener){
        this.cacheListeners.remove(cacheListener);
    }

    private synchronized void startProcessRequest()throws ProxyCacheException{
            if(null == httpProxyCache){
                HttpUrlSource httpUrlSource = new HttpUrlSource(this.url,this.config.sourceInfoStorage,this.config.headerInjector);
                FileCache fileCache = new FileCache(this.config.generateCacheFile(this.url),this.config.diskUsage);
                this.httpProxyCache = new HttpProxyCache(httpUrlSource,fileCache);
                this.httpProxyCache.registerCacheListener(this.cacheListener);
            }
    }



    private static final class UIHandler extends Handler implements CacheListener{

        private final String url;
        private final List<CacheListener> listeners;

        public UIHandler(String url,List<CacheListener> listeners){
            super(Looper.getMainLooper());
            this.url = url;
            this.listeners = listeners;
        }

        @Override
        public void onCacheAvailable(File cacheFIle, String url, int percentsAvailable) {
            Message message = obtainMessage();
            message.arg1 = percentsAvailable;
            message.obj = cacheFIle;
            sendMessage(message);
        }

        @Override
        public void handleMessage(Message msg) {
            for(CacheListener cacheListener:listeners){
                cacheListener.onCacheAvailable((File)msg.obj,url,msg.arg1);
            }
        }
    }
}


