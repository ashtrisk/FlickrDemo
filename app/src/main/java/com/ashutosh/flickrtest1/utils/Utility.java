package com.ashutosh.flickrtest1.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Vostro-Daily on 9/27/2017.
 */

public class Utility {

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

}
