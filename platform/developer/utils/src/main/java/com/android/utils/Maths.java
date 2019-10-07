package com.android.utils;

import androidx.annotation.NonNull;

/**
 * created by jiangshide on 2016-03-17.
 * email:18311271399@163.com
 */
public class Maths {
    public static int max(@NonNull int[] array) {
        int max = Integer.MIN_VALUE;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
