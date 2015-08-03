/*
 * FileName:	AppInstallReceiver.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		Kyson
 * Description:	<文件描述>
 * History:		2014-9-25 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tt.balloonperformerlibrary.storage.PackageInfoStorage;

/**
 * 应用安装卸载的广播<功能简述> </Br> <功能详细描述> </Br>
 * 
 * @author Kyson
 */
public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 更新安装包列表
        PackageInfoStorage.updateHomeList(context);
    }
}
