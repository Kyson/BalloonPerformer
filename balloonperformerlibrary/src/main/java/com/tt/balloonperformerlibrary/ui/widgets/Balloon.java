package com.tt.balloonperformerlibrary.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tt.balloonperformerlibrary.utils.ViewUtil;


/**
 * 气球view <功能简述> </Br> <功能详细描述> </Br>
 *
 * @author Kyson
 */
public class Balloon extends View {
    // 默认颜色
    public static final int COLOR = Color.RED;
    // 默认振幅
    public static final int AMPLITUDE = 15;
    // 默认振动时间
    public static final long DURATION = 350;
    // 气球线宽度
    public static final int LINE_WIDTH = 3;
    // 三角形的宽度占比
    private static final float TRIANGLE_FLOAT_W = 0.1F;
    // 三角形的高度占比
    private static final float TRIANGLE_FLOAT_H = 0.025F;
    // 刷新间隔
    private int REFRESH_DURATION = 35;

    private Paint mPaint;
    // 外部设置的参数
    private Params mParams;

    private int mCurrentAmplitude;

    private RectF mOval;

    private Path mPath;

    public Balloon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Balloon(Context context) {
        super(context);
        init();
    }

    private void init() {
        mParams = new Params();
        mPaint = new Paint();
        mPaint.setStrokeWidth(LINE_WIDTH);
        mPaint.setStyle(Style.STROKE);
        mPaint.setAntiAlias(true);
        mCurrentAmplitude = mParams.amplitude;
        mOval = new RectF();
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据参数初始化画笔
        mPaint.setColor(mParams.color);
        mPaint.setStyle(Style.FILL_AND_STROKE);
        // 第一步：气球体，椭圆
        mOval.set(0 + LINE_WIDTH, LINE_WIDTH, getWidth() - LINE_WIDTH,
                getHeight() * 0.4f);
        canvas.drawOval(mOval, mPaint);

        mPaint.setStyle(Style.STROKE);
        mPath.reset();
        // 第二步：三角形，椭圆底部
        mPath.moveTo(getWidth() * 0.5f, getHeight() * 0.4f);
        mPath.lineTo(getWidth() * 0.5f - getWidth() * TRIANGLE_FLOAT_W,
                getHeight() * 0.4f + getHeight() * TRIANGLE_FLOAT_H);
        mPath.lineTo(getWidth() * 0.5f + getWidth() * TRIANGLE_FLOAT_W,
                getHeight() * 0.4f + getHeight() * TRIANGLE_FLOAT_H);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        // 第三步:线，3条贝塞尔曲线构成,第三条为一半

        // 设置Path的起点在椭圆尾部
        mPath.moveTo(getWidth() * 0.5f, getHeight() * 0.4f + getHeight()
                * 0.025f);
        mPath.quadTo(getWidth() * 0.5f - mCurrentAmplitude,
                getHeight() * 0.52f, getWidth() * 0.5f, getHeight() * 0.64f);
        canvas.drawPath(mPath, mPaint);

        // 第二步：第二条贝塞尔线
        mPath.moveTo(getWidth() * 0.5f, getHeight() * 0.64f);
        mPath.quadTo(getWidth() * 0.5f + mCurrentAmplitude,
                getHeight() * 0.76f, getWidth() * 0.5f, getHeight() * 0.88f);
        canvas.drawPath(mPath, mPaint);

        // 第二步：第三条一半的贝塞尔曲线
        mPath.moveTo(getWidth() * 0.5f, getHeight() * 0.88f);
        mPath.quadTo(getWidth() * 0.5f - mCurrentAmplitude / 4,
                getHeight() * 0.91f, getWidth() * 0.5f - mCurrentAmplitude / 4,
                getHeight() * 0.91f);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                ViewUtil.measureWidth(widthMeasureSpec, AMPLITUDE * 2),
                ViewUtil.measureHeight(heightMeasureSpec, AMPLITUDE * 8));
    }

    private boolean mIsFlying = false;

    /**
     * 气球线摆动 <功能简述>
     */
    public void startFly() {
        mIsFlying = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                int t = 0;
                while (mIsFlying) {
                    if (t <= mParams.duration) {
                        double d = Math.cos((2 * Math.PI * t)
                                / mParams.duration);
                        mCurrentAmplitude = (int) (d * ((2 * mParams.amplitude) > getWidth() ? (getWidth() / 2)
                                : mParams.amplitude));
                        postInvalidate();
                        t = t + REFRESH_DURATION;
                        try {
                            Thread.sleep(REFRESH_DURATION);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        t = 0;
                    }
                }
            }
        }).start();
    }

    public void stopFly() {
        mIsFlying = false;
    }

    public void setParams(Params p) {
        this.mParams = p;
    }

    /**
     * 外部设置参数
     */
    public static class Params {
        // 气球颜色
        public int color;
        // 摆动幅度
        public int amplitude;
        // 摆动一周的时间
        public long duration;

        public Params() {
            color = COLOR;
            amplitude = AMPLITUDE;
            duration = DURATION;
        }
    }

    public static class Builder {
        private Params mParams;

        public Builder() {
            mParams = new Params();
        }

        public Builder setColor(int color) {
            mParams.color = color;
            return this;
        }

        public Builder setAmplitude(int amplitude) {
            mParams.amplitude = amplitude;
            return this;
        }

        public Builder setDuration(long duration) {
            mParams.duration = duration;
            return this;
        }

        public Params build() {
            return mParams;
        }
    }

}
