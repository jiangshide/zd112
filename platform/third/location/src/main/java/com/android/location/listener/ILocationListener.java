package com.android.location.listener;

import com.amap.api.location.AMapLocation;
import com.android.location.Location;

/**
 * created by jiangshide on 2019-08-18.
 * email:18311271399@163.com
 */
public interface ILocationListener {
    void onGetLocation(AMapLocation location);

    void onError(Location.Errors errors);
}
