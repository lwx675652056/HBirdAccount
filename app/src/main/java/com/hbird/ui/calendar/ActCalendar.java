package com.hbird.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.databinding.ActCalendarBinding;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.TitleBean;
import com.hbird.common.calendar.Utils;
import com.hbird.common.calendar.component.CalendarAttr;
import com.hbird.common.calendar.component.CalendarViewAdapter;
import com.hbird.common.calendar.interf.OnSelectDateListener;
import com.hbird.common.calendar.model.CalendarDate;
import com.hbird.common.calendar.view.Calendar;
import com.hbird.common.calendar.view.MonthPager;
import com.ljy.devring.DevRing;

import java.math.BigDecimal;
import java.text.ParseException;
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

import sing.common.base.BaseActivity;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActCalendar
 * @date: 2018/12/26 11:33
 * @Description: 首页 日历
 */
public class ActCalendar extends BaseActivity<ActCalendarBinding, CalendarModle> {

    MonthPager monthPager;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private Context context;
    private boolean initiated = false;

    private CalendarData data;
    private CalendarDate currentDate;// 已选日期
    private CalendarAdapter adapter;
    private List<AccountDetailedBean> list = new ArrayList<>();
    private String accountId;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_calendar;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    public class OnClick {
        // 上个月
        public void left(View view) {
            monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
        }

        // 下个月
        public void right(View view) {
            monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
        }

        // 今天
        public void today(View view) {
            refreshMonthPager(new CalendarDate());
        }
    }

    @Override
    public void initData() {
        binding.setTitle(new TitleBean("日历"));
        binding.toolbar.ivBack.setOnClickListener(v -> finish());
        data = new CalendarData();

        currentDate = new CalendarDate();
        data.setYyyy(currentDate.getYear());
        data.setMm(currentDate.getMonth());

        binding.setBean(data);
        binding.setListener(new OnClick());

        accountId = getIntent().getExtras().getString("account_id", "");

        context = this;
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewHeight(Utils.dpi2px(context, 270));

        binding.recyclerView.setHasFixedSize(true);
        //这里用线性显示 类似于listview
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalendarAdapter(this, list, R.layout.row_calendar, (position, data, type) -> onItemClick((AccountDetailedBean) data,type));
        binding.recyclerView.setAdapter(adapter);

        initCalendarView();
    }

    // 点击记账的条目
    private void onItemClick(AccountDetailedBean data, int type) {
        if (type == 0) {
            com.hbird.util.Utils.playVoice(this, R.raw.changgui02);
            Intent intent = new Intent(this, MingXiInfoActivity.class);
            intent.putExtra("ID", data.getId());
            startActivityForResult(intent, 101);
        } else if (type == 1) {
            com.hbird.util.Utils.playVoice(this, R.raw.changgui02);
            alertDialog(data);
        }
    }

    private void alertDialog(final AccountDetailedBean data) {
        new DialogUtils(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("确认删除吗？")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", view -> {
                    String id = data.getId();
                    //数据库的操作 （删除显示的是 数据库的更新）
                    Boolean b = DBUtil.updateOneDate(id, accountId);
                    if (b) {
                        //刷新界面数据
                        getData(currentDate);
                        //删除 并同步上传到服务器
//                        pullToSyncDate();
                    }
                }).show();
    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager(new CalendarDate());
            initiated = true;
        }
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.WeekArrayType.Monday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(type -> binding.recyclerView.scrollToPosition(0));
        initMonthPager();
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshMonthPager(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    data.setYyyy(date.getYear());
                    data.setMm(date.getMonth());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void refreshMonthPager(CalendarDate day) {
        calendarAdapter.notifyDataChanged(day);
        data.setYyyy(day.getYear());
        data.setMm(day.getMonth());
        currentDate = day;

        getData(day);
    }

    // 获取数据
    private void getData(CalendarDate day) {
        String MonthFirstDay = null;
        String MonthLastDay = null;
        try {
            MonthFirstDay = DateUtil.dateToStamp1(day.getYear()+"-"+day.getMonth()+"-"+day.getDay());
            MonthLastDay = DateUtil.dateToStamp1(day.getYear()+"-"+day.getMonth()+"-"+(day.getDay()+1));
        } catch (ParseException e) {
            e.printStackTrace();
            ToastUtil.showShort("时间转化异常");
            return;
        }
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";
        String sql = "";
        if (TextUtils.isEmpty(accountId)) {
            Set<String> set = new LinkedHashSet<>();
            Set<String> prefSet = SPUtil.getPrefSet(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
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
                            ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                            ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " where  ACCOUNT_BOOK_ID in " + "(" + substring + ")" + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
                }
            }
        } else {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
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

        if (null != dbList && dbList.size() > 0) {
            Collections.sort(dbList);
            Map<String, Object> dbDate = getDBDate(dbList);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);

            if (bean != null) {
                ArrayList<indexBaseListBean> dates = getDBDates(bean);
                if (dates != null && dates.size() > 0) {
                   list.clear();
                    for (int i = 0; i < dates.size(); i++) {
                        AccountDetailedBean temp = new AccountDetailedBean();
                        temp.setBean(dates.get(i));
                        list.add(temp);
                    }
//                    Collections.sort(list);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.ivNoData.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.ivNoData.setVisibility(View.VISIBLE);
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.ivNoData.setVisibility(View.VISIBLE);
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private Map<String, Object> getDBDate(List<WaterOrderCollect> list) {
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
                Map<String, BigDecimal> account = getAccount(Integer.parseInt(accountId));
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            } else {
                Map<String, BigDecimal> account = getAccounts();
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
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, "");
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
                been.add(indexBeans2);
            }
        }
        return been;
    }


    public Map<String, BigDecimal> getAccount(int accountBookId) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
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

    public Map<String, BigDecimal> getAccounts() {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Set<String> sets = new LinkedHashSet<>();
        Set<String> prefSet = SPUtil.getPrefSet(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, sets);
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