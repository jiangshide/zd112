package com.android.event;

import android.os.Handler;
import android.os.Looper;

import com.android.utils.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by jiangshide on 2019-06-18.
 * email:18311271399@163.com
 */
public class EventBus {

    private static volatile EventBus instance;

    Map<Object, List<SubscribleMethod>> cacheMap;
    private Handler mHandler;

    private EventBus() {
        cacheMap = new HashMap<>();
        mHandler = new Handler();
    }

    public static EventBus getDefault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object object) {
        List<SubscribleMethod> methodList = cacheMap.get(object);
        if (methodList == null) {
            methodList = findSubscriptMethods(object);
            cacheMap.put(object, methodList);
        }
    }

    private List<SubscribleMethod> findSubscriptMethods(Object object) {
        List<SubscribleMethod> subscribleMethods = new ArrayList<>();
        Class<?> clazz = object.getClass();
        while (null != clazz) {
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax") || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subscrible subscrible = method.getAnnotation(Subscrible.class);
                if (subscrible == null) {
                    continue;
                }
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    LogUtil.e("eventbus only accepts one params");
                }
                ThreadMode threadMode = subscrible.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method, threadMode, types[0]);
                subscribleMethods.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return subscribleMethods;
    }

    public void post(final Object object) {
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object obj = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(obj);
            for (final SubscribleMethod subscribleMethod : list) {
                if (subscribleMethod.getType().isAssignableFrom(object.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        case MAIN:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(subscribleMethod, obj, object);
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, obj, object);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:
                            invoke(subscribleMethod, obj, object);
                            break;
                    }
                    invoke(subscribleMethod, obj, object);
                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object obj, Object object) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(obj, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
