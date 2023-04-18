package com.example.customutil.download.breakpoint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "wtx_DBOpenHelper";
    private static final String DBNAME = "wtx_db";
    private static final int VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "+++DBOpenHelper onCreate+++");
        // 主键id从1开始自增
        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, threadId integer, urlPath varchar(100), alreadyDownlength integer, block integer, beginPos integer, endPos integer)");
        Log.e(TAG, "---DBOpenHelper onCreate---");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "DBOpenHelper onUpgrade.");
        db.execSQL("DROP TABLE IF EXISTS filedownlog");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
