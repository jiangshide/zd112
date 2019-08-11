package com.android.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.support.annotation.RequiresApi
import com.android.network.codec.BooleanTypeAdapter
import com.android.network.interceptor.AuthInterceptor
import com.android.network.interceptor.OfflineCacheInterceptor
import com.android.network.receiver.NetStateReciver
import com.android.utils.LogUtil
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */

class NetWorkApi private constructor() {

    companion object {
        val instance: NetWorkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetWorkApi() }
    }

    var application: Application? = null
    private var netStateReciver: NetStateReciver? = null
    private var retrofit: Retrofit? = null

    fun init(application: Application) {
        this.application = application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            netStateReciver = NetStateReciver()
            val build = NetworkRequest.Builder()
            val request = build.build()
            val connectivityManager: ConnectivityManager =
                    application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(request, netStateReciver)
            }
        }

//        okhttp init
        val loggingInterceptor = HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { message -> LogUtil.i(message) }
        )
        val cache = Cache(application.cacheDir, 100 * 1024 * 1024)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor())
                .addInterceptor(OfflineCacheInterceptor())
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        val gson = GsonBuilder().registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter()).create()
        val host = BuildConfig.API_HOST
        retrofit = Retrofit.Builder()
                .baseUrl(host)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    fun <T> createService(clazz: Class<T>): T {
        return retrofit!!.create(clazz)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun registerObserver(any: Any) {
        netStateReciver!!.registerObserver(any)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun unRegisterObserver(any: Any) {
        netStateReciver!!.unRegisterObserver(any)
        val connectivityManager: ConnectivityManager =
                application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(netStateReciver)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ping(times: Int = 3, url: String): Boolean {
        return netStateReciver!!.ping(times, url)
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
                application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager == null) {
            return false
        }
        val networkInfos = connectivityManager.allNetworkInfo;
        if (null != networkInfos) {
            for (networkInfo in networkInfos) {
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }
}