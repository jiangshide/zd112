package com.android.utils.annotation.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
public class ListenerInvocationHandler implements InvocationHandler {

    //拦截的对象
    private Object target;
    //拦截的方法
    private HashMap<String, Method> methodHashMap = new HashMap<>();

    public ListenerInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target != null) {
            String name = method.getName();
            method = methodHashMap.get(name);//如果本来需要执行onClick(),需要做拦截
            if (method != null) {
                if (method.getGenericParameterTypes().length == 0) {
                    return method.invoke(target);
                }
                method.setAccessible(true);
                method.invoke(target, args);
            }

        }
        return null;
    }

    /**
     * 将需要拦截的方法添加
     *
     * @param methodName 需要拦截:onClick()回调方法
     * @param method     执行自定义的方法: show()
     */
    public void addMethod(String methodName, Method method) {
        methodHashMap.put(methodName, method);
    }
}
