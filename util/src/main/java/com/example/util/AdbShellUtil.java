package com.example.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdbShellUtil {

    /**
     * 例：adb shell getprop ro.custom.build.version
     * @param key   ro.custom.build.version
     * @return      对应值
     */
    public static String getProp(String key) {
        String value = "";
        try {
            String command = "adb shell getprop";
            Process process = Runtime.getRuntime().exec(command.replace("adb shell ", ""));
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str;
            while (null != (str = br.readLine())) {
                int index = str.indexOf(key);
                if (0 < index) {
                    value = str.substring(index + key.length());
                    value = value.replace("[", "");
                    value = value.replace("]", "");
                    value = value.replace(":", "");
                    value = value.replace(" ", "");
                    return value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void test() {
        String version = getProp("ro.custom.build.version");
        Log.i("adb shell", "version:" + version);
    }

}
