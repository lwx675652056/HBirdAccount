package com.hbird.base.mvp.model.db.greendao;

import com.hbird.base.mvp.model.entity.table.HbirdIncomeType;
import com.hbird.base.mvp.model.entity.table.HbirdSpendType;
import com.ljy.devring.db.GreenTableManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Liul on 2018/8/31.
 * description: 系统支出类目表 数据表管理者 for GreenDao
 * DevRing中提供了GreenTableManager(基本的数据表管理者)，继承它然后实现getDao方法，将GreenDao自动生成的对应XXXDao返回即可。
 */

public class HbirdSpendTypeManager extends GreenTableManager<HbirdSpendType,Void> {
    private DaoSession mDaoSession;

    public HbirdSpendTypeManager(DaoSession daoSession) {
        mDaoSession = daoSession;
    }
    @Override
    public AbstractDao<HbirdSpendType, Void> getDao() {
        return mDaoSession.getHbirdSpendTypeDao();
    }
}
