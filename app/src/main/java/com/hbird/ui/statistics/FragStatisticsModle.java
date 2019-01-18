package com.hbird.ui.statistics;

import android.app.Application;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.hbird.base.mvc.bean.ReturnBean.HappynessReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.ljy.devring.DevRing;

import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseViewModel;

public class FragStatisticsModle extends BaseViewModel {
    public FragStatisticsModle(@NonNull Application application) {
        super(application);
    }


    //支出排行榜 某个类的具体条目前5
    public void getRanking(boolean isAll, String persionId, int dateType, int years, int weeks, String monthCurrent, String accountId,
                           String thisTime, int orderType, TextView topMoney, OnRankingCallBack callBack) {
        String sql = "";//日支出收入排行榜
        if (dateType == 1) {
//            sql = "select sum(money) money, type_name as type_name , icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo  where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "' GROUP BY wo.type_id order by money desc LIMIT 5;";
            if (isAll) {
                sql = "select sum(money) money, type_name as type_name , icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo  where wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "' GROUP BY wo.type_id order by money desc;";
            } else {
                sql = "select sum(money) money, type_name as type_name , icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo  where wo.delflag = 0 AND wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "' GROUP BY wo.type_id order by money desc;";
            }
        } else if (dateType == 2) {
            String weekTime = years + "-" + weeks;
//            sql = "select sum(money) money,type_name as type_name,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%W', charge_date2) = '" + weekTime + "' GROUP BY wo.type_id order by money DESC LIMIT 5;";
            if (isAll) {
                sql = "select sum(money) money,type_name as type_name,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%W', charge_date2) = '" + weekTime + "' GROUP BY wo.type_id order by money DESC;";
            } else {
                sql = "select sum(money) money,type_name as type_name,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND strftime('%Y-%W', charge_date2) = '" + weekTime + "' GROUP BY wo.type_id order by money DESC;";
            }
        } else if (dateType == 3) {
            if (monthCurrent.length() == 1) {
                monthCurrent = "0" + monthCurrent;
            }
            String monthTime = years + "-" + monthCurrent;
//            sql = "select sum(money) money,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_id order by money DESC LIMIT 5;";
            if (isAll) {
                sql = "select sum(money) money,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_id order by money DESC;";
            } else {
                sql = "select sum(money) money,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.delflag = 0 AND wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_id order by money DESC;";
            }
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            List<StatisticsSpendTopArraysBean> list = new ArrayList<>();
            list = DBUtil.changeToListTJ(cursor, list, StatisticsSpendTopArraysBean.class);
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
    public void getHappnessRanking(boolean isAll, String persionId, int dateType, int orderType, String accountId, String thisTime, int years, int weeks, String monthCurrent, OnHappnessRankingCallBack callBack) {
        String spendHappnessSql = "";//日支出情绪消费排行榜
        if (dateType == 1) {
//            spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "';";
            if (isAll) {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND delflag = 0 AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "';";
            } else {
                spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND CREATE_BY= " + persionId + " AND delflag = 0 AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "';";
            }
        } else if (dateType == 2) {
            String weekTime = years + "-" + weeks;
//            spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND ACCOUNT_BOOK_ID=" + accountId + " AND delflag = 0 AND strftime('%Y-%W', charge_date2) = '" + weekTime + "';";
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
//            spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND ACCOUNT_BOOK_ID=" + accountId + " AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + monthTime + "';";
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
}
