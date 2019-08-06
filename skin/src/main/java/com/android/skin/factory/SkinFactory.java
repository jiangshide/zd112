package com.android.skin.factory;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.skin.Skin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2019-07-26.
 * email:18311271399@163.com
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private static final String[] prxfixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    //装载需要换肤的容器
    private List<SkinView> viewList = new ArrayList<>();

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;//监听xml生成过程，自己去创建这些控件
        if (name.contains(".")) {//区分这个控件是否是自定义的控件
            view = onCreateView(name, context, attrs);
        } else {
            for (String s : prxfixList) {
                view = onCreateView(s + name, context, attrs);
                if (view != null) break;
            }
        }
        //收集所有需要换肤的控件
        if (view != null) {
            //如果控件已经被实列化，就去判断这个控件是否满足我们换肤的要求,然后收集起来
            parseView(view, name, attrs);
        }
        return view;
    }

    public void apply() {
        for (SkinView skinView : viewList) {
            skinView.apply();
        }
    }

    /**
     * 如果控件已经被实列化，就去判断这个控件是否满足我们换肤的要求,然后收集起来
     *
     * @param view
     * @param name
     * @param attributeSet
     */
    private void parseView(View view, String name, AttributeSet attributeSet) {
        List<SkinItem> itemList = new ArrayList<>();
        for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
            //获取到属性的名字
            String attributeName = attributeSet.getAttributeName(i);
            //获取属性的资源Id
            String attributeValue = attributeSet.getAttributeValue(i);
            //判断每条属性是否包含 background textColor color
            if (attributeName.contains("background") || attributeName.contains("textColor") || attributeName.contains("src") || attributeName.contains("color")) {
                //获取资源ID
                int resId = Integer.parseInt(attributeValue.substring(1));
                //获取到属性值的类型
                String resourceTypeName = view.getResources().getResourceTypeName(resId);
                //获取到属性的值的名字
                String resourceEntryName = view.getResources().getResourceEntryName(resId);
                SkinItem skinItem = new SkinItem(attributeName, resId, resourceEntryName, resourceEntryName);
                itemList.add(skinItem);
            }
        }
        //如果长度大于0，就说明当前的控件需要换肤
        if (itemList.size() > 0) {
            SkinView skinView = new SkinView(view, itemList);
            viewList.add(skinView);
            skinView.apply();
        }
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
            //获得name的class对象
            Class aClass = context.getClassLoader().loadClass(name);
            //
            Constructor<? extends View> constructor = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
            constructor.newInstance(context, attrs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class SkinItem {
        private String name;//属性的名字:background
        private int resId;//属性的值的id
        private String entryName;//属性的值的名字colorPrimary
        private String typeName;//属性值的类型 color mipmap

        public SkinItem(String name, int resId, String entryName, String typeName) {
            this.name = name;
            this.resId = resId;
            this.entryName = entryName;
            this.typeName = typeName;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getName() {
            return name;
        }

        public int getResId() {
            return resId;
        }

        public String getEntryName() {
            return entryName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    class SkinView {
        View view;
        List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        public View getView() {
            return view;
        }

        public List<SkinItem> getList() {
            return list;
        }

        //给单个控件进行换肤
        public void apply() {
            for (SkinItem skinItem : list) {
                if (skinItem.getName().equals("background")) {
                    if (skinItem.getTypeName().equals("color")) {
                        //从皮肤插件apk资源对象中获取到相匹配的ID进行设置
                        view.setBackgroundColor(Skin.getInstance().getColor(skinItem.getResId()));
                    } else if (skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(Skin.getInstance().getDrawable(skinItem.getResId()));
                        } else {
                            view.setBackgroundDrawable(Skin.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                } else if (skinItem.getName().equals("textColor")) {
                    if (view instanceof TextView) {
                        ((TextView) (view)).setTextColor(Skin.getInstance().getColor(skinItem.getResId()));
                    } else if (view instanceof Button) {
                        (((Button) view)).setTextColor(Skin.getInstance().getColor(skinItem.getResId()));
                    }
                }
            }
        }
    }
}
