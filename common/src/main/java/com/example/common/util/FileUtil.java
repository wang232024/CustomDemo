package com.example.common.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 读写权限：
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */

public class FileUtil {
    private static final String TAG = "wtx_FileUtil";

    public interface OnFileUtilListener {
        void onProcess(int type, File file);
    }

    static OnFileUtilListener mListener;

    /**
     * 文件排序方式
     */
    enum FileOrder {
        TYPE,
        NAME,
        SIZE,
        MODIFY
    }

    private FileUtil() {

    }

    /**
     * 判断文件或者目录是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExists(final String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFileExists(final File file) {
        return null != file && file.exists();
    }

    // 获取默认系统文件编码
    public static String getSystemDefaultEncode() {
        return System.getProperty("file.encoding");
    }

    /**
     * 判断是否是存在的文件
     *
     * @param filePath
     * @return
     */
    public static boolean isFile(final String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean isFile(final File file) {
        return null != file && file.exists() && file.isFile();
    }

    /**
     * 判断是否是存在的目录
     *
     * @param filePath
     * @return
     */
    public static boolean isDir(final String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    public static boolean isDir(final File file) {
        return null != file && file.exists() && file.isDirectory();
    }

    /**
     * 创建一个空文件
     *
     * @param filePath
     * @param isCheckDir   是否检测并创建文件的父目录
     * @return
     */
    public static boolean emptyFile(final String filePath, boolean isCheckDir) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        try {
            if (!dir.exists()) {
                if (!isCheckDir) {
                    return false;
                }

                if (!safeMakeDir(dir)) {
                    return false;
                }
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建目录及其父目录
     *
     * @param dirPath
     * @return 0:success    -1:failed.(Android:maybe no sdcard or permission deny or a same named file exist)
     */
    public static boolean safeMakeDir(final String dirPath) {
        File dir = new File(dirPath);
        if (!isDir(dir)) {
            if (dir.mkdirs()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (isDir(dirPath)) {
                return true;
            }
        }
        return false;
    }

    public static boolean safeMakeDir(final File dirFile) {
        if (dirFile != null) {
            if (!dirFile.exists()) {
                if (dirFile.mkdirs()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (isDir(dirFile)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取文件长度大小
     *
     * @param filePath
     * @return  -1:非文件或者不存在
     */
    public static long getFileLength(final String filePath) {
        if (!isFile(filePath)) {
            return -1;
        } else {
            return new File(filePath).length();
        }
    }

    private static final String LINE_SEP = System.getProperty("line.separator");

    /**
     * 获取文件行数(readLine 要快很多)
     *
     * @param filePath
     * @return
     */
    public static long getFileLines(final String filePath) {
        if (!isFileExists(filePath) || !isFile(filePath)) {
            return 0;
        }
        long count = 0;
        InputStream is = null;
        File file = new File(filePath);
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int readChars;
            if (LINE_SEP.endsWith("\n")) {
                while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\n') ++count;
                    }
                }
            } else {
                while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\r') ++count;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 文件或目录改名，目标已存在时将会返回false，并且不改变已存在的目标文件或目录
     *
     * @param srcPath
     * @param desPath
     * @return
     */
    public static boolean renameFile(final String srcPath, final String desPath) {
        File file = new File(srcPath);
        if (!isFileExists(srcPath)) {
            return false;
        }
        return file.renameTo(new File(desPath));
    }

    public static boolean renameFile(final File srcFile, final File desFile) {
        if (null == srcFile || !srcFile.exists()) {
            return false;
        }
        return srcFile.renameTo(desFile);
    }

    /**
     * 文件复制(Android10之后，系统限制，只能对指定公共目录(如Download等)的媒体文件进行操作;app对公共目录其他文件可读不可写;公共目录中自身创建的文件可读可写)
     * @param srcPath
     * @param desPath
     * @param isCheckDesDir 是否检测并创建目标文件的父目录
     * @return
     */
    public static int copyFile(final String srcPath, final String desPath, boolean isCheckDesDir) {
        if (!isFileExists(srcPath) || !isFile(srcPath)) {
            return -1;
        }

        File desFile = new File(desPath);
        File desDirFile = desFile.getParentFile();
        if (isCheckDesDir) {
            if (!safeMakeDir(desDirFile)) {
                return -2;
            }
        } else {
            if (!isDir(desDirFile)) {
                return -3;
            }
        }

        final int BUFFER_SIZE = 8 * 1024;
        int len = 0;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            bis = new BufferedInputStream(new FileInputStream(srcPath));
            bos = new BufferedOutputStream(new FileOutputStream(desPath));
            while (0 < (len = bis.read(buffer, 0, BUFFER_SIZE))) {
                bos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            Log.e(TAG, "e:" + e);
            e.printStackTrace();
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 从srcPath文件指定位置begin处开始，复制len个字节的数据到desPath文件中
     * @param srcPath
     * @param begin
     * @param len
     * @param desPath
     * @param isCheckDesDir
     * @return
     */
    public static boolean copyFilePart(final String srcPath, long begin, int len, final String desPath, boolean isCheckDesDir) {
        if (!isFileExists(srcPath) || !isFile(srcPath)) {
            return false;
        }

        File desFile = new File(desPath);
        File desDirFile = desFile.getParentFile();
        if (isCheckDesDir) {
            if (!safeMakeDir(desDirFile)) {
                return false;
            }
        } else {
            if (!isDir(desDirFile)) {
                return false;
            }
        }

        final int BUFFER_SIZE = 8 * 1024;
        int lenRead = 0;
        RandomAccessFile rafSrc = null;
        BufferedOutputStream bos = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            rafSrc = new RandomAccessFile(new File(srcPath), "r");
            rafSrc.seek(begin);

            bos = new BufferedOutputStream(new FileOutputStream(desPath));
            while (0 < (lenRead = rafSrc.read(buffer, 0, BUFFER_SIZE))) {
                if (len > lenRead) {
                    bos.write(buffer, 0, lenRead);
                    len -= lenRead;
                } else {
                    bos.write(buffer, 0, len);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rafSrc) {
                    rafSrc.close();
                }
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * 复制文件到指定目录中
     *
     * @param srcPath
     * @param dirPath
     * @param isCheckDesDir 是否检测并创建指定的目录
     * @return
     */
    public static boolean copyFileToDir(final String srcPath, final String dirPath, boolean isCheckDesDir) {
        if (!isFileExists(srcPath) || !isFile(srcPath)) {
            return false;
        }

        if (isCheckDesDir) {
            if (!safeMakeDir(dirPath)) {
                return false;
            }
        } else {
            if (!isDir(dirPath)) {
                return false;
            }
        }

        File srcFile = new File(srcPath);
        String desPath = dirPath + File.separator + srcFile.getName();
        copyFile(srcPath, desPath, isCheckDesDir);

        return true;
    }

    /**
     * 将文件夹中的内容复制到指定文件夹中
     *
     * @param srcPath
     * @param desPath
     * @param isCheckDesDir 是否检测并创建指定的目录
     * @return
     */
    public static boolean copyDirToDir(final String srcPath, final String desPath, boolean isCheckDesDir) {
        if (!isFileExists(srcPath) || !isDir(srcPath)) {

            return false;
        }

        if (isCheckDesDir) {
            if (!safeMakeDir(desPath)) {
                return false;
            }
        } else {
            if (!isDir(desPath)) {
                return false;
            }
        }

        File[] files = new File(srcPath).listFiles();
        for (File file : files) {
            if (isFile(file)) {
                copyFileToDir(srcPath + File.separator + file.getName(), desPath, isCheckDesDir);
            } else if (isDir(file)) {
                copyDirToDir(srcPath + File.separator + file.getName(), desPath + File.separator + file.getName(), isCheckDesDir);
            }
        }

        return true;
    }

    /**
     * 删除指定的文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(final String filePath) {
        if (!isFile(filePath)) {
            return true;
        }

        return new File(filePath).delete();
    }

    /**
     * 清理指定的目录
     *
     * @param dirPath       所要清理的目录路径
     * @param isDeep        是否进行深度清理
     * @param fileFilter    清理文件的过滤器，须指定f=true才能生效
     * @param f             是否清理文件
     * @param d             是否清理子目录（非空子目录无法删除，需指定f:true才可进行清理）
     * @return
     */
    public static boolean deleteDir(final String dirPath, boolean isDeep, FileFilter fileFilter, boolean f, boolean d) {
        if (!isDir(dirPath)) {
            return true;
        }

        File[] files = new File(dirPath).listFiles();
        Arrays.sort(files);

        for (File file : files) {
            if (isFile(file)) {
                if (f) {
                    if (fileFilter.accept(file)) {
                        boolean fileDeleteRes = deleteFile(dirPath + File.separator + file.getName());
                        if (fileDeleteRes) {
//                            Log.e(TAG, "delete file:" + file.getPath());
                            if (null != mListener) {
                                mListener.onProcess(0, file);
                            }
                        }
                    }
                }
            } else if (isDir(file)) {
                if (isDeep) {
                    deleteDir(dirPath + File.separator + file.getName(), isDeep, fileFilter, f, d);
                }
                if (d) {
                    boolean dirDeleteRes = file.delete();
                    if (dirDeleteRes) {
//                        Log.e(TAG, "delete dir:" + file.getPath());
                        if (null != mListener) {
                            mListener.onProcess(1, file);
                        }
                    } else {
//                        Log.e(TAG, "无法删除:" + file.getPath());
                    }
                }
            }
        }

        return true;
    }

    /**
     * 清理sdcard空目录和log日志文件。
     *
     * @param listener
     * @return
     */
    public static boolean cleanSDCard(OnFileUtilListener listener) {
        mListener = listener;

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getPath().endsWith(".log");
            }
        };

        deleteDir("/sdcard", true, fileFilter, true, true);
        return true;
    }

    private static LinkedBlockingQueue<byte[]> mLinkedBlockingQueue = new LinkedBlockingQueue<>();
    private static Thread writeThread = null;

    /**
     * 追加内容到文件末尾
     *
     * @param filePath
     * @param info
     * @param isCheckDir    是否检测并创建目标文件的父目录
     * @return
     */
    public static boolean appendFile(final String filePath, final String info, boolean isCheckDir) {
        File desFile = new File(filePath);
        File desDirFile = desFile.getParentFile();
        if (isCheckDir) {
            if (!safeMakeDir(desDirFile)) {
                return false;
            }
        } else {
            if (!isDir(desDirFile)) {
                return false;
            }
        }

        mLinkedBlockingQueue.offer(info.getBytes());

        if (null == writeThread) {
            writeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
                        byte[] buffer = null;
                        long length = 0;
                        while (0 < mLinkedBlockingQueue.size()) {
                            buffer = mLinkedBlockingQueue.take();
                            length = randomAccessFile.length();
                            randomAccessFile.seek(length);
                            randomAccessFile.write(buffer);
                            Thread.sleep(1);
                        }

                        randomAccessFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    writeThread = null;
                }
            });
            writeThread.start();
        }

//        try {
//            // RandomAccessFile效率高于FileWriter
//            synchronized (ReadWriteLock) {
//                RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
//                long length = randomAccessFile.length();
//                randomAccessFile.seek(length);
//                randomAccessFile.writeBytes(info);
//                randomAccessFile.close();
//            }
//
////            FileWriter writer = new FileWriter(filePath, true);
////            writer.write(info);
////            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }

        return true;
    }

    /**
     * 限定文件夹中的文件数量，如果大于或者等于count，则删除最早更改的文件
     *
     * @param dirPath   所操作的文件夹路径
     * @param count     限定数量
     * @return  -1：文件夹不存在   0：文件数量小于限定数     1：文件数量>=count，按修改时间删除删除多余文件
     */
    public static int limitDirFileCount(String dirPath, int count) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
            return -1;
        }

        File[] files = dirFile.listFiles();
        Log.e(TAG, "before file.length:" + files.length);
        if (files.length >= count) {
            Arrays.sort(files, new ComparatorByOrder(FileOrder.MODIFY, false));
            for (int i = 0; i < (files.length - count + 1); i++) {
                Log.e(TAG, "delete file:" + files[i].getName() + ", size:" + files[i].length());
                files[i].delete();
            }
            Log.e(TAG, "now files.length:" + files.length);
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * 比较器，根据文件类型、名称、大小、修改时间进行排序
     * order    ：   排序方式
     * reverse  ：   是否逆序
     */
    static class ComparatorByOrder implements Comparator<File> {
        private FileOrder order;
        private boolean reverse;
        int para = 1;
        public ComparatorByOrder(FileOrder order, boolean reverse) {
            this.order = order;
            this.reverse = reverse;
            para = reverse ? -1 : 1;
        }

        @Override
        public int compare(File f1, File f2) {
            int res = 0;
            if (f1.isDirectory() && f2.isFile()) {
                return para;
            } else if (f1.isFile() && f2.isDirectory()) {
                return -para;
            }
            switch (order) {
                case TYPE:
                    if (f1.isDirectory()) {
                        res = para * (f1.getName().compareTo(f2.getName()));
                    } else if (f1.isFile()) {
                        int f1Last = f1.getName().lastIndexOf(".");
                        int f2Last = f2.getName().lastIndexOf(".");
                        if (-1 == f1Last && -1 == f2Last) { // 都没有后缀名
                            res = para * (f1.getName().compareTo(f2.getName()));
                        } else if (-1 < f1Last && -1 < f2Last) {    //都有后缀名
                            String suffixF1 = f1.getName().substring(f1.getName().lastIndexOf(".") + 1);
                            String suffixF2 = f2.getName().substring(f2.getName().lastIndexOf(".") + 1);
                            if (0 == suffixF1.compareTo(suffixF2)) {
                                res = para * (f1.getName().substring(0, f1Last).compareTo(f2.getName().substring(0, f2Last)));
                            } else {
                                res = para * (suffixF1.compareTo(suffixF2));
                            }
                        } else {    // 有后缀名的排前面
                            res = para * (f1Last - f2Last);
                        }
                    }
                    break;
                case NAME:
                    int name = f1.getName().compareTo(f2.getName());
                    res = para * name;
                    break;
                case SIZE:
                    long length = f1.length() - f2.length();
                    res = (int) (para * length);
                    break;
                case MODIFY:
                    long modify = f1.lastModified() - f2.lastModified();
                    res = (int) (para * modify);
                    break;
            }
            return res;
        }
    }

    static int mCopyFileCount = 0;
    /**
     * 多线程复制文件，相比单线程复制，速度并没有提高。
     * 速度瓶颈在IO读写，当一个线程进行IO读写时，由于IO读写速度限制，其他线程只能等待。
	 *
     * @param srcFile   源文件
     * @param desFile   目标文件，若不存在，则创建
     * @param threadNum 线程数
     */
    public static void multiCopy(final File srcFile, final File desFile, final int threadNum) {
        if (srcFile == null || !srcFile.exists() || desFile == null || threadNum <= 0)
            return;
        if (!desFile.exists()) {
            if (!desFile.getParentFile().exists())
                desFile.getParentFile().mkdirs();
        }

        Thread[] threads = new Thread[threadNum];
        final int BUFFER_SIZE = 8 * 1024;
        mCopyFileCount = 0;
        final long start = System.currentTimeMillis();
        // 分配每个线程操作的文件大小
        long textLength = srcFile.length();
        final long[] textSubsAllocate = allocateGroup(textLength, threadNum);
        final long[] seekPosition = new long[threadNum];
        final long[] writeSize = new long[threadNum];
        for (int i = 0; i < threadNum; i++) {
            if (0 == i) {
                seekPosition[i] = 0;
                writeSize[i] = textSubsAllocate[0];
            } else if (threadNum - 1 == i) {
                seekPosition[i] = textSubsAllocate[0] * i;
                writeSize[i] = textSubsAllocate[1];
            } else {
                seekPosition[i] = textSubsAllocate[0] * i;
                writeSize[i] = textSubsAllocate[0];
            }
        }

        for (int i = 0; i < threadNum; i++) {
            final int j = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RandomAccessFile srcRaf = new RandomAccessFile(srcFile, "r");
                        RandomAccessFile desRaf = new RandomAccessFile(desFile, "rw");
                        srcRaf.seek(seekPosition[j]);
                        desRaf.seek(seekPosition[j]);
                        byte[] buf = new byte[BUFFER_SIZE];
                        int len;
                        while ((writeSize[j] > 0) && (-1 != (len = srcRaf.read(buf)))) {
                            if (len < writeSize[j]) {
                                desRaf.write(buf, 0, len);
                            } else {
                                desRaf.write(buf, 0, (int) writeSize[j]);
                            }
                            writeSize[j] = writeSize[j] - len;
                        }
                        srcRaf.close();
                        desRaf.close();
                        mCopyFileCount++;
                        if (threadNum == mCopyFileCount) {
                            long stop = System.currentTimeMillis();
                            Log.w(TAG, "multiCopy finish, waste:" + (stop - start));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
        Log.i(TAG, "textLength: " + textLength);
    }

    static int mSplitFileCount = 0;
    /**
     * 多线程将filePath文件拆分成splitNum个文件
     *
     * @param filePath  源文件路径
     * @param splitNum  文件拆分数量
     * @param threadNum 多线程数量
     * @return
     */
    public static int splitFile(String filePath, final int splitNum, int threadNum) {
        final File srcFile = new File(filePath);
        if (!srcFile.exists()) {
            return -1;
        }

        mSplitFileCount = 0;
        final int BUFFER_SIZE = 8 * 1024;
        final long start = System.currentTimeMillis();
        String[] filePathSplit = filePath.split("/");
        final String fileName = filePathSplit[filePathSplit.length - 1];
        final String fileParentPath = filePath.substring(0, filePath.lastIndexOf("/"));
        final String dirName = "SPLIT_" + fileName;
        File splitDir = new File(fileParentPath + File.separator + dirName);
        if (!splitDir.exists()) {
            splitDir.mkdirs();
        }

        long textLength = srcFile.length();
        final int threadNeed;
        final long threadWriteDivideSize;
        final long threadWriteLastSize;
        long[] allocateResult = allocateGroup(textLength, splitNum);    // 获取线程分配到的大小
        if (splitNum > threadNum) {
            threadNeed = threadNum;
        } else {
            threadNeed = splitNum;
        }
        threadWriteDivideSize = allocateResult[0];
        threadWriteLastSize = allocateResult[1];

        final Thread[] threads = new Thread[threadNeed];

        long[] threadDivideTimes = allocateGroup(splitNum, threadNeed);    // 获取线程运行次数
        final long threadRunDivideTimes = threadDivideTimes[0];
        final long threadRunLastTimes = threadDivideTimes[1];

        int threadRunTimes;

        for (int i = 0; i < threadNeed; i++) {
            final int finalI = i;
            if (threadNeed - 1 == i) {
                threadRunTimes = (int) threadRunLastTimes;
            } else {
                threadRunTimes = (int) threadRunDivideTimes;
            }
            final int runTimes = threadRunTimes;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < runTimes; j++) {
                            long index = finalI * threadRunDivideTimes + j;
                            RandomAccessFile raf = new RandomAccessFile(srcFile, "r");
                            File file;
                            if (index < 10) {
                                file = new File(fileParentPath + File.separator + dirName + File.separator + fileName + "_0" + index);
                            } else {
                                file = new File(fileParentPath + File.separator + dirName + File.separator + fileName + "_" + index);
                            }
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            long writeSize;
                            if (index == splitNum - 1) {
                                writeSize = threadWriteLastSize;
                            } else {
                                writeSize = threadWriteDivideSize;
                            }
                            raf.seek(index * threadWriteDivideSize);
                            byte[] buf = new byte[BUFFER_SIZE];
                            int len;
                            while (writeSize > 0 && (-1 != (len = raf.read(buf)))) {
                                if (BUFFER_SIZE > writeSize) {
                                    bos.write(buf, 0, (int) writeSize);
                                } else {
                                    bos.write(buf, 0, len);
                                }
                                writeSize = writeSize - BUFFER_SIZE;
                            }
                            bos.close();
                            raf.close();
                            synchronized (FileUtil.class) {
                                mSplitFileCount++;
                                Log.i(TAG, "write finish, index:" + index + ", mSplitFileCount:" + mSplitFileCount);
                                if (splitNum == mSplitFileCount) {
                                    long stop = System.currentTimeMillis();
                                    Log.w(TAG, "split finish, waste:" + (stop - start));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            threads[i].start();
        }

        return 0;
    }

    static int mMergeFileCount = 0;
    /**
     * 多线程合并文件
     * 合并目录中的文件到同一个文件中，按文件名排序
     * @param dirPath   需要合并的文件所在目录路径
     * @param threadNum 所使用线程数量
     * @return
     */
    public static int mergeDir(String dirPath, String desPath, int threadNum) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            return -1;
        }

        mMergeFileCount = 0;
        final int BUFFER_SIZE = 8 * 1024;
        final long start = System.currentTimeMillis();
        final File desFile = new File(desPath);
        final File[] files = dirFile.listFiles();
        Arrays.sort(files);
        final int fileNum = files.length;

        final long[] offset = new long[fileNum];
        for (int i = 0; i < fileNum; i++) {
            if (0 == i) {
                offset[i] = 0;
            } else {
                offset[i] += offset[i - 1] + files[i - 1].length();
            }
        }

        long[] fileDivideSize = allocateGroup(fileNum, threadNum);
        final long threadDivideTimes = fileDivideSize[0];
        final long threadLastTimes = fileDivideSize[1];
        Log.e(TAG, "threadDivideTimes:" + threadDivideTimes);
        Log.e(TAG, "threadLastTimes:" + threadLastTimes);

        final Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            final int finalI = i;
            final long threadRunTimes;
            if (threadNum - 1 == i) {
                threadRunTimes = threadLastTimes;
            } else {
                threadRunTimes = threadDivideTimes;
            }
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < threadRunTimes; j++) {
                        try {
                            int index = (int) (finalI * threadDivideTimes + j);
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(files[index]));
                            RandomAccessFile raf = new RandomAccessFile(desFile, "rw");
                            raf.seek(offset[index]);
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int len;
                            while (-1 != (len = bis.read(buffer))) {
                                raf.write(buffer, 0, len);
                            }
                            bis.close();
                            raf.close();
                            synchronized (FileUtil.class) {
                                mMergeFileCount++;
                                Log.i(TAG, "write finish, index:" + index + ", mMergeFileCount:" + mMergeFileCount);
                                if (fileNum == mMergeFileCount) {
                                    long stop = System.currentTimeMillis();
                                    Log.w(TAG, "merge finish, waste:" + (stop - start));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            threads[i].start();
        }

        return 0;
    }

    /**
     * 将amount的数据量分成groupNum组
     * @param amount
     * @param groupNum
     * @return [每组分配数据量，最后一组数据量]
     */
    private static long[] allocateGroup(long amount, int groupNum) {
        long groupResult;
        long groupLast;
        if (0 == (amount % groupNum)) {
            groupResult = amount / groupNum;
            groupLast = amount / groupNum;
        } else {
            groupResult = amount / groupNum + 1;
            groupLast = amount - (groupNum - 1) * groupResult;
        }
        long[] result = new long[2];
        result[0] = groupResult;
        result[1] = groupLast;
        return result;
    }


    public static void testFileUtil() {
        long start = System.currentTimeMillis();
//        splitFile("/sdcard/222/kws.pcm", 51, 4);
//        mergeDir("/sdcard/222/SPLIT_kws.pcm", "/sdcard/222/copy.pcm", 4);
//        multiCopy(new File("/sdcard/222/kws.pcm"), new File("/sdcard/222/kws2222.pcm"), 8);
        copyFile("/sdcard/222/kws.pcm", "/sdcard/222/kws3333.pcm", false);
        long stop = System.currentTimeMillis();
        Log.e(TAG, "finish:" + (stop - start));
    }

}
