package com.example.bugreport;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.common.KLog;

import java.util.HashMap;

public class BugreportManager {
    private static final String TAG = "BugreportManager";
    public static final String[] BUG_NAME_LIST = {
            "Handler waster time.",
            "OutOfMemoryError",
    };
    private static int[] mData;
    private static final int MSG_INIT = 0x1000;
    private static final int MSG_HANDLER_WASTER = 0x1000;
    private static final int MSG_OutOfMemoryError = 0x1001;

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            long time1 = System.currentTimeMillis();
            KLog.i(TAG, "msg.what:" + msg.what);
            switch (msg.what) {
                case MSG_HANDLER_WASTER:
                    mData = new int[1024 * 1024];
                    for (int k = 0; k < 8; k++) {
                        for (int j = 0; j < 128; j++) {
                            for (int i = 0; i < mData.length; i++) {
                                mData[i] = i % 1024;
                            }
                        }
                        KLog.i("wtx", "MSG_HANDLER_WASTER " + k);
                    }
                    break;
                case MSG_OutOfMemoryError:
                    mData = new int[1024 * 1024 * 1024];
                    break;
                default:

                    break;
            }
            long time2 = System.currentTimeMillis();
            KLog.i(TAG, "MSG_HANDLER_WASTER:" + (time2 - time1));
        }

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
        }

        @NonNull
        @Override
        public String getMessageName(@NonNull Message message) {
            return super.getMessageName(message);
        }

        @Override
        public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
            return super.sendMessageAtTime(msg, uptimeMillis);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    };

    public BugreportManager(Context mContext) {

    }

    public void bugTest(int position) {
        KLog.i(TAG, "test");
        mHandler.sendEmptyMessage(MSG_INIT + position);
    }

}
