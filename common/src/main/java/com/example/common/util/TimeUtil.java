package com.example.common.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class TimeUtil {
    private final static String TAG = "TimeUtil";

    /**
     * 将时间字符串转换为整形时间戳
     *
     * @param timeString
     * @return 精确到毫秒
     */
    public static long getFormatedDateLong(String timeString) {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
        try {
            Date data = sdf.parse(timeString);
            time = data.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    /**
     * 获取表示当前时间的字符串
     *
     * @param milliseconds   当前时间的长整型表示              1574922395729LL(13位整数：毫秒 / 10位整数：秒)
     * @param timeZoneOffset 时区，北京：8
     * @param format         返回字符串的格式，如"2019-11-28_14-26-35_729" / "2019-11-28_14-26-35"。
     * @return 当前时间的字符串
     */
    public static String getFormatedDateString(long milliseconds, float timeZoneOffset, String format) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }

        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);

        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);

        return sdf.format(new Date(milliseconds));
    }

    // 获取当前设置的时区
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
        return strTz;
    }

    // 获取当前设置的时区值，如中国+8
    public static int getCurrentTimeZoneOffset() {
        TimeZone tz = TimeZone.getDefault();
        long currentTimeMillis = System.currentTimeMillis();
        return tz.getOffset(currentTimeMillis) / (60 * 60 * 1000);
    }

    //    测试网址  https://tool.chinaz.com/Tools/unixtime.aspx
    public static void testTimeUtil() {
        long timeStamp = getFormatedDateLong("2019-11-28_14-26-35_729");
//        java中整数默认为int型，如果要设置为long型，数字后需加上L。
        String timeString = getFormatedDateString(1574922395729L, 8, "yyyy-MM-dd_HH-mm-ss_SSS");
        Log.e(TAG, "时间戳:" + timeStamp + ", 时间:" + timeString);
    }

}