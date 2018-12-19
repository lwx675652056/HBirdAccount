package com.hbird.base.mvp.model.db.greendao;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class BaseDao<T>{
    public static final String TAG = BaseDao.class.getSimpleName();
    public static final boolean DUBUG = true;
    public DbManager manager;
    public DaoSession daoSession;

    public BaseDao(Context context) {
        manager = DbManager.getInstance().init(context);
        daoSession = manager.getDaoSession();
        manager.setDebug(DUBUG);
    }

    /**************************数据库插入操作***********************/
    // 插入单个对象
    public boolean insert(T object){
        boolean flag = false;
        try {
            flag = manager.getDaoSession().insert(object) != -1 ? true:false;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return flag;
    }

    // 插入多个对象，并开启新的线程
    public boolean insert(final List<T> objects){
        boolean flag = false;
        if (null == objects || objects.isEmpty()){
            return false;
        }
        try {
            manager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        manager.getDaoSession().insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }finally {
//            manager.CloseDataBase();
        }
        return flag;
    }

    /**************************数据库更新操作***********************/
    // 以对象形式进行数据修改,其中必须要知道对象的主键ID
    public void  update(T object){
        if (null == object){
            return ;
        }
        try {
            manager.getDaoSession().update(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    // 批量更新数据
    public void update(final List<T> objects, Class clss){
        if (null == objects || objects.isEmpty()){
            return;
        }
        try {
            daoSession.getDao(clss).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for(T object:objects){
                        daoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**************************数据库删除操作***********************/
    // 删除某个数据库表
    public boolean deleteTable(Class clss){
        boolean flag = false;
        try {
            manager.getDaoSession().deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    // 删除某个对象
    public void delete(T object){
        try {
            daoSession.delete(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    // 异步批量删除数据
    public boolean delete(final List<T> objects, Class clss){
        boolean flag = false;
        if (null == objects || objects.isEmpty()){
            return false;
        }
        try {
            daoSession.getDao(clss).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for(T object:objects){
                        daoSession.delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    // 根据主键ID来删除
    public void deleteById(long id,Class clss){
        daoSession.getDao(clss).deleteByKey(id);
    }

    /**************************数据库查询操作***********************/

    // 获得某个表名
    public String getTableName(Class object){
        return daoSession.getDao(object).getTablename();
    }

    // 查询某个ID的对象是否存在
//    public boolean isExitObject(long id, Class object){
//        QueryBuilder<T> qb = (QueryBuilder<T>) daoSession.getDao(object).queryBuilder();
//        qb.where(UserDao.Properties.Id.eq(id));// 这里需要继承类来实现，这是例子
//        long length = qb.buildCount().count();
//        return length>0 ? true:false;
//    }

    // 根据主键ID来查询
    public T queryById(long id,Class object){
        return (T) daoSession.getDao(object).loadByRowId(id);
    }

    // 查询某条件下的对象
    public List<T> queryObject(Class object,String where,String...params){
        Object obj = null;
        List<T> objects = null;
        try {
            obj = daoSession.getDao(object);
            if (null == obj){
                return null;
            }
            objects = daoSession.getDao(object).queryRaw(where,params);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return objects;
    }
    // 查询所有对象
    public List<T> queryAll(Class object){
        List<T> objects = null;
        try {
            objects = (List<T>) daoSession.getDao(object).loadAll();
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        return objects;
    }

    /***************************关闭数据库*************************/
    // 关闭数据库一般在Odestory中使用
    public void closeDataBase(){
        manager.closeDataBase();
    }
}