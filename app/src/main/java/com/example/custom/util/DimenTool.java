package com.example.custom.util;

import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 报错：
 * > Could not create task ':app:DimenTool.main()'.
 *    > SourceSet with name 'main' not found.
 * 处理方法：
 * .idea/gradle.xml文件中
 * <GradleProjectSettings>标签内添加<option name="delegatedBuild" value="false" />
 * 该设置的意思是不委托gradle进行构建。效果是加了这行配置之后就是jvm执行Java类的main方法了，而不是gradle当做task来执行了。
 */
public class DimenTool {
    // 根目录为当前工程根目录
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            System.out.println("Create file:" + destFileName + "failed, already exist!");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("Create file:" + destFileName + "failed, file cannot be directory!");
            return false;
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                System.out.println("Create file's directory failed!");
                return false;
            }
        }

        try {
            if (file.createNewFile()) {
                System.out.println("Create file:" + destFileName + " success.");
                return true;
            } else {
                System.out.println("Create fiel:" + destFileName + "failed!");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Create file:" + destFileName + "failed!, error:" + e.getMessage());
            return false;
        }
    }

    // 将text字符串写入file文件中
    private static void writeFile(String file, String text) throws IOException {
        createFile(file);
        PrintWriter out = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            out = new PrintWriter(new BufferedWriter(fileWriter));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    // 160dpi为基准
    private static final int[] dpiArrays = {240, 320, 360, 380, 410, 480, 600, 720, 760, 800, 900, 1080, 1200, 1440};
    public static void genDimensXmlFile(float width) {
        File file = new File("./app/src/main/res/values/dimens.xml");
        BufferedReader reader = null;

        StringBuilder[] stringBuilders = new StringBuilder[dpiArrays.length];
        for (int i = 0; i < dpiArrays.length; i++) {
            stringBuilders[i] = new StringBuilder();
        }

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;

            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    String numString = tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2);
                    float num = Float.parseFloat(numString);

                    for (int i = 0; i < dpiArrays.length; i++) {
                        stringBuilders[i].append(start).append(Math.round(num * dpiArrays[i] / width)).append(end).append("\n");
                    }
                } else {
                    for (int i = 0; i < dpiArrays.length; i++) {
                        stringBuilders[i].append(tempString).append("\n");
                    }
                }
            }

            for (int i = 0; i < dpiArrays.length; i++) {
                String dimensXmlFilePath = "./app/src/main/res/values-sw" + dpiArrays[i] + "dp/dimens.xml";
                writeFile(dimensXmlFilePath, stringBuilders[i].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
//        // 测试机器屏幕参数
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int mWidth = metrics.widthPixels;
//        int mHeight = metrics.heightPixels;
//        float mDensity = metrics.density;

        int mWidth = 1200;
        float mDensity = 2.0f;
        genDimensXmlFile(mWidth / mDensity);
    }
}
