package com.android.img.listener;

/**
 * created by jiangshide on 2015-09-13.
 * email:18311271399@163.com
 */
public interface IProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
