package com.android.zdplugin;

import android.content.Context;

/**
 * 处理插件话基础逻辑,以及热修复相关逻辑
 * created by jiangshide on 2019-08-06.
 * email:18311271399@163.com
 */
public final class ZdPlugin {

    private Context mContext;

    private ZdPlugin() {
    }

    public ZdPlugin init(Context context) {
        this.mContext = context;
        return this;
    }

    private static class ZdPluginHolder {
        private static ZdPlugin instance = new ZdPlugin();
    }

    public static ZdPlugin getInstance() {
        return ZdPluginHolder.instance;
    }

    public void fixDex() {
        new FixDex().fixDex(mContext);
    }

    public void loadPlugin() {
        //todo 待实现
    }
}
