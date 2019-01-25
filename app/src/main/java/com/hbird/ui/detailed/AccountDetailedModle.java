package com.hbird.ui.detailed;

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
import com.hbird.bean.AccountDetailedBean;
import com.ljy.devring.DevRing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sing.common.base.BaseViewModel;

/**
 * @author: LiangYX
 * @ClassName: AccountDetailedModle
 * @date: 2018/12/20 14:53
 * @Description: 首页的更多 账本明细
 */
public class AccountDetailedModle extends BaseViewModel{

    public AccountDetailedModle(@NonNull Application application) {
        super(application);
    }

    public void getData(int  yyyy,int mm,String accountId,Set<String> prefSet,CallBack callBack){
        //查询指定月份记录
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";
        String sql = "";

        if (TextUtils.isEmpty(accountId)) {
            Set<String> set = new LinkedHashSet<>();

            if (prefSet != null) {
                String ids = "";
                Iterator<String> iterator = prefSet.iterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    ids += s + ",";
                }
                if (!TextUtils.isEmpty(ids)) {
                    String substring = ids.substring(0, ids.length() - 1);
                    sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                            ", type_pid, type_pname, type_id, type_name, picture_url,update_by, create_date, charge_date" +
                            ", remark, USER_PRIVATE_LABEL_ID,ASSETS_NAME, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " where  ACCOUNT_BOOK_ID in " + "(" + substring + ")" + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
                }
            }
        } else {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, update_by,create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, ASSETS_NAME,REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                    " where  ACCOUNT_BOOK_ID=" + accountId + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
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

        String monthIncomes = "0.00";
        String monthSpends = "0.00";
        List<AccountDetailedBean> list = new ArrayList<>();

        if (null != dbList && dbList.size() > 0) {
            Collections.sort(dbList);

            Map<String, Object> dbDate = getDBDate(dbList,accountId,prefSet,yyyy,mm);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);

            if (bean != null) {
                ArrayList<indexBaseListBean> dates = getDBDates(bean);
                if (dates != null && dates.size() > 0) {
                    double monthIncome = bean.getMonthIncome();
                    double monthSpend = bean.getMonthSpend();
                     monthIncomes = getNumToNumber(monthIncome);
                     monthSpends = getNumToNumber(monthSpend);

                    for (int i = 0; i < dates.size(); i++) {
                        AccountDetailedBean temp = new AccountDetailedBean();
                        temp.setBean(dates.get(i));
                        list.add(temp);
                    }
                }
            }
        }
        callBack.result(list,monthIncomes,monthSpends);
    }

    public interface CallBack{
        void result(List<AccountDetailedBean> list,String monthIncomes,String monthSpends);
    }

    private String getNumToNumber(Double d) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(d);
    }

    private Map<String, Object> getDBDate(List<WaterOrderCollect> list,String accountId,Set<String> prefSet,int yyyy,int mm) {
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
                Map<String, BigDecimal> account = getAccount(Integer.parseInt(accountId),yyyy,mm);
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            } else {
                Map<String, BigDecimal> account = getAccounts(yyyy,mm,prefSet);
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            }

        }
        return ja;
    }


    public Map<String, BigDecimal> getAccount(int accountBookId,int yyyy,int mm) {
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

    public Map<String, BigDecimal> getAccounts(int yyyy,int mm,Set<String> prefSet) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Set<String> sets = new LinkedHashSet<>();
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
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, 0,"");
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
                indexBeans2.setUpdateBy(dates.getUpdateBy());
                been.add(indexBeans2);
            }
        }
        return been;
    }
}
