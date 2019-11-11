package com.mitchelltucker.popularmovies.utils.layout_res;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class screen_metrics {

    private static int width;
    private static int height;
    private static Integer rowSize;

    /**
     * updateScreenSize
     * @param context : Context, activity called
     * @param row_size : Integer, row_size
     */
    public static void updateScreenSize(Context context,@Nullable Integer row_size) {
        if(row_size != null){
            rowSize = row_size;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
    }

    /**
     * getRawHeight
     * @return int
     */
    public static int getRawHeight(){
        return height;
    }

    /**
     * getRawWidth
     * @return int
     */
    public static int getRawWidth(){
        return width;
    }

    /**
     * getScreenHeight
     * @return int
     */
    public static Integer getScreenHeight(){
        double row_width = width / rowSize;
        double new_height = row_width * 1.5;
        return (int) new_height;
    }

    /**
     * getScreenWidth
     * @return int
     */
    public static Integer getScreenWidth(){
        return width / rowSize;
    }

    /**
     * getPadding
     * @param context : Context : activity called
     * @param dp : int dp size for padding
     * @return int
     */
     public static int getPadding(Context context, int dp){
         final float scale = context.getResources().getDisplayMetrics().density;
         return (int) (dp * scale + 0.5f);
     }

}
