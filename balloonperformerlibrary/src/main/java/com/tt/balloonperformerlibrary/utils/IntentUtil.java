package com.tt.balloonperformerlibrary.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * intent相关工具类 <功能简述> </Br> <功能详细描述> </Br>
 * 
 * @author Kyson
 */
public class IntentUtil {
    /**
     * 获取intent启动（没有满足intent条件就不启动） <功能简述>
     * 
     * @param context
     * @param intent
     */
    public static void startIntent(Context context, Intent intent) {
        boolean b = isIntentExist(context, intent);
        if (b) {
            context.startActivity(intent);
        }
    }

    /**
     * 满足intent的应用是否存在 <功能简述>
     * 
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentExist(Context context, Intent intent) {
        List<ResolveInfo> localList = context.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.GET_INTENT_FILTERS);
        if ((localList != null) && (localList.size() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取应用市场应用详情的intent <功能简述>
     * 
     * @param context
     * @return
     */
    public static Intent getMarketIntent(Context context) {
        StringBuilder localStringBuilder = new StringBuilder()
                .append("market://details?id=");
        String str = context.getPackageName();
        localStringBuilder.append(str);
        Uri localUri = Uri.parse(localStringBuilder.toString());
        return new Intent(Intent.ACTION_VIEW, localUri);
    }

    /**
     * 获取文本分享的itent <功能简述>
     * 
     * @param context
     * @return
     */
    public static Intent getShareIntent(Context context, String subject,
            String content, String title) {
        // 启动分享发送的属性
        Intent intent = new Intent(Intent.ACTION_SEND);
        // 分享发送的数据类型
        intent.setType("text/plain");
        // 分享的主题
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        // 分享的内容
        intent.putExtra(Intent.EXTRA_TEXT, content);
        // 目标应用选择对话框的标题
        return Intent.createChooser(intent, title);
    }

}
