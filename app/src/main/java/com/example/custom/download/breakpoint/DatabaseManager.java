package com.example.custom.download.breakpoint;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.util.Logger;

/**
 * 用于操作数据库的类
 */
public class DatabaseManager {
    private static final String TAG = "wtx_DatabaseManager";
    private final DBOpenHelper openHelper;
    private boolean DEBUG_DATABASE = true;

    public DatabaseManager(Context context) {
        Log.e(TAG, "DatabaseManager, context:" + context);
        openHelper = new DBOpenHelper(context);
    }

    public synchronized List<FileDownloadBean> getFileDownloadBeanList(String path) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from " + DBOpenHelper.TABLE_NAME_DOWNLOAD + " where urlPath=?",
                new String[] {path}
        );
        Logger.logCursor("aaaaa", cursor);
        List<FileDownloadBean> list = new ArrayList<>();
        if (0 < cursor.getCount()) {
            while (cursor.moveToNext()) {
                FileDownloadBean bean = new FileDownloadBean();
                bean.setId(cursor.getInt(0));
                bean.setUrl(cursor.getString(1));
                bean.setTmpFilePath(cursor.getString(2));
                bean.setAlreadyDownloadLength(cursor.getLong(3));
                bean.setBlock(cursor.getLong(4));
                bean.setBeginPos(cursor.getLong(5));
                bean.setEndPos(cursor.getLong(6));
                list.add(bean);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    public synchronized void insert(int id, String urlPath, String filePath, long alreadyDownlength, long block, long beginPos, long endPos) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            if (DEBUG_DATABASE) {
                Log.i(TAG, "insert begin, id:" + id + ", urlPath:" + urlPath + ", filePath:" + filePath +
                        ", alreadyDownlength:" + alreadyDownlength + ", block:" + block + ", beginPos:" + beginPos + ", endPos:" + endPos
                );
            }
            db.execSQL("insert into " + DBOpenHelper.TABLE_NAME_DOWNLOAD + "(_id, urlPath, filePath, alreadyDownlength, block, beginPos, endPos) values (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{id, urlPath, filePath, alreadyDownlength, block, beginPos, endPos});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public synchronized void update(int id, String urlPath, long alreadyDownlength) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("update " + DBOpenHelper.TABLE_NAME_DOWNLOAD + " set alreadyDownlength=? where _id=? and urlPath=?",
                new Object[]{alreadyDownlength, id, urlPath});
        db.close();
    }

    public synchronized void delete(String path) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("delete from " + DBOpenHelper.TABLE_NAME_DOWNLOAD + " where urlPath=?", new Object[]{path});
        db.close();
    }

    public void query() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.TABLE_NAME_DOWNLOAD, new String[]{});
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
