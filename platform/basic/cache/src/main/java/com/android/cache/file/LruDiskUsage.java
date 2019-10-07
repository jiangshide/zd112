package com.android.cache.file;

import com.android.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public abstract class LruDiskUsage implements DiskUsage{

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void touch(File file) throws IOException {
        executorService.submit(new TouchCallable(file));
    }


    private class TouchCallable implements Callable<Void>{

        private final File file;

        public TouchCallable(File file){
            this.file = file;
        }

        @Override
        public Void call() throws Exception {
            Files.setLastModifiedNow(file);
            List<File> files = Files.getLruListFiles(file.getParentFile());
            trim(files);
            return null;
        }
    }

    private void trim(List<File> files){
        long totalSize = countTotalSize(files);
        int totalCount = files.size();
        for(File file:files){
            boolean accepted = accept(file,totalSize,totalCount);
            if(!accepted){
                long fileSize = file.length();
                boolean deleted = file.delete();
                if(deleted){
                    totalCount--;
                    totalSize -= fileSize;
                    LogUtil.e("Cache file "+file+" is deleted because it exceeds cache limit");
                }else{
                    LogUtil.e("Error deleting file "+file+" for trimming cache");
                }
            }
        }
    }

    protected abstract boolean accept(File file,long totalSize,int totalCount);

    private long countTotalSize(List<File> files){
        long totalSize = 0;
        for(File file:files){
            totalSize += file.length();
        }
        return totalSize;
    }
}
