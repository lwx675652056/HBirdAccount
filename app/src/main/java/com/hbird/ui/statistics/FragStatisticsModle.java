package com.hbird.ui.statistics;

import android.app.Application;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.hbird.base.mvc.bean.ReturnBean.HappynessReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.ljy.devring.DevRing;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseViewModel;

public class FragStatisticsModle extends BaseViewModel {
    public FragStatisticsModle(@NonNull Application application) {
        super(application);
    }


    // 支出排行榜 某个类的具体条目
    public void getRanking(boolean isAll, String persionId, int dateType, String firstDay, String lastDay, int years, int weeks, String monthCurrent,
                           String thisTime, int orderType, TextView topMoney, OnRankingCallBack callBack) {
        String sql = "";//日支出收入排行榜
        if (dateType == 1) {
            if (isAll) {
                sql = "select sum(money) money, type_name as type_name , icon as icon"
                        + " from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo"
                        + " where wo.delflag = 0"
                        + " AND wo.order_type = " + orderType
                        + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "'"
                        + " GROUP BY wo.type_name"
                        + " order by money desc;";
            } else {
                sql = "select sum(money) money, type_name as type_name , icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo  where wo.delflag = 0 AND wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "' GROUP BY wo.type_name order by money desc;";
            }
        } else if (dateType == 2) {
            String weekTime = years + "-" + (weeks < 10 ? "0" + weeks : weeks);
            if (isAll) {
                sql = "select sum(money) money,type_name as type_name,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%W', charge_date2) = '" + weekTime + "' GROUP BY wo.type_name order by money DESC;";
            } else {
                try {
                    firstDay = DateUtil.dateToStamp1(firstDay);
                    lastDay = DateUtil.dateToStamp1(lastDay);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                sql = "SELECT  sum(money) money,icon,type_name from WATER_ORDER_COLLECT"
                        + " where"
                        + " DELFLAG = 0"
                        + " AND order_type = " + orderType
                        + " AND CREATE_BY = " + persionId
                        + " AND CHARGE_DATE >= " + firstDay
                        + " AND CHARGE_DATE < " + lastDay
//                        + " GROUP BY strftime('%Y-%W', datetime(charge_date/1000, 'unixepoch', 'localtime'))"
                        + " GROUP BY type_name"
                        + " order by money DESC;";

            }
        } else if (dateType == 3) {
            if (monthCurrent.length() == 1) {
                monthCurrent = "0" + monthCurrent;
            }
            String monthTime = years + "-" + monthCurrent;
            if (isAll) {
                sql = "select sum(money) money,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_name order by money DESC;";
            } else {
                sql = "select sum(money) money,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_name order by money DESC;";
            }
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> dbList = new ArrayList<>();

        if (cursor != null) {
            try {
                dbList = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<StatisticsSpendTopArraysBean> list = new ArrayList<>();
            for (int i = 0; i < dbList.size(); i++) {
                StatisticsSpendTopArraysBean bean = new StatisticsSpendTopArraysBean();
                bean.money = dbList.get(i).getMoney();
                bean.icon = dbList.get(i).getIcon();
                bean.typeName = dbList.get(i).getTypeName();
                list.add(bean);
            }
//            list = DBUtil.changeToListTJ(cursor, list, StatisticsSpendTopArraysBean.class);
            double maxMoney;
            if (TextUtils.isEmpty(topMoney.getText().toString())) {
                maxMoney = 0;
            } else {
                maxMoney = Double.parseDouble(topMoney.getText().toString());
            }

            callBack.result(list, maxMoney);
        } else {
            callBack.result(null, 0);
        }
    }

    // 消费心情
    public void getHappnessRanking(boolean isAll, String persionId, int dateType, int orderType, String thisTime, int years, int weeks, String monthCurrent, OnHappnessRankingCallBack callBack) {
        String spendHappnessSql = "";//日支出情绪消费排行榜
        if (dateType == 1) {
            if (isAll) {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND delflag = 0 AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "';";
            } else {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND delflag = 0 AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "';";
            }
        } else if (dateType == 2) {
            String weekTime = years + "-" + (weeks < 10 ? "0" + weeks : weeks);
            if (isAll) {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND delflag = 0 AND strftime('%Y-%W', charge_date2) = '" + weekTime + "';";
            } else {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND delflag = 0 AND strftime('%Y-%W', charge_date2) = '" + weekTime + "';";
            }
        } else if (dateType == 3) {
            if (monthCurrent.length() == 1) {
                monthCurrent = "0" + monthCurrent;
            }
            String monthTime = years + "-" + monthCurrent;
            if (isAll) {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + monthTime + "';";
            } else {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + monthTime + "';";
            }
        }

        Cursor cursors = DevRing.tableManager(WaterOrderCollect.class).rawQuery(spendHappnessSql, null);
        if (cursors != null) {
            List<HappynessReturn> ll = new ArrayList<>();
            ll = DBUtil.changeToListHappy(cursors, ll, HappynessReturn.class);
            List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> happinessArrays = new ArrayList<>();
            if (ll.get(0).getSpendHappinessCount() != 0) {
                if (!TextUtils.isEmpty(ll.get(0).getHappy() + "") && ll.get(0).getHappy() != 0) {
                    chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean be = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                    be.setSpendHappiness(0);
                    be.setCount(ll.get(0).getHappy());
                    happinessArrays.add(be);
                }
                if (!TextUtils.isEmpty(ll.get(0).getNormal() + "") && ll.get(0).getNormal() != 0) {
                    chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean be = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                    be.setSpendHappiness(1);
                    be.setCount(ll.get(0).getNormal());
                    happinessArrays.add(be);
                }
                if (!TextUtils.isEmpty(ll.get(0).getSad() + "") && ll.get(0).getSad() != 0) {
                    chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean be = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                    be.setSpendHappiness(2);
                    be.setCount(ll.get(0).getSad());
                    happinessArrays.add(be);
                }

            }
            if (happinessArrays.size() > 0) {
                int totalCount = ll.get(0).getSpendHappinessCount();
                callBack.result(happinessArrays, totalCount);
            } else {
                callBack.result(null, 0);
            }
        } else {
            callBack.result(null, 0);
        }
    }

    interface OnRankingCallBack {
        void result(List<StatisticsSpendTopArraysBean> list, double maxMoney);
    }

    interface OnHappnessRankingCallBack {
        void result(List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> list, int totalCount);
    }

    // 獲取月中每天的金額
    public void getDayData(String persionId, int yyyy, int month, boolean isAll, int orderType, onMonthCallBack callBack) {
        String SQL;
        if (isAll) {// 全部数据
            SQL = "select sum(money) as money,charge_date2 as day_time  from "
                    + "(select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT )"
                    + " where delflag = 0 AND"
                    + " ORDER_TYPE = " + orderType
                    + " AND strftime('%Y-%m', charge_date2 ) = '" + yyyy + "-" + (month < 10 ? "0" + month : month) + "'"
                    + " GROUP BY strftime( '%Y-%m-%d', charge_date2)  "
                    + " order by  charge_date2 desc;";
        } else {
            SQL = "select sum(money) as money,charge_date2 as day_time  from "
                    + "(select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT )"
                    + " where delflag = 0 AND"
                    + " ORDER_TYPE = " + orderType
                    + " AND strftime('%Y-%m', charge_date2 ) = '" + yyyy + "-" + (month < 10 ? "0" + month : month) + "'"
                    + " AND CREATE_BY = " + persionId
                    + " GROUP BY strftime( '%Y-%m-%d', charge_date2)  "
                    + " order by  charge_date2 desc;";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(SQL, null);
        if (cursor != null) {
            List<chartToBarReturn.ResultBean.ArraysBean> dbList = new ArrayList<>();
            dbList.clear();
            if (null != cursor) {
                dbList = DBUtil.changeToListDYY(cursor, dbList, chartToBarReturn.ResultBean.ArraysBean.class);
            }
            callBack.result(dbList);
        }
    }

    // 獲取一年中周的数据
    public void getWeekData(String persionId, int yyyy, boolean isAll, int orderType, onMonthCallBack callBack) {
        String SQL;
        if (isAll) {// 全部数据
            SQL = "select sum(money) money ,strftime('%W', charge_date2) week from"
                    + " (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1)"
                    + " where delflag = 0"
                    + " AND ORDER_TYPE = " + orderType
                    + " AND strftime('%Y', charge_date2) = '" + yyyy + "'"
                    + " GROUP BY strftime( '%Y-%W', charge_date2 )"
                    + " order by week DESC;";
        } else {
            SQL = "select sum(money) money ,strftime('%W', charge_date2) week from"
                    + " (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) "
                    + " where delflag = 0"
                    + " AND ORDER_TYPE = " + orderType
                    + " AND CREATE_BY = " + persionId
                    + " AND strftime('%Y', charge_date2) = '" + yyyy + "'"
                    + " GROUP BY strftime( '%Y-%W', charge_date2 )"
                    + " order by week DESC;";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(SQL, null);
        if (cursor != null) {
            List<chartToBarReturn.ResultBean.ArraysBean> dbList = new ArrayList<>();
            dbList.clear();
            if (null != cursor) {
                dbList = DBUtil.changeToListDYY(cursor, dbList, chartToBarReturn.ResultBean.ArraysBean.class);
            }
            callBack.result(dbList);
        }
    }

    // 獲取一年中月的数据
    public void getMonthData(String persionId, int yyyy, boolean isAll, int orderType, onMonthCallBack callBack) {
        String SQL;

        if (isAll) {// 全部数据
            SQL = "select sum(money) money ,strftime('%m', charge_date2) month from"
                    + " (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1)"
                    + " where delflag = 0"
                    + " AND ORDER_TYPE= " + orderType
                    + " AND strftime('%Y', charge_date2) = '" + yyyy + "'"
                    + " GROUP BY strftime( '%Y-%m', charge_date2 )"
                    + " order by month ASC;";
        } else {
            SQL = "select sum(money) money ,strftime('%m', charge_date2) month from"
                    + " (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1) "
                    + " where delflag = 0"
                    + " AND ORDER_TYPE = " + orderType
                    + " AND CREATE_BY = " + persionId
                    + " AND strftime('%Y', charge_date2) = '" + yyyy + "'"
                    + " GROUP BY strftime( '%Y-%m', charge_date2 ) "
                    + " order by month ASC;";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(SQL, null);
        if (cursor != null) {
            List<chartToBarReturn.ResultBean.ArraysBean> dbList = new ArrayList<>();
            dbList.clear();
            if (null != cursor) {
                dbList = DBUtil.changeToListDYY(cursor, dbList, chartToBarReturn.ResultBean.ArraysBean.class);
            }
            callBack.result(dbList);
        }
    }

    interface onMonthCallBack {
        void result(List<chartToBarReturn.ResultBean.ArraysBean> dbList);
    }
}
