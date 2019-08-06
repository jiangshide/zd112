package com.android.zdannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    String listenerSetter();
    Class<?> listenerType();
    String callbackListener();
}
