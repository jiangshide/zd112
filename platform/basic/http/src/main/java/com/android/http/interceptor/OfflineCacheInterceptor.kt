package com.android.http.interceptor

import com.android.network.NetWork
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
class OfflineCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        if(!NetWork.instance.isNetworkAvailable()){
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            response = chain.proceed(request)
        }else{
            response = chain.proceed(request)
            response.newBuilder().removeHeader("Pragma").header(
                    "Cache-Control",
                    "public,only-if-cached,max-stale="+60*60*24*30).build()
        }
        return response
    }
}