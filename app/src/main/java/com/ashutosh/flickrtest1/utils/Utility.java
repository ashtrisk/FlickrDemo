package com.ashutosh.flickrtest1.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Vostro-Daily on 9/27/2017.
 */

public class Utility {

    private static int screenWidth;

    public static int parseInteger(String intValue){
        int value = 0;
        try {
            value = Integer.parseInt(intValue);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return value;
    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }
}
