package com.example.common;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * 关闭打印:    DEBUG置为false，并且清理掉logger.init文件
 * 开启打印:    添加logger.init文件     开启打印，并且保存日志文件
 *             DEBUG置为true          开启打印
 *
 * 开启打印和保存日志，需在Application或activity中调用Logger.init(context);
 * storage/emulated/0/Android/data/${packagename}/files/logger.init     生成时保存日志文件/删除时停止保存日志文件
 * storage/emulated/0/Android/data/${packagename}/files/log/xxxx.log    日志保存路径
 */
public class Logger {
    private static final boolean DEBUG = true;
    private static final String TAG = "wtx_Logger";
    private static Thread mLogThread;
    private static boolean mLogThreadRun;
    private static final Object mLogThreadObject = new Object();
    private static StringBuffer mStringBuffer = new StringBuffer();
    private static FileWriter mLoggerFileWriter;
    private static final String LOG_SWITCH = "logger.init";
    private static final String LOG_DIR_NAME = "log";       // "storage/emulated/0/Android/data/${packagename}/files/log"目录下
    private static final String LOG_NAME_FORMAT = "yyyyMMdd_HHmmss";    // 日志保存文件名称格式
    private static final String LOG_TAG_TIME = "yyyy-MM-dd HH:mm:ss.SSS";   // 日志打印时间格式
    private static final float TIME_ZONE = 8;
    private static File mFiles;
    private static File mLogDir;

    public static void init(Context context) {
        mFiles = context.getExternalFilesDir("");
        mLogDir = context.getExternalFilesDir(LOG_DIR_NAME);
        Log.w(TAG, "init:" + mLogDir.toString());
        Log.w(TAG, "init:" + LOG_SWITCH);
    }

    private static void startLogThread() {
        if (null == mLogThread) {
            mLogThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.w(TAG, "startLogThread.");
                    mLogThreadRun = true;
                    try {
                        while (mLogThreadRun) {
                            synchronized (mLogThreadObject) {
                                if (null != mLoggerFileWriter) {
                                    mLoggerFileWriter.write(mStringBuffer.toString());
                                    mLoggerFileWriter.flush();
                                }
                                mStringBuffer = new StringBuffer();
                                mLogThreadObject.wait();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mLogThread.start();
        }
    }

    private static void stopLogThread() {
        if (null != mLogThread) {
            mLogThread.interrupt();
            mLogThread = null;
            mLogThreadRun = false;
            mLoggerFileWriter = null;
            Log.w(TAG, "stopLogThread.");
        }
    }

    private static boolean initLogFile() {
        if (null == mFiles) {
            return false;
        }
        File loggerInitFile = new File(mFiles.toString() + File.separator + LOG_SWITCH);
        if (loggerInitFile.isFile() && loggerInitFile.exists()) {
            if (null == mLoggerFileWriter) {
                String logName = getFormatedDateString(System.currentTimeMillis(), TIME_ZONE, LOG_NAME_FORMAT) + ".log";
                try {
                    mLoggerFileWriter = new FileWriter(mLogDir.toString() + File.separator + logName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static void log(String tag, int priority, String content) {
        if (initLogFile()) {
            String time = getFormatedDateString(System.currentTimeMillis(), TIME_ZONE, LOG_TAG_TIME);
            synchronized (mLogThreadObject) {
                mStringBuffer.append(time).append(" ");
                mStringBuffer.append(PRIORITY[priority]).append("/");
                mStringBuffer.append(tag).append(": ");
                mStringBuffer.append(content).append("\n");
            }
            if (null == mLogThread) {
                startLogThread();
            } else {
                synchronized (mLogThreadObject) {
                    mLogThreadObject.notify();
                }
            }
            Log.println(priority, tag, content);
        } else {
            if (DEBUG) {
                Log.println(priority, tag, content);
            }
            stopLogThread();
        }
    }

    private static final String[] PRIORITY = {
            "0", "1",
            "V", "D", "I", "W", "E"
    };

    public static void d(String tag, String info) {
        log(tag, Log.DEBUG, info);
    }

    public static void i(String tag, String info) {
        log(tag, Log.INFO, info);
    }

    public static void w(String tag, String info) {
        log(tag, Log.WARN, info);
    }

    public static void e(String tag, String info) {
        log(tag, Log.ERROR, info);
    }

    public static void logUri(Context context, String tag, Uri uri) {
        i(TAG, tag + ", uri:" + uri);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        logCursor(tag, cursor);
    }

    /**
     * 打印游标
     * @param tag       标记字符串
     * @param cursor
     */
    public static void logCursor(String tag, Cursor cursor) {
        if (null == cursor) {
            e(TAG, "logCursor, cursor is null:" + tag);
            return;
        }
        if (0 >= cursor.getCount()) {
            w(TAG, "logCursor, cursor is empty:" + tag);
            return;
        }

        i(TAG, tag + ", getCount:" + cursor.getCount() + ", getColumnCount:" + cursor.getColumnCount());

        cursor.moveToFirst();
        StringBuilder columnStringBuilder = new StringBuilder();
        columnStringBuilder.append("[");
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String columnName = cursor.getColumnName(i);
            columnStringBuilder.append(i).append(":").append(columnName);
            if (i != cursor.getColumnCount() - 1) {
                columnStringBuilder.append(", ");
            }
        }
        columnStringBuilder.append("]");
        i(TAG, tag + ", column Name:" + columnStringBuilder.toString());

        cursor.moveToFirst();
        do {
            StringBuilder cursorStringBuilder = new StringBuilder();
            cursorStringBuilder.append("[");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String string = "Unable to convert BLOB to string";
                try {
                    string = cursor.getString(i);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                cursorStringBuilder.append(columnName).append(":");
                cursorStringBuilder.append(string);
                if (i != cursor.getColumnCount() - 1) {
                    cursorStringBuilder.append(", ");
                }
            }
            cursorStringBuilder.append("]");
            i(TAG, tag + ", columnValue:" + cursorStringBuilder.toString());
        } while (cursor.moveToNext());

        cursor.moveToFirst();
    }

    /**
     * 打印字符串列表
     * @param tag       标记字符串
     * @param list
     */
    public static void logList(String tag, List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i != list.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        i(TAG, tag + ", list:" + stringBuilder.toString());
    }

    /**
     * 获取表示当前时间的字符串
     *
     * @param milliseconds   当前时间的长整型表示              1574922395729LL(13位整数：毫秒 / 10位整数：秒)
     * @param timeZoneOffset 时区，北京：8
     * @param format         返回字符串的格式，如"2019-11-28_14-26-35_729" / "2019-11-28_14-26-35"。
     * @return 当前时间的字符串
     */
    public static String getFormatedDateString(long milliseconds, float timeZoneOffset, String format) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }

        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);

        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);

        return sdf.format(new Date(milliseconds));
    }

    public static final String[] STATUS_TEXT = {
            "InvalidContent", "InvalidWBXML", "InvalidXML", "InvalidDateTime", "InvalidIDCombo",
            "InvalidIDs", "InvalidMIME", "DeviceIdError", "DeviceTypeError", "ServerError",
            "ServerErrorRetry", "ADAccessDenied", "Quota", "ServerOffline", "SendQuota",
            "RecipientUnresolved", "ReplyNotAllowed", "SentPreviously", "NoRecipient", "SendFailed",
            "ReplyFailed", "AttsTooLarge", "NoMailbox", "CantBeAnonymous", "UserNotFound",
            "UserDisabled", "NewMailbox", "LegacyMailbox", "DeviceBlocked", "AccessDenied",
            "AcctDisabled", "SyncStateNF", "SyncStateLocked", "SyncStateCorrupt", "SyncStateExists",
            "SyncStateInvalid", "BadCommand", "BadVersion", "NotFullyProvisionable", "RemoteWipe",
            "LegacyDevice", "NotProvisioned", "PolicyRefresh", "BadPolicyKey", "ExternallyManaged",
            "NoRecurrence", "UnexpectedClass", "RemoteHasNoSSL", "InvalidRequest", "ItemNotFound"
    };

    public static void test(Context context) {
        Uri uri = Uri.parse("content://com.example.customutil.fileprovider/external_cache_path/yanglao.xls");
        logUri(context, "logger", uri);

        // 打印数组
        i(TAG, Arrays.toString(STATUS_TEXT));

        // 数组转列表
        List<String> list = Arrays.asList(STATUS_TEXT);

        // 打印列表
        logList("logger", list);

//        new Thread(() -> {
//            // 测试增删logger.init以开启和关闭log线程
//            for (int i = 0; i < 3000; i++) {
//                try {
//                    Thread.sleep(500);
//                    long cur = System.currentTimeMillis();
//                    long time = cur > 0 ? cur : -cur;
//                    int random = (int) (time % Logger.STATUS_TEXT.length);
//                    Logger.i(TAG, i + "--->" + Logger.STATUS_TEXT[random]);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

}