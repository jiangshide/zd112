package com.android.network.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
class AuthInterceptor : Interceptor {
    private val appVersionName = ""
    private val androidId = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val builder = originRequest.newBuilder()

        val token = ""
        if(!TextUtils.isEmpty(token)){
            builder.addHeader("X-Token",token)
        }
        val uid = ""
        val urlBuilder = originRequest.url().newBuilder()
        if(!TextUtils.isEmpty(uid)){
            urlBuilder.addQueryParameter("uid",uid)
        }
        urlBuilder.addQueryParameter("appVersion",appVersionName)
        urlBuilder.addQueryParameter("platform","Android")
        urlBuilder.addQueryParameter("deviceId",androidId)
        builder.url(urlBuilder.build())
        val request = builder.build()
        return chain.proceed(request)
    }
}