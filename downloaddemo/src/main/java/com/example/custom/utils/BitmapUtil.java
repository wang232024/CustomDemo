package com.example.custom.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Author:wenjunjie
 * Date:2022/6/2
 * Time:下午5:21
 * Description:
 **/
public class BitmapUtil {
    /**
     * 将bitmap转换成为Base64
     * @param bitmap
     * @return
     */
    public  static String bitmaptoString(Bitmap bitmap,Bitmap.CompressFormat compressFormat,int quality) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 将Base64转换成为Bitmap
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray=Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



}
