package com.example.common.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// 在当前包名私有目录下获取文件
//File file = new File(getCacheDir().getAbsolutePath(), fileName);

/**
Android中Assets文件绝对路径是不存在的,assets文件夹在apk安装包中，没有解压出来，无法直接以普通文件方式进行读写
apk安装包在/data/app/<package name>/里
 * 位于main目录下，与java，res同级
 */
public class AssetsUtil {
    private static final String TAG = "wtx_AssetsUtil";

    /**
     * assets目录中的指定文件复制到指定路径
     * @param context
     * @param filePath      assets目录下的文件路径，不包含assets， 如"yanglao/yanglao.xls"
     * @param desPath       目标全路径, 如"/storage/emulated/0/Android/data/com.example.customutil/cache/yanglao.xls"
     *                      该文件Uri为"content://com.example.customutil.fileprovider/external_cache_path/yanglao.xls"
     * @return
     */
    public static int copyAssetFile(Context context, String filePath, String desPath) {
        try {
            File desFile = new File(desPath);
            if (desFile.exists()) {
                Log.i(TAG, "Already exist:" + desPath);
                return 0;
            }
            File desDirFile = desFile.getParentFile();
            if (!desDirFile.exists()) {
                desDirFile.mkdirs();
            }

            InputStream is = context.getAssets().open(filePath);
            OutputStream os = new FileOutputStream(desFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while (0 < (len = is.read(buffer, 0, 1024))) {
                os.write(buffer, 0, len);
            }
            is.close();
            os.close();
            Log.i(TAG, "copy finish:" + desPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 复制assets目录中指定文件到指定路径，只能复制assets目录下的文件
     * @param context
     * @param fileName  assets目录下的文件
     * @param desDir
     * @return
     */
    public static int copyAssetFileDir(Context context, String fileName, String desDir) {
        try {
            FileUtil.safeMakeDir(desDir);
            InputStream is = context.getAssets().open(fileName);
            File desFile = new File(desDir + File.separator + fileName);
            OutputStream os = new FileOutputStream(desFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while (0 < (len = is.read(buffer, 0, 1024))) {
                os.write(buffer, 0, len);
            }
            is.close();
            os.close();
            Log.i(TAG, "copy:" + desDir + File.separator + fileName + " finish.");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "copyAssetFile error, no file:" + fileName + " in assets dir.");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "getAssets().open " + fileName + " error:" + e);
            copyAssetDir(context, fileName, desDir);
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将assets目录下的子目录(包括该子目录)复制到目标目录中，只能复制子目录及其中的文件，无法深度复制
     * @param context
     * @param dirName       子目录名称
     * @param desDirPath    目标目录路径
     * @return
     */
    public static int copyAssetDir(Context context, String dirName, String desDirPath) {
        String[] filesName = new String[0];
        try {
            filesName = context.getAssets().list(dirName);
        } catch (IOException e) {
            Log.e(TAG, "list error, dirName:" + dirName);
            e.printStackTrace();
        }
        if (0 < filesName.length) {
            FileUtil.safeMakeDir(desDirPath + File.separator + dirName);
            for (String fileName : filesName) {
                copyAssetFile(context, dirName + File.separator + fileName, desDirPath);
            }
        } else {
            Log.e(TAG, "there is no file in assets/" + dirName);
        }

        return 0;
    }

    public static void test(Context context) {
        // 内部存储
        String files_path = context.getFilesDir().getPath();
        String cache_path = context.getCacheDir().getPath();
        String data_path = context.getDataDir().getPath();
        Log.i(TAG, "files_path:" + files_path); // /data/user/0/com.example.customutil/files
        Log.i(TAG, "cache_path:" + cache_path); // /data/user/0/com.example.customutil/cache
        Log.i(TAG, "data_path:" + data_path);   // /data/user/0/com.example.customutil

        // sdcard外部存储
        String external_files_path = context.getExternalFilesDir("").getPath();
        String external_cache_path = context.getExternalCacheDir().getPath();
        Log.i(TAG, "external_files_path:" + external_files_path);   // /storage/emulated/0/Android/data/com.example.customutil/files
        Log.i(TAG, "external_cache_path:" + external_cache_path);   // /storage/emulated/0/Android/data/com.example.customutil/cache

//        AssetsUtil.copyAssetDir(this, "yanglao", external_cache_path);
//        AssetsUtil.copyAssetFile(context, "yanglao/yanglao.xls", external_cache_path + "/yanglao.xls");
        AssetsUtil.copyAssetFile(context, "quick_sheet.zip", external_files_path + "/quick_sheet.zip");
    }

}
