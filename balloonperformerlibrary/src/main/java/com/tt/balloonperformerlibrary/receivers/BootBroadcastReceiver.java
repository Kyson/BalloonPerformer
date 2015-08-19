/*
 * FileName:	BootReceiver.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-24 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tt.balloonperformerlibrary.services.ReleaseService;
import com.tt.balloonperformerlibrary.storage.PreferenceHelper;
import com.tt.balloonperformerlibrary.utils.ServiceUtil;


/**
 * 开机启动接收器
 *
 * @author kysonX
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    // 重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean b = ServiceUtil.isWorked(context,
                ReleaseService.class.getName());
        if (PreferenceHelper.isRunning(context) && !b) {
            ReleaseService.startService(context);
        }
    }

}
