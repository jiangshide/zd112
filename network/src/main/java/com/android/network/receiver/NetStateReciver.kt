package com.android.network.receiver

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.support.annotation.RequiresApi
import com.android.network.listener.NetType
import com.android.network.listener.NetType.*
import com.android.network.listener.NetWork
import com.android.network.vm.data.NetTypedData
import com.android.utils.LogUtil
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class NetStateReciver : ConnectivityManager.NetworkCallback() {

    private var networkList: MutableMap<Any, List<NetTypedData>>? = null

    init {
        networkList = HashMap()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        post(AVAILABLE)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        post(NONE)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                post(CELLULAR)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                post(WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                post(BLUETOOTH)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                post(ETHERNET)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                post(VPN)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                post(WIFI_AWARE)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                post(LOWPAN)
            } else {
                post(NONE)
            }
        } else {
            post(NONE)
        }
    }

    private fun post(@NetType netType: Int) {
        val set = networkList!!.keys
        for (getter in set) {
            val methodManagers = networkList!![getter]
            if (methodManagers != null) {
                for (methodManager in methodManagers) {
                    if (methodManager.type.isAssignableFrom(NetType::class.java)) {
                        when (methodManager.netType) {
                            AUTO -> invoke(methodManager, getter, netType)
                            WIFI -> if (netType == WIFI || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                            CELLULAR -> if (netType == CELLULAR || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                            BLUETOOTH -> if (netType == BLUETOOTH || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                            ETHERNET -> if (netType == ETHERNET || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                            VPN -> if (netType == VPN || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                            WIFI_AWARE -> if (netType == WIFI_AWARE || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                            LOWPAN -> if (netType == LOWPAN || netType == NONE) {
                                invoke(methodManager, getter, netType)
                            }
                        }
                    }
                }
            }
        }
    }

    private operator fun invoke(methodManager: NetTypedData, getter: Any, @NetType netType: Int) {
        try {
            methodManager.method.invoke(getter, netType)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }

    fun registerObserver(register: Any) {
        var methodManagers = networkList!![register]
        if (methodManagers == null) {
            methodManagers = findAnnotationMethod(register)
            networkList!![register] = methodManagers
        }
    }

    fun unRegisterObserver(register: Any) {
        if (!networkList!!.isEmpty()) {
            networkList!!.remove(register)
        }
        if (!networkList!!.isEmpty()) {
            networkList!!.clear()
        }
        networkList = null
    }

    fun ping(times: Int = 3, url: String = "www.baidu.com"): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val result = runtime.exec("ping  -c $times $url").waitFor()
            return result == 0
        } catch (e: Exception) {
            LogUtil.e(e)
        }
        return false
    }

    private fun findAnnotationMethod(register: Any): List<NetTypedData> {
        val methodManagers = ArrayList<NetTypedData>()
        val clazz = register.javaClass
        val methods = clazz.methods
        for (method in methods) {
            val netWork = method.getAnnotation(NetWork::class.java) ?: continue
            val paramterTypes = method.parameterTypes
            if (paramterTypes.size != 1) {
                continue
            }
            val methodManager = NetTypedData(NetType::class.java, netWork.netType, method)
            methodManagers.add(methodManager)
        }
        return methodManagers
    }
}