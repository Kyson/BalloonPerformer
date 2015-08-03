package com.tt.balloonperformer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.Formatter;
import android.view.View;

import com.tt.balloonperformerlibrary.BalloonPerformer;
import com.tt.balloonperformerlibrary.configs.Config;
import com.tt.balloonperformerlibrary.ui.widgets.BalloonGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Config.Builder builder = new Config.Builder(MainActivity.this);
        Config config = builder.pullSensitivity(2.0f).lineLength(64).isOnlyDestop(false).flyDuration(3000).balloonCount(6).create();

        BalloonPerformer.getInstance().init(MainActivity.this, config);
    }

    public void start(View view) {
        BalloonPerformer.getInstance().show(MainActivity.this, new BalloonGroup.OnBalloonFlyedListener() {
            @Override
            public void onBalloonFlyed() {
                releaseMemory(MainActivity.this);
            }
        });
    }

    public void stop(View view) {
        BalloonPerformer.getInstance().gone(MainActivity.this);
    }

    /**
     * 释放内存 <功能简述>
     */
    public static void releaseMemory(final Context context) {
        ReleasePhoneMemoryTask releasePhoneMemoryTask = new ReleasePhoneMemoryTask(
                context) {

            @Override
            protected void onPostExecute(Long result) {
                String s;
                if (result <= 0) {
                    s = "当前已是最佳状态！";
                } else {
                    s = "已经为您清理" + "<font color='#4898eb'>"
                            + Formatter.formatShortFileSize(context, result)
                            + "</font>" + "内存！";
                }
                ReleaseToast.showToast(context, Html.fromHtml(s));
            }
        };
        releasePhoneMemoryTask.execute();
    }

}
