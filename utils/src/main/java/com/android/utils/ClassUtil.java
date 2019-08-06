package com.android.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public final class ClassUtil {

    public static List<String> getClassName(Context context, String pkgName) {
        List<String> classList = new ArrayList<>();
        String path = null;
        try {
            path = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            DexFile dexFile = new DexFile(path);
            Enumeration enumeration = dexFile.entries();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement().toString();
                if (name.contains(pkgName)) {
                    classList.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }
}
