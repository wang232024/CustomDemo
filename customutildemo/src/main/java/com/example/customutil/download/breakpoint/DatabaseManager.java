package com.example.customutil.download.breakpoint;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于操作数据库的类
 */
public class DatabaseManager {
    private static final String TAG = "wtx_DatabaseManager";
    private DBOpenHelper openHelper;

    public DatabaseManager(Context context) {
        Log.e(TAG, "DatabaseManager, context:" + context);
        openHelper = new DBOpenHelper(context);
    }

    public Map<Integer, FileDownloadBean> getDatabaseLogInfo(String path) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from filedownlog where urlPath=?", new String[]{path});
        Map<Integer, FileDownloadBean> map = new HashMap<>();
        while (cursor.moveToNext()) {
            FileDownloadBean bean = new FileDownloadBean();
            bean.setId(cursor.getInt(1));
            bean.setUrl(cursor.getString(2));
            bean.setAlreadyDownloadLength(cursor.getLong(3));
            bean.setBlock(cursor.getLong(4));
            bean.setBeginPos(cursor.getLong(5));
            bean.setEndPos(cursor.getLong(6));
            map.put(cursor.getInt(1), bean);
        }
        cursor.close();
        db.close();
        return map;
    }

    public void insert(int threadId, String path, long alreadyDownlength, long block, long beginPos, long endPos) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("insert into filedownlog(threadId, urlPath, alreadyDownlength, block, beginPos, endPos) values (?, ?, ?, ?, ?, ?)",
                    new Object[]{threadId, path, alreadyDownlength, block, beginPos, endPos});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public synchronized void update(int threadId, String urlPath, long alreadyDownlength) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("update filedownlog set alreadyDownlength=? where threadId=? and urlPath=?",
                new Object[]{alreadyDownlength, threadId, urlPath});
        db.close();
    }

    public synchronized void delete(String path) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("delete from filedownlog where urlPath=?", new Object[]{path});
        db.close();
    }

    public void query() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
//        Cursor cursor = db1.rawQuery("select * from filedownlog where downpath=?", new String[]{path});
        Cursor cursor = db.rawQuery("select * from filedownlog", new String[]{});
        Map<Integer, Integer> data = new HashMap<>();
        int count = cursor.getColumnCount();
//        Log.i(TAG, "moveToNext:" + cursor.moveToNext());
//        Log.i(TAG, "getColumnNames" + Arrays.toString(cursor.getColumnNames()));
//        Log.i(TAG, "moveToFirst:" + cursor.moveToFirst());
//        Log.i(TAG, "cursor:" + cursor.toString());
        Log.i(TAG, "query------>getCount:" + cursor.getCount());
        cursor.close();
        db.close();
    }
}
