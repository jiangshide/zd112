package com.android.network.receiver

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.network.annotation.INetType.*
import com.android.network.model.NetModel
import com.android.utils.LogUtil
import java.util.*

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class NetCallback : ConnectivityManager.NetworkCallback() {

    private var networkList: MutableMap<Any, List<NetModel>>? = null
    private var netReceiver: NetReceiver? = null

    init {
        networkList = HashMap()
        netReceiver = NetReceiver()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        netReceiver!!.post(AVAILABLE)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        netReceiver!!.post(NONE)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                netReceiver!!.post(CELLULAR)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                netReceiver!!.post(WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                netReceiver!!.post(BLUETOOTH)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                netReceiver!!.post(ETHERNET)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                netReceiver!!.post(VPN)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                netReceiver!!.post(WIFI_AWARE)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                netReceiver!!.post(LOWPAN)
            } else {
                netReceiver!!.post(NONE)
            }
        } else {
            netReceiver!!.post(NONE)
        }
    }

    fun registerObserver(register: Any) {
        netReceiver!!.registerObserver(register)
    }

    fun unRegisterObserver(register: Any) {
        if (netReceiver != null) {
            netReceiver!!.unRegisterObserver(register)
        }
        netReceiver = null;
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
}