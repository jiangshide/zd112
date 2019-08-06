package com.android.utils.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by jiangshide on 2019-07-23.
 * email:18311271399@163.com
 */
@Target(ElementType.TYPE)//作用类上
@Retention(RetentionPolicy.RUNTIME)//运行是通过反射获取
public @interface ContentView {
    int value();
}
