package com.android.cache.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
class Files {
    static void makeDir(File directory) throws IOException {
        if (null != directory && directory.exists() && !directory.isDirectory()) {
            throw new IOException("File " + directory + " is not directory!");
        } else {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                throw new IOException(String.format("Directory %s can't be created", directory.getAbsolutePath()));
            }
        }
    }

    static List<File> getLruListFiles(File directory) {
        List<File> result = new LinkedList<>();
        File[] files = directory.listFiles();
        if (null != files) {
            result = Arrays.asList(files);
            Collections.sort(result, new LastModifiedComparator());
        }
        return result;
    }

    static void setLastModifiedNow(File file)throws IOException{
        if(null != file && file.exists()){
            long now = System.currentTimeMillis();
            boolean modified = file.setLastModified(now);
            if(!modified){
                modify(file);
            }
        }
    }

    static void modify(File file)throws IOException{
        long size = file.length();
        if(size == 0){
            recreateZeroSizeFile(file);
            return;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rwd");
        randomAccessFile.seek(size-1);
        byte lastByte = randomAccessFile.readByte();
        randomAccessFile.seek(size-1);
        randomAccessFile.write(lastByte);
        randomAccessFile.close();
    }

    private static void recreateZeroSizeFile(File file)throws IOException{
        if(null != file && (!file.delete() || !file.createNewFile())){
            throw new IOException("Error recreate zero-size file:"+file);
        }
    }

    private static final class LastModifiedComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            long first = o1.lastModified();
            long second = o2.lastModified();
            return (first < second) ? -1 : ((first == second) ? 0 : 1);
        }
    }
}
