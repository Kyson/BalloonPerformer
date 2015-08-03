package com.tt.balloonperformerlibrary.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.tt.balloonperformerlibrary.BalloonPerformer;
import com.tt.balloonperformerlibrary.storage.PackageInfoStorage;
import com.tt.balloonperformerlibrary.storage.PreferenceHelper;
import com.tt.balloonperformerlibrary.ui.widgets.ReleaseViewManager;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 启动桌面悬浮窗service <功能简述> <Br> <功能详细描述> <Br>
 *
 * @author kysonX
 */
public class ReleaseService extends Service {

    public static abstract class RefreshHandler extends Handler {
        private final WeakReference<ReleaseService> mReference;

        public RefreshHandler(ReleaseService releaseService) {
            mReference = new WeakReference<ReleaseService>(releaseService);
        }

        @Override
        public void handleMessage(Message msg) {
            ReleaseService releaseService = mReference.get();
            if (releaseService != null) {
                handle(msg);
            }
        }

        public abstract void handle(Message msg);

    }

    private final RefreshHandler mRefreshHandler = new RefreshHandler(this) {

        @Override
        public void handle(Message msg) {

        }
    };

    private ReleaseViewManager mReleaseViewManager;

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer mTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startForeground(1, notification);
        Log.i("kyson","ReleaseService onStartCommand");
        if (mTimer == null) {
            mReleaseViewManager = ReleaseViewManager.getInstance(this);
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new RefreshTask(), 0, 1000);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReleaseViewManager.end();
        mReleaseViewManager = null;
        mTimer.cancel();
        mTimer = null;
        stopForeground(true);
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            Log.i("kyson","ReleaseService RefreshTask run");
            if (!BalloonPerformer.getInstance().getConfig().isOnlyDestop(ReleaseService.this)) {
                // 所有情况都显示的话
                startWithCheck();
                return;
            }
            // 当前界面是桌面
            if (PackageInfoStorage.isHome(ReleaseService.this)) {
                startWithCheck();
            }
            // 当前界面不是桌面
            else {
                endWithCheck();
            }
        }
    }

    private void startWithCheck() {
        mRefreshHandler.post(new Runnable() {

            @Override
            public void run() {
                Log.i("kyson","ReleaseService startWithCheck");
                if (mReleaseViewManager != null
                        && !mReleaseViewManager.isRunning()) {
                    mReleaseViewManager.start();
                }
            }
        });
    }

    private void endWithCheck() {
        mRefreshHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mReleaseViewManager != null
                        && mReleaseViewManager.isRunning()) {
                    mReleaseViewManager.end();
                }
            }
        });
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, ReleaseService.class);
        context.startService(intent);
        // 用户想要打开悬浮窗
        PreferenceHelper.setIsRunning(context, true);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, ReleaseService.class);
        context.stopService(intent);
        // 用户想要关闭悬浮窗
        PreferenceHelper.setIsRunning(context, false);
    }
}
