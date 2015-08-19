/*
 * FileName:	ReleaseViewManager.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-20 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.ui.widgets;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.tt.balloonperformerlibrary.BalloonPerformer;
import com.tt.balloonperformerlibrary.services.ReleaseService;
import com.tt.balloonperformerlibrary.ui.widgets.BalloonGroup.OnBalloonFlyedListener;
import com.tt.balloonperformerlibrary.ui.widgets.HandView.onHandMovedListener;
import com.tt.balloonperformerlibrary.ui.widgets.HandView.onLooseListener;
import com.tt.balloonperformerlibrary.ui.widgets.LineView.OnLinePackedListener;
import com.tt.balloonperformerlibrary.utils.ViewUtil;


/**
 * 视图总体的管理<Br>
 * API start and end control the {@link WindowManager}<Br>
 *
 * @author kysonX
 */
public class ReleaseViewManager {
    // 当前的悬浮窗是否正在运行
    private boolean mIsRunning = false;

    private static ReleaseViewManager sInstance;

    private Context mContext;

    private WindowManager mWindowManager;

    // manager of ballons
    private BalloonGroup mBalloonGroup;
    // which can control ballons
    private HandView mHandView;
    // which moved base on handview
    private LineView mLineView;

    private HiderView mHiderView;

    public static ReleaseViewManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ReleaseViewManager(context);
        }
        return sInstance;
    }

    private ReleaseViewManager(Context context) {
        this.mContext = context;
    }

    /**
     * start show views ,actually only handview
     */
    public void start() {
        Log.i("kyson", "Start ReleaseViewManager");
        end();
        mHandView = new HandView(mContext);
        mLineView = new LineView(mContext);
        mBalloonGroup = new BalloonGroup(mContext);
        mHiderView = new HiderView(mContext);
        int y = ViewUtil.getStatusBarHeight(mContext)
                + BalloonPerformer.getInstance().getConfig().getLineLength(mContext);
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int x = mWindowManager.getDefaultDisplay().getWidth() * 3 / 4;
        mHandView.attachToWindow(x, y);
        mLineView.attachToWindow(x, y, mHandView.getContentWidth() / 2);
        setupListener();
        mIsRunning = true;
    }

    /**
     * release view from {@link WindowManager}
     */
    public void end() {
        if (mHandView != null) {
            mHandView.release();
            mHandView = null;
        }
        if (mLineView != null) {
            mLineView.release();
            mLineView = null;
        }
        if (mBalloonGroup != null) {
            mBalloonGroup.release();
            mBalloonGroup = null;
        }
        if (mHiderView != null) {
            mHiderView.release();
            mHiderView = null;
        }
        mIsRunning = false;
    }

    private void setupListener() {
        mHandView.setOnLooseListener(new onLooseListener() {

            @Override
            public void onLoose(boolean canFly, int x, int y) {
                if (mLineView != null) {
                    mLineView.pack(true);
                }
                if (mHiderView != null && mHandView != null) {
                    int bHandY = y + mHandView.getHeight()
                            + ViewUtil.getStatusBarHeight(mContext);
                    int tHiderY = mWindowManager.getDefaultDisplay()
                            .getHeight() - mHiderView.getHeight();

                    int tHandY = y;
                    int bHiderY = mWindowManager.getDefaultDisplay()
                            .getHeight();

                    int rHandX = x + mHandView.getWidth();
                    int lHiderX = mWindowManager.getDefaultDisplay().getWidth()
                            / 2 - mHiderView.getWidth() / 2;

                    int lHandX = x;
                    int rHiderX = mWindowManager.getDefaultDisplay().getWidth()
                            / 2 + mHiderView.getWidth() / 2;

                    if (rHandX >= lHiderX && lHandX <= rHiderX
                            && bHandY >= tHiderY && tHandY <= bHiderY) {
                        // 滑动的位置在隐藏视图内
                        ReleaseService.stopService(mContext);
                    } else {
                        mHiderView.release();
                    }
                }
                if (canFly && mBalloonGroup != null) {
                    mBalloonGroup.startFly();
                }
            }
        });
        mHandView.setOnHandMovedListener(new onHandMovedListener() {

            @Override
            public void onHandMoved(int x, int y) {
                if (mLineView != null) {
                    mLineView.updateByPos(x, y, mHandView.getContentWidth() / 2);
                }
                if (mHiderView != null) {
                    mHiderView.attachToWindow();
                }
            }
        });
        mLineView.setOnLinePackedListener(new OnLinePackedListener() {

            @Override
            public void onLinePacked() {
                if (mHandView != null) {
                    mHandView.backToTop();
                }

            }
        });
        mBalloonGroup.setOnBalloonFlyedListener(new OnBalloonFlyedListener() {

            @Override
            public void onBalloonFlyed() {
                // 释放内存
                // releaseMemory();
                if (BalloonPerformer.getInstance().getOnBalloonFlyedListener() != null) {
                    BalloonPerformer.getInstance().getOnBalloonFlyedListener().onBalloonFlyed();
                }
            }
        });
    }


    /**
     * @return 返回 mIsRunning
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * @param mIsRunning
     */
    public void setIsRunning(boolean mIsRunning) {
        this.mIsRunning = mIsRunning;
    }
}
