package com.example.custom.download.breakpoint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "wtx_DBOpenHelper";
    private static final String DBNAME = "multhread_breakpoint_download_db";
    private static final int VERSION = 1;

    public static final String TABLE_NAME_DOWNLOAD = "filedown";
    public static final String[] TAB_PROJECTION = new String[] {
            "_id", "urlPath", "filePath",
            "alreadyDownlength", "block",
            "beginPos", "endPos",
    };

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "DBOpenHelper onCreate");
//        // 主键id从1开始自增
//        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, " +
//                "threadId integer, urlPath varchar(100), tmpFilePath varchar(100), alreadyDownlength integer, block integer, beginPos integer, endPos integer)"
//        );
        // 主键为id号
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DOWNLOAD + "("
                + TAB_PROJECTION[0] + " INTEGER PRIMARY KEY,"
                + TAB_PROJECTION[1] + " VARCHAR(100),"
                + TAB_PROJECTION[2] + " varchar(100),"
                + TAB_PROJECTION[3] + " INTEGER,"
                + TAB_PROJECTION[4] + " INTEGER,"
                + TAB_PROJECTION[5] + " INTEGER,"
                + TAB_PROJECTION[6] + " INTEGER"
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "DBOpenHelper onUpgrade.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOWNLOAD);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
