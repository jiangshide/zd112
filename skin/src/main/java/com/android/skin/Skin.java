package com.android.skin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;

import com.android.skin.factory.SkinFactory;
import com.android.utils.LogUtil;

import java.lang.reflect.Method;

/**
 * 动态主题换肤依赖与实现
 * created by jiangshide on 2019-07-26.
 * email:18311271399@163.com
 */
public class Skin {

    private Skin() {
    }

    private Resources mResources;
    private Context mContext;
    private String mSkinPkgName;

    private SkinFactory mSkinFactory;

    private static class SkinManagerHolder {
        private static Skin instance = new Skin();
    }

    public static Skin getInstance() {
        return SkinManagerHolder.instance;
    }

    public void initContext(Application context) {
        this.mContext = context;
        mSkinFactory = new SkinFactory();
    }

    /**
     * 复制文件与加载apk信息
     * @param path
     */
    public void loadSkinApk(String path) {
        //复制文件
        //todo...
        //获取包管理器
        PackageManager packageManager = this.mContext.getPackageManager();
        //获取皮肤apk的包信息类
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        //获取apk的包名
        mSkinPkgName = packageInfo.packageName;
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            mResources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());//后面两个参数为标识在系统是唯一的，只要是在同一个手机去获取拿到的都是同一个对象
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public void setFactory(Activity activity) {
        LayoutInflaterCompat.setFactory2(activity.getLayoutInflater(), mSkinFactory);
    }

    public void apply(){
        if(mSkinFactory != null){
            mSkinFactory.apply();
        }
    }

    /**
     * 根据传进来的ID,去匹配皮肤插件APK资源对象,如果有类型和名字一样的就返回
     *
     * @param id
     * @return
     */
    public int getColor(int id) {
        if (mResources == null) {
            return id;
        }
        //获取到的属性值的名字 colorPrimary
        String resourceEntryName = mContext.getResources().getResourceEntryName(id);
        //获取到的属性值的类型 colorPrimary
        String typeName = mContext.getResources().getResourceTypeName(id);
        //就是名字和类型匹配的资源对象中的ID
        int identifier = mResources.getIdentifier(resourceEntryName, typeName, mSkinPkgName);
        if (identifier == 0) {
            return id;
        }
        return mResources.getColor(identifier);
    }

    /**
     * 从外置apk中拿到drawable的资源id
     *
     * @param id
     * @return
     */
    public Drawable getDrawable(int id) {
        if (mResources == null) {
            return ContextCompat.getDrawable(mContext, id);
        }
        //获取到资源id的类型
        String resourceTypeName = mContext.getResources().getResourceTypeName(id);
        //获取到的就是资源id的名字
        String resourceEntryName = mContext.getResources().getResourceEntryName(id);
        //就是colorAccent这个资源在外置apk中的id
        int identifier = mResources.getIdentifier(resourceEntryName, resourceTypeName, mSkinPkgName);
        if (identifier == 0) {
            return ContextCompat.getDrawable(mContext, 0);
        }
        return mResources.getDrawable(identifier);
    }
}
