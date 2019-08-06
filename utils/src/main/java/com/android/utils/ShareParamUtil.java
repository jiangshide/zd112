package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * created by jiangshide on 2014-12-11.
 * email:18311271399@163.com
 */
public final class ShareParamUtil {

    public static SharedPreferences.Editor getEdit() {
        SharedPreferences sharedPreferences = AppUtil.getApplicationContext().getSharedPreferences("jsd_android", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences() {
        return AppUtil.getApplicationContext().getSharedPreferences("jsd_android", Context.MODE_PRIVATE);
    }

    public static boolean putBoolean(String key, boolean value) {
        return getEdit().putBoolean(key, value).commit();
    }

    public static boolean putFloat(String key, float value) {
        return getEdit().putFloat(key, value).commit();
    }

    public static boolean putInt(String key, int value) {
        return getEdit().putInt(key, value).commit();
    }

    public static boolean putLong(String key, long value) {
        return getEdit().putLong(key, value).commit();
    }

    public static boolean putString(String key, String value) {
        return getEdit().putString(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static float getFloat(String key) {
        return getSharedPreferences().getFloat(key, 0);
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public static boolean clear() {
        return getEdit().clear().commit();
    }

    public static boolean clear(String key) {
        return getEdit().remove(key).commit();
    }
}
