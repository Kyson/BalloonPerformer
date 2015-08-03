/*
 * FileName:	PhoneUtil.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		Kyson
 * Description:	<文件描述>
 * History:		2014-8-18 1.00 初始版本
 */
package com.tt.balloonperformer;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * 手机相关工具<功能简述> <Br>
 * <功能详细描述> <Br>
 * 
 * @author Kyson
 */
public class PhoneMemoryUtil {
    /**
     * 释放手机内存，清理缓存 <功能简述>
     * 
     * @param context
     */
    public static void releaseMemory(Context context) {
        ActivityManager activityManger = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManger
                .getRunningAppProcesses();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
                String[] pkgList = apinfo.pkgList;
                if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE
                        && !apinfo.processName.equals("com.tt.realeasememory")) {
                    for (int j = 0; j < pkgList.length; j++) {
                        if (Build.VERSION.SDK_INT >= 8) {
                            activityManger.killBackgroundProcesses(pkgList[j]);
                        } else {
                            activityManger.restartPackage(pkgList[j]);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取已经使用内存的百分比 <功能简述>
     * 
     * @return
     */
    public static int getMemoryRatio(Context context) {
        long availMemory = getAvailMemory(context);
        long totalMemory = getTotalMemory(context);
        int momeryProgress = (int) ((totalMemory - availMemory) * 100 / totalMemory);
        if (momeryProgress < 0) {
            momeryProgress = 0;
        } else if (momeryProgress > 100) {
            momeryProgress = 100;
        }
        return momeryProgress;
    }

    /**
     * 得到当前可用内存大小
     * 
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;// 将获取的内存大小规格化
    }

    /**
     * 得到手机系统内存大小
     * 
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory;// Byte转换为KB或者MB，内存大小规格化
    }

}
