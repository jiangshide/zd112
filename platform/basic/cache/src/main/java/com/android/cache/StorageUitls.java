package com.android.cache;

import android.content.Context;
import android.os.Environment;

import com.android.utils.LogUtil;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * created by jiangshide on 2019-06-15.
 * email:18311271399@163.com
 */
final class StorageUitls {
    protected static final String INDIVIDUAL_DIR_NAME = "video-cache";

    public static File getIndividualCacheDirectory(Context context){
        File cacheDir = getCacheDirectory(context,true);
        return new File(cacheDir,INDIVIDUAL_DIR_NAME);
    }

    private static File getCacheDirectory(Context context,boolean preferExternal){
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        }catch (NullPointerException e){
            externalStorageState = "";
        }
        if(preferExternal && MEDIA_MOUNTED.equals(externalStorageState)){
            appCacheDir = getExternalCacheDir(context);
        }
        if(appCacheDir == null){
            appCacheDir = context.getCacheDir();
        }
        if(appCacheDir == null){
            String cacheDirPath = "/data/data/"+context.getPackageName()+"/cache/";
            LogUtil.w("Can't define system cache directory! '"+cacheDirPath+"%s' will be used.");
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context){
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(),"Android"),"Data");
        File appCacheDir = new File(new File(dataDir,context.getPackageName()),"cache");
        if(!appCacheDir.exists()){
            if(!appCacheDir.mkdir()){
                LogUtil.w("Unable to create external cache directory");
                return null;
            }
        }
        return appCacheDir;
    }
}
