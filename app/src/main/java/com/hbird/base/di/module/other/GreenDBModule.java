package com.hbird.base.di.module.other;

import android.app.Application;

import com.hbird.base.di.qualifier.GreenDB;
import com.hbird.base.di.scope.DBScope;
import com.hbird.base.mvp.model.db.greendao.DaoMaster;
import com.hbird.base.mvp.model.db.greendao.DaoSession;
import com.hbird.base.mvp.model.db.greendao.HbirdIncomeTypeDao;
import com.hbird.base.mvp.model.db.greendao.HbirdIncomeTypeManager;
import com.hbird.base.mvp.model.db.greendao.HbirdSpendTypeDao;
import com.hbird.base.mvp.model.db.greendao.HbirdSpendTypeManager;
import com.hbird.base.mvp.model.db.greendao.HbirdUserCommTypePriorityManager;
import com.hbird.base.mvp.model.db.greendao.HbirdUserCommUseIncomManager;
import com.hbird.base.mvp.model.db.greendao.HbirdUserCommUseIncomeDao;
import com.hbird.base.mvp.model.db.greendao.HbirdUserCommUseSpendDao;
import com.hbird.base.mvp.model.db.greendao.HbirdUserCommUseSpendManager;
import com.hbird.base.mvp.model.db.greendao.MovieCollectDao;
import com.hbird.base.mvp.model.db.greendao.MovieGreenTableManager;
import com.hbird.base.mvp.model.db.greendao.WaterOrderCollectDao;
import com.hbird.base.mvp.model.db.greendao.WaterOrderTableManager;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommTypePriority;
import com.ljy.devring.db.support.GreenOpenHelper;

import org.greenrobot.greendao.AbstractDao;

import dagger.Module;
import dagger.Provides;

/**
 * author:  admin
 * date:    2018/3/10
 * description: 对GreenDBManager中的相关变量进行初始化
 */
@Module
public class GreenDBModule {

    @DBScope
    @Provides
    DaoSession daoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    @DBScope
    @Provides
    DaoMaster daoMaster(GreenOpenHelper greenOpenHelper) {
        //这里使用DevRing提供的GreenOpenHelper对DaoMaster进行初始化，这样就可以实现数据库升级时的数据迁移
        //默认的DaoMaster.OpenHelper不具备数据迁移功能，它会在数据库升级时将数据删除。
        return new DaoMaster(greenOpenHelper.getWritableDatabase());
//        return new DaoMaster(greenOpenHelper.getEncryptedWritableDb("your_secret"));
    }

    @DBScope
    @Provides
    GreenOpenHelper greenOpenHelper(Application context, @GreenDB String dbName, @GreenDB Integer schemaVersion, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        return new GreenOpenHelper(context, dbName, schemaVersion, daoClasses);
    }

    @GreenDB
    @DBScope
    @Provides
    String dbName() {
        return "greendao1.db";
    }

    @GreenDB
    @DBScope
    @Provides
    Integer schemaVersion() {
        //返回数据库版本号，使用DaoMaster.SCHEMA_VERSION即可
        return DaoMaster.SCHEMA_VERSION;
    }

    @DBScope
    @Provides
    Class<? extends AbstractDao<?, ?>>[] daoClasses() {
        //传入各数据表对应的Dao类
        return new Class[]{MovieCollectDao.class, WaterOrderCollectDao.class, HbirdIncomeTypeDao.class
                , HbirdSpendTypeDao.class, HbirdUserCommUseIncomeDao.class, HbirdUserCommUseSpendDao.class
                , HbirdUserCommTypePriority.class};
    }

    @DBScope
    @Provides
    MovieGreenTableManager movieTableManager(DaoSession daoSession) {
        return new MovieGreenTableManager(daoSession);
    }

    @DBScope
    @Provides
    WaterOrderTableManager waterOrderTableManager(DaoSession daoSession) {
        return new WaterOrderTableManager(daoSession);
    }

    @DBScope
    @Provides
    HbirdIncomeTypeManager hbirdIncomeTypeManager(DaoSession daoSession) {
        return new HbirdIncomeTypeManager(daoSession);
    }

    @DBScope
    @Provides
    HbirdSpendTypeManager hbirdSpendTypeManager(DaoSession daoSession) {
        return new HbirdSpendTypeManager(daoSession);
    }

    @DBScope
    @Provides
    HbirdUserCommUseIncomManager hbirdUserCommUseIncomManager(DaoSession daoSession) {
        return new HbirdUserCommUseIncomManager(daoSession);
    }

    @DBScope
    @Provides
    HbirdUserCommUseSpendManager hbirdUserCommUseSpendManager(DaoSession daoSession) {
        return new HbirdUserCommUseSpendManager(daoSession);
    }

    @DBScope
    @Provides
    HbirdUserCommTypePriorityManager hbirdUserCommTypePriorityManager(DaoSession daoSession) {
        return new HbirdUserCommTypePriorityManager(daoSession);
    }
}
