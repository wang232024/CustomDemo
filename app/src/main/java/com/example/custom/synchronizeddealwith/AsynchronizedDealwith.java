package com.example.custom.synchronizeddealwith;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 开启线程后，按顺序向列表中插入数据
 * 每次取出数据后休眠eventFrqTime
 */
public class AsynchronizedDealwith {
    private static final String TAG = "wtx_AsynchronizedDealwith";
    private OnDealwithListener mListener;
    final static private List<DataWrap> mList = new ArrayList<>();
    private static final int LIST_MAX_SIZE = 10;
    private static final long eventFrqTime = 2000L;
    private static final long threadSleep = 1000L;
    private AtomicBoolean isDealwithThreadRun = new AtomicBoolean(false);
    private AtomicBoolean isNotSafeClosed = new AtomicBoolean(false);
    private Thread mDealwithThread = null;

    public interface OnDealwithListener {
        void onDealBegin();
        void onDealwith(Object object);
        void onDealFinish();
    }

    public AsynchronizedDealwith(OnDealwithListener listener) {
        mListener = listener;
    }

    public void insertAndDealwith(String data) {
        insertData(data);
        if (!getDealwithThreadRun()) {
            startDealwithThread();
        }
    }

    private boolean getDealwithThreadRun() {
        return isDealwithThreadRun.get();
    }

    private void setDealwithThreadRun(boolean flag) {
        isDealwithThreadRun.set(flag);
    }

    private void startDealwithThread() {
        stopDealwithThread();
        mDealwithThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onDealBegin();
                }
                Log.e(TAG, "startDealwithThread(), start.");
                try {
                    if (isNotSafeClosed.get()) {
                        Log.e(TAG, "isNotSafeClosed, sleep");
                        Thread.sleep(threadSleep);
                    }
                    isNotSafeClosed.set(true);
                    setDealwithThreadRun(true);

                    while (getDealwithThreadRun()) {
                        DataWrap dataWrap = fetchData();
                        if (dataWrap != null) {
                            if (mListener != null) {
                                mListener.onDealwith(dataWrap);
                            }
                        } else {
                            break;
                        }
                        Thread.sleep(eventFrqTime);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "startDealwithThread(), catch:" + e);
                }
                isNotSafeClosed.set(false);
                setDealwithThreadRun(false);
                Log.e(TAG, "startDealwithThread(), end.");
                if (mListener != null) {
                    mListener.onDealFinish();
                }
                mDealwithThread = null;
            }
        });
        mDealwithThread.start();
    }

    private void stopDealwithThread() {
        setDealwithThreadRun(false);
        if (mDealwithThread != null) {
            mDealwithThread.interrupt();
            mDealwithThread = null;
        }
    }

    private void insertData(String data) {
        synchronized (mList) {
            if (LIST_MAX_SIZE == mList.size()) {
                Log.i(TAG, "mList.size() is LIST_MAX_SIZE.");
                return;
            }
            DataWrap dataWrap = new DataWrap(data);
            mList.add(dataWrap);
            Log.i(TAG, "mList.size() is : " + mList.size());
        }
    }

    private DataWrap fetchData() {
        synchronized (mList) {
            if (mList.size() > 0) {
                DataWrap dataWrap = mList.get(0);
                mList.remove(0);
                return dataWrap;
            }
            return null;
        }
    }

    public void cleanList() {
        synchronized (mList) {
            mList.clear();
        }
    }

}
