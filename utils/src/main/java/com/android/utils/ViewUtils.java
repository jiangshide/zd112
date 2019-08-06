package com.android.utils;

import android.app.Activity;
import android.view.View;

import com.android.utils.annotation.ContentView;
import com.android.utils.annotation.EventBase;
import com.android.utils.annotation.InjectView;
import com.android.utils.annotation.handler.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * created by jiangshide on 2015-05-09.
 * email:18311271399@163.com
 */
public final class ViewUtils {

    /**
     * 需要在父类注册
     *
     * @param activity
     */
    public static void inject(Activity activity) {
        injectLayout(activity);
        injectView(activity);
        injectEvents(activity);
    }

    public static void injectLayout(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
//            activity.setContentView(layoutId);
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.value();
//                activity.findViewById(viewId);
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();
                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("value");//通过注解的类型获取OnClick注解的value值
                            //执行value方法获取注解的值
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            ListenerInvocationHandler listenerInvocationHandler = new ListenerInvocationHandler(activity);
                            listenerInvocationHandler.addMethod(callBackListener, method);
                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, listenerInvocationHandler);

                            for (int viewId : viewIds) {
                                View view = activity.findViewById(viewId);
                                //获取指定方法
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                setter.invoke(view, listener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
