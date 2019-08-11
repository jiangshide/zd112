package com.android.network.listener;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@IntDef({NetType.NONE, NetType.AVAILABLE, NetType.AUTO, NetType.CELLULAR, NetType.WIFI, NetType.BLUETOOTH, NetType.ETHERNET, NetType.VPN, NetType.WIFI_AWARE, NetType.LOWPAN})
@Retention(RetentionPolicy.SOURCE)
public @interface NetType {
    int NONE = -1;
    int AVAILABLE = 1;
    int AUTO = 2;
    int CELLULAR = 3;//cmwap
    int WIFI = 4;
    int BLUETOOTH = 5;
    int ETHERNET = 6;
    int VPN = 7;
    int WIFI_AWARE = 8;
    int LOWPAN = 9;
}
