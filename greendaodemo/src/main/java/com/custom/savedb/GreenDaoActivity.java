package com.custom.savedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.custom.savedb.greendaobean.DaoMaster;
import com.custom.savedb.greendaobean.DaoSession;
import com.custom.savedb.greendaobean.UserImDao;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class GreenDaoActivity extends AppCompatActivity {
    private static final String TAG = "wtx_MainActivity";

    private DaoMaster daoMaster;
    private static DaoSession daoSession;
    private DaoMaster.DevOpenHelper devOpenHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greendao);

        Log.i(TAG, "getPackageName:" + getPackageName());

        devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(),"数据库名称.db",null);
        //实例化DaoMaster对象
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        //实例化DaoSession对象
        daoSession = daoMaster.newSession();

        UserImDao userDao = daoSession.getUserImDao();
        Log.w(TAG, "------------------0");
        query(userDao);
        add(userDao);
        query(userDao);
        Log.w(TAG, "------------------1");
        modify(userDao);
        query(userDao);
        Log.w(TAG, "------------------2");
        delete(userDao);
        query(userDao);
        Log.w(TAG, "------------------3");
        deleteAll();
        query(userDao);
        Log.w(TAG, "------------------4");
    }

    // 增
    private void add(UserImDao userDao) {
        for (int i = 0; i < 10; i++) {
            userDao.insert(new UserIm(null, "id_" + i, "name_" + i, "http://www.xxx.com"));
        }
    }

    // 删
    private void delete(UserImDao userDao) {
        //删除某一条数据
        userDao.deleteByKey(7L);
    }

    private void deleteAll() {
        //清空所有数据
        DaoSession daoSession = getDaoSession();
        Database database = daoSession.getUserImDao().getDatabase();
        DaoMaster.dropAllTables(database, true);
        DaoMaster.createAllTables(database, true);
    }

    // 改
    private void modify(UserImDao userDao) {
        List<UserIm> userIms; //所有的user
        userIms = userDao.queryBuilder().where(UserImDao.Properties.Url.eq("http://www.xxx.com")).list();
        if (userIms.size() > 0){
            userIms.get(0).setName("modify_name");
            userIms.get(0).setUserId("modify_userId");
            userDao.update(userIms.get(0));
        }
    }

    // 查
    private void query(UserImDao userDao) {
        List<UserIm> userIms; //所有的user
        userIms = userDao.queryBuilder().where(UserImDao.Properties.Url.eq("http://www.xxx.com")).list();
        for (UserIm userIm : userIms) {
            Log.i(TAG, "getId:" + userIm.getId() + ", getUserId:" + userIm.getUserId() +
                    ", getName:" + userIm.getName() + ", getUrl:" + userIm.getUrl());
        }
    }

    //通过此方法,进行增删改查
    public static DaoSession getDaoSession() {
        return daoSession;
    }

}