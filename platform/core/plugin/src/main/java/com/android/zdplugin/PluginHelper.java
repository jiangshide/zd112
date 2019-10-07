package com.android.zdplugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.android.utils.LogUtil;
import com.android.utils.ReflectUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * created by jiangshide on 2019-08-12.
 * email:18311271399@163.com
 */
public class PluginHelper {

    private static final String CLASS_DEX_PATH_LIST = "dalvik.system.DexPathList";
    private static final String FIELD_PATH_LIST = "pathList";
    private static final String FIELD_DEX_ELEMENTS = "dexElements";

    private static Resources mResource;

    public static void loadPlugin(Context context, ClassLoader classLoader) throws Exception {
        loadClass(context, classLoader);
        initResource(context);
    }

    private static void loadClass(Context context, ClassLoader classLoader) throws Exception {
        File file = context.getExternalFilesDir("plugin");
        LogUtil.e("------file:", file.getAbsolutePath());
        if (file == null || !file.exists() || file.listFiles().length == 0) {
            return;
        }

        file = file.listFiles()[0];
        DexClassLoader pluginDexClassLoader = new DexClassLoader(file.getAbsolutePath(), null, null, classLoader);
        Object pluginDexPathList = ReflectUtil.getObject(BaseDexClassLoader.class, pluginDexClassLoader, FIELD_PATH_LIST);
        Object pluginElements = ReflectUtil.getObject(Class.forName(CLASS_DEX_PATH_LIST), pluginDexPathList, FIELD_DEX_ELEMENTS);

        Object hostDexPathList = ReflectUtil.getObject(BaseDexClassLoader.class, classLoader, FIELD_PATH_LIST);
        Object hostElements = ReflectUtil.getObject(Class.forName(CLASS_DEX_PATH_LIST), hostDexPathList, FIELD_DEX_ELEMENTS);

        Object array = combineArray(hostElements, pluginElements);

        ReflectUtil.setField(Class.forName(CLASS_DEX_PATH_LIST), hostDexPathList, FIELD_DEX_ELEMENTS, array);
    }

    private static Object combineArray(Object hostElements, Object pluginELements) {
        Class<?> componentType = hostElements.getClass().getComponentType();
        int i = Array.getLength(hostElements);
        int j = Array.getLength(pluginELements);
        int k = i + j;
        Object result = Array.newInstance(componentType, k);
        System.arraycopy(pluginELements, 0, result, 0, j);
        System.arraycopy(hostElements, 0, result, j, i);
        return result;
    }

    public static void initResource(Context context) throws Exception {
        Class<AssetManager> clazz = AssetManager.class;
        AssetManager assetManager = clazz.newInstance();
        Method method = clazz.getMethod("addAssetPath", String.class);
        method.invoke(assetManager, context.getExternalFilesDir("plugin").listFiles()[0].getAbsolutePath());
        mResource = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
    }

    public static Resources getResource() {
        return mResource;
    }
}
