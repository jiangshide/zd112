package com.android.utils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by jiangshide on 2019-08-05.
 * email:18311271399@163.com
 */
public final class StringUtil {

    /**
     * 转换小写
     *
     * @param var0
     * @return
     */
    public static String toLowerCase(String var0) {
        return var0.toLowerCase(Locale.US);
    }

    /**
     * 转换为大写
     *
     * @param var0
     * @return
     */
    public static String toUpperCase(String var0) {
        return var0.toUpperCase(Locale.US);
    }

    /**
     * 查找，不区分大小写,没有找到返回-1
     *
     * @param var0
     * @param var1
     * @return
     */
    public static int indexOfIgnoreCase(String var0, String var1) {
        return indexOfIgnoreCase(var0, var1, 0);
    }

    /**
     * 从指定位置开始查找，不区分大小写--返回-1
     *
     * @param var0
     * @param var1
     * @param var2
     * @return
     */
    public static int indexOfIgnoreCase(String var0, String var1, int var2) {
        Matcher var3 = Pattern.compile(Pattern.quote(var1), 2).matcher(var0);
        return var3.find(var2) ? var3.start() : -1;
    }

    /**
     * 获取随机昵称
     *
     * @return 随机昵称
     */
    public static String getRandomNick() {
        Random random = new Random();
        int len = random.nextInt(12) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(getRandomSingleCharacter());
        }
        return builder.toString();
    }

    /**
     * 获取随机单个汉字
     *
     * @return 随机单个汉字
     */
    public static String getRandomSingleCharacter() {
        String str = "";
        int heightPos;
        int lowPos;
        Random rd = new Random();
        heightPos = 176 + Math.abs(rd.nextInt(39));
        lowPos = 161 + Math.abs(rd.nextInt(93));
        byte[] bt = new byte[2];
        bt[0] = Integer.valueOf(heightPos).byteValue();
        bt[1] = Integer.valueOf(lowPos).byteValue();
        try {
            str = new String(bt, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 多字符组装
     * @param params
     * @return
     */
    public static String getAppend(String... params) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String param : params) {
            stringBuffer.append(param);
        }
        return stringBuffer.toString();
    }
}
