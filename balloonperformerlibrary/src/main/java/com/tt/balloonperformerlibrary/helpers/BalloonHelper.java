package com.tt.balloonperformerlibrary.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseIntArray;
import android.view.WindowManager;

import com.tt.balloonperformerlibrary.BalloonPerformer;
import com.tt.balloonperformerlibrary.R;


public class BalloonHelper {

    private static SparseIntArray sColorArray;

    static {
        sColorArray = new SparseIntArray();
        sColorArray.put(0, R.color.common_purple);
        sColorArray.put(1, R.color.common_indigo);
        sColorArray.put(2, R.color.common_blue);
        sColorArray.put(3, R.color.common_green);
        sColorArray.put(4, R.color.common_yellow);
        sColorArray.put(5, R.color.common_orange);
        sColorArray.put(6, R.color.common_red);
        sColorArray.put(7, R.color.common_brown);
        sColorArray.put(8, R.color.common_brown_pressed);
        sColorArray.put(9, R.color.common_purple_pressed);
        sColorArray.put(10, R.color.common_indigo_pressed);
        sColorArray.put(11, R.color.common_blue_pressed);
        sColorArray.put(12, R.color.common_green_pressed);
        sColorArray.put(13, R.color.common_yellow_pressed);
        sColorArray.put(14, R.color.common_orange_pressed);
        sColorArray.put(15, R.color.common_red_pressed);
    }

    /**
     * 获取颜色值 <功能简述>
     *
     * @param context
     * @return
     */
    public static int[] getColorArray(Context context) {
        int length = sColorArray.size() / 2;
        int[] colorArray = new int[length];
        Resources res = context.getResources();
        for (int i = 0; i < length; i++) {
            colorArray[i] = res.getColor(sColorArray.get(i));
        }
        return colorArray;
    }

    /**
     * 从一个列表中取出一个颜色<功能简述>
     *
     * @return
     */
    public static int getRandomColor() {
        int size = sColorArray.size();
        int index = (int) (Math.random() * size);
        return sColorArray.get(index);
    }

    /**
     * 获取随机的初始位置 <功能简述>
     *
     * @param manager
     * @return
     */
    public static int[] getBallonPosByRandom(Context context, WindowManager manager) {
        int[] pos = new int[2];
        pos[1] = manager.getDefaultDisplay().getHeight();

        int floatP = manager.getDefaultDisplay().getWidth()
                - getMinWidth(context, manager);
        pos[0] = (int) (Math.random() * floatP);
        return pos;
    }

    /**
     * 随机获取气球大小 <功能简述>
     *
     * @param manager
     * @return
     */
    public static int[] getBallonSizeByRandom(Context context, WindowManager manager) {
        int minWidth = getMinWidth(context, manager);
        int width = (int) (minWidth + Math.random() * (minWidth * FLOAT_W));
        int[] size = new int[2];
        size[0] = width;
        size[1] = width * 4;
        return size;
    }

    /**
     * 获取气球随机可能的最大高度 <功能简述>
     *
     * @return
     */
    public static int getMaxHeight(Context context, WindowManager manager) {
        int minWidth = getMinWidth(context, manager);
        int width = (int) (minWidth + (minWidth * FLOAT_W));
        return 4 * width;
    }

    /**
     * 获取随机的时间
     */
    public static long getRandomFlyDuration(Context context) {
        long duration = BalloonPerformer.getInstance().getConfig().getFlyDuration(context);
        return (long) (duration + Math.random()
                * duration);
    }

    private static final float FLOAT_W = 0.3f;

    private static int getMinWidth(Context context, WindowManager manager) {
        int minWidth = (int) (manager.getDefaultDisplay().getWidth() / (BalloonPerformer.getInstance().getConfig().getBalloonCount(context) * (1 + FLOAT_W)));
        return minWidth;
    }
}
