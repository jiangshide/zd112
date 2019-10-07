package com.android.location.listener;

import com.amap.api.services.poisearch.PoiResult;
import com.android.location.Location;

/**
 * created by jiangshide on 2019-08-18.
 * email:18311271399@163.com
 */
public interface IPoiSearchListener {
    void onPoiResult(PoiResult poiResult);

    void onError(Location.Errors errors);
}
