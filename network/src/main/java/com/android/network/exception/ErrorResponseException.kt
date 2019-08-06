package com.android.network.exception

import java.lang.RuntimeException

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
class ErrorResponseException(val msg:String,val code:Int): RuntimeException(){
}