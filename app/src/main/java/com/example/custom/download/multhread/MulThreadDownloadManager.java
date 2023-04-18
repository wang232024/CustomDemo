package com.example.custom.download.multhread;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 多线程下载
 * 1.根据http连接应答，创建一个与下载源文件同名同大小的文件。
 * 2.根据文件大小和线程数量，分配每个线程应该下载的大小，起始和结束等位置。
 * 3.线程读取输入流，并通过RandomAccessFile写入文件对应的位置。
 *
 * 只能一个文件一个文件下载。
 */
public class MulThreadDownloadManager {
    private static final String TAG = "wtx_MulThreadDownload";
    private MulThreadDownloadManagerListener mMulThreadDownloadManagerListener;
    private final Object mObjectByte = new Object();
    private final Object mObjectThread = new Object();
    private long mTimeStart;
    private long mTimeStop;
    private long mTimeWaste;
    private long mFinishByteCount;
    private long mFinishThreadCount;
    private boolean isAlreadyRun = false;

    public MulThreadDownloadManager(MulThreadDownloadManagerListener listener) {
        mMulThreadDownloadManagerListener = listener;
    }

    public interface MulThreadDownloadManagerListener {
        void onStart(long length);

        void onProcess(long len, long length);

        void onFinish(long sum, int responseCode, long timeWaste);

        void onFailed(int responseCode);
    }

    private Thread mThreadFileAccess;   // 预读取线程

    public void download(final String urlPath, int threadNum, File file) {
        if (isAlreadyRun) {
            return;
        }
        isAlreadyRun = true;
        mTimeStart = System.currentTimeMillis();

        mThreadFileAccess = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    Log.i(TAG, "MulThreadDownload download, urlPath:" + urlPath);
                    Log.i(TAG, "MulThreadDownload download, file:" + file.getPath());
                    Log.i(TAG, "MulThreadDownload download, responseCode:" + responseCode);

                    boolean support = isSupportRange(conn);
                    Log.w(TAG, "support:" + support);

                    if (200 == responseCode) {
                        long length = conn.getContentLength();

                        RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                        accessFile.setLength(length);
                        accessFile.close();

                        Log.i(TAG, "download begin.");
                        if (null != mMulThreadDownloadManagerListener) {
                            mMulThreadDownloadManagerListener.onStart(length);
                        }
                        if (!isSupportRange(conn)) {
                            new ThreadDownload(0, 1, length, url, file, mThreadDownloadListener).start();
                        } else {
                            for (int index = 0; index < threadNum; index++) {
                                new ThreadDownload(index, threadNum, length, url, file, mThreadDownloadListener).start();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mThreadFileAccess.start();
    }

    private ThreadDownload.ThreadDownloadListener mThreadDownloadListener = new ThreadDownload.ThreadDownloadListener() {
        @Override
        public void onStart(int index, long begin, long end, long block, long length) {

        }

        @Override
        public void onProcess(int index, byte[] buffer, int len) {
            if (null != mMulThreadDownloadManagerListener) {
                synchronized (mObjectByte) {
                    mFinishByteCount += len;
                    mMulThreadDownloadManagerListener.onProcess(len, mFinishByteCount);
                }
            }
        }

        @Override
        public void onFinish(int index, int threadNum, File file, int responseCode, long timeConnect, long timeReadWrite, long timeWaste) {
            if (null != mMulThreadDownloadManagerListener) {
                synchronized (mObjectThread) {
                    mFinishThreadCount++;
                    if (threadNum == mFinishThreadCount) {
                        mTimeStop = System.currentTimeMillis();
                        mMulThreadDownloadManagerListener.onFinish(mFinishByteCount, responseCode, (mTimeStop - mTimeStart));
                    }
                }
            }
        }

        @Override
        public void onStop(int index, int threadNum, File file, long sum) {

        }

        @Override
        public void onFailed(int index, int responseCode) {
            if (null != mMulThreadDownloadManagerListener) {
                if (0 == index) {
                    mMulThreadDownloadManagerListener.onFailed(responseCode);
                }
            }
        }
    };

    // 是否支持获取指定范围内的字节
    public static boolean isSupportRange(HttpURLConnection conn) {
        Map<String, List<String>> map = conn.getHeaderFields();
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = String.valueOf(map.get(key));
            if (null != key && key.contains("Ranges") && value.contains("bytes")) {
                return true;
            }
        }
        return false;
    }

}
