package com.hbird.ui.statistics_details;

import android.app.Application;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.ljy.devring.DevRing;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sing.common.base.BaseViewModel;

public class DetailModle extends BaseViewModel {
    public DetailModle(@NonNull Application application) {
        super(application);
    }

    // 获取类别排行
    public void getRankingData(String firstDay, String lastDay, boolean isAll, int orderType, String persionId,String typeName, onRankingCallBack callBack) {
        List<indexBaseListBean> list = new ArrayList<>();
        String moneyIncome = "";
        String moneySpend = "";
        try {
            firstDay = DateUtil.dateToStamp1(firstDay);
            lastDay = DateUtil.dateToStamp1(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sql;
        if (isAll) {
            sql = "SELECT * FROM WATER_ORDER_COLLECT"
                    + " where"
                    + " DELFLAG = 0"
                    + " AND type_name = '" + typeName
                    + "' AND order_type = " + orderType
                    + " AND CHARGE_DATE >= " + firstDay
                    + " AND CHARGE_DATE < " + lastDay
                    + " ORDER BY CHARGE_DATE DESC, CREATE_DATE DESC";
        } else {
            sql = "SELECT * FROM WATER_ORDER_COLLECT"
                    + " where"
                    + " DELFLAG = 0"
                    + " AND type_name = '" + typeName
                    + "' AND order_type = " + orderType
                    + " AND CREATE_BY = " + persionId
                    + " AND CHARGE_DATE >= " + firstDay
                    + " AND CHARGE_DATE < " + lastDay
                    + " ORDER BY CHARGE_DATE DESC, CREATE_DATE DESC";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> dbList = new ArrayList<>();
        dbList.clear();
        if (null != cursor) {
            try {
                dbList = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != dbList && dbList.size() > 0) {
            Collections.sort(dbList);
            Map<String, Object> dbDate = getDBDate(dbList, firstDay, lastDay);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);


            if (bean != null) {
                moneyIncome = String.valueOf(bean.getMonthIncome());
                moneySpend = String.valueOf(bean.getMonthSpend());

                list = getDBDates(bean);
            }
        }

        callBack.result(list, moneyIncome, moneySpend);
    }


    // 获取心情消费排行
    public void getPieChatData(String firstDay, String lastDay, boolean isAll, int orderType, String persionId,String typeName, onRankingCallBack callBack) {
        int spendHappiness = -1;
        if (typeName.equals("花的多")){
            spendHappiness = 2;
        }else if (typeName.equals("花的少")){
            spendHappiness = 0;
        }else if (typeName.equals("差不多")){
            spendHappiness = 1;
        }
        List<indexBaseListBean> list = new ArrayList<>();
        String moneyIncome = "";
        String moneySpend = "";
        try {
            firstDay = DateUtil.dateToStamp1(firstDay);
            lastDay = DateUtil.dateToStamp1(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sql;
        if (isAll) {
            sql = "SELECT * FROM WATER_ORDER_COLLECT"
                    + " where"
                    + " DELFLAG = 0"
                    + " AND SPEND_HAPPINESS = " + spendHappiness
                    + " AND order_type = " + orderType
                    + " AND CHARGE_DATE >= " + firstDay
                    + " AND CHARGE_DATE < " + lastDay
                    + " ORDER BY CHARGE_DATE DESC, CREATE_DATE DESC";
        } else {
            sql = "SELECT * FROM WATER_ORDER_COLLECT"
                    + " where"
                    + " DELFLAG = 0"
                    + " AND SPEND_HAPPINESS = " + spendHappiness
                    + " AND order_type = " + orderType
                    + " AND CREATE_BY = " + persionId
                    + " AND CHARGE_DATE >= " + firstDay
                    + " AND CHARGE_DATE < " + lastDay
                    + " ORDER BY CHARGE_DATE DESC, CREATE_DATE DESC";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> dbList = new ArrayList<>();
        dbList.clear();
        if (null != cursor) {
            try {
                dbList = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != dbList && dbList.size() > 0) {
            Collections.sort(dbList);
            Map<String, Object> dbDate = getDBDate(dbList, firstDay, lastDay);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);


            if (bean != null) {
                moneyIncome = String.valueOf(bean.getMonthIncome());
                moneySpend = String.valueOf(bean.getMonthSpend());

                list = getDBDates(bean);
            }
        }

        callBack.result(list, moneyIncome, moneySpend);
    }

    private Map<String, Object> getDBDate(List<WaterOrderCollect> list, String firstDay, String lastDay) {
        //获取到当月所有记录
        Map<Date, Object> map = new LinkedHashMap<>();
        for (Iterator<WaterOrderCollect> it = list.iterator(); it.hasNext(); ) {
            WaterOrderCollect warter = it.next();
            //判断是否包含日期
            Date chargeDate = warter.getChargeDate();
            Date day = DateUtil.getDay(chargeDate);
            if (map.containsKey(day)) {
                ((ArrayList) map.get(day)).add(warter);
            } else {
                List<WaterOrderCollect> lists = new ArrayList<>();
                lists.add(warter);
                map.put(day, lists);
            }
        }
        Map<String, Object> ja = new HashMap();
        if (map.size() > 0) {
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            for (Map.Entry<Date, Object> entry : map.entrySet()) {
                //封装成key value格式
                JSONObject obj = new JSONObject();
                if (!TextUtils.equals(entry.getKey() + "", "dayTime")) {
                    obj.put("dayTime", entry.getKey());
                }

                if (!TextUtils.equals(entry.getKey() + "", "dayTime")) {
                    obj.put("dayArrays", entry.getValue());
                }
                array.add(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
            }
            //获取日统计
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = JSON.parseObject((String) array.get(i));
                if (!TextUtils.isEmpty(jsonObject.getString("dayArrays"))) {
                    BigDecimal dayIncome = new BigDecimal(0);
                    BigDecimal daySpend = new BigDecimal(0);
                    List lists = JSONArray.parseArray(jsonObject.getString("dayArrays"));
                    for (int j = 0; j < lists.size(); j++) {
                        //支出
                        WaterOrderCollect warter = JSONObject.parseObject(JSONObject.toJSONString(lists.get(j)), WaterOrderCollect.class);
                        if (warter.getOrderType() == 1) {
                            daySpend = daySpend.add(new BigDecimal(warter.getMoney()));
                        }
                        if (warter.getOrderType() == 2) {
                            dayIncome = dayIncome.add(new BigDecimal(warter.getMoney()));
                        }
                    }
                    jsonObject.put("dayIncome", dayIncome);
                    jsonObject.put("daySpend", daySpend);
                    array2.add(jsonObject);
                    //jsonObject转json
                }
            }

            //获取月份统计数据
            Map<String, BigDecimal> account = getAccounts(firstDay, lastDay);
            ja.put("arrays", array2);
            ja.put("monthSpend", account.get("spend"));
            ja.put("monthIncome", account.get("income"));
            return ja;
        }
        return ja;
    }

    // 获取指定天之内的收入、支出金额
    public Map<String, BigDecimal> getAccounts(String firstDay, String lastDay) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();

        String sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                " WHERE charge_date >= '" + firstDay + "' and charge_date <= '" + lastDay + "'" +
                " AND delflag = 0;";
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }

    private ArrayList<indexBaseListBean> getDBDates(dayListBean.ResultBean bean) {
        List<dayListBean.ResultBean.ArraysBean> arrays = bean.getArrays();
        if (arrays == null) return null;
        //数据处理 抽取到同一个集合indexBeans中去
        ArrayList<indexBaseListBean> been = new ArrayList<>();
        for (int i = 0; i < arrays.size(); i++) {
            List<dayListBean.ResultBean.ArraysBean.DayArraysBean> dayArrays = arrays.get(i).getDayArrays();
            indexBaseListBean indexBeans = new indexBaseListBean();
            if (dayArrays != null && dayArrays.size() > 0) {
                ArrayList<indexBaseListBean.indexBean> iBeen = new ArrayList<>();
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, 0, "");
                indexBaseListBean.indexBean xBean = new indexBaseListBean.indexBean();
                xBean.setDayIncome(arrays.get(i).getDayIncome());
                xBean.setDaySpend(arrays.get(i).getDaySpend());
                xBean.setDayTime(arrays.get(i).getDayTime());
                iBeen.add(xBean);
                indexBeans.setIndexBeen(iBeen);
                been.add(indexBeans);
            }
            for (int j = 0; j < dayArrays.size(); j++) {
                indexBaseListBean indexBeans2 = new indexBaseListBean();
                dayListBean.ResultBean.ArraysBean.DayArraysBean dates = dayArrays.get(j);
                indexBeans2.setTag(1);//1为数据条目 （自定义标签）
                indexBeans2.setOrderType(dates.getOrderType());
                indexBeans2.setIcon(dates.getIcon());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setIsStaged(dates.getIsStaged());
                indexBeans2.setRemark(dates.getRemark());
                if (null != dates.getSpendHappiness()) {
                    indexBeans2.setSpendHappiness(dates.getSpendHappiness());
                }
                indexBeans2.setMoney(dates.getMoney());
                indexBeans2.setTypePid(dates.getTypePid());
                indexBeans2.setTypeId(dates.getTypeId());
                indexBeans2.setId(dates.getId());
                indexBeans2.setAccountBookId(dates.getAccountBookId());
                indexBeans2.setChargeDate(dates.getChargeDate());
                indexBeans2.setCreateDate(dates.getCreateDate());
                indexBeans2.setUpdateDate(dates.getUpdateDate());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setReporterAvatar(dates.getReporterAvatar());
                indexBeans2.setReporterNickName(dates.getReporterNickName());
                indexBeans2.setUpdateBy(dates.getUpdateBy());
                been.add(indexBeans2);
            }
        }
        return been;
    }

    interface onRankingCallBack {
        void result(List<indexBaseListBean> dbList, String moneyIncome, String moneySpend);
    }
}
