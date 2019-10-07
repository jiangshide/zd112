package com.android.cache;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.android.utils.LogUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.android.cache.Preconditions.checkArgument;
import static com.android.cache.Preconditions.checkNotNull;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class ProxyCacheUtils {

    static final int DEFAULT_BUFFER_SIZE = 8*1024;
    static final int MAX_ARRAY_PREVIEW = 16;

    static String getSupposablyMime(String url){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return TextUtils.isEmpty(extension)?null:mimeTypeMap.getMimeTypeFromExtension(extension);
    }

    static void assertBuffer(byte[] buffer,long offset,int length){
        checkNotNull(buffer,"Buffer must be not null!");
        checkArgument(offset >= 0,"Data offset must be positive!");
        checkArgument(length >= 0 && length <= buffer.length,"Length must be in range [0...buffer.lengt]");
    }

    static String preview(byte[] data,int length){
        int previewLength = Math.min(MAX_ARRAY_PREVIEW,Math.max(length,0));
        byte[] dataRange = Arrays.copyOfRange(data,0,previewLength);
        String preview = Arrays.toString(dataRange);
        if(previewLength < length){
            preview = preview.substring(0,preview.length()-1)+",...]";
        }
        return preview;
    }

    static String encode(String url){
        try {
            return URLEncoder.encode(url,"utf-8");
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException("Error encoding url",e);
        }
    }

    static String decode(String url){
        try {
            return URLDecoder.decode(url,"utf-8");
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException("Error decoding url",e);
        }
    }

    static void close(Closeable closeable){
        if(null != closeable){
            try {
                closeable.close();
            }catch (IOException e){
                LogUtil.e("Error closing resource",e);
            }
        }
    }

    public static String computeMD5(String str){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digestBytes = messageDigest.digest(str.getBytes());
            return bytesToHexString(digestBytes);
        }catch (NoSuchAlgorithmException e){
            throw new IllegalStateException(e);
        }
    }

    private static String bytesToHexString(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        for(byte b:bytes){
            stringBuffer.append(String.format("%02x",b));
        }
        return stringBuffer.toString();
    }
}
