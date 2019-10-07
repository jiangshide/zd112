package com.android.cache;

import com.android.cache.exception.ProxyCacheException;
import com.android.utils.LogUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.android.cache.Preconditions.checkArgument;
import static com.android.cache.Preconditions.checkNotNull;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
class Pinger {
    private static final String PING_REQUEST = "ping";
    private static final String PING_RESPONSE = "ping ok";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String host;
    private final int port;

    Pinger(String host,int port){
        this.host = checkNotNull(host);
        this.port =port;
    }

    boolean ping(int maxAttempts,int startTimeout){
        checkArgument(maxAttempts >= 1);
        checkArgument(startTimeout >0);

        int timeout = startTimeout;
        int attempts = 0;
        while (attempts < maxAttempts){
            try {
                Future<Boolean> pingFuture = executorService.submit(new PingCallable());
                if(pingFuture.get(timeout,TimeUnit.MILLISECONDS)){
                    return true;
                }
            }catch (TimeoutException e){
                LogUtil.w("Error pinging server (attempt:"+attempts+",timeout:"+timeout+").");
            }catch (InterruptedException| ExecutionException e){
                LogUtil.e("Error pinging server due to unexpected error",e);
            }
            attempts++;
            timeout *= 2;
        }
        String error = String.format(Locale.US,"Error pinging server (attempts:%d,max timeout:%d).Default proxies are:%s",attempts,timeout/2,getDefaultProxies());
        LogUtil.e(new ProxyCacheException(error));
        return false;
    }

    private List<Proxy> getDefaultProxies(){
        try {
            ProxySelector proxySelector = ProxySelector.getDefault();
            return proxySelector.select(new URI(getPingUrl()));
        }catch (URISyntaxException e){
            throw new IllegalStateException(e);
        }
    }

    boolean isPingRequest(String request){
        return PING_REQUEST.endsWith(request);
    }

    void responseToPing(Socket socket)throws IOException{
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("HTTP/1.1 200 OK\n\n".getBytes());
        outputStream.write(PING_RESPONSE.getBytes());
    }

    private class PingCallable implements Callable<Boolean>{
        @Override
        public Boolean call() throws Exception {
            return pingServer();
        }
    }

    private boolean pingServer()throws ProxyCacheException{
        String pingUrl = getPingUrl();
        boolean pingOk = false;
        HttpUrlSource httpUrlSource = new HttpUrlSource(pingUrl);
        try {
            byte[] exectedResponse = PING_RESPONSE.getBytes();
            httpUrlSource.open(0);
            byte[] response = new byte[exectedResponse.length];
            httpUrlSource.read(response);
            pingOk = Arrays.equals(exectedResponse,response);
            LogUtil.i("Ping response:`"+new String(response)+"`,pinged? "+pingOk);
        }catch (ProxyCacheException e){
            LogUtil.e("Error reading ping response",e);
        }finally {
            httpUrlSource.close();
        }
        return pingOk;
    }

    private String getPingUrl(){
        return String.format(Locale.US,"http://%s:%d/%s",host,port,PING_REQUEST);
    }
}
