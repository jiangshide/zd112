package com.android.skin.model;

/**
 * created by jiangshide on 2019-08-11.
 * email:18311271399@163.com
 */
public class SkinViewItemModel {
    public String name;
    public int resId;
    public String entryName;
    public String typeName;

    public SkinViewItemModel(String name, int resId, String entryName, String typeName) {
        this.name = name;
        this.resId = resId;
        this.entryName = entryName;
        this.typeName = typeName;
    }
}
