package com.example.customutil.download.breakpoint;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.util.FileUtil;

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
    private Context mContext;
    private DatabaseManager mDatabaseManager;
    private long mDownloadLength = 0;
    private long mFileLength = 0;
    private DownloadThread[] mThreads;
    private long[] mAlreadDownloadLength;
    private int stopThreadCount = 0;
    private int finishThreadCount = 0;
    private File mTempSaveFile;
    private File mSaveFile;
    private Map<Integer, FileDownloadBean> mDownloadInfo = new ConcurrentHashMap<>();    // 当前下载的进度信息
    private String mDownloadUrl;
    private String mSaveDir;
    final String mFilename;
    private int mThreadNum;
    private OnDownloadListener mListener;

    public interface OnDownloadListener {
        void onStart(long fileLength, long downloadLength);
        void onProcess(long length);
        void onStop();
        void onFinish();
        void onError(int errorCode);
        void onFailed(int threadId, int errorCode);
    }

    public MulThreadBreakpointManager(Context context, String downloadUrl, String saveDir, int threadNum) {
        mContext = context;
        mDownloadUrl = downloadUrl;
        mSaveDir = saveDir;
        mThreadNum = threadNum;
        mDatabaseManager = new DatabaseManager(context);
        mFilename = getFilename(mDownloadUrl);
        mThreads = new DownloadThread[mThreadNum];
        mAlreadDownloadLength = new long[mThreadNum];
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        mListener = listener;
    }

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
            printResponseHeader(conn);
            int responseCode = conn.getResponseCode();
            if (200 == responseCode) {
                mFileLength = conn.getContentLength();
                Log.i(TAG, "the download file's length:" + mFileLength);
                if (mFileLength <= 0) {
                    Log.e(TAG, "the startDownload file size is 0");
                    return -1;
                }

                // 1.读取数据库，更新下载信息
                Map<Integer, FileDownloadBean> databaseLogInfo = mDatabaseManager.getDatabaseLogInfo(mDownloadUrl);
                if (null != databaseLogInfo && 0 < databaseLogInfo.size()) {
                    Log.e(TAG, "databaseLogInfo.size:" + databaseLogInfo.size());
                    // 如果已经存在下载记录，则将各条线程已下载的数据长度放入mData中
                    for (Map.Entry<Integer, FileDownloadBean> entry : databaseLogInfo.entrySet()) {
                        mDownloadInfo.put(entry.getKey(), entry.getValue());
                    }

                    // 原先的下载线程数与当前线程数不一致
                    if (mDownloadInfo.size() != mThreads.length) {
                        Log.e(TAG, "mDownloadInfo.size() != mThreads.length, clean...");
                        mDownloadInfo.clear();
                        mDatabaseManager.delete(mDownloadUrl);
                        mDownloadLength = 0;
                    } else {
                        FileDownloadBean bean;
                        long alreadDownloadLength;
                        for (int i = 0; i < mThreads.length; i++) {
                            bean = mDownloadInfo.get(i);
                            if (null != bean) {
                                alreadDownloadLength = bean.getAlreadyDownloadLength();
                                mDownloadLength += alreadDownloadLength;
                                mAlreadDownloadLength[i] += alreadDownloadLength;
                                Log.w(TAG, "thread " + i + " alreadDownloadSize:" + alreadDownloadLength);
                            }
                        }
                        Log.w(TAG, "mDownloadLength:" + mDownloadLength);
                    }
                }

                if (null != mListener) {
                    mListener.onStart(mFileLength, mDownloadLength);
                }

                // 创建临时文件
                mTempSaveFile = new File(mSaveDir, mFilename + "_temp");
                Log.e(TAG, "mSaveDir:" + mSaveDir + ", filename:" + mFilename + "_temp" + ", mFileLength:" + mFileLength);
                RandomAccessFile randomAccessFile = new RandomAccessFile(mTempSaveFile, "rwd");
                if (0 < mFileLength) {
                    randomAccessFile.setLength(mFileLength);
                }
                randomAccessFile.close();
                Log.i(TAG, "create the temporary file.");

                // 目标已存在时备份
                mSaveFile = new File(mSaveDir, mFilename);
                if (FileUtil.isFileExists(mSaveFile)) {
                    FileUtil.renameFile(mSaveFile, new File(mSaveDir, mFilename.substring(0, mFilename.lastIndexOf(".")) + "_bak_" + getCurrentTimeString() + mFilename.substring(mFilename.lastIndexOf("."))));
                }

                long per = 0;
                long block = 0;
                long beginPos = -1;
                long endPos = -1;
                long alreadyDownloadLength = 0;

                for (int i = 0; i < mThreads.length; i++) {
                    FileDownloadBean databaseBean = mDownloadInfo.get(i);
                    if (null != databaseBean) {
                        Log.e(TAG, "databaseBean is ok.");
                        alreadyDownloadLength = databaseBean.getAlreadyDownloadLength();
                    } else {
                        Log.e(TAG, "databaseBean is null.");
                        // 如果数据库中没有记录，则添加对应线程的记录
                        mDatabaseManager.insert(i, mDownloadUrl, 0, block, beginPos, endPos);
                    }

                    // 2.启动线程，根据下载信息确定个线程所要下载的起始位置和结束位置
                    if (0 == mFileLength % mThreadNum) {
                        per = mFileLength / mThreadNum;
                        block = mFileLength / mThreadNum;
                        beginPos = i * per + alreadyDownloadLength;
                        endPos = (i + 1) * per - 1;
                    } else {
                        per = mFileLength / mThreadNum + 1;
                        if (i < mThreadNum - 1) {
                            block = mFileLength / mThreadNum + 1;
                            beginPos = i * per + alreadyDownloadLength;
                            endPos = (i + 1) * per - 1;
                        } else if (i == (mThreadNum - 1)) {
                            block = mFileLength - i * per;
                            beginPos = i * per + alreadyDownloadLength;
                            endPos = mFileLength - 1;
                        }
                    }

                    Log.i(TAG, "MulThreadBreakpointManager thread i:" + i + ", beginPos:" + beginPos + ", endPos:" + endPos + ", block:" + block + ", alreadyDownloadSize:" + alreadyDownloadLength);

                    if (alreadyDownloadLength < block) {
                        if (null == mThreads[i]) {
                            mThreads[i] = new DownloadThread(this, url, mTempSaveFile, i, beginPos, endPos, new DownloadThread.OnDownloadListener() {
                                @Override
                                public void onStart(int id) {
                                    Log.w(TAG, "OnDownloadListener onStart:" + id);
                                }

                                @Override
                                public void onProcess(int id, int len) {
                                    Log.d(TAG, "OnDownloadListener onProcess:" + id + ", len:" + len);
                                    mAlreadDownloadLength[id] += len;
                                    mDatabaseManager.update(id, mDownloadUrl, mAlreadDownloadLength[id]);
                                    mDownloadLength += len;
                                    if (null != mListener) {
                                        mListener.onProcess(mDownloadLength);
                                    }
                                }

                                @Override
                                public void onStop(int id) {
                                    Log.w(TAG, "OnDownloadListener onStop:" + id);
                                    stopThreadCount++;
                                    if (stopThreadCount == mThreadNum) {
                                        stopThreadCount = 0;
                                        if (null != mListener) {
                                            mListener.onStop();
                                        }
                                    }
                                }

                                @Override
                                public void onFinish(int id) {
                                    Log.w(TAG, "OnDownloadListener onFinish:" + id);
                                    mThreads[id] = null;
                                    finishThreadCount++;
                                    if (finishThreadCount == mThreadNum) {
                                        // 3.下载完成后，更改临时文件名，清理数据库
                                        finishThreadCount = 0;
                                        mFileLength = 0;
                                        mDownloadLength = 0;
                                        FileUtil.renameFile(mTempSaveFile, new File(mSaveDir, mFilename));
                                        mDatabaseManager.delete(mDownloadUrl);
                                        if (null != mListener) {
                                            mListener.onFinish();
                                        }
                                    }
                                }

                                @Override
                                public void onError(int id, int errorId) {
                                    Log.e(TAG, "OnDownloadListener onError:" + id + ", errorId:" + errorId);
                                    if (null != mListener) {
                                        mListener.onFailed(id, errorId);
                                    }
                                }
                            });
                            mThreads[i].setPriority(7);
                            mThreads[i].start();
                        }
                    } else {
                        mThreads[i] = null;
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

    public void stopDownload() {
        for (int i = 0; i < mThreadNum; i++) {
            if (null != mThreads[i]) {
                mThreads[i].stopDownload();
                mThreads[i] = null;
            }
        }
    }

    private String getFilename(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String getCurrentTimeString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();//创建一个date对象保存当前时间
        return simpleDateFormat.format(date);
    }

    private void printResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key : map.keySet()) {
            Log.d(TAG, key + "=" + map.get(key));
        }
    }

    private MulThreadBreakpointManager mulThreadBreakpointManager;
    public void testStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.safeMakeDir("/sdcard/startDownload/breakpoint");
                mulThreadBreakpointManager = new MulThreadBreakpointManager(mContext, "http://192.168.1.84:8080/wtx/wtx.exe", "/sdcard/startDownload/breakpoint", 2);
                mulThreadBreakpointManager.setOnDownloadListener(new OnDownloadListener() {
                    @Override
                    public void onStart(long fileLength, long downloadLength) {

                    }

                    @Override
                    public void onProcess(long length) {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onError(int errorCode) {

                    }

                    @Override
                    public void onFailed(int threadId, int errorCode) {

                    }
                });
                mulThreadBreakpointManager.startDownload();
            }
        }).start();
    }

    public void testStop() {
        mulThreadBreakpointManager.stopDownload();
    }

}
