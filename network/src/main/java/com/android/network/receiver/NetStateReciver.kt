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
    }

    override fun onLost(network: Network) {
        super.onLost(network)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                post(NetType::class.java, CELLULAR)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                post(NetType::class.java, WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                post(NetType::class.java, BLUETOOTH)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                post(NetType::class.java, ETHERNET)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                post(NetType::class.java, VPN)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                post(NetType::class.java, WIFI_AWARE)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                post(NetType::class.java, LOWPAN)
            } else {
                post(NetType::class.java, NONE)
            }
        }
    }

    private fun post(clazz: Class<*>, @NetType netType: Int) {
        val set = networkList!!.keys
        for (getter in set) {
            val methodManagers = networkList!![getter]
            if (methodManagers != null) {
                for (methodManager in methodManagers) {
                    if (methodManager.type.isAssignableFrom(clazz)) {
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