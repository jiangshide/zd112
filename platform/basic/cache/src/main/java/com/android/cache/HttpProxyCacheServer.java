package com.android.cache;

import android.content.Context;
import android.net.Uri;

import com.android.cache.exception.ProxyCacheException;
import com.android.cache.file.DiskUsage;
import com.android.cache.file.FileNameGenerator;
import com.android.cache.file.Md5FileNameGenerator;
import com.android.cache.file.TotalCountLruDiskUsage;
import com.android.cache.file.TotalSizeLruDiskUsage;
import com.android.cache.headers.EmptyHeadersInjector;
import com.android.cache.headers.HeaderInjector;
import com.android.cache.sourcestorage.SourceInfoStorage;
import com.android.cache.sourcestorage.SourceInfoStorageFactory;
import com.android.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.android.cache.Preconditions.checkAllNotNull;
import static com.android.cache.Preconditions.checkNotNull;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class HttpProxyCacheServer {

    private static final String PROXY_HOST = "127.0.0.1";

    private final Object clientLock = new Object();
    private final ExecutorService socketProcessor = Executors.newFixedThreadPool(8);
    private final Map<String,HttpProxyCacheServerClient> clientMap = new ConcurrentHashMap<>();
    private final ServerSocket serverSocket;
    private final int port;
    private final Thread waitConnectionThread;
    private final Config config;
    private final Pinger pinger;

    public HttpProxyCacheServer(Context context){
        this(new Builder(context).buildConfig());
    }

    private HttpProxyCacheServer(Config config){
        this.config = checkNotNull(config);
        try {
            InetAddress inetAddress = InetAddress.getByName(PROXY_HOST);
            this.serverSocket = new ServerSocket(0,8,inetAddress);
            this.port = serverSocket.getLocalPort();
            IgnoreHostProxySelector.install(PROXY_HOST,port);
            CountDownLatch startSignal = new CountDownLatch(1);
            this.waitConnectionThread = new Thread(new WaitRequestsRunnable((startSignal)));
            this.waitConnectionThread.start();
            startSignal.await();
            this.pinger = new Pinger(PROXY_HOST,port);
            LogUtil.e("Proxy cache server started.Is it alive? "+isAlive());
        }catch (IOException|InterruptedException e){
            socketProcessor.shutdown();
            throw new IllegalStateException("Error starting local proxy server",e);
        }
    }

    public String getProxyUrl(String url){
        return getProxyUrl(url,true);
    }

    public String getProxyUrl(String url,boolean allowCacheFileUri){
        if(allowCacheFileUri && isCached(url)){
            File cacheFile = getCacheFile(url);
            touchFileSafely(cacheFile);
            return Uri.fromFile(cacheFile).toString();
        }
        return isAlive() ? appendToProxyUrl(url):url;
    }

    public boolean isCached(String url){
        checkNotNull(url,"Url can't be null!");
        return getCacheFile(url).exists();
    }

    public void shutdown(){
        LogUtil.i("Shutdown proxy server");
        shutdownClients();
        config.sourceInfoStorage.release();
        waitConnectionThread.interrupt();
        try {
            if(!serverSocket.isClosed()){
                serverSocket.close();
            }
        }catch (IOException e){
            onError(new ProxyCacheException("Error shutting down proxy server ",e));
        }
    }

    private void shutdownClients(){
        synchronized (clientLock){
            for (HttpProxyCacheServerClient client:clientMap.values()){
                client.shutdown();
            }
            clientMap.clear();
        }
    }

    private String appendToProxyUrl(String url){
        return String.format(Locale.US,"http://%s:%d/%s",PROXY_HOST,port,ProxyCacheUtils.encode(url));
    }

    private void touchFileSafely(File cacheFile){
        try {
            config.diskUsage.touch(cacheFile);
        }catch (IOException e){
            LogUtil.e("Error touching file "+cacheFile,e);
        }
    }

    private File getCacheFile(String url){
        File cacheDir = config.cacheRoot;
        String fileName = config.fileNameGenerator.generate(url);
        return new File(cacheDir,fileName);
    }

    public void registerCacheListener(CacheListener cacheListener,String url){
        checkNotNull(cacheListener,url);
        synchronized (clientLock){
            try {
                getClients(url).registerCacheListener(cacheListener);
            }catch (ProxyCacheException e){
                LogUtil.w("Error registering cache listener ",e);
            }
        }
    }

    public void unregisterCacheListener(CacheListener cacheListener){
        checkNotNull(cacheListener);
        synchronized (clientLock){
            for (HttpProxyCacheServerClient client:clientMap.values()){
                client.unregisterCacheListener(cacheListener);
            }
        }
    }

    public void unregisterCacheListener(CacheListener cacheListener,String url){
        checkAllNotNull(cacheListener,url);
        synchronized (clientLock){
            try {
                getClients(url).unregisterCacheListener(cacheListener);
            }catch (ProxyCacheException e){
                LogUtil.w("Error registering cache listener ",e);
            }
        }
    }

    private boolean isAlive(){
        return pinger.ping(3,70);//70+140+280=max~500mx
    }

    private final class WaitRequestsRunnable implements Runnable{
        private final CountDownLatch countDownLatch;
        public WaitRequestsRunnable(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            countDownLatch.countDown();
            waitForRequest();
        }
    }

    private void waitForRequest(){
        try {
            while (!Thread.currentThread().isInterrupted()){
                Socket socket = serverSocket.accept();
                LogUtil.d("Accept new socket "+socket);
                socketProcessor.submit(new SocketProcessorRunnable(socket));
            }
        }catch (IOException e){
            onError(new ProxyCacheException("Error during waiting connection",e));
        }
    }

    private final class SocketProcessorRunnable implements Runnable{
        private final Socket socket;

        public SocketProcessorRunnable(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            processSocket(socket);
        }
    }

    private void processSocket(Socket socket){
        try {
            GetRequest getRequest = GetRequest.read(socket.getInputStream());
            LogUtil.d("Request to cache proxy:"+getRequest);
            String url = ProxyCacheUtils.decode(getRequest.uri);
            if(pinger.isPingRequest(url)){
                pinger.responseToPing(socket);
            }else{
                HttpProxyCacheServerClient httpProxyCacheServerClient = getClients(url);
                httpProxyCacheServerClient.processRequest(getRequest,socket);
            }
        }catch (SocketException e){
            LogUtil.d("Closing socket... Socket is closed by client.");
        }catch (ProxyCacheException | IOException e){
            onError(new ProxyCacheException("Error processing request",e));
        }finally {
            releaseSocket(socket);
            LogUtil.d("Opened connections:"+getClientsCount());
        }
    }

    private int getClientsCount(){
        synchronized (clientLock){
            int count = 0;
            for(HttpProxyCacheServerClient client:clientMap.values()){
                count += client.getClientCount();
            }
            return count;
        }
    }

    private void releaseSocket(Socket socket){
        closeSocketInput(socket);
        closeSocketOutput(socket);
        closeSocket(socket);
    }

    private void closeSocket(Socket socket){
        try {
            if(!socket.isClosed()){
                socket.close();
            }
        }catch (IOException e){
            onError(new ProxyCacheException("Error closing socket",e));
        }
    }

    private void closeSocketOutput(Socket socket){
        try {
            if(!socket.isOutputShutdown()){
                socket.shutdownOutput();
            }
        }catch (IOException e){
            LogUtil.w("Failed to close socket on proxy side:{},It seems client have already closed connection.",e.getMessage());
        }
    }

    private void closeSocketInput(Socket socket){
        try {
            if (!socket.isInputShutdown()) {
                socket.shutdownInput();
            }
        }catch (SocketException e){
            LogUtil.d("Releasing input stream... Socket is closed by client.");
        }catch (IOException e){
            onError(new ProxyCacheException("Error closing socket input stream",e));
        }
    }

    private HttpProxyCacheServerClient getClients(String url)throws ProxyCacheException{
        synchronized (clientLock){
            HttpProxyCacheServerClient client = clientMap.get(url);
            if(client == null){
                client = new HttpProxyCacheServerClient(url,config);
                clientMap.put(url,client);
            }
            return client;
        }
    }

    private void onError(Throwable throwable){
        LogUtil.e("HttpProxyCacheServer error",throwable);
    }

    public static final class Builder {
        private static final long DEFAULT_MAX_SIZE = 512*1024*1024;
        private File cacheRoot;
        private FileNameGenerator fileNameGenerator;
        private DiskUsage diskUsage;
        private SourceInfoStorage sourceInfoStorage;
        private HeaderInjector headerInjector;

        public Builder(Context context){
            this.sourceInfoStorage = SourceInfoStorageFactory.newSourceInfoStorage(context);
            this.cacheRoot = StorageUitls.getIndividualCacheDirectory(context);
            this.diskUsage = new TotalSizeLruDiskUsage(DEFAULT_MAX_SIZE);
            this.fileNameGenerator = new Md5FileNameGenerator();
            this.headerInjector = new EmptyHeadersInjector();
        }

        public Builder cacheDirectory(File file){
            this.cacheRoot = checkNotNull(file);
            return this;
        }

        public Builder fileNameGenerator(FileNameGenerator fileNameGenerator){
            this.fileNameGenerator = checkNotNull(fileNameGenerator);
            return this;
        }

        public Builder maxCacheSize(long maxSize){
            this.diskUsage = new TotalSizeLruDiskUsage(maxSize);
            return this;
        }

        public Builder maxCacheFilesCount(int count){
            this.diskUsage = new TotalCountLruDiskUsage(count);
            return this;
        }

        public Builder diskUsage(DiskUsage diskUsage){
            this.diskUsage = checkNotNull(diskUsage);
            return this;
        }

        public Builder headerInjector(HeaderInjector headerInjector){
            this.headerInjector = checkNotNull(headerInjector);
            return this;
        }

        public HttpProxyCacheServer build(){
            Config config = buildConfig();
            return new HttpProxyCacheServer(config);
        }

        private Config buildConfig(){
            return new Config(cacheRoot,fileNameGenerator,diskUsage,sourceInfoStorage,headerInjector);
        }
    }
}
