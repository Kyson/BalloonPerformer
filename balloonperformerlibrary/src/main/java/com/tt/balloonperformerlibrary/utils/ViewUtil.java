/*
 * FileName:	ViewUtil.java
 * Copyright:	kyson
 * Author: 		kysonX
 * Description:	<文件描述>
 * History:		2014-11-21 1.00 初始版本
 */
package com.tt.balloonperformerlibrary.utils;

import android.content.Context;
import android.view.View.MeasureSpec;

import java.lang.reflect.Field;

/**
 * View相关的工具类
 *
 * @author kysonX
 */
public class ViewUtil {
    // status bar height
    public static int sStatusBarHeight;

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                sStatusBarHeight = context.getResources()
                        .getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sStatusBarHeight;
    }

    public static int measureSize(int measureSpec, int wantSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // wrap_content的时候宽度最小
            result = wantSize;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
