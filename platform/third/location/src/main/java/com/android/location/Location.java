package com.android.location;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.location.listener.IAmapListener;
import com.android.location.listener.ILocationListener;
import com.android.location.listener.IPoiSearchListener;
import com.android.network.NetWork;
import com.android.permission.annotation.Permission;
import com.android.utils.AppUtil;
import com.android.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * created by jiangshide on 2019-08-18.
 * email:18311271399@163.com
 */
public class Location {

    public static final String CITY_TYPES = "190102|190103|190104";

    private double longitude;
    private double latitude;
    private AMapLocation location;

    private HashSet<ILocationListener> callbacks = new HashSet<>();

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    private IAmapListener mIAmapListener;

    public enum Errors {
        common_error,
        no_permission_error,
        no_network_error
    }

    private Location() {
        locationClient = new AMapLocationClient(AppUtil.getApplicationContext());
        locationOption = getDefaultOption();
        locationClient.setLocationListener(locationListener);
    }

    private static class LocationHolder {
        private static Location instance = new Location();
    }

    public static Location getInstance() {
        return LocationHolder.instance;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public AMapLocation getLocation() {
        return location;
    }

    public void startLocation() {
        this.startLocation(false);
    }

    public void startLocation(boolean isOnce) {
        locationOption.setOnceLocation(isOnce);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
        LogUtil.e("-------->isOnce:",isOnce);
    }

    public void stopLocation() {
        locationClient.stopLocation();
    }

    public void setAmapListener(IAmapListener listener) {
        this.mIAmapListener = listener;
    }

    public void searchPOI(Context context, String keyWord, String types, IPoiSearchListener iPoiSearchListener) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, types, "");
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            WeakReference<IPoiSearchListener> weakReference = new WeakReference<>(iPoiSearchListener);

            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                IPoiSearchListener callback = weakReference.get();
                if (callback != null) {
                    callback.onPoiResult(poiResult);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    public void searchNearPOI(Context context, IPoiSearchListener iPoiSearchListener) {
        // 先获取定位

        if (longitude == 0 && latitude == 0) {
            registerCallback(new ILocationListener() {
                WeakReference<IPoiSearchListener> weakReference = new WeakReference<>(iPoiSearchListener);

                @Override
                public void onError(Errors errors) {
                    unregisterCallback(this);
                    IPoiSearchListener callback = weakReference.get();
                    if (callback == null) return;
                    callback.onError(errors);
                }


                @Override
                public void onGetLocation(AMapLocation location) {
                    unregisterCallback(this);
                    IPoiSearchListener callback = weakReference.get();
                    if (callback == null) return;
                    realySearchNearPOI(context, callback);
                    unregisterCallback(this);
                }
            });
            startLocation(false);
        } else {
            realySearchNearPOI(context, iPoiSearchListener);
        }
    }


    private void realySearchNearPOI(Context context, IPoiSearchListener iPoiSearchListener) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            WeakReference<IPoiSearchListener> weakReference = new WeakReference<>(iPoiSearchListener);

            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                IPoiSearchListener callback = weakReference.get();
                if (callback != null) {
                    callback.onPoiResult(poiResult);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,
                longitude), 1000));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();
    }

    public Location getCity(Context context, IPoiSearchListener iPoiSearchListener) {
        // 先获取定位

        if (location == null || TextUtils.isEmpty(location.getCity())) {
            registerCallback(new ILocationListener() {
                WeakReference<IPoiSearchListener> weakReference = new WeakReference<>(iPoiSearchListener);

                @Override
                public void onError(Errors errors) {
                    unregisterCallback(this);
                    IPoiSearchListener callback = weakReference.get();
                    if (callback == null) return;
                    callback.onError(errors);
                }

                @Override
                public void onGetLocation(AMapLocation location) {
                    unregisterCallback(this);
                    IPoiSearchListener callback = weakReference.get();
                    if (callback == null) return;
                    searchPOI(context, location.getCity(), CITY_TYPES, iPoiSearchListener);
                }
            });
            startLocation(false);
        } else {
            searchPOI(context, location.getCity(), CITY_TYPES, iPoiSearchListener);
        }
        return this;
    }


    public void registerCallback(ILocationListener callback) {
        callbacks.add(callback);
    }

    public void unregisterCallback(ILocationListener callback) {
        callbacks.remove(callback);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(10000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息，逆地理编码
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(5000);

        // 设置网络请求超时时间
        locationOption.setHttpTimeOut(6000);
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = location -> {
//        stopLocation();
        LogUtil.e("---------location:",location);
        if (null != location) {

//            StringBuffer sb = new StringBuffer();
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                this.location = location;
                latitude = this.location.getLatitude();
                longitude = location.getLongitude();
                notifyLocationSuccess(location);
                LogUtil.e("---------latitude:", latitude, " | longitude:", longitude, " | mIAmapListener:", mIAmapListener);
                if (null != mIAmapListener) {
                    mIAmapListener.update(latitude, longitude);
                }
//                sb.append("定位成功" + "\n");
//                sb.append("定位类型: " + location.getLocationType() + "\n");
//                sb.append("经    度    : " + latitude + "\n");
//                sb.append("纬    度    : " + longitude + "\n");
//                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//                sb.append("提供者    : " + location.getProvider() + "\n");
//
//                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                sb.append("角    度    : " + location.getBearing() + "\n");
//                // 获取当前提供定位服务的卫星个数
//                sb.append("星    数    : " + location.getSatellites() + "\n");
//                sb.append("国    家    : " + location.getCountry() + "\n");
//                sb.append("省            : " + location.getProvince() + "\n");
//                sb.append("市            : " + location.getCity() + "\n");
//                sb.append("城市编码 : " + location.getCityCode() + "\n");
//                sb.append("区            : " + location.getDistrict() + "\n");
//                sb.append("区域 码   : " + location.getAdCode() + "\n");
//                sb.append("地    址    : " + location.getAddress() + "\n");
//                sb.append("兴趣点    : " + location.getPoiName() + "\n");
//                //定位完成的时间
//                sb.append("定位时间: " + location.getTime());
            } else if (location.getErrorCode() == 12) {
                LogUtil.e("没有联网或定位权限被关闭");
                notifyLocationFailed(NetWork.Companion.getInstance().isNetworkAvailable() ? Errors.no_permission_error : Errors.no_network_error);

            } else if (location.getErrorCode() == 13) {
                LogUtil.e("没有联网");
                notifyLocationFailed(Errors.no_network_error);
            } else {
                //定位失败
//                sb.append("定位失败" + "\n");
//                sb.append("错误码:" + location.getErrorCode() + "\n");
//                sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                sb.append("错误描述:" + location.getLocationDetail() + "\n");
                notifyLocationFailed(Errors.common_error);
            }
//            sb.append("***定位质量报告***").append("\n");
//            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
//            sb.append("* GPS状态：").append(location.getLocationQualityReport().getGPSStatus()).append("\n");
//            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
//            sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
//            sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
//            sb.append("****************").append("\n");
        } else {
            notifyLocationFailed(Errors.common_error);
        }
    };

    private void notifyLocationSuccess(AMapLocation location) {
        ArrayList<ILocationListener> callbacks = new ArrayList<>(this.callbacks);
        for (ILocationListener callback : callbacks) {
            callback.onGetLocation(location);
        }
    }

    private void notifyLocationFailed(Errors errors) {
        ArrayList<ILocationListener> callbacks = new ArrayList<>(this.callbacks);
        for (ILocationListener callback : callbacks) {
            callback.onError(errors);
        }
    }

    public static void showLocationError(Context context, Errors error) {
        if (error == Errors.no_permission_error) {
            permission();
        } else if (error == Errors.no_network_error) {
//            ZdToast.txt(R.string.no_network);
        } else {
//            ZdToast.txt(R.string.location_error);
        }
    }

    @Permission(value = {Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode = 0)
    public static void permission() {
    }
}
