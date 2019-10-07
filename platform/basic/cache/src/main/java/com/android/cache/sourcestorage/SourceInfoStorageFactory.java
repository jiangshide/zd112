package com.android.cache.sourcestorage;

import android.content.Context;

/**
 * created by jiangshide on 2019-06-14.
 * email:18311271399@163.com
 */
public class SourceInfoStorageFactory {
    public static SourceInfoStorage newSourceInfoStorage(Context context){
        return new DatabaseSourceInfoStorage(context);
    }

    public static SourceInfoStorage newEmptySourceInfoStorage(){
        return new NoSourceInfoStorage();
    }
}
