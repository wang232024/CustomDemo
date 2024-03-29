package com.custom.savedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.custom.savedb.greendaobean.DaoMaster;
import com.custom.savedb.greendaobean.DaoSession;
import com.custom.savedb.greendaobean.UserImDao;
import com.example.common.KLog;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class GreenDaoActivity extends AppCompatActivity {
    private static final String TAG = "wtx_GreenDaoActivity";

    private DaoMaster daoMaster;
    private static DaoSession daoSession;
    private DaoMaster.DevOpenHelper devOpenHelper = null;
    private UserImDao mUserDao;
    private TextView tv_database_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_database);

        Button btn_database_insert = findViewById(R.id.btn_database_insert);
        Button btn_database_delete = findViewById(R.id.btn_database_delete);
        Button btn_database_update = findViewById(R.id.btn_database_update);
        Button btn_database_query = findViewById(R.id.btn_database_query);
        tv_database_content = findViewById(R.id.tv_database_content);

        btn_database_insert.setOnClickListener(mOnClickListener);
        btn_database_delete.setOnClickListener(mOnClickListener);
        btn_database_update.setOnClickListener(mOnClickListener);
        btn_database_query.setOnClickListener(mOnClickListener);

        KLog.i(TAG, "getPackageName:" + getPackageName());

        devOpenHelper = new DaoMaster.DevOpenHelper(
                getApplicationContext(),
                UserIm.GreenDao_db,
                null
        );
        //实例化DaoMaster对象
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        //实例化DaoSession对象
        daoSession = daoMaster.newSession();

        mUserDao = daoSession.getUserImDao();
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            long time = System.currentTimeMillis();
            if (R.id.btn_database_insert == viewId) {
                UserIm userIm = new UserIm(null, "id_" + time, "GreenDao", "http://www.green.dao/" + time);
                mUserDao.insert(userIm);
            } else if (R.id.btn_database_delete == viewId) {
                mUserDao.deleteByKey(7L);   // 根据主键进行删除
            } else if (R.id.btn_database_update == viewId) {
                List<UserIm> userIms;
                userIms = mUserDao.queryBuilder().where(UserImDao.Properties.Name.isNotNull()).list();
                if (userIms.size() > 0){
                    userIms.get(0).setUserId("modify_userId");
                    mUserDao.update(userIms.get(0));
                }
            } else if (R.id.btn_database_query == viewId) {
                List<UserIm> userIms; //所有的user
                userIms = mUserDao.queryBuilder().where(UserImDao.Properties.Name.isNotNull()).list();
                for (UserIm userIm : userIms) {
                    KLog.i(TAG, "userIm:" + userIm.toString());
                }
            }
            List<UserIm> userIms; //所有的user
            userIms = mUserDao.queryBuilder().where(UserImDao.Properties.Name.isNotNull()).list();
            StringBuilder stringBuilder = new StringBuilder();
            for (UserIm userIm : userIms) {
                KLog.i(TAG, "userIm:" + userIm.toString());
                stringBuilder.append(userIm.toString());
                stringBuilder.append("\n");
            }
            tv_database_content.setText("");
            tv_database_content.setText(stringBuilder.toString());
        }
    };

    private void deleteAll() {
        //清空所有数据
        DaoSession daoSession = getDaoSession();
        Database database = daoSession.getUserImDao().getDatabase();
        DaoMaster.dropAllTables(database, true);
        DaoMaster.createAllTables(database, true);
    }

    //通过此方法,进行增删改查
    public static DaoSession getDaoSession() {
        return daoSession;
    }

}