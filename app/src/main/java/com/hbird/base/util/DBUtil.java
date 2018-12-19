package com.hbird.base.util;

import android.database.Cursor;
import android.text.TextUtils;

import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.CreatAccountReturn;
import com.hbird.base.mvc.bean.ReturnBean.HappynessReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturn;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.bean.ReturnBean.SystemParamsReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvp.model.entity.table.HbirdIncomeType;
import com.hbird.base.mvp.model.entity.table.HbirdSpendType;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseIncome;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseSpend;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.ljy.devring.DevRing;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import sing.util.LogUtil;


/**
 * Created by Liul on 2018/9/4.
 * 数据库 相关操作方法
 */

public class DBUtil {

    //删除，并非直接从本地数据库删除 而是更新数据库中id所对应数据的状态（插入或替换其中的一条数据）
    public static Boolean deleteOneDate(String id){
        WaterOrderCollect waterOrderCollect = new WaterOrderCollect();
        waterOrderCollect.setDelflag(1);
        waterOrderCollect.setId(id);
        String s = System.currentTimeMillis() / 1000 + "000";
        long l = Long.parseLong(s);
        waterOrderCollect.setUpdateDate(new Date(l));
        boolean b = DevRing.tableManager(WaterOrderCollect.class).insertOrReplaceOne(waterOrderCollect);
        return b;
    }
    //删除，并非直接从本地数据库删除 而是更新数据库中id所对应数据的状态（插入或替换其中的一条数据）
    public static Boolean deleteOnesDate(String id,String accountId){
        WaterOrderCollect waterOrderCollect = new WaterOrderCollect();
        waterOrderCollect.setDelflag(1);
        waterOrderCollect.setId(id);
        waterOrderCollect.setAccountBookId(Integer.parseInt(accountId));
        String s = System.currentTimeMillis() / 1000 + "000";
        long l = Long.parseLong(s);
        waterOrderCollect.setUpdateDate(new Date(l));
        boolean b = DevRing.tableManager(WaterOrderCollect.class).insertOrReplaceOne(waterOrderCollect);
        return b;
    }
    //删除，并非直接从本地数据库删除 而是更新数据库中id所对应数据的状态
    public static boolean updateOneDate(String id,String accountId){
        WaterOrderCollect waterOrderCollect = new WaterOrderCollect();
        waterOrderCollect.setDelflag(1);
        waterOrderCollect.setId(id);
        waterOrderCollect.setAccountBookId(Integer.parseInt(accountId));
        String s = System.currentTimeMillis() / 1000 + "000";
        long l = Long.parseLong(s);
        waterOrderCollect.setUpdateDate(new Date(l));
        boolean b = DevRing.tableManager(WaterOrderCollect.class).updateOne(waterOrderCollect);
        return b;
    }
    //插入本地数据库(首页明细相关数据)
    public static void insertLocalDB(List<PullSyncDateReturn.ResultBean.SynDataBean> synData){
        if(null==synData){
            return;
        }
        for(int i=0;i<synData.size();i++){
            //现根据主键 删除表中该条数据 再重新插入
            PullSyncDateReturn.ResultBean.SynDataBean be = synData.get(i);

            WaterOrderCollect w = new WaterOrderCollect();
            w.setId(be.getId());
            w.setRemark(be.getRemark());
            w.setCreateName(be.getCreateName());
            w.setUpdateBy(be.getUpdateBy());
            w.setUpdateName(be.getUpdateName());
            w.setCreateBy(be.getCreateBy());
            w.setIcon(be.getIcon());
            w.setAccountBookId(be.getAccountBookId());
            w.setChargeDate(new Date(be.getChargeDate()));
            w.setCreateDate(new Date(be.getCreateDate()));
            w.setDelDate(new Date(be.getDelDate()));
            w.setDelflag(be.getDelflag());
            w.setIsStaged(be.getIsStaged());
            w.setMoney(be.getMoney());
            w.setOrderType(be.getOrderType());
            w.setParentId(be.getParentId());
            w.setPictureUrl(be.getPictureUrl());
            if(null==be.getSpendHappiness() || TextUtils.isEmpty(be.getSpendHappiness()+"")){
                w.setSpendHappiness(-1);
            }else {
                w.setSpendHappiness(be.getSpendHappiness());
            }
            w.setTypeId(be.getTypeId());
            w.setTypeName(be.getTypeName());
            w.setTypePid(be.getTypePid());
            w.setTypePname(be.getTypePname());
            w.setDelDate(new Date(be.getDelDate()));
            w.setUseDegree(be.getUseDegree());
            w.setUserPrivateLabelId(be.getUserPrivateLabelId());
            w.setReporterNickName(be.getReporterNickName());
            w.setReporterAvatar(be.getReporterAvatar());
            w.setAbName(be.getAbName());
            boolean b = DevRing.tableManager(WaterOrderCollect.class).insertOrReplaceOne(w);
        }
    }
    //插入本地数据库(系统收入类目) -- 对应数据表为：HbirdIncomeType
    public static void insertSysIcomeTypeToLocalDB(List<SystemParamsReturn.ResultBean.AllSysIncomeTypeBean.AllSysIncomeTypeArraysBean> data){
        for(int i=0;i<data.size();i++){
            SystemParamsReturn.ResultBean.AllSysIncomeTypeBean.AllSysIncomeTypeArraysBean be = data.get(i);
            HbirdIncomeType w = new HbirdIncomeType();
            w.setId(be.getId());
            w.setDelflag(be.getDelflag());
            w.setCreateDate(new Date(be.getCreateDate()));
            w.setDelDate(new Date(be.getDelDate()));
            w.setIcon(be.getIcon());
            w.setIncomeName(be.getIncomeName());
            w.setMark(be.getMark());
            w.setParentId(be.getParentId());
            w.setPriority(be.getPriority());
            w.setStatus(be.getStatus());
            w.setUpdateDate(new Date(be.getUpdateDate()));
            DevRing.tableManager(HbirdIncomeType.class).insertOne(w);
        }
    }
    //插入本地数据库(系统支出类目) -- 对应数据表为：HbirdSpendType
    public static void insertSysSpendTypeToLocalDB(List<SystemParamsReturn.ResultBean.AllSysSpendTypeBean.AllSysSpendTypeArraysBean>  data){
        for(int i=0;i<data.size();i++){
            SystemParamsReturn.ResultBean.AllSysSpendTypeBean.AllSysSpendTypeArraysBean be = data.get(i);
            HbirdSpendType w = new HbirdSpendType();
            w.setId(be.getId());
            w.setDelflag(be.getDelflag());
            w.setCreateDate(new Date(be.getCreateDate()));
            w.setDelDate(new Date(be.getDelDate()));
            w.setIcon(be.getIcon());
            w.setSpendName(be.getSpendName());
            w.setMark(be.getMark());
            w.setParentId(be.getParentId());
            w.setPriority(be.getPriority());
            w.setStatus(be.getStatus());
            w.setUpdateDate(new Date(be.getUpdateDate()));
            DevRing.tableManager(HbirdSpendType.class).insertOne(w);
        }
    }

    //插入本地数据库(用户常用支出类目) -- 对应数据表为：HbirdUserCommUseSpend
    public static void insertAllUserCommUseSpendToLocalDB(List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean>  data,Integer abTypeId,int userInfoId){
        if(null == data){
            return;
        }
        for(int i=0;i<data.size();i++){
            SystemBiaoqReturn.ResultBean.LabelBean.SpendBean b = data.get(i);
            HbirdUserCommUseSpend w = new HbirdUserCommUseSpend();
            w.setId(b.getId()+"");
            w.setPriority(b.getPriority());
            w.setIcon(b.getIcon());
            w.setSpendName(b.getSpendName());
            w.setAbTypeId(abTypeId);
            w.setUserInfoId(userInfoId);
            DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(w);
        }
    }
    //插入本地数据库(根据不同的账本 插入不同的标签)
    public static void insertSpendToLocalDB(List<CreatAccountReturn.ResultBean.SpendBean> data, String abTypeId, String userInfoId){
        if(null == data){
            return;
        }
        for(int i=0;i<data.size();i++){
            CreatAccountReturn.ResultBean.SpendBean b = data.get(i);
            HbirdUserCommUseSpend w = new HbirdUserCommUseSpend();
            w.setId(b.getId()+"");
            w.setPriority(b.getPriority());
            w.setIcon(b.getIcon());
            w.setSpendName(b.getSpendName());
            w.setAbTypeId(Integer.valueOf(abTypeId));
            w.setUserInfoId(Integer.valueOf(userInfoId));
            DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(w);
        }
    }
    //插入本地数据库(根据不同的账本 插入不同的标签)
    public static void insertIncomeToLocalDB(List<CreatAccountReturn.ResultBean.IncomeBean> data,String id,String userInfoId){
        for(int i=0;i<data.size();i++){
            CreatAccountReturn.ResultBean.IncomeBean b = data.get(i);
            HbirdUserCommUseIncome w = new HbirdUserCommUseIncome();
            w.setId(b.getId()+"");
            w.setPriority(b.getPriority());
            w.setIcon(b.getIcon());
            w.setIncomeName(b.getIncomeName());
            w.setAbTypeId(Integer.valueOf(id));
            w.setUserInfoId(Integer.valueOf(userInfoId));
            DevRing.tableManager(HbirdUserCommUseIncome.class).insertOne(w);
        }
    }
    //插入本地数据库(用户常用收入类目) -- 对应数据表为：HbirdUserCommUseIncome
    public static void insertAllUserCommUseIncomeToLocalDB(List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> data,Integer id,int userInfoId){
        if (data == null)return;
        for(int i=0;i<data.size();i++){
            SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean b = data.get(i);
            HbirdUserCommUseIncome w = new HbirdUserCommUseIncome();
            w.setId(b.getId()+"");
            w.setPriority(b.getPriority());
            w.setIcon(b.getIcon());
            w.setIncomeName(b.getIncomeName());
            w.setAbTypeId(id);
            w.setUserInfoId(userInfoId);
            DevRing.tableManager(HbirdUserCommUseIncome.class).insertOne(w);
        }
    }
    //插入本地数据库(用户常用类目优先级排序关系) -- 对应数据表为：HbirdUserCommTypePriority
   /* public static void insertTypePriorityToLocalDB(List<SystemParamsReturn.ResultBean.AllUserCommUseTypePriorityBean.AllUserCommUseTypePriorityArraysBean>  data){
        for(int i=0;i<data.size();i++){
            SystemParamsReturn.ResultBean.AllUserCommUseTypePriorityBean.AllUserCommUseTypePriorityArraysBean b = data.get(i);
            HbirdUserCommTypePriority w = new HbirdUserCommTypePriority();
            w.setId(b.getId());
            w.setUserInfoId(b.getUserInfoId());
            w.setCreateDate(new Date(b.getCreateDate()));
            w.setRelation(b.getRelation());
            w.setType(b.getType());
            w.setUpdateDate(new Date(b.getUpdateDate()));
            DevRing.tableManager(HbirdUserCommTypePriority.class).insertOne(w);
        }
    }*/

    //从数据库中查出 组合为集合的形式返回
    public static List<WaterOrderCollect> changeToList(Cursor cursor, List<WaterOrderCollect> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        WaterOrderCollect module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModule(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }

    public static WaterOrderCollect changeToModule(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            WaterOrderCollect module = null;
            try {
                module = (WaterOrderCollect) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            if(filedValue.contains("GMT")){
                                date = DateUtil.dateTode(filedValue);
                            }else {
                                date = DateUtil.dateToStamp(filedValue);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }

            }
            return module;
        }
    }

    //从数据库中查出 组合为集合的形式返回
    public static List<OffLineReq.SynDataBean> changeToListPull(Cursor cursor, List<OffLineReq.SynDataBean> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        OffLineReq.SynDataBean module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModulePull(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }
    public static OffLineReq.SynDataBean changeToModulePull(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            OffLineReq.SynDataBean module = null;
            try {
                module = (OffLineReq.SynDataBean) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else if(TextUtils.equals("USE_DEGREE",cursor.getColumnName(j))){
                        String filedValue = cursor.getString(j);
                        if(!TextUtils.isEmpty(filedValue)){
                            field.set(module, filedValue);
                        }
                    }else if(TextUtils.equals("UPDATE_BY",cursor.getColumnName(j))){
                        String filedValue = cursor.getString(j);
                        if(!TextUtils.isEmpty(filedValue)){
                            if(!TextUtils.equals(filedValue,"0")){
                                field.set(module, Integer.parseInt(filedValue));
                            }
                        }
                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            //date = DateUtil.dateToStamp(filedValue);
                            if(TextUtils.equals("CREATE_DATE",cursor.getColumnName(j))){
                                field.set(module, new Date(Long.parseLong(filedValue)));
                            }else {
                                if(!TextUtils.equals(filedValue,"0")){
                                    Date date1 = new Date(Long.parseLong(filedValue));
                                    field.set(module, date1);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //field.set(module, date);
                    }
                }else if (TextUtils.equals(s, "long")) {
                    String filedValue = cursor.getString(j);
                    long l = Long.parseLong(filedValue);
                    if (filedValue != null) {
                        field.set(module, new Date(l));
                    }
                }else if(TextUtils.equals(s,"int")){
                    if("ACCOUNT_BOOK_ID".equals(columnNames[j])){
                        String filedValue = cursor.getString(j);
                        int i = Integer.parseInt(filedValue);
                        field.set(module,i);
                    }else if("IS_STAGED".equals(columnNames[j])){
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }

    //从数据库中查出 组合为集合的形式返回(日月年的统计)
    public static List<chartToBarReturn.ResultBean.ArraysBean> changeToListDYY(Cursor cursor, List<chartToBarReturn.ResultBean.ArraysBean> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        chartToBarReturn.ResultBean.ArraysBean module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModuleDYY(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }
    public static chartToBarReturn.ResultBean.ArraysBean changeToModuleDYY(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            chartToBarReturn.ResultBean.ArraysBean module = null;
            try {
                module = (chartToBarReturn.ResultBean.ArraysBean) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            date = DateUtil.dateToStamp(filedValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }else if(TextUtils.equals(s,"double")){
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }




    public static  <T> T get(Class<T> clz,Object o){
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }
    //从数据库中查出 组合为集合的形式返回(日月年的统计)
    public static List<chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean> changeToListTJ(Cursor cursor, List<chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModuleTJ(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }
    public static chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean changeToModuleTJ(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean module = null;
            try {
                module = (chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            date = DateUtil.dateToStamp(filedValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }else if(TextUtils.equals(s,"double")){
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }

    //从数据库中查出 组合为集合的形式返回(日月年记账心情的统计)
    public static List<HappynessReturn> changeToListHappy(Cursor cursor, List<HappynessReturn> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        HappynessReturn module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModuleHappy(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }
    public static HappynessReturn changeToModuleHappy(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            HappynessReturn module = null;
            try {
                module = (HappynessReturn) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            date = DateUtil.dateToStamp(filedValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }else if(TextUtils.equals(s,"double")){
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }

    //从数据库中查出 组合为集合的形式返回(日月年记账心情的统计)
    public static List<ZhiChuTagReturn.ResultBean.CommonListBean> changeToListTypes(Cursor cursor, List<ZhiChuTagReturn.ResultBean.CommonListBean> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        ZhiChuTagReturn.ResultBean.CommonListBean module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModuleTypes(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }

    //从数据库中查出 组合为集合的形式返回(日月年记账心情的统计)
    public static List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> aaa(Cursor cursor, List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        SystemBiaoqReturn.ResultBean.LabelBean.SpendBean module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = bbbb(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (Exception e) {
               LogUtil.e("数据库异常");
            } finally {
                cursor.close();
            }
            return null;
        }
    }

    public static ZhiChuTagReturn.ResultBean.CommonListBean changeToModuleTypes(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            ZhiChuTagReturn.ResultBean.CommonListBean module = null;
            try {
                module = (ZhiChuTagReturn.ResultBean.CommonListBean) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            date = DateUtil.dateToStamp(filedValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }else if(TextUtils.equals(s,"double")){
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }


    public static SystemBiaoqReturn.ResultBean.LabelBean.SpendBean bbbb(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException, InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            LogUtil.e(columnNames.toString());
            int columncount = columnNames.length;
            Field field;
            SystemBiaoqReturn.ResultBean.LabelBean.SpendBean module = null;
            try {
                module = (SystemBiaoqReturn.ResultBean.LabelBean.SpendBean) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            date = DateUtil.dateToStamp(filedValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }else if(TextUtils.equals(s,"double")){
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }

    //从数据库中查出 组合为集合的形式返回(日月年记账心情的统计)
    public static List<ShouRuTagReturn.ResultBean.CommonListBean> changeToListTyp(Cursor cursor, List<ShouRuTagReturn.ResultBean.CommonListBean> modules, Class<?> moduleClass) {
        // 取出所有的列名
        int count = cursor.getCount();
        ShouRuTagReturn.ResultBean.CommonListBean module;
        cursor.moveToFirst();
        synchronized (DBUtil.class) {
            try {
                // 遍历游标
                for (int i = 0; i < count; i++) {
                    // 转化为moduleClass类的一个实例
                    module = changeToModuleTyp(cursor, moduleClass);
                    modules.add(module);
                    cursor.moveToNext();
                }
                return modules;
            } catch (SecurityException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalArgumentException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (IllegalAccessException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (InstantiationException e) {
                // Log.e(TAG, e + FusionCode.EMPTY_STRING);
            } catch (NoSuchFieldException e) {
                System.out.println("");
            } finally {
                cursor.close();
            }
            return null;
        }
    }
    public static ShouRuTagReturn.ResultBean.CommonListBean changeToModuleTyp(Cursor cursor, Class<?> moduleClass) throws IllegalAccessException,
            InstantiationException, SecurityException, NoSuchFieldException {
        synchronized (DBUtil.class) {
            // 取出所有的列名
            String[] columnNames = cursor.getColumnNames();
            int columncount = columnNames.length;
            Field field;
            ShouRuTagReturn.ResultBean.CommonListBean module = null;
            try {
                module = (ShouRuTagReturn.ResultBean.CommonListBean) moduleClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            // 遍历有列 -1的原因是 多表联查时新增了一个字段 且这个地方查出来的数据没有此字段类型与之对应
            for (int j = 0; j < columncount; j++) {
                // 根据列名找到相对应 的字段

                field = moduleClass.getField(UnderLine2Name.camelName(columnNames[j]));
                String s = field.getType().toString();
                if (TextUtils.equals(s, "class java.lang.Double")) {
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.String")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                } else if (TextUtils.equals(s, "class java.lang.Integer")) {

                    if(TextUtils.equals(cursor.getColumnName(j),"SPEND_HAPPINESS") ){
                        String s2 = cursor.getString(j);
                        if(s2!=null && s2!=""){
                            field.set(module, Integer.parseInt(s2));
                        }else {
                            field.set(module, -1);
                        }

                    }else {
                        int filedValue = cursor.getInt(j);
                        field.set(module, filedValue);
                    }

                } else if (TextUtils.equals(s, "class java.util.Date")) {
                    String filedValue = cursor.getString(j);
                    if (filedValue != null) {
                        Date date = null;
                        try {
                            date = DateUtil.dateToStamp(filedValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        field.set(module, date);
                    }
                }else if(TextUtils.equals(s,"double")){
                    Double filedValue = cursor.getDouble(j);
                    if (filedValue != null) {
                        field.set(module, filedValue);
                    }
                }

            }
            return module;
        }
    }

}
