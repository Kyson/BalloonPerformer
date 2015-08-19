/*
 * FileName:	BallonGroup.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-18 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.ui.widgets;


import android.content.Context;
import android.graphics.PixelFormat;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tt.balloonperformerlibrary.BalloonPerformer;
import com.tt.balloonperformerlibrary.helpers.BalloonHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 气球视图管理
 *
 * @author kysonX
 */
public class BalloonGroup {
    public interface OnBalloonFlyedListener {
        void onBalloonFlyed();
    }

    // 可配置 气球个数
    // public static final int BALLON_COUNT = 5;

    private Context mContext;
    private List<Balloon> mBalloons;

    private WindowManager mWindowManager;
    private FrameLayout mContainer;

    private OnBalloonFlyedListener mOnBalloonFlyedListener;

    public BalloonGroup(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mBalloons = new ArrayList<Balloon>();
    }

    /**
     * 释放气球
     */
    public void startFly() {
        // 设置容器
        setContainer();
        // 添加若干气球
        addContent();
        // 执行动画
        performAnim();
    }

    /**
     * 设置容器
     */
    private void setContainer() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.RGBA_8888;

        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCHABLE;
        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.FILL_PARENT;
        wmParams.height = WindowManager.LayoutParams.FILL_PARENT;

        if (mContainer == null) {
            mContainer = new FrameLayout(mContext);
        }
        // 添加容器
        if (mContainer.getParent() == null) {
            mWindowManager.addView(mContainer, wmParams);
        }
    }

    /**
     * 添加气球
     */
    private void addContent() {
        for (int i = 0; i < BalloonPerformer.getInstance().getConfig().getBalloonCount(mContext); i++) {
            int[] pos = BalloonHelper.getBallonPosByRandom(mContext, mWindowManager);
            int[] size = BalloonHelper.getBallonSizeByRandom(mContext, mWindowManager);
            int color = BalloonHelper.getRandomColor();
            addBallonByPosition(pos[0], pos[1], color, size[0], size[1]);
        }
    }

    /**
     * 添加一个气球
     *
     * @param x
     * @param y
     * @param color
     * @param width
     * @param height
     */
    private Balloon addBallonByPosition(int x, int y, int color, int width,
                                        int height) {
        Balloon balloon = new Balloon(mContext);
        balloon.setParams(new Balloon.Builder().setColor(
                mContext.getResources().getColor(color)).build());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width,
                height);
        // 固定位置
        lp.setMargins(x, y, 0, 0);
        // 如果view没有被加入到某个父组件中，则加入WindowManager中
        if (balloon.getParent() == null) {
            mContainer.addView(balloon, lp);
            mBalloons.add(balloon);
            balloon.startFly();
            return balloon;
        }
        return null;
    }

    private void performAnim() {
        for (final Balloon ballon : mBalloons) {
            int offset = BalloonHelper.getMaxHeight(mContext, mWindowManager);
            int transY = -(mWindowManager.getDefaultDisplay().getHeight() + offset);
            ObjectAnimator animator = ObjectAnimator.ofFloat(ballon,
                    "translationY", transY);
            animator.setDuration(BalloonHelper.getRandomFlyDuration(mContext));
            AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
            animator.setInterpolator(accelerateInterpolator);
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {

                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                    onEnd(ballon);
                }

                @Override
                public void onAnimationCancel(Animator arg0) {
                    onEnd(ballon);
                }
            });
            animator.start();
        }
    }

    /**
     * 动画结束释放资源
     *
     * @param balloon
     */
    private void onEnd(Balloon balloon) {
        if (balloon != null && balloon.getParent() != null) {
            mContainer.removeView(balloon);
        }
        if (mContainer != null && mContainer.getChildCount() <= 0
                && mContainer.getParent() != null) {
            if (mOnBalloonFlyedListener != null) {
                mOnBalloonFlyedListener.onBalloonFlyed();
            }
            mWindowManager.removeView(mContainer);
        }
    }

    /**
     * 释放全部资源
     */
    public void release() {
        for (Balloon balloon : mBalloons) {
            onEnd(balloon);
        }
        mBalloons.clear();
    }

    /**
     * @return 返回 mOnBalloonFlyedListener
     */
    public OnBalloonFlyedListener getOnBalloonFlyedListener() {
        return mOnBalloonFlyedListener;
    }

    /**
     * @param mOnBalloonFlyedListener
     */
    public void setOnBalloonFlyedListener(
            OnBalloonFlyedListener mOnBalloonFlyedListener) {
        this.mOnBalloonFlyedListener = mOnBalloonFlyedListener;
    }

}
