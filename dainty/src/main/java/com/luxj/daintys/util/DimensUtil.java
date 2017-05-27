package com.luxj.daintys.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author Luxj
 * @time 2015-5-5 14:41:43
 * @类说明 Android的一些单位转化工具 例如px<-->dp转化 px<-->sp转化等等都可以写在这里
 * <p>
 * <pre>
 * dp-->px
 * sp-->px
 * 获得屏幕宽度
 * 获得屏幕高度
 * </pre>
 */
public class DimensUtil {

    /**
     * dp-->px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPixels(Context context, float dp) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return (int) px;
    }

    public static float dpToPixelsF(Context context, float dp) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return px;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param px
     * @return
     */
    public static int pixelsToDp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static float pixelsToDpF(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param px
     * @return
     */
    public static int pixelsToSp(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    public static float pixelsToSpF(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale;
    }

    /**
     * sp-->px
     *
     * @param context
     * @param sp
     * @return
     */
    public static int spToPixels(Context context, float sp) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, res.getDisplayMetrics());
        return (int) px;
    }

    public static float spToPixelsF(Context context, float sp) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, res.getDisplayMetrics());
        return px;
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 获取手机密度
     *
     * @return
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.density;
    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }

    /**
     * 标题栏高度
     *
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取是否存在NavigationBar
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }
}
