package com.example.customutil.download.multhread;

import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.common.util.FileUtil;

/**
 * 多线程下载
 * 1.根据http连接应答，创建一个与下载源文件同名同大小的文件。
 * 2.根据文件大小和线程数量，分配每个线程应该下载的大小，起始和结束等位置。
 * 3.线程读取输入流，并通过RandomAccessFile写入文件对应的位置。
 *
 * 只能一个文件一个文件下载。
 */
public class MulThreadDownload {
    private static final String TAG = "wtx_MulThreadDownload";
    private DownloadListener mListener;
    private int mThreadfinishCount;
    private long mTimeStart;
    private long mTimeStop;
    private long mTimeWaste;
    private boolean isAlreadyRun = false;

    public MulThreadDownload(DownloadListener listener) {
        mListener = listener;
    }

    public interface DownloadListener {
        void onStart(int threadid, long begin, long end, long block, long length);

        void onProcess(int threadid, byte[] buffer, int len);

        void onFinish(int threadid, long timeConnect, long timeReadWrite, long timeWaste);

        void onAllFinish(long tiemWaste);

        void onFailed(int threadid);
    }

    public void download(final String path, int threadNum) throws Exception {
        if (isAlreadyRun) {
            return;
        }
        isAlreadyRun = true;
        mTimeStart = System.currentTimeMillis();
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");
        if (200 == conn.getResponseCode()) {
            long length = conn.getContentLength();
            if (FileUtil.safeMakeDir(Constant.DOWNLOAD_DIR)) {
                File file = new File(Constant.DOWNLOAD_DIR + File.separator + getFilename(path));

                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                accessFile.setLength(length);
                accessFile.close();

                for (int threadid = 0; threadid < threadNum; threadid++) {
                    new DownloadThread(threadid, threadNum, length, url, file).start();
                }
            } else {
                Log.e(TAG, "MulThreadDownload startDownload create DOWNLOAD_DIR failed.");
            }
        } else {
            Log.e(TAG, "MulThreadDownload startDownload failed.");
        }
    }

    private class DownloadThread extends Thread {
        private int threadid;
        private int threadNum;
        private long length;
        private URL url;
        private File file;
        private long block;          // 线程下载的数据量
        private long beginPos = 0;      // 线程开始下载的位置
        private long endPos = -1;       // 线程结束下载的位置
        private long per;            // 非最后线程的下载数据量
        private long timeStart;
        private long timeConnect;
        private long timeReadWrite;
        private long timeStop;

        public DownloadThread(int threadid, int threadNum, long length, URL url, File file) {
            this.threadid = threadid;
            this.threadNum = threadNum;
            this.length = length;
            this.url = url;
            this.file = file;
        }

        @Override
        public void run() {
            if (threadid >= threadNum) {
                return ;
            }
            timeStart = System.currentTimeMillis();
            // 计算对应id的线程所要下载的起始位置和结束位置
            if (0 == length % threadNum) {
                per = length / threadNum;
                block = length / threadNum;
                beginPos = threadid * per;
                endPos = (threadid + 1) * per - 1;
            } else {
                per = length / threadNum + 1;
                if (threadid < threadNum - 1) {
                    block = length / threadNum + 1;
                    beginPos = threadid * per;
                    endPos = (threadid + 1) * per - 1;
                } else if (threadid == (threadNum - 1)) {
                    block = length - threadid * per;
                    beginPos = threadid * per;
                    endPos = length - 1;
                }
            }

            if (null != mListener) {
                mListener.onStart(threadid, beginPos, endPos, block, length);
            }

            try {
                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                accessFile.seek(beginPos);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                //通过Range字段指定访问的数据段
                conn.setRequestProperty("Range", "bytes=" + beginPos + "-" + endPos);
                int code = conn.getResponseCode();
                if (206 == code) {
                    timeConnect = System.currentTimeMillis();
                    InputStream inputStream = conn.getInputStream();
                    byte[] buffer = new byte[10240];
                    int len = 0;
                    while (0 < (len = inputStream.read(buffer))) {
                        accessFile.write(buffer, 0, len);
                        if (null != mListener) {
                            mListener.onProcess(threadid, buffer, len);
                        }
                    }
                    accessFile.close();
                    inputStream.close();
                    timeReadWrite = System.currentTimeMillis();
                    if (null != mListener) {
                        timeStop = System.currentTimeMillis();
                        mListener.onFinish(threadid, (timeConnect - timeStart), (timeReadWrite - timeConnect), (timeStop - timeStart));
                        mThreadfinishCount++;
                        if (mThreadfinishCount == threadNum) {
                            mTimeStop = System.currentTimeMillis();
                            mListener.onAllFinish(mTimeStop - mTimeStart);
                            initParameter();
                        }
                    }
                } else {
                    if (null != mListener) {
                        mListener.onFailed(threadid);
                        initParameter();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                initParameter();
            }
        }
    }

    private String getFilename(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private void initParameter() {
        mThreadfinishCount =0;
        isAlreadyRun =false;
    }

    public void mulThreadDownloadTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    MulThreadDownload.startDownload("http://192.168.1.84:9000/update.binary", 3);
//                    MulThreadDownload.startDownload("http://192.168.1.84:8080/wtx/Postman-win64-7.0.9-Setup.exe", 8, mulThreadDownloadListener);
//                    MulThreadDownload.startDownload("http://192.168.1.84:8080/wtx/HexEditor_0_9_5_UNI_dll.zip", 3);
//                    MulThreadDownload.startDownload("http://192.168.1.84:8080/wtx/temp.zip", 1, mulThreadDownloadListener);
//                    MulThreadDownload.startDownload("http://192.168.1.84:8080/wtx/wtx.exe", 3);

//                    MulThreadDownload mulThreadDownload1 = new MulThreadDownload(mulThreadDownloadListener);
//                    mulThreadDownload1.startDownload("http://192.168.1.84:8080/wtx/Postman-win64-7.0.9-Setup.exe", 8);

                    MulThreadDownload mulThreadDownload2 = new MulThreadDownload(mulThreadDownloadListener);
                    mulThreadDownload2.download("http://192.168.1.84:8080/wtx/wtx.exe", 8);

//                    MulThreadDownload mulThreadDownload3 = new MulThreadDownload(mulThreadDownloadListener);
//                    mulThreadDownload3.startDownload("http://192.168.1.84:8080/wtx/HexEditor_0_9_5_UNI_dll.zip", 8);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private DownloadListener mulThreadDownloadListener = new DownloadListener() {
        @Override
        public void onStart(int threadid, long begin, long end, long block, long length) {
            Log.i(TAG, "onStart threadid:" + threadid + ", begin:" + begin + ", end:" + end + ", block:" + block + ", length:" + length);
        }

        @Override
        public void onProcess(int threadid, byte[] buffer, int len) {

        }

        @Override
        public void onFinish(int threadid, long timeConnect, long timeReadWrite, long timeWaste) {
            Log.w(TAG, "onFinish threadid:" + threadid + ", timeConnect:" + timeConnect + ", timeReadWrite:" + timeReadWrite + ", timeWaste:" + timeWaste);
        }

        @Override
        public void onAllFinish(long tiemWaste) {
            Log.w(TAG, "onAllFinish tiemWaste:" + tiemWaste);
        }

        @Override
        public void onFailed(int threadid) {
            Log.w(TAG, "onFailed threadid:" + threadid);
        }
    };

}
