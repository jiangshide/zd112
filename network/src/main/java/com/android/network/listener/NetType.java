package com.android.network.listener;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by jiangshide on 2019-07-31.
 * email:18311271399@163.com
 */
@IntDef({NetType.NONE, NetType.AUTO, NetType.CELLULAR, NetType.WIFI, NetType.BLUETOOTH, NetType.ETHERNET, NetType.VPN, NetType.WIFI_AWARE, NetType.LOWPAN})
@Retention(RetentionPolicy.SOURCE)
public @interface NetType {
    int NONE = 0;
    int AUTO = 1;
    int CELLULAR = 2;//cmwap
    int WIFI = 3;
    int BLUETOOTH = 4;
    int ETHERNET = 5;
    int VPN = 6;
    int WIFI_AWARE = 7;
    int LOWPAN = 8;
}
