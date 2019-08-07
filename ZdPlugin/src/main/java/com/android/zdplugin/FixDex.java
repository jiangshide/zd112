package com.android.zdplugin;

import android.content.Context;
import android.text.TextUtils;

import com.android.utils.StringUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 实现热修复
 * created by jiangshide on 2019-08-08.
 * email:18311271399@163.com
 */
class FixDex {
    /**
     * @param context
     */
    public void fixDex(Context context) {
        Iterator<Map.Entry<File, HashSet<File>>> iterator = getDexsFile(context).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<File, HashSet<File>> entry = iterator.next();
            Object pathListObject = null;
            for (File file : entry.getValue()) {
                Object systemElements = null;
                HashMap<?, ?> systemElementsHash = getElements(context);
                if (systemElementsHash != null && systemElementsHash.size() > 0) {
                    Map.Entry<?, ?> systemEntry = systemElementsHash.entrySet().iterator().next();
                    pathListObject = systemEntry.getKey();
                    systemElements = systemEntry.getValue();
                }

                Object appElements = null;
                HashMap<?, ?> appElementsHash = getElements(context, file.getAbsolutePath(), entry.getKey().getAbsolutePath());
                if (appElementsHash != null && appElementsHash.size() > 0) {
                    appElements = appElementsHash.entrySet().iterator().next().getValue();
                }
                if (systemElements != null && appElements != null) {
                    Class<?> componentType = systemElements.getClass().getComponentType();
                    int appElementsLength = Array.getLength(appElements);
                    int newElementsLenth = Array.getLength(systemElements) + appElementsLength;
                    Object newElementArr = Array.newInstance(componentType, newElementsLenth);
                    for (int i = 0; i < newElementsLenth; i++) {
                        if (i < appElementsLength) {
                            Array.set(newElementArr, i, Array.get(appElements, i));
                        } else {
                            Array.set(newElementArr, i, Array.get(systemElements, i - appElementsLength));
                        }
                    }
                    try {
                        Field elementsField = pathListObject.getClass().getDeclaredField("dexElements");
                        elementsField.setAccessible(true);
                        elementsField.set(pathListObject, newElementArr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param context
     * @return
     */
    public HashMap<File, HashSet<File>> getDexsFile(Context context) {
        HashMap<File, HashSet<File>> hashMap = new HashMap<>();
        HashSet<File> loadedDex = new HashSet<>();
        File filesDir = context.getDir("odex", Context.MODE_PRIVATE);
        File[] files = filesDir.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".dex") | fileName.startsWith("classses")) {
                loadedDex.add(file);
            }
        }
        File fileOpimize = new File(StringUtil.getAppend(filesDir.getAbsolutePath(), File.separator, "opt_dex"));
        if (!fileOpimize.exists()) fileOpimize.mkdirs();
        hashMap.put(fileOpimize, loadedDex);
        return hashMap;
    }

    /**
     * @param context
     * @return
     */
    public HashMap<?, ?> getElements(Context context) {
        return this.getElements(context, null, null);
    }

    /**
     * 获取所有dexElements
     *
     * @param context
     * @param filePath
     * @param optFilePath
     * @return
     */
    public HashMap<?, ?> getElements(Context context, String filePath, String optFilePath) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        try {
            Class<?> baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = baseDexClassLoader.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathListObject = null;
            if (!TextUtils.isEmpty(filePath)) {
                pathListObject = pathListField.get(new DexClassLoader(filePath, optFilePath, null, context.getClassLoader()));
            } else {
                pathListObject = pathListField.get((PathClassLoader) context.getClassLoader());
            }
            Field dexElementsField = pathListObject.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            hashMap.put(pathListObject, dexElementsField.get(pathListObject));
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
