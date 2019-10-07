package com.android.network

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.net.TrafficStats
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.android.network.annotation.INetType.*
import com.android.network.receiver.NetCallback
import com.android.network.receiver.NetReceiver
import com.android.utils.AppUtil

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */

class NetWork private constructor() {

    companion object {
        val instance: NetWork by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetWork() }
    }

    var application: Application? = null
    private var netCallback: NetCallback? = null
    private var netReceiver: NetReceiver? = null

    val NETWORK_WIFI = 1    // wifi network
    val NETWORK_4G = 4    // "4G" networks
    val NETWORK_3G = 3    // "3G" networks
    val NETWORK_2G = 2    // "2G" networks
    val NETWORK_UNKNOWN = 5    // unknown network
    val NETWORK_NO = -1   // no network

    private val NETWORK_TYPE_GSM = 16
    private val NETWORK_TYPE_TD_SCDMA = 17
    private val NETWORK_TYPE_IWLAN = 18

    private var lastTotalRxBytes: Long = 0
    private var lastTimeStamp: Long = 0

    fun init(application: Application) {
        this.application = application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            netCallback = NetCallback()
            val build = NetworkRequest.Builder()
            val request = build.build()
            val connectivityManager: ConnectivityManager =
                    application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(request, netCallback)
        } else {
            if (netReceiver == null) {
                netReceiver = NetReceiver()
            }
            val intentFilter = IntentFilter()
            intentFilter.addAction(netReceiver!!.ACTION)
            application.registerReceiver(netReceiver, intentFilter)
        }
    }

    fun registerObserver(any: Any) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            netCallback!!.registerObserver(any)
        } else {
            netReceiver!!.registerObserver(any)
        }
    }

    fun unRegisterObserver(any: Any) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            netCallback!!.unRegisterObserver(any)
            val connectivityManager: ConnectivityManager =
                    application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(netCallback)
        } else {
            if (netReceiver != null) {
                netReceiver!!.unRegisterObserver(any)
                application!!.unregisterReceiver(netReceiver)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ping(times: Int = 3, url: String): Boolean {
        return netCallback!!.ping(times, url)
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
                application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivityManager.allNetworkInfo
        if (null != networkInfos) {
            for (networkInfo in networkInfos) {
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    fun getNetType(): Int {
        val connectivityManager = AppUtil.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return NONE
        val networkInfo = connectivityManager.activeNetworkInfo ?: return NONE
        val type = networkInfo.type
        if (type == ConnectivityManager.TYPE_MOBILE)
            return CELLULAR
        else if (type == ConnectivityManager.TYPE_WIFI)
            return WIFI
        else if (type == ConnectivityManager.TYPE_ETHERNET)
            return ETHERNET
        else if (type == ConnectivityManager.TYPE_VPN)
            return VPN
        else if (type == ConnectivityManager.TYPE_BLUETOOTH) return BLUETOOTH
        return NONE
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 需添加权限 android.permission.ACCESS_NETWORK_STATE
     *
     * @param context 上下文
     * @return 网络类型
     *
     *  * NETWORK_WIFI    = 1;
     *  * NETWORK_4G      = 4;
     *  * NETWORK_3G      = 3;
     *  * NETWORK_2G      = 2;
     *  * NETWORK_UNKNOWN = 5;
     *  * NETWORK_NO      = -1;
     *
     */
    fun getNetWorkType(context: Context): Int {
        var netType = NETWORK_NO
        val info = getActiveNetworkInfo(context)
        if (info != null && info!!.isAvailable()) {

            if (info!!.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NETWORK_WIFI
            } else if (info!!.getType() == ConnectivityManager.TYPE_MOBILE) {
                when (info!!.getSubtype()) {

                    NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> netType = NETWORK_2G

                    NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> netType = NETWORK_3G

                    NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> netType = NETWORK_4G
                    else -> {

                        val subtypeName = info!!.getSubtypeName()
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                || subtypeName.equals("WCDMA", ignoreCase = true)
                                || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                            netType = NETWORK_3G
                        } else {
                            netType = NETWORK_UNKNOWN
                        }
                    }
                }
            } else {
                netType = NETWORK_UNKNOWN
            }
        }
        return netType
    }

    /**
     * 获取网络速率
     * @param context
     * @return
     */
    fun getNetBytes(context: Context?): Long {
        if (context == null) return -1
        return if (TrafficStats.getUidRxBytes(context.applicationInfo.uid) == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getTotalRxBytes() / 1024
    }

    /**
     * 获取网络速率 kb
     * @param context
     * @return
     */
    fun getNetSpeed(context: Context): String {
        val nowTotalRxBytes = getNetBytes(context)
        val nowTimeStamp = System.currentTimeMillis()
        val speed = (nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp)//毫秒转换
        lastTimeStamp = nowTimeStamp
        lastTotalRxBytes = nowTotalRxBytes
        return String.format("%dkb/s", speed)
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 依赖上面的方法
     *
     * @param context 上下文
     * @return 网络类型名称
     *
     *  * NETWORK_WIFI
     *  * NETWORK_4G
     *  * NETWORK_3G
     *  * NETWORK_2G
     *  * NETWORK_UNKNOWN
     *  * NETWORK_NO
     *
     */
    fun getNetWorkTypeName(context: Context): String {
        when (getNetWorkType(context)) {
            NETWORK_WIFI -> return "wifi"
            NETWORK_4G -> return "4G"
            NETWORK_3G -> return "3G"
            NETWORK_2G -> return "2G"
            NETWORK_NO -> return "UNKNOWN"
            else -> return "UNKNOWN"
        }
    }

    /**
     * 获取活动网路信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 判断网络是否可用
     *
     * 需添加权限 android.permission.ACCESS_NETWORK_STATE
     */
    fun isAvailable(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable
    }
}