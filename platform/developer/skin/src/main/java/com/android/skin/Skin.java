package com.android.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.android.skin.factory.SkinFactory;
import com.android.utils.LogUtil;

import java.lang.reflect.Method;

/**
 * 动态主题换肤依赖与实现
 * created by jiangshide on 2019-07-26.
 * email:18311271399@163.com
 */
public final class Skin {

    private Skin() {
        mSkinFactory = new SkinFactory();
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

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * 复制文件与加载apk信息
     *
     * @param path
     */
    public void loadSkinApk(String path) {
        PackageManager packageManager = this.mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (null == packageInfo) return;
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

    public void apply() {
        if (mSkinFactory != null) {
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
        int identifier = getIdentifier(id);
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
        int identifier = getIdentifier(id);
        if (identifier == 0) {
            return ContextCompat.getDrawable(mContext, 0);
        }
        return mResources.getDrawable(identifier);
    }

    /**
     * 获取外置apk中String的资源id
     *
     * @param id
     * @return
     */
    public String getText(int id) {
        if (mResources == null) {
            return mContext.getResources().getString(id);
        }
        int identifier = getIdentifier(id);
        if (identifier == 0) {
            return mContext.getResources().getString(id);
        }
        return mResources.getString(identifier);
    }

    /**
     * 与外置apk中的资源id进行匹配
     *
     * @param id
     * @return
     */
    public int getIdentifier(int id) {
        String typeName = mContext.getResources().getResourceTypeName(id);
        String entryName = mContext.getResources().getResourceEntryName(id);
        return mResources.getIdentifier(entryName, typeName, mSkinPkgName);
    }
}
