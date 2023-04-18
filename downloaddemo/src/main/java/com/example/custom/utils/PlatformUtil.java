package com.example.custom.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Author:wenjunjie
 * Date:2022/10/18
 * Time:上午10:10
 * Description:
 **/
public class PlatformUtil {


    //获取设备厂商
    public static String getDeviceManufacturer(){
        return Build.MANUFACTURER;
    }

    //获取设备品牌
    public static String getDeviceBrand(){
        return Build.BRAND;
    }


    //获取设备型号
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    //获取设备当前语言
    public static String getLocalLocale(){
        return Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry();
    }

    //获取设备系统
    public static String getOS(){
        return "android";
    }

    //获取设备sdk版本号
    public static String getSdkVserision(){
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    //获取设备os版本号
    public static String getOsVserision(){
        return Build.VERSION.RELEASE;
    }

    //获取设备设备水平分辨率
    public static int getHorizontalResolution(Context context){
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取设备垂直分辨率
    public static int getVerticalResolution(Context context){
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    //获取设备DPI
    public static int getDpi(Context context){
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }


    //获取设备序列号
    public static String getDeviceId(){
//        return Build.getSerial();
        return "8S21E600";
    }



}
