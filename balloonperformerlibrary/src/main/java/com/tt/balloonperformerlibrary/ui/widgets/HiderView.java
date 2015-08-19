/*
 * FileName:	HiderView.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-24 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.ui.widgets;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tt.balloonperformerlibrary.R;


/**
 * 拖动到此view隐藏
 * 
 * @author kysonX
 */
public class HiderView extends LinearLayout {
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLp;

    public HiderView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public HiderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.widget_hiderview_ll, this);
    }

    public void attachToWindow() {
        if (this.getParent() != null) {
            return;
        }
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mLp = new WindowManager.LayoutParams();
        mLp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mLp.format = PixelFormat.RGBA_8888;
        mLp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLp.gravity = Gravity.CENTER | Gravity.TOP;
        mLp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        int y = mWindowManager.getDefaultDisplay().getHeight();
        mLp.y = y;
        mWindowManager.addView(this, mLp);
    }

    public void release() {
        if (this.getParent() != null) {
            mWindowManager.removeView(this);
        }
    }

    public int[] getPos() {
        int[] pos = new int[2];
        pos[0] = mLp.x;
        pos[1] = mLp.y;
        return pos;
    }
}
