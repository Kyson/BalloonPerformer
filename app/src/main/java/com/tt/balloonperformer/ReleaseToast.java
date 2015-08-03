/*
 * FileName:	ToastUtil.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-23 1.00 初始版本
 */
package com.tt.balloonperformer;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.tt.balloonperformerlibrary.R;


/**
 * <功能简述> </Br> <功能详细描述> </Br>
 * 
 * @author kysonX
 */
public class ReleaseToast extends Toast {
    private Context mContext;

    private TextView mTextView;

    /**
     * <默认构造函数>
     */
    public ReleaseToast(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER, 0, 0);
        mTextView = new TextView(mContext);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.girl, 0,
                0, 0);
        mTextView.setCompoundDrawablePadding(18);
        mTextView.setTextSize(14);
        mTextView.setPadding(24, 12, 24, 12);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(mContext.getResources().getColor(R.color.white));
        mTextView.setBackgroundResource(R.drawable.toast_bg);
        setView(mTextView);
        setDuration(Toast.LENGTH_SHORT);
    }

    public void setToastText(CharSequence text) {
        mTextView.setText(text);
    }

    public static void showToast(Context context, CharSequence text) {
        ReleaseToast toast = new ReleaseToast(context);
        toast.setToastText(text);
        toast.show();
    }

}
