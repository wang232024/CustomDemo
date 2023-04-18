package com.example.custom.download.multhread;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThreadDownload extends Thread {
    private static final String TAG = "wtx_ThreadDownload";
    private final int mIndex;
    private final int mThreadNum;
    private final long mLength;
    private final URL mUrl;
    private final File mFile;
    private long mBlock = 0;          // 线程下载的数据量
    private long mBeginPos = 0;      // 线程开始下载的位置
    private long mEndPos = 0;       // 线程结束下载的位置
    private ThreadDownloadListener mThreadDownloadListener;
    private boolean isRunning;

    public interface ThreadDownloadListener {
        void onStart(int index, long begin, long end, long block, long length);

        void onProcess(int index, byte[] buffer, int len);

        void onFinish(int index, int threadNum, File file, int responseCode, long timeConnect, long timeReadWrite, long timeWaste);

        void onStop(int index, int threadNum, File file, long sum);

        void onFailed(int index, int responseCode);
    }

    public ThreadDownload(int index, int threadNum, long length, URL url, File file) {
        mIndex = index;
        mThreadNum = threadNum;
        mLength = length;
        mUrl = url;
        mFile = file;
    }

    public ThreadDownload(int index, int threadNum, long length, URL url, File file, ThreadDownloadListener listener) {
        this(index, threadNum, length, url, file);
        mThreadDownloadListener = listener;

        // 计算对应id的线程所要下载的起始位置和结束位置，非最后线程的下载数据量
        long per;
        if (0 == mLength % mThreadNum) {
            per = mLength / mThreadNum;
            mBlock = mLength / mThreadNum;
            mBeginPos = mIndex * per;
            mEndPos = (mIndex + 1) * per - 1;
        } else {
            per = mLength / mThreadNum + 1;
            if (mIndex < mThreadNum - 1) {
                mBlock = mLength / mThreadNum + 1;
                mBeginPos = mIndex * per;
                mEndPos = (mIndex + 1) * per - 1;
            } else if (mIndex == (mThreadNum - 1)) {
                mBlock = mLength - mIndex * per;
                mBeginPos = mIndex * per;
                mEndPos = mLength - 1;
            }
        }
    }

    public ThreadDownload(int index, int threadNum, long length, long block, long beginPos, long endPos, URL url, File file, ThreadDownloadListener listener) {
        this(index, threadNum, length, url, file);
        mThreadDownloadListener = listener;

        mBlock = block;
        mBeginPos = beginPos;
        mEndPos = endPos;
    }

    @Override
    public void run() {
        if (mIndex >= mThreadNum) {
            return;
        }

        long timeStart = System.currentTimeMillis();

        if (null != mThreadDownloadListener) {
            mThreadDownloadListener.onStart(mIndex, mBeginPos, mEndPos, mBlock, mLength);
        }

        try {
            RandomAccessFile accessFile = new RandomAccessFile(mFile, "rwd");
            accessFile.seek(mBeginPos);

            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            //通过Range字段指定访问的数据段
            conn.setRequestProperty("Range", "bytes=" + mBeginPos + "-" + mEndPos);
            int responseCode = conn.getResponseCode();
            Log.d(TAG, mIndex + ", responseCode:" + responseCode);
            isRunning = true;
            if (206 == responseCode) {      // 不支持获取指定范围内字节
                long timeConnect = System.currentTimeMillis();
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[10240];
                int len;
                long sum = 0;
                while (0 < (len = inputStream.read(buffer)) && isRunning) {
                    accessFile.write(buffer, 0, len);
                    if (null != mThreadDownloadListener) {
                        mThreadDownloadListener.onProcess(mIndex, buffer, len);
                    }
                    sum += len;
                }
                accessFile.close();
                inputStream.close();

                Log.d(TAG, mIndex + ", sum:" + sum + ", [" + mBeginPos + ", " + mEndPos + "]");
                long timeReadWrite = System.currentTimeMillis();
                if (null != mThreadDownloadListener) {
                    long timeStop = System.currentTimeMillis();
//                    mThreadDownloadListener.onFinish(mIndex, mThreadNum, mFile, responseCode, (timeConnect - timeStart), (timeReadWrite - timeConnect), (timeStop - timeStart));
                    if (isRunning) {
                        mThreadDownloadListener.onFinish(mIndex, mThreadNum, mFile, responseCode, (timeConnect - timeStart), (timeReadWrite - timeConnect), (timeStop - timeStart));
                    } else {
                        mThreadDownloadListener.onStop(mIndex, mThreadNum, mFile, sum);
                    }
                }
            } else if (200 == responseCode) {   // 不支持获取指定范围内字节
                if (0 == mIndex) {           // 返回全部数据，只调用第一个线程进行处理
                    long timeConnect = System.currentTimeMillis();
                    InputStream inputStream = conn.getInputStream();
                    byte[] buffer = new byte[10240];
                    int len;
                    long sum = 0;
                    while (0 < (len = inputStream.read(buffer)) && isRunning) {
                        accessFile.write(buffer, 0, len);
                        if (null != mThreadDownloadListener) {
                            mThreadDownloadListener.onProcess(mIndex, buffer, len);
                        }
                        sum += len;
                    }
                    accessFile.close();
                    inputStream.close();

                    Log.d(TAG, "mThreadid:" + mIndex + ", sum:" + sum + ", [" + mBeginPos + ", " + mEndPos + "]");
                    long timeReadWrite = System.currentTimeMillis();
                    if (null != mThreadDownloadListener) {
                        long timeStop = System.currentTimeMillis();
                        if (isRunning) {
                            mThreadDownloadListener.onFinish(mIndex, mThreadNum, mFile, responseCode, (timeConnect - timeStart), (timeReadWrite - timeConnect), (timeStop - timeStart));
                        } else {
                            mThreadDownloadListener.onStop(mIndex, mThreadNum, mFile, sum);
                        }
                    }
                }
            } else {
                if (null != mThreadDownloadListener) {
                    mThreadDownloadListener.onFailed(mIndex, responseCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isRunning = false;
        }
    }

    public void stopDownload() {
        isRunning = false;
    }

}
