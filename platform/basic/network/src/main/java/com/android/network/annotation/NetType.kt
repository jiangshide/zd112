package com.android.network.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class NetType(@INetType val netType: Int = INetType.AUTO)