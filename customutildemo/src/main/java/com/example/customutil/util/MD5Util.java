package com.example.customutil.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final String TAG = "wtx_MD5Util";

    public static String encode(String text) {
        return encode(text, text.length());
    }

    /**
     * 字符串转MD5
     * @param text
     * @param len
     * @return
     */
    public static String encode(String text, int len){
        try {
            String sub = text.substring(0, len);
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] buffer = digest.digest(sub.getBytes());
            return chB2S(buffer);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件(较大字节流)转MD5
     * @param in
     * @return
     */
    public static String encode(InputStream in) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = in.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            byte[] buffer = digester.digest();
            return chB2S(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return null;
    }

    private static String chB2S(byte[] bytes) {
        return chB2S(bytes, null);
    }

    /**
     * 将每个字节的数据转换成16进制字符串形式
     * @param bytes
     * @param separator 分隔符
     * @return
     */
    private static String chB2S(byte[] bytes, String separator) {
        int val;
        String hex;
        StringBuffer sb = new StringBuffer();
        // byte -128 ---- 127
        for (int i = 0; i < bytes.length; i++) {
            val = bytes[i] & 0xff;
            hex = Integer.toHexString(val);
            if (1 == hex.length()) {
                hex = 0 + hex;
            }
            if (null != separator) {
                if (bytes.length - 1 != i) {
                    hex = "0x" + hex + separator;
                } else {
                    hex = "0x" + hex;
                }
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void test() {
        Log.e(TAG, "" + MD5Util.encode("https://md5jiami.51240.com/"));
        //77eb61a077c89105b8f1f371710fadd6
        Log.e(TAG, "" + encode("https://md5jimi.51240", 5));
        //5e056c500a1c4b6a7110b50d807bade5
        Log.e(TAG, "" + encode("https://md5ji40.com/", 5));
        //5e056c500a1c4b6a7110b50d807bade5
        byte[] b = {'a', 'b', 'c', 125, 127, (byte) 128, -127, -128, (byte) -129, 1, 2};
        Log.e(TAG, "" + chB2S(b, " "));

    }
}