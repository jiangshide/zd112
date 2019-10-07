package com.android.cache;

import android.text.TextUtils;

import com.android.cache.exception.InterruptedProxyCacheException;
import com.android.cache.exception.ProxyCacheException;
import com.android.cache.headers.EmptyHeadersInjector;
import com.android.cache.headers.HeaderInjector;
import com.android.cache.sourcestorage.SourceInfoStorage;
import com.android.cache.sourcestorage.SourceInfoStorageFactory;
import com.android.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import static com.android.cache.Preconditions.checkNotNull;
import static com.android.cache.ProxyCacheUtils.DEFAULT_BUFFER_SIZE;
import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
public class HttpUrlSource implements Source {

    private static final int MAX_REDIRECTS = 5;
    private final SourceInfoStorage sourceInfoStorage;
    private final HeaderInjector headerInjector;
    private SourceInfo sourceInfo;
    private HttpURLConnection httpURLConnection;
    private InputStream inputStream;

    public HttpUrlSource(String url) {
        this(url, SourceInfoStorageFactory.newEmptySourceInfoStorage());
    }

    public HttpUrlSource(String url, SourceInfoStorage sourceInfoStorage) {
        this(url, sourceInfoStorage, new EmptyHeadersInjector());
    }

    public HttpUrlSource(String url, SourceInfoStorage sourceInfoStorage, HeaderInjector headerInjector) {
        this.sourceInfoStorage = checkNotNull(sourceInfoStorage);
        this.headerInjector = checkNotNull(headerInjector);
        SourceInfo sourceInfo = sourceInfoStorage.get(url);
        this.sourceInfo = sourceInfo != null ? sourceInfo : new SourceInfo(url, Integer.MIN_VALUE, ProxyCacheUtils.getSupposablyMime(url));
    }

    public HttpUrlSource(HttpUrlSource httpUrlSource) {
        this.sourceInfo = httpUrlSource.sourceInfo;
        this.sourceInfoStorage = httpUrlSource.sourceInfoStorage;
        this.headerInjector = httpUrlSource.headerInjector;
    }

    @Override
    public void open(long offset) throws ProxyCacheException {
        try {
            httpURLConnection = openConnection(offset, -1);
            String mime = httpURLConnection.getContentType();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream(), DEFAULT_BUFFER_SIZE);
            long length = readSourceAvailableBytes(httpURLConnection, offset, httpURLConnection.getResponseCode());
            this.sourceInfo = new SourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
        } catch (IOException e) {
            throw new ProxyCacheException("Error opening connection for " + sourceInfo.url + " with offset " + offset, e);
        }
    }

    private long readSourceAvailableBytes(HttpURLConnection httpURLConnection, long offset, int responseCode) throws IOException {
        long contentLength = getContentLength(httpURLConnection);
        return responseCode == HTTP_OK ? contentLength : responseCode == HTTP_PARTIAL ? contentLength + offset : sourceInfo.length;
    }

    @Override
    public synchronized long length() throws ProxyCacheException {
        if (sourceInfo.length == Integer.MIN_VALUE) {
            fetchContentInfo();
        }
        return sourceInfo.length;
    }

    private void fetchContentInfo() throws ProxyCacheException {
        LogUtil.d("Read content info from " + sourceInfo.url);
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = openConnection(0, 10 * 1000);
            long length = getContentLength(httpURLConnection);
            String mime = httpURLConnection.getContentType();
            inputStream = httpURLConnection.getInputStream();
            this.sourceInfo = new SourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
            LogUtil.d("Source info fetched:" + sourceInfo);
        } catch (IOException e) {
            LogUtil.e("Error fetching info from " + sourceInfo.url, e);
        } finally {
            ProxyCacheUtils.close(inputStream);
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private long getContentLength(HttpURLConnection httpURLConnection) {
        String contentLengthValue = httpURLConnection.getHeaderField("Content-Length");
        return contentLengthValue == null ? -1 : Long.parseLong(contentLengthValue);
    }

    private HttpURLConnection openConnection(long offset, int timeout) throws IOException {
        HttpURLConnection httpURLConnection;
        boolean redirected;
        int redirectCount = 0;
        String url = this.sourceInfo.url;
        do {
            LogUtil.d("Open connection " + (offset > 0 ? " with offset " + offset : "") + " to " + url);
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            injectCustomHeaders(httpURLConnection, url);
            if (offset > 0) {
                httpURLConnection.setRequestProperty("Range", "bytes=" + offset + "-");
            }
            if (timeout > 0) {
                httpURLConnection.setConnectTimeout(timeout);
                httpURLConnection.setReadTimeout(timeout);
            }
            int code = httpURLConnection.getResponseCode();
            redirected = code == HTTP_MOVED_PERM || code == HTTP_MOVED_TEMP || code == HTTP_SEE_OTHER;
            if (redirected) {
                url = httpURLConnection.getHeaderField("Location");
                redirectCount++;
                httpURLConnection.disconnect();
            }
            if (redirectCount > MAX_REDIRECTS) {
                throw new ProtocolException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return httpURLConnection;
    }

    private void injectCustomHeaders(HttpURLConnection httpURLConnection, String url) {
        Map<String, String> extraHeaders = headerInjector.addHeaders(url);
        for (Map.Entry<String, String> header : extraHeaders.entrySet()) {
            httpURLConnection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    @Override
    public int read(byte[] buffer) throws ProxyCacheException {
        if (inputStream == null) {
            throw new ProxyCacheException("Error reading data from " + sourceInfo.url + ":connection is abent!");
        }
        try {
            return inputStream.read(buffer, 0, buffer.length);
        } catch (InterruptedIOException e) {
            throw new InterruptedProxyCacheException("Reading source " + sourceInfo.url + " is interrupted", e);
        } catch (IOException e) {
            throw new ProxyCacheException("Error reading data from " + sourceInfo.url, e);
        }
    }

    public synchronized String getMime() throws ProxyCacheException {
        if (TextUtils.isEmpty(sourceInfo.mime)) {
            fetchContentInfo();
        }
        return sourceInfo.mime;
    }

    public String getUrl() {
        return sourceInfo.url;
    }

    @Override
    public void close() throws ProxyCacheException {
        if (httpURLConnection != null) {
            try {
                httpURLConnection.disconnect();
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }
}
