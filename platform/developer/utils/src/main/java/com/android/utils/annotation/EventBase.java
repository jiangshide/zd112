package com.android.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
@Target(ElementType.ANNOTATION_TYPE)//该注解只能作用在注解之上
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    //1 setXXXListener();
    String listenerSetter();

    //2 View.OnxxxListener
    Class<?> listenerType();

    //3 回调
    String callBackListener();
}
