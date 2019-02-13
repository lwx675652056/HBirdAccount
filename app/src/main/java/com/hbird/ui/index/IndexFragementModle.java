package com.hbird.ui.index;

import android.app.Application;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;

public class IndexFragementModle extends BaseViewModel {

    // 查询某月的总支出
//    sql = "select sum(money) money from WATER_ORDER_COLLECT"
//            + " where account_book_id= '" + accountId
//                    + "' AND delflag = 0 "
//                            + "AND order_type = 1 "
//                            + " AND CHARGE_DATE >= 1548950400000"
//                            + " AND CHARGE_DATE< 1551369600000;";
    public IndexFragementModle(@NonNull Application application) {
        super(application);
    }

    //月支出排行榜 前四
    public List<StatisticsSpendTopArraysBean> getRankingBar(int yyyy, int mm, String accountId) {
        List<StatisticsSpendTopArraysBean> list = new ArrayList<>();

        int orderType = 1;//1:支出  2:收入
        String monthTime = mm < 10 ? yyyy + "-0" + mm : yyyy + "-" + mm;
        String sql;
        if (TextUtils.isEmpty(accountId)) {// 总账本
            sql = "select sum(money) money,type_id,USER_PRIVATE_LABEL_ID,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_name order by money DESC LIMIT 5;";
        } else {
            sql = "select sum(money) money,type_id,USER_PRIVATE_LABEL_ID,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_name order by money DESC LIMIT 5;";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            list.clear();
            DBUtil.changeToListTJ(cursor, list, StatisticsSpendTopArraysBean.class);
        }
        return list;
    }

    //月支出排行榜 某个类的具体条目前5
    public List<WaterOrderCollect> getTypeRanking(String typeName, int yyyy, int mm, String accountId) {
        List<WaterOrderCollect> list = new ArrayList<>();

        int orderType = 1;//1:支出  2:收入

        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";

        String sql;
        if (TextUtils.isEmpty(accountId)) {// 总账本
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, ASSETS_NAME,  REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                    " where"
                    + " DELFLAG = 0 "
                    + " AND CHARGE_DATE >=" + MonthFirstDay
                    + " AND CHARGE_DATE<" + MonthLastDays
                    + " AND type_name = '" + typeName
                    + "' AND order_type = " + orderType
                    + " ORDER BY money DESC LIMIT 5";
        } else {
            sql = "SELECT * FROM WATER_ORDER_COLLECT " +
                    " where"
                    + " ACCOUNT_BOOK_ID=" + accountId
                    + " AND  DELFLAG = 0 "
                    + " AND CHARGE_DATE >=" + MonthFirstDay
                    + " AND CHARGE_DATE<" + MonthLastDays
                    + " AND type_name = '" + typeName
                    + "' AND order_type = " + orderType
                    + " ORDER BY  money DESC LIMIT 5";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            list.clear();
            DBUtil.changeToList(cursor, list, WaterOrderCollect.class);
        }
        return list;
    }

    // 获取“其它”的排行前五
    public List<WaterOrderCollect> getOthereRanking(String typeName1, String typeName2, String typeName3, String typeName4, int yyyy, int mm, String accountId) {
        List<WaterOrderCollect> list = new ArrayList<>();

        int orderType = 1;//1:支出  2:收入

        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";

        String sql;
        if (TextUtils.isEmpty(accountId)) {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, ASSETS_NAME,  REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                    " WHERE"
                    + " DELFLAG = 0 "
                    + " AND CHARGE_DATE >=" + MonthFirstDay
                    + " AND CHARGE_DATE<" + MonthLastDays
                    + " AND type_name != '" + typeName1
                    + "' AND type_name != '" + typeName2
                    + "' AND type_name != '" + typeName3
                    + "' AND type_name != '" + typeName4
                    + "' AND order_type = " + orderType
                    + " ORDER BY  money DESC LIMIT 5";
        } else {
            sql = "SELECT * FROM WATER_ORDER_COLLECT " +
                    " WHERE ACCOUNT_BOOK_ID=" + accountId
                    + " AND  DELFLAG = 0 "
                    + " AND CHARGE_DATE >=" + MonthFirstDay
                    + " AND CHARGE_DATE<" + MonthLastDays
                    + " AND type_name != '" + typeName1
                    + "' AND type_name != '" + typeName2
                    + "' AND type_name != '" + typeName3
                    + "' AND type_name != '" + typeName4
                    + "' AND order_type = " + orderType
                    + " ORDER BY  money DESC LIMIT 5";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            list.clear();
            DBUtil.changeToList(cursor, list, WaterOrderCollect.class);
        }
        return list;
    }

    // 获取月的总支出
    public void getApiBarDate(String persionId, int yyyy, int mm, String accountId, OnTotalCallBack callBack) {
        int orderType = 1;//1:支出  2:收入

        String yyyy_mm1 = mm == 1 ? ((yyyy - 1) + "-12") : (mm < 10 ? yyyy + "-0" + (mm - 1) : yyyy + "-" + (mm - 1));// 上个月 年月
        String yyyy_mm2 = mm < 10 ? yyyy + "-0" + mm : yyyy + "-" + mm;// 当前年月
        //查询 一年中12个月的统计
        String lastMonth = "select sum(money) money ,strftime('%m', charge_date2) month from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1) where delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND ORDER_TYPE= " + orderType + " AND CREATE_BY= " + persionId + " AND  strftime('%Y-%m', charge_date2) = '" + yyyy_mm1 + "' GROUP BY strftime( '%Y-%m', charge_date2) order by  month ASC;";
        String currentMonth = "select sum(money) money ,strftime('%m', charge_date2) month from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1) where delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND ORDER_TYPE= " + orderType + " AND CREATE_BY= " + persionId + " AND  strftime('%Y-%m', charge_date2) = '" + yyyy_mm2 + "' GROUP BY strftime( '%Y-%m', charge_date2) order by  month ASC;";

        Cursor cursor1 = DevRing.tableManager(WaterOrderCollect.class).rawQuery(lastMonth, null);
        Cursor cursor2 = DevRing.tableManager(WaterOrderCollect.class).rawQuery(currentMonth, null);
        double lastMonthStr = 0;
        double currentMonthStr = 0;

        if (cursor1 != null) {
            List<chartToBarReturn.ResultBean.ArraysBean> dbList1 = new ArrayList<>();
            DBUtil.changeToListDYY(cursor1, dbList1, chartToBarReturn.ResultBean.ArraysBean.class);
            if (null != dbList1 && dbList1.size() > 0) {
                lastMonthStr = dbList1.get(0).money;
            }
        }
        if (cursor2 != null) {
            List<chartToBarReturn.ResultBean.ArraysBean> dbList2 = new ArrayList<>();
            DBUtil.changeToListDYY(cursor2, dbList2, chartToBarReturn.ResultBean.ArraysBean.class);
            if (null != dbList2 && dbList2.size() > 0) {
                currentMonthStr = dbList2.get(0).money;
            }
        }
        callBack.result(lastMonthStr, currentMonthStr);
    }

    interface OnTotalCallBack {
        void result(double lastMonth, double currentMonth);
    }

    // 查询指定月份的记录
    public void getMonthData(int yyyy, int mm, String accountId, Set<String> prefSet, OnMonthCallBack callBack) {
        LogUtil.e("查询的年月为：" + yyyy + mm);
        ArrayList<indexBaseListBean> dates = new ArrayList<>();
        double monthSpends = 0;// 支出
        double monthIncomes = 0;// 收入

        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";
        String sql = "";
        if (TextUtils.isEmpty(accountId)) {
            if (prefSet != null) {
                String ids = "";
                Iterator<String> iterator = prefSet.iterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    ids += s + ",";
                }
                if (!TextUtils.isEmpty(ids)) {
                    String substring = ids.substring(0, ids.length() - 1);
                    sql = "SELECT * FROM WATER_ORDER_COLLECT "
                            + " where ACCOUNT_BOOK_ID in " + "(" + substring + ")"
                            + " AND DELFLAG = 0"
                            + " AND CHARGE_DATE >= " + MonthFirstDay
                            + " and CHARGE_DATE < " + MonthLastDays
                            + " ORDER BY CHARGE_DATE DESC, CREATE_DATE DESC LIMIT 3;";
                }
            }
        } else {
            sql = "SELECT * FROM WATER_ORDER_COLLECT"
                    + " where ACCOUNT_BOOK_ID = " + accountId
                    + " AND  DELFLAG = 0"
                    + " AND CHARGE_DATE >= " + MonthFirstDay
                    + " and CHARGE_DATE < " + MonthLastDays
                    + " ORDER BY CHARGE_DATE DESC, CREATE_DATE DESC LIMIT 3;";
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
            Map<String, Object> dbDate = getDBDate(dbList, accountId, yyyy, mm, prefSet);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);

            if (bean != null) {
                monthSpends = Utils.to2Digit(bean.getMonthSpend());
                monthIncomes = Utils.to2Digit(bean.getMonthIncome());
                dates = getDBDates(bean);
            }
        }
        callBack.result(monthSpends, monthIncomes, dates);
    }

    // 查询上月份的记录
    public void getLastMonthData(int yyyy, int mm, String accountId, Set<String> prefSet, OnMonthCallBack callBack) {
        if (mm == 1) {
            yyyy -= 1;
            mm = 12;
        } else {
            mm -= 1;
        }
        getMonthData(yyyy, mm, accountId, prefSet, callBack);
    }

    interface OnMonthCallBack {
        void result(double spend, double income, ArrayList<indexBaseListBean> list);
    }

    private Map<String, Object> getDBDate(List<WaterOrderCollect> list, String accountId, int yyyy, int mm, Set<String> prefSet) {
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
            //Map<Date, Object> resultMap = sortMapByKey(map);
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
            if (!TextUtils.isEmpty(accountId)) {
                Map<String, BigDecimal> account = getAccount(Integer.parseInt(accountId), yyyy, mm);
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            } else {
                Map<String, BigDecimal> account = getAccounts(yyyy, mm, prefSet);
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            }

        }
        return ja;
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
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setReporterAvatar(dates.getReporterAvatar());
                indexBeans2.setReporterNickName(dates.getReporterNickName());
                indexBeans2.setUserPrivateLabelId(dates.getUserPrivateLabelId());
                indexBeans2.setUpdateBy(dates.getUpdateBy());
                indexBeans2.setUpdateName(dates.getUpdateName());
                indexBeans2.setAbName(dates.getAbName());
                indexBeans2.setAssetsId(dates.getAssetsId());
                indexBeans2.setAssetsName(dates.getAssetsName());
                been.add(indexBeans2);
            }
        }
        return been;
    }

    public Map<String, BigDecimal> getAccount(int accountBookId, int yyyy, int mm) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                " AND account_book_id = '" + accountBookId + "' AND delflag = 0;";
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            int count = cursor.getCount();
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }

    public Map<String, BigDecimal> getAccounts(int yyyy, int mm, Set<String> prefSet) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String sql = "";
        Cursor cursor = null;
        if (prefSet != null) {
            String ids = "";
            Iterator<String> iterator = prefSet.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                ids += s + ",";
            }
            String substring = ids.substring(0, ids.length() - 1);
            sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                    "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                    " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                    " AND account_book_id in " + "(" + substring + ")" + " AND delflag = 0;";
            cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        }


        if (cursor != null) {
            int count = cursor.getCount();
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }
}
