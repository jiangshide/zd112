package com.android.skin.model;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.skin.Skin;

import java.util.List;

/**
 * created by jiangshide on 2019-08-11.
 * email:18311271399@163.com
 */
public class SkinViewModel {

    public View view;
    public List<SkinViewItemModel> list;

    public SkinViewModel(View view, List<SkinViewItemModel> list) {
        this.view = view;
        this.list = list;
    }

    /**
     * 可针对单个控件进行换肤
     */
    public void apply() {
        for (SkinViewItemModel skinItem : list) {
            if (skinItem.name.equals("background")) {
                if (skinItem.typeName.equals("color") || skinItem.typeName.equals("colorPrimary")) {
                    view.setBackgroundColor(Skin.getInstance().getColor(skinItem.resId));
                } else if (skinItem.typeName.equals("drawable") || skinItem.typeName.equals("mipmap")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.setBackground(Skin.getInstance().getDrawable(skinItem.resId));
                    } else {
                        view.setBackgroundDrawable(Skin.getInstance().getDrawable(skinItem.resId));
                    }
                }
            } else if (skinItem.name.equals("textColor")) {
                if (view instanceof TextView) {
                    int res = Skin.getInstance().getColor(skinItem.resId);
                    ((TextView) (view)).setTextColor(res);
                } else if (view instanceof Button) {
                    (((Button) view)).setTextColor(Skin.getInstance().getColor(skinItem.resId));
                }
            } else if (skinItem.name.equals("src")) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageDrawable(Skin.getInstance().getDrawable(skinItem.resId));
                }
            } else if (skinItem.name.equals("text")) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(Skin.getInstance().getText(skinItem.resId));
                }
            }
            //todo...
        }
    }
}
