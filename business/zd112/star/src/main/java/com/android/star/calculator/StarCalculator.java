package com.android.star.calculator;

import com.android.star.model.StarModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * created by jiangshide on 2016-04-15.
 * email:18311271399@163.com
 */
public class StarCalculator {
    private static final int DEFAULT_RADIUS = 3;
    private static final float[] DEFAULT_COLOR_DARK = {0.886f, 0.725f, 0.188f, 1f};
    private static final float[] DEFAULT_COLOR_LIGHT = {0.3f, 0.3f, 0.3f, 1f};
    private float maxDelta = Float.MIN_VALUE;
    private float minDelta = Float.MAX_VALUE;
    private List<StarModel> mStarDataList;
    private int radius;
    private float[] tagColorLight;
    private float[] tagColorDark;
    private float sinAngleX, cosAngleX, sinAngleY, cosAngleY, sinAngleZ, cosAngleZ;
    private float mAngleZ = 0;
    private float mAngleX = 0;
    private float mAngleY = 0;
    /**
     * 用于查找标签颜色的光谱
     */
    private int smallest, largest;
    /**
     * 默认设置是在云端均匀分布标签
     */
    private boolean isEvenly = true;

    public StarCalculator() {
        this(DEFAULT_RADIUS);
    }

    public StarCalculator(int radius) {
        this(new ArrayList<StarModel>(), radius);
    }

    public StarCalculator(List<StarModel> dataList, int radius) {
        this(dataList, radius, DEFAULT_COLOR_DARK, DEFAULT_COLOR_LIGHT);
    }

    public StarCalculator(List<StarModel> dataList, int radius, float[] tagColorLight, float[] tagColorDark) {
        this.mStarDataList = dataList;
        this.radius = radius;
        this.tagColorLight = tagColorLight;
        this.tagColorDark = tagColorDark;
    }

    public StarCalculator(List<StarModel> dataList) {
        this(dataList, DEFAULT_RADIUS);
    }

    public void clear() {
        mStarDataList.clear();
    }

    public List<StarModel> getTagList() {
        return mStarDataList;
    }

    public void setTagList(List<StarModel> dataList) {
        mStarDataList = dataList;
    }

    public StarModel getTop() {
        int i = mStarDataList.size() - 1;
        return get(i);
    }

    public StarModel get(int position) {
        return mStarDataList.get(position);
    }

    public int indexOf(StarModel starData) {
        return mStarDataList.indexOf(starData);
    }

    public void reset() {
        create(isEvenly);
    }

    /**
     * 创建并初始化每个Tag的位置
     *
     * @param isEvenly 是否平均分布
     */
    public void create(boolean isEvenly) {
        this.isEvenly = isEvenly;
        // 计算和设置每个Tag的位置
        locationAll(isEvenly);
        sineCosine(mAngleX, mAngleY, mAngleZ);
        updateAll();
        // 现在，让我们计算并设置每个标记的颜色：
        // 首先遍历所有标记以查找最小和最大的填充
        // 权重得到t颜色2，最小的得到t颜色1，其余在中间
        smallest = 9999;
        largest = 0;
        for (int i = 0; i < mStarDataList.size(); i++) {
            int j = mStarDataList.get(i).getPopularity();
            largest = Math.max(largest, j);
            smallest = Math.min(smallest, j);
        }
        // 计算并分配颜色/文本大小
        for (int i = 0; i < mStarDataList.size(); i++) {
            initTag(mStarDataList.get(i));
        }
    }

    /**
     * 计算所有的位置
     * <p>
     * 球坐标系(r,θ,φ)与直角坐标系(x,y,z)的转换关系:
     * x=rsinθcosφ.
     * y=rsinθsinφ.
     * z=rcosθ.
     * <p>
     * r -> radius
     * θ -> phi
     * φ -> theta
     *
     * @param isEvenly 是否均匀分布
     */
    private void locationAll(boolean isEvenly) {
        double phi;
        double theta;
        int count = mStarDataList.size();
        for (int i = 1; i < count + 1; i++) {
            if (isEvenly) {
                // 平均（三维直角得Z轴等分[-1,1]） θ范围[-π/2,π/2])
                phi = Math.acos(-1.0 + (2.0 * i - 1.0) / count);
                theta = Math.sqrt(count * Math.PI) * phi;
            } else {
                phi = Math.random() * (Math.PI);
                theta = Math.random() * (2 * Math.PI);
            }

            mStarDataList.get(i - 1).setLocX((float) (radius * Math.cos(theta) * Math.sin(phi)));
            mStarDataList.get(i - 1).setLocY((float) (radius * Math.sin(theta) * Math.sin(phi)));
            mStarDataList.get(i - 1).setLocZ((float) (radius * Math.cos(phi)));
        }
    }

    /**
     * 返回角度转换成弧度之后各方向的值
     * <p>
     * 1度=π/180
     *
     * @param mAngleX x方向旋转距离
     * @param mAngleY y方向旋转距离
     * @param mAngleZ z方向旋转距离
     */
    private void sineCosine(float mAngleX, float mAngleY, float mAngleZ) {
        double degToRad = (Math.PI / 180);
        sinAngleX = (float) Math.sin(mAngleX * degToRad);
        cosAngleX = (float) Math.cos(mAngleX * degToRad);
        sinAngleY = (float) Math.sin(mAngleY * degToRad);
        cosAngleY = (float) Math.cos(mAngleY * degToRad);
        sinAngleZ = (float) Math.sin(mAngleZ * degToRad);
        cosAngleZ = (float) Math.cos(mAngleZ * degToRad);
    }

    /**
     * 更新所有的
     */
    private void updateAll() {
        // 更新标签透明度和比例
        int count = mStarDataList.size();
        for (int i = 0; i < count; i++) {
            StarModel starData = mStarDataList.get(i);
            // 此部分有两个选项：
            // 绕x轴旋转
            float rx1 = (starData.getLocX());
            float ry1 = (starData.getLocY()) * cosAngleX + starData.getLocZ() * -sinAngleX;
            float rz1 = (starData.getLocY()) * sinAngleX + starData.getLocZ() * cosAngleX;
            // 绕y轴旋转
            float rx2 = rx1 * cosAngleY + rz1 * sinAngleY;
            float ry2 = ry1;
            float rz2 = rx1 * -sinAngleY + rz1 * cosAngleY;
            // 绕z轴旋转
            float rx3 = rx2 * cosAngleZ + ry2 * -sinAngleZ;
            float ry3 = rx2 * sinAngleZ + ry2 * cosAngleZ;
            float rz3 = rz2;
            // 将数组设置为新位置
            starData.setLocX(rx3);
            starData.setLocY(ry3);
            starData.setLocZ(rz3);

            // 添加透视图
            int diameter = 2 * radius;
            float per = diameter / (diameter + rz3);
            // 让我们为标签设置位置、比例和透明度
            starData.setLoc2DX(rx3);
            starData.setLoc2DY(ry3);
            starData.setScale(per);

            // 计算透明度
            float delta = diameter + rz3;
            maxDelta = Math.max(maxDelta, delta);
            minDelta = Math.min(minDelta, delta);
            float alpha = (delta - minDelta) / (maxDelta - minDelta);
            starData.setAlpha(1 - alpha);
        }
        sortTagByScale();
    }

    private void initTag(StarModel starData) {
        float percentage = getPercentage(starData);
        float[] argb = getColorFromGradient(percentage);
        starData.setColorByArray(argb);
    }

    /**
     * 根据缩放值排序
     */
    public void sortTagByScale() {
        Collections.sort(mStarDataList, new TagComparator());
    }

    private float getPercentage(StarModel starData) {
        int p = starData.getPopularity();
        return (smallest == largest) ? 1.0f : ((float) p - smallest) / ((float) largest - smallest);
    }

    private float[] getColorFromGradient(float percentage) {
        float[] rgba = new float[4];
        rgba[0] = 1f;
        rgba[1] = (percentage * (tagColorDark[0])) + ((1f - percentage) * (tagColorLight[0]));
        rgba[2] = (percentage * (tagColorDark[1])) + ((1f - percentage) * (tagColorLight[1]));
        rgba[3] = (percentage * (tagColorDark[2])) + ((1f - percentage) * (tagColorLight[2]));
        return rgba;
    }

    /**
     * 更新所有元素的透明度/比例
     */
    public void update() {
        // 如果mAngleX和mAngleY低于阈值，则跳过运动计算以获得性能
        if (Math.abs(mAngleX) > .1 || Math.abs(mAngleY) > .1) {
            sineCosine(mAngleX, mAngleY, mAngleZ);
            updateAll();
        }
    }

    /**
     * 添加单个标签
     *
     * @param starData 标签
     */
    public void add(StarModel starData) {
        initTag(starData);
        location(starData);
        mStarDataList.add(starData);
        updateAll();
    }

    /**
     * 添加新标签时，只需将其放置在某个随机位置
     * 在多次添加之后，执行一次重置以重新排列所有标记
     *
     * @param starData 标签
     */
    private void location(StarModel starData) {
        double phi;
        double theta;
        phi = Math.random() * (Math.PI);
        theta = Math.random() * (2 * Math.PI);
        starData.setLocX((int) (radius * Math.cos(theta) * Math.sin(phi)));
        starData.setLocY((int) (radius * Math.sin(theta) * Math.sin(phi)));
        starData.setLocZ((int) (radius * Math.cos(phi)));
    }

    /**
     * 设置半径
     *
     * @param radius 半径
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setTagColorLight(float[] tagColor) {
        this.tagColorLight = tagColor;
    }

    public void setTagColorDark(float[] tagColorDark) {
        this.tagColorDark = tagColorDark;
    }

    public void setAngleX(float mAngleX) {
        this.mAngleX = mAngleX;
    }

    public void setAngleY(float mAngleY) {
        this.mAngleY = mAngleY;
    }

    private static class TagComparator implements Comparator<StarModel> {

        @Override
        public int compare(StarModel starData, StarModel starDataTo) {
            return starData.getScale() > starDataTo.getScale() ? 1 : 0;
        }
    }
}
