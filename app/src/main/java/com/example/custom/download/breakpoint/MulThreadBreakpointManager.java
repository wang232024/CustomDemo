package com.example.custom.download.breakpoint;

import android.content.Context;
import android.util.Log;
import com.example.custom.download.multhread.ThreadDownload;
import com.example.util.FileUtil;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多线程断点下载
 * 1.读取数据库，更新下载信息
 * 2.启动线程，根据下载信息确定个线程所要下载的起始位置和结束位置
 * 3.下载完成后，更改临时文件名，清理数据库
 *
 * 连续两次startDownload之后，条用stopDownload，进度条依然在跑？？？
 */
public class MulThreadBreakpointManager {
    private static final String TAG = "wtx_MulThreadBreakpointManager";
    private final DatabaseManager mDatabaseManager;
    private long mDownloadLength = 0;       // 已经下载量
    private long mFileLength = 0;
    private DownloadThread[] mThreads;
    private ThreadDownload[] mThreadDownloads;
    private long[] mBlock;
    private long[] mAlready;
    private long[] mBeginPos;
    private long[] mEndPos;
    private int stopThreadCount = 0;
    private int finishThreadCount = 0;
    private File mTempSaveFile = null;
    private File mFile;
    private Map<Integer, FileDownloadBean> mDownloadInfo = new ConcurrentHashMap<>();    // 当前下载的进度信息
    private String mDownloadUrl;
    private String mSaveDir;
    private String mFilename;
    private int mThreadNum;
    private OnDownloadListener mListener;
    private int mFinishCount = 0;
    private Object mObjectThread = new Object();

    public interface OnDownloadListener {
        void onStart(long fileLength, long downloadLength);
        void onProcess(long length);
        void onStop();
        void onFinish();
        void onError(int errorCode);
        void onFailed(int threadId, int errorCode);
    }

    // 测试数据库用
    public MulThreadBreakpointManager(Context context) {
        mDatabaseManager = new DatabaseManager(context);
    }

    public MulThreadBreakpointManager(Context context, String downloadUrl, String saveDir, File file, int threadNum) {
        mDownloadUrl = downloadUrl;
        mSaveDir = saveDir;
        mFile = file;
        mThreadNum = threadNum;
        mDatabaseManager = new DatabaseManager(context);
        mFilename = getFilename(mDownloadUrl);
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        mListener = listener;
    }

    private void initDataInfo() {
        mThreads = new DownloadThread[mThreadNum];
        mThreadDownloads = new ThreadDownload[mThreadNum];
        mBlock = new long[mThreadNum];
        mAlready = new long[mThreadNum];
        mBeginPos = new long[mThreadNum];
        mEndPos = new long[mThreadNum];

        List<FileDownloadBean> list = mDatabaseManager.getFileDownloadBeanList(mDownloadUrl);
        if (mThreadNum != list.size() && 0 < list.size()) {
            Log.w(TAG, "clean download database..." + mThreadNum + ", " + list.size());
            mDatabaseManager.delete(mDownloadUrl);
            mDownloadLength = 0;
        } else {
            long per = mFileLength / mThreadNum;
            long left = mFileLength % mThreadNum;
            for (int i = 0; i < mThreadNum; i++) {
                if (mThreadNum - 1 == i) {
                    mBlock[i] = mFileLength - i * per;
                    mBeginPos[i] = i * per;
                    mEndPos[i] = mFileLength - 1;
                } else {
                    mBlock[i] = per;
                    mBeginPos[i] = i * per;
                    mEndPos[i] = (i + 1) * per - 1;
                }

                Log.i(TAG, "initDataInfo, list.size:" + list.size());
                if (0 < list.size()) {
                    FileDownloadBean bean = list.get(i);
                    if (null != bean) {
                        long alreadDownloadLength = bean.getAlreadyDownloadLength();
                        mDownloadLength += alreadDownloadLength;
                        mAlready[i] = alreadDownloadLength;
                        String tmpFilePath = bean.getTmpFilePath();
                        Log.i(TAG, "initDataInfo, " + i + ", tmpFilePath:" + tmpFilePath);
                        if (null != tmpFilePath) {
                            if (0 == i) {
                                mTempSaveFile = new File(tmpFilePath);
                                Log.i(TAG, "mTempSaveFile:" + mTempSaveFile.getPath());
                            }
                        }
                    }
                }
                Log.d(TAG, i + ", [" + mBeginPos[i] + ", " + mEndPos[i] + "], block:" + mBlock[i] + ", already:" + mAlready[i]);
            }
        }

    }

    ThreadDownload.ThreadDownloadListener mThreadDownloadListener = new ThreadDownload.ThreadDownloadListener() {
        @Override
        public void onStart(int index, long begin, long end, long block, long length) {
            Log.w(TAG, "onStart, index:" + index + ", [" + begin + ", " + end + "], block:" + block + ", length:" + length + ", mTempSaveFile:" + mTempSaveFile.getPath());
            mDatabaseManager.insert(index, mDownloadUrl, mTempSaveFile.getPath(), 0, 0, 0, 0);
            query("start");
        }

        @Override
        public void onProcess(int index, byte[] buffer, int len) {
            mAlready[index] += len;
            mDatabaseManager.update(index, mDownloadUrl, mAlready[index]);
        }

        @Override
        public void onFinish(int index, int threadNum, File file, int responseCode, long timeConnect, long timeReadWrite, long timeWaste) {
            Log.w(TAG, "onFinish, index:" + index + ", threadNum:" + threadNum + ", responseCode:" + responseCode);
            synchronized (mObjectThread) {
                mFinishCount++;
                if (threadNum == mFinishCount) {
//                    mTimeStop = System.currentTimeMillis();
                    FileUtil.renameFile(file, mFile);
                    Log.w(TAG, "All finish, file:" + file.getPath() + ", mFile:" + mFile);
                    mDatabaseManager.delete(mDownloadUrl);
                    mTempSaveFile = null;
                }
            }
        }

        @Override
        public void onStop(int index, int threadNum, File file, long sum) {
            query("onStop");
        }

        @Override
        public void onFailed(int index, int responseCode) {
            Log.e(TAG, "onFailed, index:" + index + ", responseCode:" + responseCode);
        }
    };

    public int startDownload() {
        try {
            URL url = new URL(mDownloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, application/xml");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", mDownloadUrl);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; wINDOWS NT 5.2; Trident");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
//            printResponseHeader(conn);
            int responseCode = conn.getResponseCode();
            Log.i(TAG, "responseCode:" + responseCode);
            if (200 == responseCode) {
                mFileLength = conn.getContentLength();
                Log.i(TAG, "the download file's length:" + mFileLength);

                if (null != mListener) {
                    mListener.onStart(mFileLength, mDownloadLength);
                }

                // 读取数据库数据，确定线程下载起始位置和数据量
                initDataInfo();
                Log.e(TAG, "initDataInfo, mTempSaveFile:" + mTempSaveFile);

                // 创建临时文件
                String mFileSufix = getCurrentTimeString();
                if (null == mTempSaveFile) {
                    mTempSaveFile = new File(mFile.getPath() + mFileSufix);
                }
                Log.e(TAG, "mTempSaveFile.getPath:" + mTempSaveFile.getPath());
                Log.w(TAG, "create the temporary file.");

                for (int i = 0; i < mThreadNum; i++) {
                    if (mAlready[i] < mBlock[i]) {
                        mThreadDownloads[i] = new ThreadDownload(i, mThreadNum,
                                mFileLength, mBlock[i], mBeginPos[i], mEndPos[i],
                                url, mTempSaveFile, mThreadDownloadListener
                        );
                        mThreadDownloads[i].start();
                    } else {
                        Log.w(TAG, "Thread:" + i + " already download all data.");
                        mDatabaseManager.delete(mDownloadUrl);
                    }
                }
            } else {
                if (null != mListener) {
                    mListener.onError(responseCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mListener) {
                mListener.onError(-1);
            }
        }
        return 0;
    }

    public void query(String tag) {
        List<FileDownloadBean> list = mDatabaseManager.getFileDownloadBeanList(mDownloadUrl);
        Log.i(TAG, "query, " + tag + ", list.size:" + list.size());
        Log.i(TAG, "query, " + tag + ", mTempSaveFile:" + mTempSaveFile.getPath());
        if (0 < list.size()) {
            for (int i = 0; i < list.size(); i++) {
                FileDownloadBean bean = list.get(i);
                Log.i(TAG, "query, " + tag + ", bean:" + bean);
            }
        }
    }

    public void breakpointInsert() {
        mDatabaseManager.insert(0, "http://test.com", "/sdcard/test/abc.apk", 5, 3, 7, 2);
    }
    public void breakpointDelete() {
        mDatabaseManager.delete("http://test.com");
    }
    public void breakpointUpdate() {
        mDatabaseManager.update(0, "http://test.com", 100);
    }
    public void breakpointQuery() {
        List<FileDownloadBean> list = mDatabaseManager.getFileDownloadBeanList("http://test.com");
        Log.i(TAG, "breakpointQuery, list.size:" + list.size());
        if (0 < list.size()) {
            for (int i = 0; i < list.size(); i++) {
                FileDownloadBean bean = list.get(i);
                Log.i(TAG, "breakpointQuery, bean:" + bean);
            }
        }
    }

    public void stopDownload() {
        for (int i = 0; i < mThreadNum; i++) {
            if (null != mThreadDownloads && null != mThreadDownloads[i]) {
                mThreadDownloads[i].stopDownload();
                mThreadDownloads[i] = null;
            }
        }
    }

    private static String getFilename(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private static String getCurrentTimeString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();//创建一个date对象保存当前时间
        return simpleDateFormat.format(date);
    }

    private void printResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key : map.keySet()) {
            Log.d(TAG, key + "=" + map.get(key));
        }
    }

}
