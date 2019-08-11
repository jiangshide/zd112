package com.android.skin.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.android.skin.R;
import com.android.skin.model.SkinViewItemModel;
import com.android.skin.model.SkinViewModel;
import com.android.utils.AppUtil;
import com.android.utils.LogUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2019-07-26.
 * email:18311271399@163.com
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private List<SkinViewModel> mSkinViewList;

    public SkinFactory() {
        mSkinViewList = new ArrayList<>();
    }

    private static final String[] prxfixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.contains(".")) {
            view = onCreateView(name, context, attrs);
        } else {
            for (String s : prxfixList) {
                view = onCreateView(s + name, context, attrs);
                if (view != null) break;
            }
        }
        if (view != null) {
            parseView(view, attrs);
        }
        return view;
    }

    public void apply() {
        for (SkinViewModel skinView : mSkinViewList) {
            skinView.apply();
        }
    }

    /**
     * 获取换肤控件
     *
     * @param view
     * @param attributeSet
     */
    private void parseView(View view, AttributeSet attributeSet) {
        List<SkinViewItemModel> itemList = new ArrayList<>();
        int count = attributeSet.getAttributeCount();
        for (int i = 0; i < count; i++) {
            SkinViewItemModel skinItem = getSkinItem(view, attributeSet, i);
            if (null != skinItem) itemList.add(skinItem);
        }

        if (itemList.size() == 0) return;
        SkinViewModel skinViewModel = new SkinViewModel(view, itemList);
        mSkinViewList.add(skinViewModel);
        skinViewModel.apply();
    }

    /**
     * @param view
     * @param attributeSet
     * @param i
     * @return
     */
    private SkinViewItemModel getSkinItem(View view, AttributeSet attributeSet, int i) {
        String attributeValue = attributeSet.getAttributeValue(i);
        if (!attributeValue.contains("@")) return null;
        String attributeName = attributeSet.getAttributeName(i);
        String[] skinTypes = AppUtil.getApplicationContext().getResources().getStringArray(R.array.skinTyep);
        for (String skinType : skinTypes) {
            if (attributeName.equals(skinType)) {
                int resId = Integer.parseInt(attributeValue.substring(1));
                String typeName = view.getResources().getResourceTypeName(resId);
                String entryName = view.getResources().getResourceEntryName(resId);
                return new SkinViewItemModel(attributeName, resId, entryName, typeName);
            }
        }
        return null;
    }

    /**
     * 将控件进行实例化的方法
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            Class aClass = context.getClassLoader().loadClass(name);
            //
            Constructor<? extends View> constructor = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
            view = constructor.newInstance(context, attrs);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return view;
    }

    /**
     * 判读当前类是否已经被加载过
     *
     * @param name
     * @return
     */
    public boolean isLoaded(String name) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
           LogUtil.e(e);
        }
        return false;
    }
}
