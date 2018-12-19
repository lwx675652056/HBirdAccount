package com.hbird.base.mvp.model.db.greendao;

import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.ljy.devring.db.GreenTableManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Liul on 2018/8/30.
 * description: 支出收入流水 数据表管理者 for GreenDao
 * DevRing中提供了GreenTableManager(基本的数据表管理者)，继承它然后实现getDao方法，将GreenDao自动生成的对应XXXDao返回即可。
 */

public class WaterOrderTableManager extends GreenTableManager<WaterOrderCollect,String> {
    private DaoSession mDaoSession;

    public WaterOrderTableManager (DaoSession daoSession) {
        mDaoSession = daoSession;
    }
    @Override
    public AbstractDao<WaterOrderCollect, String> getDao() {
        return mDaoSession.getWaterOrderCollectDao();
    }
}
