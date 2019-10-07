package com.android.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * created by jiangshide on 2019-08-12.
 * email:18311271399@163.com
 */
public final class ReflectUtil {
    public static Object getObject(Class clazz, Object target, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    public static Field getField(Class clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static Method getMethod(Class clazz, String name) throws Exception {
        Method method = clazz.getDeclaredMethod(name);
        return method;
    }

    public static void setField(Class clazz, Object target, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
