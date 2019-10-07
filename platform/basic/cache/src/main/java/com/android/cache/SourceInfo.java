package com.android.cache;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class SourceInfo {

    public final String url;
    public final long length;
    public final String mime;

    public SourceInfo(String url,long length,String mime){
        this.url = url;
        this.length = length;
        this.mime = mime;
    }

    @Override
    public String toString() {
        return "SourceInfo{" +
                "url='" + url + '\'' +
                ", length=" + length +
                ", mime='" + mime + '\'' +
                '}';
    }
}
