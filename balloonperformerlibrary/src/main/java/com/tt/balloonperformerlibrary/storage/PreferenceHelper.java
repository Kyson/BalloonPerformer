/*
 * FileName:	PreferenceHelper.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-23 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.storage;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 存储变量 <功能简述> <Br>
 * <功能详细描述> <Br>
 *
 * @author kysonX
 */
public class PreferenceHelper {
    private static final String CONFIG = "config";

    private static final String EXT_CONFIG = "ext_config";

    private static final String KEY_BC = "balloon_count";
    private static final String KEY_BD = "balloon_duration";
    private static final String KEY_LL = "line_len";
    private static final String KEY_PS = "pull_sen";
    private static final String KEY_ONLY_DESTOP = "only_destop";
    private static final String KEY_IS_RUNNING = "is_running";

    /**
     * 存储气球个数
     *
     * @param context
     */
    public static void cacheBalloonCount(Context context, int count) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_BC, count).commit();
    }

    /**
     * 存储飞行时间
     *
     * @param context
     * @param time
     */
    public static void cacheFlyDuration(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putLong(KEY_BD, time).commit();
    }

    /**
     * 存储线长
     *
     * @param context
     * @param lineLen
     */
    public static void cacheLineLength(Context context, int lineLen) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_LL, lineLen).commit();
    }

    /**
     * 拉线灵敏度
     *
     * @param context
     * @param sen
     */
    public static void cachePullSen(Context context, float sen) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putFloat(KEY_PS, sen).commit();
    }

    /**
     * 设置是否仅在桌面显示 <功能简述>
     *
     * @param context
     * @param onlyDestop
     */
    public static void cacheOnlyDestop(Context context, boolean onlyDestop) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_ONLY_DESTOP, onlyDestop).commit();
    }

    // 气球个数
    public static final int DEFAULT_BALLOON_COUNT = 5;
    // 气球飞行速度(时间)
    public static final long DEFAULT_FLY_DURATION = 2500;
    // 拉线长度
    public static final int DEFAULT_LINE_LENGTH = 72;
    // 下拉灵敏度
    public static final float DEFAULT_PULL_SENSITIVITY = 1.8f;

    /**
     * 获取气球个数
     *
     * @param context
     * @return
     */
    public static int balloonCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        return sp.getInt(KEY_BC, DEFAULT_BALLOON_COUNT);
    }

    /**
     * 获取飞行时间
     *
     * @param context
     * @return
     */
    public static long flyDuration(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        return sp.getLong(KEY_BD, DEFAULT_FLY_DURATION);
    }

    /**
     * 获取拉线长度
     *
     * @param context
     * @return
     */
    public static int lineLength(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        return sp.getInt(KEY_LL, DEFAULT_LINE_LENGTH);
    }

    /**
     * 拉线灵敏度
     *
     * @param context
     * @return
     */
    public static float pullSen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        return sp.getFloat(KEY_PS, DEFAULT_PULL_SENSITIVITY);
    }


    /**
     * 获取是否在桌面显示 <功能简述>
     *
     * @param context
     * @return
     */
    public static boolean isOnlyDestop(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_ONLY_DESTOP, false);
    }


    /**
     * 服务是否需要运行 <功能简述>
     *
     * @param context
     * @param isRunning
     */
    public static void setIsRunning(Context context, boolean isRunning) {
        SharedPreferences sp = context.getSharedPreferences(EXT_CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_IS_RUNNING, isRunning).commit();
    }

    /**
     * 服务是否需要运行 <功能简述>
     *
     * @param context
     */
    public static boolean isRunning(Context context) {
        SharedPreferences sp = context.getSharedPreferences(EXT_CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_IS_RUNNING, false);
    }
}
