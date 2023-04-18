package com.example.animation;

import android.graphics.Color;
import android.util.Log;

public class ColorUtil {
    private static final String TAG = "ColorUtil";

    /**
     * 颜色值转字符串
     * @param value
     * @return
     */
    public static String integerToString(Integer value) {
        if (value < 0xFF000000 || value > 0xFFFFFF) {
            return "out of range";
        }

        String hexColor = String.format("#%08X", (0xFFFFFFFF & value));
        Log.i(TAG, value + " : " + hexColor + ", " + Color.parseColor(hexColor));

        return hexColor;
    }

}
