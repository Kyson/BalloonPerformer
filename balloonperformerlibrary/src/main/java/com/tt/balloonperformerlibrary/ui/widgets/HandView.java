package com.tt.balloonperformerlibrary.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.tt.balloonperformerlibrary.BalloonPerformer;
import com.tt.balloonperformerlibrary.R;
import com.tt.balloonperformerlibrary.utils.ViewUtil;

/**
 * 拉手的视图
 *
 * @author Kyson
 */
public class HandView extends View {
    /**
     * loose hand,when true,ballon can fly now  <Br>
     *
     * @author kysonX
     */
    public interface onLooseListener {
        void onLoose(boolean canFly, int x, int y);
    }

    /**
     * {@link HandView} moved ,and {@link LineView} move
     *
     * @author kysonX
     */
    public interface onHandMovedListener {
        void onHandMoved(int x, int y);
    }

    private Context mContext;

    private WindowManager.LayoutParams mLp;

    private WindowManager mWindowManager;

    private onLooseListener mOnLooseListener;

    private onHandMovedListener mOnHandMovedListener;

    private Paint mPaint;

    private boolean mIsGrasp;

    private Bitmap mHandGrasp;

    private Bitmap mHandLoose;

    private int mOriY;

    public HandView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public HandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        this.mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mHandGrasp = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.grasp);
        mHandLoose = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.loose);
        mIsGrasp = true;
    }

    /**
     * attach handview to {@link WindowManager}
     *
     * @param y
     */
    public void attachToWindow(int x, int y) {
        if (this.getParent() != null) {
            return;
        }
        mLp = new WindowManager.LayoutParams();
        mLp.type = LayoutParams.TYPE_SYSTEM_ALERT;
        mLp.format = PixelFormat.RGBA_8888;
        mLp.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        mLp.gravity = Gravity.LEFT | Gravity.TOP;
        mLp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLp.x = x;
        mOriY = y;
        mLp.y = mOriY;
        mWindowManager.addView(this, mLp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = null;
        if (mIsGrasp) {
            bitmap = mHandGrasp;
        } else {
            bitmap = mHandLoose;
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                ViewUtil.measureSize(widthMeasureSpec, mHandGrasp.getWidth()),
                ViewUtil.measureSize(heightMeasureSpec,
                        mHandGrasp.getHeight()));
    }

    private float mViewX;
    private float mViewY;

    private float mRawX;
    private float mRawY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidateByStatus(true);
                mViewX = event.getX();
                mViewY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidateByStatus(true);
                mRawX = event.getRawX();
                mRawY = event.getRawY() - ViewUtil.getStatusBarHeight(mContext);
                updateViewPositionWithNotify((int) (mRawX - mViewX),
                        (int) (mRawY - mViewY));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                onActionUp(event);
                break;
        }
        return true;
    }

    private void onActionUp(MotionEvent event) {
        invalidateByStatus(false);
        onActionLoose();
    }

    /**
     * on hand loose
     */
    private void onActionLoose() {
        if (mOnLooseListener != null) {
            int len = ViewUtil.getStatusBarHeight(mContext)
                    + BalloonPerformer.getInstance().getConfig().getLineLength(mContext);
            mOnLooseListener.onLoose(
                    mLp.y > len * BalloonPerformer.getInstance().getConfig().getPullSensitivity(mContext), mLp.x,
                    mLp.y);
        }
    }

    /**
     * put the hand to top
     */
    public void backToTop() {
        invalidateByStatus(true);
        ValueAnimator animation = ValueAnimator.ofInt(mLp.y, mOriY);
        animation.setDuration(250);
        animation.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (Integer) animation.getAnimatedValue();
                updateViewPosition(mLp.x, y);
            }
        });
        animation.start();
    }

    /**
     * 释放资源
     */
    public void release() {
        if (this.getParent() != null) {
            mWindowManager.removeView(this);
        }
    }

    /**
     * update {@link HandView} positon when moved auto
     */
    private void updateViewPosition(int x, int y) {
        if (this.getParent() == null) {
            return;
        }
        mLp.x = x;
        mLp.y = y;
        mWindowManager.updateViewLayout(this, mLp);
    }

    /**
     * when moved by touch,notify {@link HandView} moved event
     *
     * @param x
     * @param y
     */
    private void updateViewPositionWithNotify(int x, int y) {
        updateViewPosition(x, y);
        if (mOnHandMovedListener != null) {
            mOnHandMovedListener.onHandMoved(mLp.x, mLp.y);
        }
    }

    private void invalidateByStatus(boolean isGrasp) {
        this.mIsGrasp = isGrasp;
        postInvalidate();
    }

    /**
     * @return 返回 mOnLooseListener
     */
    public onLooseListener getOnLooseListener() {
        return mOnLooseListener;
    }

    /**
     * @param mOnLooseListener
     */
    public void setOnLooseListener(onLooseListener mOnLooseListener) {
        this.mOnLooseListener = mOnLooseListener;
    }

    /**
     * @return 返回 mOnHandMovedListener
     */
    public onHandMovedListener getOnHandMovedListener() {
        return mOnHandMovedListener;
    }

    /**
     * @param mOnHandMovedListener
     */
    public void setOnHandMovedListener(onHandMovedListener mOnHandMovedListener) {
        this.mOnHandMovedListener = mOnHandMovedListener;
    }

    /**
     * get content width,actually the bitmap width
     *
     * @return
     */
    public int getContentWidth() {
        return mHandGrasp.getWidth();
    }

    /**
     * get {@link HandView} position
     *
     * @return
     */
    // public int[] getHandPos() {
    // return new int[] { mLp.x, mLp.y };
    // }
}
