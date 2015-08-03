package com.tt.balloonperformerlibrary;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by Kyson on 2015/7/31.
 */
public class PerformerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        BalloonPerformer.getInstance().delegateOnConfigurationChanged(this);
    }
}
