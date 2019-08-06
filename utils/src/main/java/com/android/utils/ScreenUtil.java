package com.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * created by jiangshide on 2016-07-18.
 * email:18311271399@163.com
 */
public final class ScreenUtil {

    public static int HEIGHT;
    public static int WIDTH;
    public static int DENSITY_DPI;
    public static float DENSITY;

    public static final int HIGH_MODE = 0;
    public static final int MIDDLE_MODE = 1;
    public static final int LOW_MODE = 2;

    public static int getTitleDisplayMode() {
        int highHeight = 800;
        int lowHeight = 320;

        if (highHeight > HEIGHT && lowHeight < HEIGHT) {
            return MIDDLE_MODE;
        } else if (HEIGHT >= highHeight) {
            return HIGH_MODE;
        } else {
            return LOW_MODE;
        }
    }

    public static int dip2px(Context ctx, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context ctx, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return Math.round(pxValue / density);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window win = activity.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);

        return rect.top;
    }

    public static int getTitleBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window win = activity.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);

        return win.findViewById(Window.ID_ANDROID_CONTENT).getTop() - rect.top;
    }

    public static void setDisplay(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;
        DENSITY_DPI = dm.densityDpi;
        DENSITY = dm.density;
    }

    public static float getDensity(Context context) {
        if (DENSITY == 0.0f) {
            DENSITY = context.getResources().getDisplayMetrics().density;
        }

        return DENSITY;
    }

    public static int getScreenHeight(Context context) {
        if (HEIGHT > 0) return HEIGHT;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        HEIGHT = dm.heightPixels;

        return HEIGHT;
    }

    public static int getScreenWidth(Context context) {
        if (WIDTH > 0) return WIDTH;

        WIDTH = getRtScreenWidth(context);
        return WIDTH;
    }

    // 实时获取，防止屏幕旋转
    public static int getRtScreenHeight(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getHeight();
    }

    // 实时获取,防止屏幕旋转
    public static int getRtScreenWidth(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

}
