package com.android.network.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@IntDef({INetType.NONE, INetType.AVAILABLE, INetType.AUTO, INetType.CELLULAR, INetType.WIFI, INetType.BLUETOOTH, INetType.ETHERNET, INetType.VPN, INetType.WIFI_AWARE, INetType.LOWPAN})
@Retention(RetentionPolicy.SOURCE)
public @interface INetType {
    int NONE = -1;
    int AVAILABLE = 1;
    int AUTO = 2;//有网络:wifi/gprs
    int CELLULAR = 3;//cmwap
    int WIFI = 4;
    int BLUETOOTH = 5;
    int ETHERNET = 6;
    int VPN = 7;
    int WIFI_AWARE = 8;
    int LOWPAN = 9;
}
