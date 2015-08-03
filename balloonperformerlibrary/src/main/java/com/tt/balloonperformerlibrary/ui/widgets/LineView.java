package com.tt.balloonperformerlibrary.ui.widgets;

import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.tt.balloonperformerlibrary.helpers.BalloonHelper;
import com.tt.balloonperformerlibrary.utils.ViewUtil;


/**
 * 线的view <功能简述> <Br>
 * <功能详细描述> <Br>
 * 
 * @author Kyson
 */
public class LineView extends View {
    /**
     * 绳子收起的监听器 <功能简述> </Br> <功能详细描述> </Br>
     * 
     * @author kysonX
     */
    public interface OnLinePackedListener {
        void onLinePacked();
    }

    private static int[] colors;

    private Context mContext;

    private WindowManager mWindowManager;

    private FrameLayout mContainer;

    private Paint mPaint;

    private float mLineLength;

    private OnLinePackedListener mOnLinePackedListener;

    private float mOriY;

    public LineView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(15f);
        mPaint.setAntiAlias(true);
        colors = BalloonHelper.getColorArray(mContext);
        this.mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mLineLength = mWindowManager.getDefaultDisplay().getHeight();
    }

    /**
     * attach {@link LineView} to {@link WindowManager} <功能简述>
     * 
     * @param offsetX
     */
    public void attachToWindow(int x, int y, int offsetX) {
        setContainer();
        addLine(x, y, offsetX);
    }

    /**
     * {@link Set} container <功能简述>
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

    private void addLine(int x, int y, int offsetX) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mOriY = -mLineLength + y + ViewUtil.getStatusBarHeight(mContext);
        // 如果view没有被加入到某个父组件中，则加入WindowManager中
        if (this.getParent() == null) {
            mContainer.addView(this, lp);
            // ViewHelper.setTranslationY(this, mOriY);
            // ViewHelper.setTranslationX(this, x);
            updateByPos(x, y, offsetX);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = colors.length;
        for (int i = 0; i < size; i++) {
            // 分七段
            mPaint.setColor(colors[i]);
            canvas.drawLine(0, (mLineLength * i / size), 0, (mLineLength
                    * (i + 1) / size), mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                ViewUtil.measureWidth(widthMeasureSpec,
                        (int) mPaint.getStrokeWidth()),
                ViewUtil.measureHeight(heightMeasureSpec, (int) mLineLength));
    }

    /**
     * 绳子收起 <功能简述>
     */
    public void pack(final boolean isNotify) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY",
                mOriY);
        animator.setDuration(500);
        animator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                if (isNotify && mOnLinePackedListener != null) {
                    mOnLinePackedListener.onLinePacked();
                }
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                if (isNotify && mOnLinePackedListener != null) {
                    mOnLinePackedListener.onLinePacked();
                }
            }
        });
        animator.start();
    }

    /**
     * 根据横向位置和纵向高度更新视图 <功能简述>
     * 
     * @param x
     * @param offsetX
     */
    public void updateByPos(int x, int y, int offsetX) {
        if (this.getParent() == null) {
            return;
        }
        ViewHelper.setTranslationX(this, x + offsetX);
        ViewHelper.setTranslationY(this,
                y - mLineLength + ViewUtil.getStatusBarHeight(mContext));
    }

    /**
     * 释放资源 <功能简述>
     */
    public void release() {
        if (mContainer != null && mContainer.getParent() != null) {
            mContainer.removeAllViews();
            mWindowManager.removeView(mContainer);
        }
    }

    /**
     * @return 返回 mOnLinePackedListener
     */
    public OnLinePackedListener getOnLinePackedListener() {
        return mOnLinePackedListener;
    }

    /**
     * @param mOnLinePackedListener
     */
    public void setOnLinePackedListener(
            OnLinePackedListener mOnLinePackedListener) {
        this.mOnLinePackedListener = mOnLinePackedListener;
    }

}
