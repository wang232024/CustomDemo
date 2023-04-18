package com.custom.savedb.greendaobean;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.custom.savedb.UserIm;

import com.custom.savedb.greendaobean.UserImDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userImDaoConfig;

    private final UserImDao userImDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userImDaoConfig = daoConfigMap.get(UserImDao.class).clone();
        userImDaoConfig.initIdentityScope(type);

        userImDao = new UserImDao(userImDaoConfig, this);

        registerDao(UserIm.class, userImDao);
    }
    
    public void clear() {
        userImDaoConfig.clearIdentityScope();
    }

    public UserImDao getUserImDao() {
        return userImDao;
    }

}
