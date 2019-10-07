package com.android.cache;

import android.text.TextUtils;

import com.android.cache.exception.ProxyCacheException;
import com.android.cache.file.FileCache;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Locale;

import static com.android.cache.ProxyCacheUtils.DEFAULT_BUFFER_SIZE;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
class HttpProxyCache extends ProxyCache {

    private static final float NO_CACHE_BARRIER = .2f;

    private final HttpUrlSource httpUrlSource;
    private final FileCache fileCache;
    private CacheListener cacheListener;

    public HttpProxyCache(HttpUrlSource httpUrlSource, FileCache fileCache) {
        super(httpUrlSource, fileCache);
        this.httpUrlSource = httpUrlSource;
        this.fileCache = fileCache;
    }

    public void registerCacheListener(CacheListener cacheListener) {
        this.cacheListener = cacheListener;
    }

    public void processRequest(GetRequest getRequest, Socket socket) throws IOException, ProxyCacheException {
        OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
        String responseHeaders = newResponseHeaders(getRequest);
        outputStream.write(responseHeaders.getBytes("UTF-8"));
        long offset = getRequest.rangeOffset;
        if (isUseCache(getRequest)) {
            responseWithCache(outputStream, offset);
        } else {
            responseWithoutCache(outputStream, offset);
        }
    }

    private void responseWithoutCache(OutputStream outputStream, long offset) throws IOException, ProxyCacheException {
        HttpUrlSource httpUrlSource = new HttpUrlSource(this.httpUrlSource);
        try {
            httpUrlSource.open((int) offset);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = httpUrlSource.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readBytes);
                offset += readBytes;
            }
            outputStream.flush();
        } finally {
            httpUrlSource.close();
        }
    }

    private void responseWithCache(OutputStream outputStream, long offset) throws IOException, ProxyCacheException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int readBytes;
        while ((readBytes = read(buffer, offset, buffer.length)) != -1) {
            outputStream.write(buffer, 0, readBytes);
            offset += readBytes;
        }
        outputStream.flush();
    }

    private boolean isUseCache(GetRequest getRequest) throws ProxyCacheException {
        long sourceLength = httpUrlSource.length();
        boolean sourceLengthKnown = sourceLength > 0;
        long cacheAvailable = fileCache.available();
        return !sourceLengthKnown || !getRequest.partial || getRequest.rangeOffset <= cacheAvailable + sourceLength * NO_CACHE_BARRIER;
    }

    private String newResponseHeaders(GetRequest getRequest) throws IOException, ProxyCacheException {
        String mime = httpUrlSource.getMime();
        boolean mimeKnown = !TextUtils.isEmpty(mime);
        long length = fileCache.isCompleted() ? fileCache.available() : httpUrlSource.length();
        boolean lengthKnown = length >= 0;
        long contentLength = getRequest.partial ? length - getRequest.rangeOffset : length;
        boolean addRange = lengthKnown && getRequest.partial;
        return new StringBuilder()
                .append(getRequest.partial ? "HTTP/1.1 206 PARTIAL CONTENT\n" : "HTTP/1.1 200 ok\n")
                .append("Accept-Ranges:bytes\n")
                .append(lengthKnown ? format("Content-Length:%d\n", contentLength) : "")
                .append(addRange ? format("Content-Range:bytes %d-%d/%d\n", getRequest.rangeOffset, length - 1, length) : "")
                .append(mimeKnown ? format("Content-Type:%s\n", mime) : "")
                .append("\n")
                .toString();
    }

    private String format(String pattern, Object... args) {
        return String.format(Locale.US, pattern, args);
    }

    @Override
    protected void onCachePercentsAvailableChanged(int percentsAvailable) {
        if (cacheListener != null) {
            cacheListener.onCacheAvailable(fileCache.file, httpUrlSource.getUrl(), percentsAvailable);
        }
    }
}
