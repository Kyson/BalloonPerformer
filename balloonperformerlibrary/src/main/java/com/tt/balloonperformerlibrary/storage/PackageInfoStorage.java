package com.tt.balloonperformerlibrary.storage;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

/**
 * 存储已经安装的包信息
 */
public class PackageInfoStorage {

    private static List<String> sHomeList = new ArrayList<String>();

    /**
     * 更新属于桌面的应用的应用包名称
     *
     */
    public static void updateHomeList(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        sHomeList = names;
    }

    public static List<String> getHomeList() {
        return sHomeList;
    }

    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        boolean isHome = PackageInfoStorage.getHomeList().contains(
                rti.get(0).topActivity.getPackageName());
        Log.i("kyson", "ishome:" + isHome);
        return isHome;
    }

}
