package com.example.customutil.download.breakpoint;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
    private static final String TAG = "wtx_DownloadThread";
    private MulThreadBreakpointManager mDownloader;
    private URL mUrl;
    private File mSaveFile;
    private int mThreadId = -1;
    private boolean isRunning = false;
    private long mBeginPos;
    private long mEndPos;
    private OnDownloadListener mListener;

    public interface OnDownloadListener {
        void onStart(int id);
        void onProcess(int id, int len);
        void onStop(int id);
        void onFinish(int id);
        void onError(int id, int errorId);
    }

    public DownloadThread(MulThreadBreakpointManager downloader, URL url, File saveFile, int id, long beginPos, long endPos, OnDownloadListener listener) {
        mDownloader = downloader;
        mUrl = url;
        mSaveFile = saveFile;
        mThreadId = id;
        mBeginPos = beginPos;
        mEndPos = endPos;
        mListener = listener;
    }

    @Override
    public void run() {
        super.run();
        try {
            RandomAccessFile accessFile = new RandomAccessFile(mSaveFile, "rwd");
            accessFile.seek(mBeginPos);

            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, application/xml");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", mUrl.toString());
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; wINDOWS NT 5.2; Trident");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Range", "bytes=" + mBeginPos + "-" + mEndPos);
            int code = conn.getResponseCode();
            if (null != mListener) {
                mListener.onStart(mThreadId);
            }
            if (206 == code) {
                isRunning = true;
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[10240];
                int len = 0;
                while (0 < (len = inputStream.read(buffer)) && isRunning) {
                    accessFile.write(buffer, 0, len);
                    if (null != mListener) {
                        mListener.onProcess(mThreadId, len);
                    }
                }
                accessFile.close();
                inputStream.close();
                if (null != mListener) {
                    if (isRunning) {
                        mListener.onFinish(mThreadId);
                    } else {
                        mListener.onStop(mThreadId);
                    }
                }
            } else {
                if (null != mListener) {
                    mListener.onError(mThreadId, code);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mListener) {
                mListener.onError(mThreadId, -1);
            }
        } finally {
            isRunning = false;
        }
    }

    public void stopDownload() {
        isRunning = false;
    }

}
