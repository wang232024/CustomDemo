package com.example.custom.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final String TAG = "wtx_ZipUtil";

    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    /**
     * 压缩文件到压缩包中，递归调用
     * @param dirFile
     * @param rawFile
     * @param zipout
     * @throws Exception
     */
    private static void zipDir_(File dirFile, File rawFile, ZipOutputStream zipout) throws Exception  {
        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                zipDir_(file, rawFile, zipout);
            } else {
                String dirPath = rawFile.getName() + file.getPath().replace(rawFile.getPath(), "");
                writeFileToZip(file, zipout, dirPath);
                Log.i(TAG, "ZipDir success:" + file.getPath());
            }
        }
    }

    /**
     * 将待压缩文件写入到压缩流中
     * @param srcFile       待压缩文件
     * @param zipout        压缩流
     * @param desPath       resFile在压缩包中所在路径
     * @throws Exception
     */
    private static void writeFileToZip(File srcFile, ZipOutputStream zipout, String desPath) throws Exception {
        byte[] buffer = new byte[BUFF_SIZE];
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFile), BUFF_SIZE);
        zipout.putNextEntry(new ZipEntry(desPath));
        int realLength;
        while ((realLength = in.read(buffer)) != -1) {
            zipout.write(buffer, 0, realLength);
        }
        in.close();
    }

    /**
     * 1. 压缩列表中的文件(不同目录下)
     * @param list
     * @param desFile
     */
    public static int zipFiles(List<File> list, File desFile) throws Exception {
        for (File file : list) {
            if (!file.exists()) {
                Log.e(TAG, "zipFiles failed, file in list not exists:" + file.getPath());
                return -1;
            }
        }

        File parentFile = desFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(desFile), BUFF_SIZE));

        for (File file : list) {
            Log.i(TAG, "zipFiles, file:" + file.getPath());
            writeFileToZip(file, zipout, file.getName());
        }

        zipout.flush();
        zipout.closeEntry();
        zipout.close();
        Log.w(TAG, "zipFiles success to:" + desFile.getPath());
        return 0;
    }

    /**
     * 2.压缩同一目录下文件到指定压缩包中
     * @param srcFile
     * @param desFile
     */
    public static int zipDir(File srcFile, File desFile) throws Exception {
        if (!srcFile.exists()) {
            Log.e(TAG, "ZipDir failed, srcFile not exists:" + srcFile.getPath());
            return -1;
        }

        File parentFile = desFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(desFile), BUFF_SIZE));
        zipDir_(srcFile, srcFile, zipout);
        zipout.flush();
        zipout.closeEntry();
        zipout.close();
        Log.w(TAG, "ZipDir success to:" + desFile.getPath());
        return 0;
    }

    /**
     * 3. 解压文件到指定路径
     * @param srcFile       压缩包
     * @param desDirFile    将要解压到的目录
     * @return
     * @throws IOException
     */
    public static int upZipFile(File srcFile, File desDirFile) throws IOException {
        if (null == srcFile) {
            Log.e(TAG, "upZipFile failed, srcFile is null");
            return -1;
        }
        if (!srcFile.exists()) {
            Log.e(TAG, "upZipFile failed, srcFile not exists:" + srcFile.getPath());
            return -2;
        }

        if (!desDirFile.exists()) {
            desDirFile.mkdirs();
        }
        ZipFile zf = new ZipFile(srcFile);

        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            if (entry.isDirectory()) {
                continue;
            }
            InputStream in = zf.getInputStream(entry);
            File desFile = new File(desDirFile.getPath() + File.separator + entry.getName());
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
            Log.i(TAG, "upZipFile success to:" + desFile.getPath());
        }
        Log.w(TAG, "upZipFile success to:" + desDirFile.getPath());
        return 0;
    }

    public static void test(Context context, int type) {
        // sdcard外部存储
        String external_files_path = context.getExternalFilesDir("").getPath();
        String external_cache_path = context.getExternalCacheDir().getPath();

//        Log.i(TAG, "external_files_path:" + external_files_path);   // /storage/emulated/0/Android/data/${packagename}/files
//        Log.i(TAG, "external_cache_path:" + external_cache_path);   // /storage/emulated/0/Android/data/${packagename}/cache

        LinkedList<File> list = new LinkedList<>();
        list.add(new File(external_files_path + "/quick_sheet/config.xml"));
        list.add(new File(external_files_path + "/quick_sheet/page1.leremark"));
        list.add(new File(external_files_path + "/quick_sheet/page1.leremark-journal"));
        list.add(new File(external_files_path + "/quick_sheet/txt/page1.txt"));
        list.add(new File(external_files_path + "/quick_sheet/img/page1.png"));
        list.add(new File(external_files_path + "/quick_sheet/rec/ivr_1657014588752.wav"));

        try {
            switch (type) {
                case 0:
                    ZipUtil.zipFiles(list, new File(external_cache_path + "/quick_sheet_list.zip"));
                    break;
                case 1:
                    ZipUtil.zipDir(
                            new File(external_files_path + "/quick_sheet"),
                            new File(external_cache_path + "/quick_sheet.zip")
                    );
                    break;
                case 2:
                    ZipUtil.upZipFile(
                            new File(external_files_path + "/quick_sheet.zip"),
                            new File(external_files_path)
                    );
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
