package com.android.cache.file;

import com.android.cache.Cache;
import com.android.cache.exception.ProxyCacheException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class FileCache implements Cache {

    private static final String TEMP_POSTFIX = ".download";

    private final DiskUsage diskUsage;
    public File file;
    private RandomAccessFile dataFile;

    public FileCache(File file) throws ProxyCacheException {
        this(file, new UnlimitedDiskUsage());
    }

    public FileCache(File file, DiskUsage diskUsage) throws ProxyCacheException {
        try {
            if (null == diskUsage) {
                throw new NullPointerException();
            }
            this.diskUsage = diskUsage;
            File directory = file.getParentFile();
            Files.makeDir(directory);
            boolean completed = file.exists();
            this.file = completed ? file : new File(file.getParentFile(), file.getName() + TEMP_POSTFIX);
            this.dataFile = new RandomAccessFile(this.file, completed ? "r" : "rw");
        } catch (IOException e) {
            throw new ProxyCacheException("Error using file " + file + "as disk cache:", e);
        }
    }

    @Override
    public synchronized long available() throws ProxyCacheException {
        try {
            return (int) this.dataFile.length();
        } catch (IOException e) {
            throw new ProxyCacheException("Error reading length of file:" + file, e);
        }
    }

    @Override
    public synchronized int read(byte[] buffer, long offset, int length) throws ProxyCacheException {
        try {
            this.dataFile.seek(offset);
            return this.dataFile.read(buffer,0,length);
        }catch (IOException e){
            String format = "Error reading %d bytes with offset %d from file[%d bytes] to buffer[%d bytes]";
            throw new ProxyCacheException(String.format(format,length,offset,available(),buffer.length));
        }
    }

    @Override
    public synchronized void append(byte[] data, int length) throws ProxyCacheException {
        try {
            if(isCompleted()){
                throw new ProxyCacheException("Error append cache:cache file:"+file+" is completed!");
            }
            this.dataFile.seek(available());
            this.dataFile.write(data,0,length);
        }catch (IOException e){
            String format = "Error writing %d bytes to %s from buffer with size %d";
            throw new ProxyCacheException(String.format(format,length,dataFile,data.length,e));
        }
    }

    @Override
    public synchronized void close() throws ProxyCacheException {
        try {
            this.dataFile.close();
            this.diskUsage.touch(file);
        }catch (IOException e){
            throw new ProxyCacheException("Error closeing file:"+file,e);
        }
    }

    @Override
    public synchronized void complete() throws ProxyCacheException {
        if(isCompleted()){
            return;
        }
        this.close();
        String fileName = file.getName().substring(0,file.getName().length()-TEMP_POSTFIX.length());
        File completedFile = new File(file.getParentFile(),fileName);
        boolean reName = file.renameTo(completedFile);
        if(!reName){
            throw new ProxyCacheException("Error renaming file:"+file+" to "+completedFile+" for complation!");
        }
        file = completedFile;
        try {
            this.dataFile = new RandomAccessFile(file,"r");
            diskUsage.touch(file);
        }catch (IOException e){
            throw new ProxyCacheException("Error opening:"+file+" as disk cache ",e);
        }
    }

    @Override
    public synchronized boolean isCompleted() {
        return !isTempFile(file);
    }

    public File getFile(){
        return file;
    }

    private boolean isTempFile(File file){
        return file.getName().endsWith(TEMP_POSTFIX);
    }
}
