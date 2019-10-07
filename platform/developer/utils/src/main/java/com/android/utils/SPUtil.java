package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2014-12-11.
 * email:18311271399@163.com
 */
public final class SPUtil {

  public static SharedPreferences.Editor getEdit() {
    SharedPreferences sharedPreferences = AppUtil.getApplicationContext()
        .getSharedPreferences(AppUtil.getPackageName(), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    return editor;
  }

  public static SharedPreferences getSharedPreferences() {
    return AppUtil.getApplicationContext()
        .getSharedPreferences(AppUtil.getPackageName(), Context.MODE_PRIVATE);
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

  public static boolean put(Class clazz) {
    return putString(clazz.getCanonicalName(), JsonUtil.toJson(clazz));
  }

  public static boolean put(Class clazz, List<Class> classList) {
    return putString(clazz.getCanonicalName(), JsonUtil.toJson(classList));
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

  public static <T> T get(Class<T> clazz) {
    String json = getString(clazz.getCanonicalName());
    if (TextUtils.isEmpty(json)) return null;
    return JsonUtil.fromJson(json, clazz);
  }

  public static <T> ArrayList<T> getList(Class<T> clazz) {
    String json = getString(clazz.getCanonicalName());
    if (TextUtils.isEmpty(json)) return null;
    return (ArrayList<T>) JsonUtil.fromJsonToList(json, clazz);
  }

  public static boolean clear() {
    return getEdit().clear().commit();
  }

  public static boolean clear(String key) {
    return getEdit().remove(key).commit();
  }
}
