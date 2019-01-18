package com.hbird.ui.statistics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.chart_3_0_1v.charts.LineChart;
import com.github.mikephil.chart_3_0_1v.charts.PieChart;
import com.github.mikephil.chart_3_0_1v.components.Legend;
import com.github.mikephil.chart_3_0_1v.components.XAxis;
import com.github.mikephil.chart_3_0_1v.components.YAxis;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.LineDataSet;
import com.github.mikephil.chart_3_0_1v.data.PieData;
import com.github.mikephil.chart_3_0_1v.data.PieDataSet;
import com.github.mikephil.chart_3_0_1v.data.PieEntry;
import com.github.mikephil.chart_3_0_1v.highlight.Highlight;
import com.github.mikephil.chart_3_0_1v.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.animation.Easing;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.FragStatisticsBinding;
import com.hbird.base.mvc.adapter.barChart2Adapter;
import com.hbird.base.mvc.adapter.barChartAdapter;
import com.hbird.base.mvc.adapter.barChartTotypeAdapter;
import com.hbird.base.mvc.bean.RealListEntity;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.mvc.widget.MyChart.LineChartEntity;
import com.hbird.base.mvc.widget.MyChart.NewMarkerView;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.hbird.util.Utils;
import com.hbird.widget.LineChartInViewPager;
import com.ljy.devring.DevRing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sing.common.base.BaseFragment;
import sing.common.util.LogUtil;
import sing.common.util.StringUtils;

import static com.hbird.base.util.Utils.getDateByWeeks;

/**
 * @author: LiangYX
 * @ClassName: FragStatistics
 * @date: 2019/1/17 13:33
 * @Description: 统计
 */
public class FragStatistics extends BaseFragment<FragStatisticsBinding, FragStatisticsModle> {

    TextView topMoney;

    private barChartAdapter mAdapter;
    private barChartTotypeAdapter mTypeAdapter;
    private barChart2Adapter dAdapter;
    private String token;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<YearAndMonthBean> mDayList;
    private YearAndMonthBean b;
    private ArrayList<YearAndMonthBean> dayList;
    private double maxMoney;
    private String beginWeek = "1";
    private String endWeek = "53";
    private String beginTime = "";
    private String endTime = "";
    private String thisTime = "";
    private ArrayList<YearAndMonthBean> weekList;
    private ArrayList<YearAndMonthBean> monthList;
    private int weeks;
    private String monthCurrent;
    private int years;
    private String accountId;
    private List<RealListEntity> realList = new ArrayList<>();//此数据集合是用来对比折线用的 暂未用到 为空即可（方便对比两年的数据）
    private List<YoyListEntity> yoyList;
    private DecimalFormat mFormat;
    private List<Entry> values1, values2;
    private RealListEntity realListEntity;
    private YoyListEntity yoyListEntity;
    private LineChartInViewPager lineChart;
    private LineChartInViewPager lineChart2;
    private LineChartInViewPager lineChart3;
    private LineChartInViewPager lineChartSr3;
    private LineChartInViewPager lineChartSr2;
    private LineChartInViewPager lineChartSr1;
    private int firsts2 = 0;
    private int firsts = 0;
    private int first1s = 0;
    private int first1s2 = 0;
    private boolean isClick = true;
    int aaa = 0;
    boolean aa = false;

    private StatisticsData data;

    private FragStatisticsAdapter adapter;
    private List<StatisticsSpendTopArraysBean> list = new ArrayList<>();// 实际上显示的数据
    private List<StatisticsSpendTopArraysBean> t = new ArrayList<>();// 全部数据

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.frag_statistics;
    }

    @Override
    public void initData() {
        data = new StatisticsData();
        binding.setData(data);
        binding.setListener(new OnClick());

        topMoney = binding.tvTopMoney;

        adapter = new FragStatisticsAdapter(getActivity(), list, R.layout.row_statistics, (position, data, type) -> LogUtil.e(""));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setNestedScrollingEnabled(false);

        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        //获取当前周

        weeks = Utils.getYearToWeek();
        //获取当前月
        monthCurrent = DateUtils.getMonthCurrent();
        //造数据 一年中的每一天
        years = DateUtils.getYears();
        beginTime = years + "-01-01";
        endTime = years + "-12-31";
        thisTime = DateUtils.getYearMonthDay();
        dayList = getYearDate(years);
        getWeekDate();//初始化周数
        //初始化月的数据
        getMonthDate();

        loadDataForNet();
    }

    public void loadDataForNet() {
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        thisTime = DateUtils.getYearMonthDay();

        getApiBarDate();//获取日周月 竖向柱状图的数据信息
        getRankingBar();//获取支出排行情绪消费统计
    }

    public class OnClick {
        public void changeType(boolean isInCome) {
            data.setInCome(isInCome);
            Utils.playVoice(getActivity(), R.raw.changgui01);
            if (isInCome) {// 收入
                thisTime = DateUtils.getYearMonthDay();
                weeks = Utils.getYearToWeek();
                monthCurrent = DateUtils.getMonthCurrent();
                loadDataForNet();
            } else { // 支出
                thisTime = DateUtils.getYearMonthDay();
                weeks = Utils.getYearToWeek();
                monthCurrent = DateUtils.getMonthCurrent();
                loadDataForNet();
            }
        }

        // 切换了日期类型 1 2 3 , 日 周 年
        public void changeDateType(int pos) {
            data.setDateType(pos);
            Utils.playVoice(getActivity(), R.raw.changgui01);
            if (pos == 1) {// 日
                thisTime = DateUtils.getYearMonthDay();
            } else if (pos == 2) {// 周
                weeks = Utils.getYearToWeek();
            } else if (pos == 3) {// 月
                monthCurrent = DateUtils.getMonthCurrent();
            }
            loadDataForNet();
        }

        // 个人数据全部数据的切换
        public void isAll(View view) {
            data.setAll(!data.isAll());
            thisTime = DateUtils.getYearMonthDay();
            weeks = Utils.getYearToWeek();
            monthCurrent = DateUtils.getMonthCurrent();
            loadDataForNet();
        }

        // 展开更多、收起
        public void open(View view) {
            data.setOpen(!data.isOpen());

            list.clear();
            if (data.isOpen()) {
                list.addAll(t);
            } else {// 既然有按钮，一定大于5
                list.add(t.get(0));
                list.add(t.get(1));
                list.add(t.get(2));
                list.add(t.get(3));
                list.add(t.get(4));
            }

            adapter.setOpen(data.isOpen());
        }
    }

    private String getEnumToNumer(Double money) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(money);
    }

    // 获取日周月的支出排行榜和情绪消费统计
    private void getRankingBar() {
        String persionId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        viewModel.getRanking(data.isAll(), persionId, data.getDateType(), years, weeks, monthCurrent, accountId, thisTime, (data.isInCome() ? 2 : 1), topMoney, new FragStatisticsModle.OnRankingCallBack() {
            @Override
            public void result(List<StatisticsSpendTopArraysBean> temp, double maxMoney) {
                if (null != temp && temp.size() > 0) {
                    data.setNoDataRanking(false);
                    data.setShowOpen(temp.size() > 5);// 不显示展开按钮

                    t.clear();
                    t.addAll(temp);

                    list.clear();
                    if (temp.size() <= 5) {
                        list.addAll(temp);
                    } else {
                        list.add(temp.get(0));
                        list.add(temp.get(1));
                        list.add(temp.get(2));
                        list.add(temp.get(3));
                        list.add(temp.get(4));
                    }

                    adapter.setMoney(maxMoney);
                    binding.recyclerView.setFocusable(false);
                } else {
                    data.setNoDataRanking(true);
                    data.setShowOpen(false);// 不显示展开按钮
                }
            }
        });

        viewModel.getHappnessRanking(data.isAll(), persionId, data.getDateType(), (data.isInCome() ? 2 : 1), accountId, thisTime, years, weeks, monthCurrent, new FragStatisticsModle.OnHappnessRankingCallBack() {
            @Override
            public void result(List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> list, int totalCount) {
                pieList.clear();
                if (list != null && list.size() > 0) {
                    pieList.addAll(list);
                }
                setChart(totalCount);
            }
        });
    }


    private void getApiBarDate() {
        String SQL = "";
        String maxSQL = "";
        String persionId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        //flag 1 2 3代表 日、周、月
        if (data.getDateType() == 1) {// 日
            if (data.isAll()) {// 全部数据
                //查询全年 每日的统计数据
                SQL = "select sum(money) money,charge_date2 as day_time  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%j', charge_date2 )  order by  charge_date2 desc;";
                //查询一年中日的最大值
                maxSQL = "select max (moneyList.totalDay) as money from (select sum(money) as totalDay  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m-%d', charge_date2 ) ) as moneyList ;";
            } else {
                //查询全年 每日的统计数据
                SQL = "select sum(money) money,charge_date2 as day_time  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND CREATE_BY= " + persionId + " AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%j', charge_date2 )  order by  charge_date2 desc;";
                //查询一年中日的最大值
                maxSQL = "select max (moneyList.totalDay) as money from (select sum(money) as totalDay  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m-%d', charge_date2 ) ) as moneyList ;";
//                //查询全年 每日的统计数据
//                SQL = "select sum(money) money,charge_date2 as day_time  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND CREATE_BY= " + persionId + " AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%j', charge_date2 )  order by  charge_date2 desc;";
//                //查询一年中日的最大值
//                maxSQL = "select max (moneyList.totalDay) as money from (select sum(money) as totalDay  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND ACCOUNT_BOOK_ID=" + accountId + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m-%d', charge_date2 ) ) as moneyList ;";
            }
        } else if (data.getDateType() == 2) {// 周
            if (data.isAll()) {// 全部数据
                //查询 一年中54周的统计
                SQL = "select sum(money) money ,strftime('%W', charge_date2) week from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND CREATE_BY= " + persionId + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%W', charge_date2 )  order by  week DESC;";
                //查询一年中周的最大值
                maxSQL = "SELECT max( moneyList.totalWeek) AS money FROM ( SELECT sum(money) AS totalWeek FROM (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND order_type = " + (data.isInCome() ? 2 : 1) + " AND strftime( '%Y',charge_date2 ) LIKE strftime( '%Y', date('now') ) GROUP BY strftime( '%Y-% W ', charge_date2 ) ) AS moneyList;";
            } else {
                //查询 一年中54周的统计
                SQL = "select sum(money) money ,strftime('%W', charge_date2) week from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND CREATE_BY= " + persionId + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%W', charge_date2 )  order by  week DESC;";
                //查询一年中周的最大值
                maxSQL = "SELECT max( moneyList.totalWeek) AS money FROM ( SELECT sum(money) AS totalWeek FROM (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND order_type = " + (data.isInCome() ? 2 : 1) + " AND strftime( '%Y',charge_date2 ) LIKE strftime( '%Y', date('now') ) GROUP BY strftime( '%Y-% W ', charge_date2 ) ) AS moneyList;";
            }
        } else if (data.getDateType() == 3) {// 月
            if (data.isAll()) {// 全部数据
                //查询 一年中12个月的统计
                SQL = "select sum(money) money ,strftime('%m', charge_date2) month from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND  strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m', charge_date2 )  order by  month ASC;";
                //查询一年中月的最大值
                maxSQL = "SELECT max( moneyList.totalWeek) AS money FROM ( SELECT sum(money) AS totalWeek FROM (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT  where 1=1) where delflag = 0 AND order_type = " + (data.isInCome() ? 2 : 1) + " AND strftime( '%Y',charge_date2 ) LIKE strftime( '%Y', date('now') ) GROUP BY strftime( '%Y-%m', charge_date2 ) ) AS moneyList;";
            } else {
                //查询 一年中12个月的统计
                SQL = "select sum(money) money ,strftime('%m', charge_date2) month from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + (data.isInCome() ? 2 : 1) + " AND CREATE_BY= " + persionId + " AND  strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m', charge_date2 )  order by  month ASC;";
                //查询一年中月的最大值
                maxSQL = "SELECT max( moneyList.totalWeek) AS money FROM ( SELECT sum(money) AS totalWeek FROM (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT  where 1=1) where delflag = 0 AND order_type = " + (data.isInCome() ? 2 : 1) + " AND strftime( '%Y',charge_date2 ) LIKE strftime( '%Y', date('now') ) GROUP BY strftime( '%Y-%m', charge_date2 ) ) AS moneyList;";
            }
        }
        Cursor cursorMax = DevRing.tableManager(WaterOrderCollect.class).rawQuery(maxSQL, null);
        if (cursorMax != null) {
            cursorMax.moveToFirst();
            try {
                String max = cursorMax.getString(0);
                if (null == max || TextUtils.isEmpty(max)) {
                    maxMoney = 0;
                } else {
                    maxMoney = Double.parseDouble(max);//最大值
                }
            } catch (Exception e) {
            }
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(SQL, null);
        if (cursor != null) {
            List<chartToBarReturn.ResultBean.ArraysBean> dbList = new ArrayList<>();
            dbList.clear();
            if (null != cursor) {
                dbList = DBUtil.changeToListDYY(cursor, dbList, chartToBarReturn.ResultBean.ArraysBean.class);
            }
            if (null != dbList && dbList.size() > 0) {
                int size = dayList.size();
                //如果是月 周 此处不需要执行
                if (data.getDateType() == 1) {
                    //只有日数据刷新时 执行此操作
                    dayList.clear();
                    dayList = getYearDate(years);
                }
                setChartForks(dbList);
            }
        }
    }

    private void setChartForks(List<chartToBarReturn.ResultBean.ArraysBean> arrays) {
        if (data.getDateType() == 1) {
            //日
            for (int i = 0; i < arrays.size(); i++) {
                //long time = arrays.get(i).getTime();
                long time = DateUtils.dateToTimestamp(arrays.get(i).getDayTime());
                double money = arrays.get(i).getMoney();
                String day = DateUtils.getMonthDays(time);
                for (int j = 0; j < dayList.size(); j++) {
                    String s = dayList.get(j).getmDate();
                    if (TextUtils.equals(s, day)) {
                        dayList.get(j).setMoney(money);
                    }
                }
            }
            //柱状图列表 每一天的
            setChartViews(1, dayList);
        } else if (data.getDateType() == 2) {
            //周
            //对weekList重新初始化
            if (null != weekList) {
                weekList.clear();
            }
            getWeekDate();//初始化周数
            for (int i = 0; i < arrays.size(); i++) {
                String week = arrays.get(i).getWeek();
                double money = arrays.get(i).getMoney();
                for (int j = 0; j <= weekList.size(); j++) {
                    if (null != week && week.equals((j + 1) + "")) {
                        weekList.get(j).setMoney(money);
                    }
                }
            }
            //柱状图列表 每一周的
            setChartViews(2, weekList);
        } else {
            //月
            //对monthList初始化操作(重新初始化)
            if (null != monthList) {
                monthList.clear();
            }
            getMonthDate();
            for (int i = 0; i < arrays.size(); i++) {
                String month2M = arrays.get(i).getMonth();
                //获取当前月份
                int i1 = Integer.parseInt(month2M);
                double money = arrays.get(i).getMoney();
                for (int j = 0; j < monthList.size(); j++) {
                    if (i1 - 1 == j) {
                        monthList.get(j).setMoney(money);
                    }
                }
            }
            //柱状图列表 每一周的
            setChartViews(3, monthList);
        }
    }


    //设置每天的
    private void setChartViews(final int item, final ArrayList<YearAndMonthBean> list) {
        //判断指定日期在集合中是第几个条目
        int pos = 0;

        if (item == 1) {
            //日 ，每天的数据
            String currentTime = DateUtils.getMonthDays(new Date().getTime());
            for (int j = 0; j < list.size(); j++) {
                String time = list.get(j).getmDate();
                if (TextUtils.equals(time, currentTime)) {
                    pos = j;
                }
            }
        } else if (item == 2) {
            //周 每周的数据
            pos = Utils.getYearToWeek() - 1;
        } else {
            //月 获取当前月
            pos = DateUtils.getMonthToNum();
        }

        //折线图 曲线
        yoyList = new ArrayList<>();
        yoyList.clear();
        for (int i = 0; i < list.size(); i++) {
            YoyListEntity yoyListEntity = new YoyListEntity();
            double money = list.get(i).getMoney();
            String amount = String.valueOf(money);
            String month = list.get(i).getmDate();
            String year = DateUtils.getYears() + "";
            yoyListEntity.setAmount(amount);
            yoyListEntity.setMonth(month);
            yoyListEntity.setYear(year);
            yoyList.add(yoyListEntity);
        }
        initChart(pos, list);

        double money = list.get(pos).getMoney();
        String formats = getEnumToNumer(money);
        topMoney.setText(formats);
        binding.natvMoney.setNumberString("0", formats);
    }

    private void setDateToCharts(int position, ArrayList<YearAndMonthBean> list) {
        //选中对应条目 刷新界面

        String s = list.get(position).getmDate();
        double money = list.get(position).getMoney();
        String formats = getEnumToNumer(money);

        topMoney.setText(formats);
        binding.natvMoney.setNumberString("0", formats);
        if (data.getDateType() == 1) {

            int years = DateUtils.getYears();
            thisTime = years + "-" + s;
        } else if (data.getDateType() == 2) {
            weeks = position + 1;
        } else {
            monthCurrent = position + 1 + "";
        }

        getRankingBar();//获取支出排行情绪消费统计
    }

    //获取一年中所有的月份
    private void getMonthDate() {
        monthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            YearAndMonthBean bean = new YearAndMonthBean();
            bean.setmDate(i + "月份");
            bean.setMoney(0);
            monthList.add(bean);
        }
    }

    //获取一年中的所有周数
    private void getWeekDate() {
        weekList = new ArrayList<>();
        for (int i = 1; i < 54; i++) {
            YearAndMonthBean bean = new YearAndMonthBean();
            String weekDay = getDateByWeeks(i);
            //String weekDays = Utils.getWeekDays(2018, i, 1);
            bean.setmDate(weekDay);
            bean.setMoney(0);
            weekList.add(bean);
        }
    }

    //获取一年中的每一天 365条数据 相当于
    private ArrayList<YearAndMonthBean> getYearDate(int year) {
        mDayList = new ArrayList<>();
        for (int k = 0; k < 12; k++) {
            //k表示月份
            int dayNum = 0;
            if (k == 0 || k == 2 || k == 4 || k == 6 || k == 7 || k == 9 || k == 11) {
                //31天
                dayNum = 32;
                setList(k, dayNum);
            } else if (k == 1) {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    //29天
                    dayNum = 30;
                    setList(k, dayNum);
                } else {
                    //28天
                    dayNum = 29;
                    setList(k, dayNum);
                }
            } else {
                //有30天
                dayNum = 31;
                setList(k, dayNum);
            }
        }
        return mDayList;

    }

    private void setList(int k, int dayNum) {
        for (int i = 1; i < dayNum; i++) {
            b = new YearAndMonthBean();
            if (i < 10) {
                if (k < 9) {
                    b.setmDate("0" + (k + 1) + "-" + "0" + i);
                    b.setMoney(0);
                } else {
                    b.setmDate((k + 1) + "-" + "0" + i);
                    b.setMoney(0);
                }

            } else {
                if (k < 9) {
                    b.setmDate("0" + (k + 1) + "-" + i);
                    b.setMoney(0);
                } else {
                    b.setmDate((k + 1) + "-" + i);
                    b.setMoney(0);
                }

            }
            mDayList.add(b);
        }
    }

    private void initChart(int pos, ArrayList<YearAndMonthBean> list) {
        mFormat = new DecimalFormat("#,###.##");
        lineChart = binding.newLineChart;
        lineChart2 = binding.newLineChart2;
        lineChart3 = binding.newLineChart3;
        lineChartSr1 = binding.srLineChart1;
        lineChartSr2 = binding.srLineChart2;
        lineChartSr3 = binding.srLineChart3;
        //这个地方不去复用的原因是 复用时数据之间会相互影响  单独的数据UI界面之间不会相互影响
        if (data.getDateType() == 3) {    //月
            if (data.isInCome()) {  //收入的
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.VISIBLE);
            } else {
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.VISIBLE);
                lineChartSr3.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
            }
        } else if (data.getDateType() == 2) {    //周
            if (data.isInCome()) {   //收入的
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.VISIBLE);
                lineChartSr3.setVisibility(View.GONE);
            } else {
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.VISIBLE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.GONE);
            }

        } else if (data.getDateType() == 1) {  //日
            if (data.isInCome()) {  //收入的
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.VISIBLE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.GONE);
            } else {
                lineChart.setVisibility(View.VISIBLE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
            }
        }

        values1 = new ArrayList<>();
        values2 = new ArrayList<>();
        for (int i = 0; i < yoyList.size(); i++) {
            yoyListEntity = yoyList.get(i);
            String amount = yoyListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(i + 1, f);
                values1.add(entry);
            }
        }

        for (int i = 0; i < realList.size(); i++) {
            realListEntity = realList.get(i);
            String amount = realListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(i + 1, f);
                values2.add(entry);
            }
        }
        //折线图下面阴影遮罩的颜色
        Drawable[] drawables = {
                ContextCompat.getDrawable(getActivity(), R.drawable.chart_thisyear_blue),
                ContextCompat.getDrawable(getActivity(), R.drawable.chart_callserice_call_casecount)
        };
        //这个颜色我不说 自己猜
        int[] callDurationColors = {Color.parseColor("#45A2FF"), Color.parseColor("#F15C3C")};
        String thisYear = "";
        if (realList.size() > 0) {
            thisYear = realList.get(0).getYear();
        }

        String lastYear = "";
        if (yoyList.size() > 0) {
            //去掉右上角的 圆点2018
            lastYear = yoyList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};

        if (data.getDateType() == 3) {
            if (data.isInCome()) {    //收入的
                updateLinehart(yoyList, realList, lineChartSr3, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            } else {
                updateLinehart(yoyList, realList, lineChart3, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            }
        } else if (data.getDateType() == 2) {
            if (data.isInCome()) {   //收入的
                if (firsts2 == 0) {
                    //重复添加一遍的原因为 第一次添加会产生一个页面加载全部数据 挤到一块的问题 （删除自己看）
                    updateLinehart(yoyList, realList, lineChartSr2, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
                }
                firsts2 = firsts2 + 1;
                updateLinehart(yoyList, realList, lineChartSr2, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            } else {
                if (firsts == 0) {
                    //重复添加一遍的原因为 第一次添加会产生一个页面加载全部数据 挤到一块的问题 （删除自己看）
                    updateLinehart(yoyList, realList, lineChart2, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
                }
                firsts = firsts + 1;
                updateLinehart(yoyList, realList, lineChart2, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            }
        } else if (data.getDateType() == 1) {
            if (data.isInCome()) {   //收入
                if (first1s2 == 0) {
                    updateLinehart(yoyList, realList, lineChartSr1, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
                }
                first1s2 = first1s2 + 1;
                updateLinehart(yoyList, realList, lineChartSr1, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            } else { //支出
                if (first1s == 0) {
                    updateLinehart(yoyList, realList, lineChart, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
                }
                first1s = first1s + 1;
                updateLinehart(yoyList, realList, lineChart, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            }
        }
    }

    // 双平滑曲线传入数据，添加markview，添加实体类单位
    private void updateLinehart(final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables, final String unit, List<Entry> values2, List<Entry> values1, final String[] labels, int pos, final ArrayList<YearAndMonthBean> list) {
        List<Entry>[] entries = new ArrayList[2];
        entries[0] = values1;
        entries[1] = values2;
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors, Color.parseColor("#999999"), 12f);
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);

        lineChart.setDragEnabled(true);//设置是否可拖拽
        lineChart.setScaleEnabled(false);//设置可缩放
        lineChart.setTouchEnabled(true); //可点击
        //lineChart.setPinchZoom(false);
        lineChart.setBackgroundColor(Color.WHITE); //设置背景颜色
        //移到某个位置
        lineChart.moveViewToX(pos);
        //日月切换时重新设定缩放倍数(还没来得及尝试此方法是否有用)
        //lineChart.getViewPortHandler().setMinMaxScaleX(2,2);
        //lineChart.highlightValue(pos,pos);//设置 默认显示对应月份时间 对应的记账金额 (0,0 默认显示第一个覆盖物)（不起作用 醉了）
        //lineChart.getLineData().getDataSets().get(0).setVisible(true); //线条的隐藏以及显示

        if (data.getDateType() == 3) {
           /* lineChart.setDragEnabled(false);//设置是否可拖拽
            //缩放第一种方式
            Matrix matrix = new Matrix();
            //1f代表不缩放
            matrix.postScale(1f, 1f);
            lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
            //重设所有缩放和拖动，使图表完全适合它的边界（完全缩小）。
            lineChart.fitScreen();*/
        } else if (data.getDateType() == 2) {
          /*  //X轴的数量过多 坐标显示不全 （貌似不起作用）
            XAxis xl = lineChart.getXAxis();
            xl.setSpaceBetweenLabels(1);*/
            XAxis xl = lineChart.getXAxis();
            YAxis axisLeft = lineChart.getAxisLeft();
            //xl.setGranularity(50f);
            xl.setTextSize(8);
            //axisLeft.setAxisLineWidth(50f);
        }
        //设置 不展示没有数据的点  未成功 暂时注释 后期调试
        //lineChart.setRenderer(new EmptyLineChartRendererNew(lineChart));
        /**
         * 这里切换平滑曲线或者折现图
         */
        //lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER);// LINEAR, STEPPED,
        lineChartEntity.setLineMode(LineDataSet.Mode.LINEAR);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);

        lineChartEntity.setAxisFormatter((value, axis) -> {
                    //设置折线图最底部月份显示
                    if (value == 1.0f) {
                        //return mFormat.format(value) + "月";
                        if (data.getDateType() == 3) {
                            return yoyList.get(0).getMonth().split("份")[0];
                        } else if (data.getDateType() == 2) {

                            return yoyList.get(0).getMonth();
                        } else if (data.getDateType() == 1) {
                            return yoyList.get(0).getMonth();
                        }

                    }
                    String monthStr = mFormat.format(value);
                    int i = Integer.parseInt(monthStr);
                    if (i > yoyList.size()) {
                        return "";
                    }
                    if (data.getDateType() == 3) {
                        monthStr = yoyList.get(i - 1).getMonth().split("份")[0];
                    } else {
                        monthStr = yoyList.get(i - 1).getMonth();
                    }
                    if (monthStr.contains(".")) {
                        return "";
                    } else {
                        return monthStr;
                    }
                },
                //设置折线图最右边的百分比
                (value, axis) -> "");

        //设置折线图最右边的百分比
        lineChartEntity.setDataValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> "");

        final NewMarkerView markerView = new NewMarkerView(getActivity(), R.layout.custom_marker_view_layout);

        markerView.setCallBack(new NewMarkerView.CallBack() {
            @Override
            public void onCallBack(float x, String value) {
                int index = (int) (x);
                if (index < 0) {
                    return;
                }
                if (index > yoyList.size() && index > realList.size()) {
                    return;
                }
                String textTemp = "";

                if (index <= yoyList.size()) {
                    if (!StringUtils.isEmpty(textTemp)) {
                    }
                    if (data.getDateType() == 2) {
                        textTemp += yoyList.get(index - 1).getMonth().split("月份")[0] + "  " + mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                    } else {
                        textTemp += yoyList.get(index - 1).getYear() + "." + yoyList.get(index - 1).getMonth().split("月份")[0] + "  " + mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                    }
                }

                if (index <= realList.size()) {
                    textTemp += "\n";
                    textTemp += realList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(realList.get(index - 1).getAmount())) + unit;
                }
                markerView.getTvContent().setText(textTemp);

                if (!aa) {
                    setDateToCharts(index - 1, list);
                    aa = true;
                    aaa = index;
                }
                if (index != aaa) {
                    Utils.playVoice(getActivity(), R.raw.changgui01);
                    setDateToCharts(index - 1, list);
                    aaa = index;
                }

            }
        });

        lineChartEntity.setMarkView(markerView);
        setDateToCharts(pos, list);
        lineChart.getData().setDrawValues(false);
    }

    // 双平滑曲线添加线下的阴影
    private void toggleFilled(LineChartEntity lineChartEntity, Drawable[] drawables, int[] colors) {
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            lineChartEntity.toggleFilled(drawables, null, true);
        } else {
            lineChartEntity.toggleFilled(null, colors, true);
        }
    }


    // 饼图相关
    private PieChart mChart;

    private void initChart(int totalCount) {
        mChart = binding.pieChart;

        //mChart的半径是根据整体的控件大小来动态计算的，设置外边距等都会影响到圆的半径
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);  //禁止显示图例
        mChart.setCenterTextSize(14f); //设置文本字号
        mChart.setHoleRadius(50f); //设置中心孔半径占总圆的百分比
        mChart.setRotationAngle(90); //初始旋转角度
        mChart.setData(generatePieData(totalCount)); //设置数据源
        mChart.setRotationEnabled(false);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float[] mDrawAngles = mChart.getDrawAngles();
                float[] mAbsoluteAngles = mChart.getAbsoluteAngles();
                float start = mChart.getRotationAngle();
                int i = (int) h.getX();
                float offset = mDrawAngles[i] / 2;
                float end = 90 - (mAbsoluteAngles[i] - offset);

                if (end < 0) {
                    end = 360 + end;
                }

                if (pieList1.size() > 0) {
                    data.setSelect(i + 1);
                }

                spin(start, end, i);
            }

            @Override
            public void onNothingSelected() {
                data.setSelect(-1);// 没有选择任何
                data.setSelestStr("");
                data.setShow(false);
            }
        });
    }

    private boolean first = true;// 第一次需要初始化饼图，之后不再初始化只设置值

    private void setChart(int totalCount) {
        if (first) {
            initChart(totalCount);
            first = false;
        } else {
            mChart.setData(generatePieData(totalCount)); //设置数据源
        }
        mChart.setRotationAngle(90); //初始旋转角度

        String textStr = "<font color=\"#808080\">总消费笔数</font>\n" + (totalCount == 0 ? "0" : totalCount) + "笔";
        mChart.setCenterText(Html.fromHtml(textStr)); //设置中心文本
        mChart.invalidate();
    }

    private void spin(float fromangle, float toangle, final int i) {
        mChart.setRotationAngle(fromangle);
        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(mChart, "rotationAngle", fromangle, toangle);
        spinAnimator.setDuration(500);
        spinAnimator.setInterpolator(Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad));

        spinAnimator.addUpdateListener(animation -> mChart.postInvalidate());
        spinAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (pieList != null && pieList.size() > 0) {
                    data.setShow(true);
                    int spendHappiness = pieList1.get(i).spendHappiness;
                    data.setCount(pieList1.get(i).count + "笔");
                    if (spendHappiness == 0) {
                        data.setSelestStr("花的少");
                        data.setIcon(R.mipmap.icon_mood_happy_blue);
                    } else if (spendHappiness == 1) {
                        data.setSelestStr("差不多");
                        data.setIcon(R.mipmap.icon_mood_normal_blue);
                    } else if (spendHappiness == 2) {
                        data.setSelestStr("花的多");
                        data.setIcon(R.mipmap.icon_mood_blue);
                    }
                }
            }
        });
        spinAnimator.start();
    }

    // 后台返回的数据
    List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> pieList = new ArrayList<>();
    // 拼装的数据
    List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> pieList1 = new ArrayList<>();

    protected PieData generatePieData(int totalCount) {
        data.setCop1("--");// 清空数据
        data.setCop2("--");
        data.setCop3("--");
        data.setSelect(-1);// 没有选择任何
        data.setShow(false);

        pieList1.clear();
        ArrayList<PieEntry> entries1 = new ArrayList<>();

        if (pieList == null || pieList.size() == 0) {
            entries1.add(new PieEntry((float) (100), ""));
        } else {
            int p = 0;//给list中加入了几条数据
            while (p < 3) {
//                花的少 差不多 花的多
//                0        1      2
                chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean temp = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                if (p == 0) {
                    temp.setSpendHappiness(2);
                    for (int i = 0; i < pieList.size(); i++) {
                        if (pieList.get(i).spendHappiness == 2) {
                            temp.setCount(pieList.get(i).count);
                            p += 1;
                        }
                    }
                    if (p == 0) {
                        temp.setCount(0);
                        p += 1;
                    }
                } else if (p == 1) {
                    temp.setSpendHappiness(0);
                    for (int i = 0; i < pieList.size(); i++) {
                        if (pieList.get(i).spendHappiness == 0) {
                            temp.setCount(pieList.get(i).count);
                            p += 1;
                        }
                    }
                    if (p == 1) {
                        temp.setCount(0);
                        p += 1;
                    }
                } else if (p == 2) {
                    temp.setSpendHappiness(1);
                    for (int i = 0; i < pieList.size(); i++) {
                        if (pieList.get(i).spendHappiness == 1) {
                            temp.setCount(pieList.get(i).count);
                            p += 1;
                        }
                    }
                    if (p == 2) {
                        temp.setCount(0);
                        p += 1;
                    }
                }

                pieList1.add(temp);
            }

            if (pieList1.size() == 3) {// 自己拼装的，应该是3条
                double a1 = (double) pieList1.get(0).getCount() / totalCount;
                entries1.add(new PieEntry((float) Utils.to4Digit(a1), ""));
                data.setCop1(Utils.to2Digit(a1 * 100) + "%");

                double a2 = (double) pieList1.get(1).getCount() / totalCount;
                entries1.add(new PieEntry((float) Utils.to4Digit(a2), ""));
                data.setCop2(Utils.to2Digit(a2 * 100) + "%");

                double a3 = 1 - a1 - a2;
                entries1.add(new PieEntry((float) Utils.to4Digit(a3), ""));
                data.setCop3(Utils.to2Digit(a3 * 100) + "%");
            }
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");

        if (pieList == null || pieList.size() == 0) {
            ds1.setColors(Color.parseColor("#BDBDBD")); //添加默认的颜色组
        } else {
            ds1.setColors(ContextCompat.getColor(getActivity(), R.color.index_1),
                    ContextCompat.getColor(getActivity(), R.color.index_2),
                    ContextCompat.getColor(getActivity(), R.color.index_3)); //添加默认的颜色组
        }
        ds1.setSliceSpace(1f); //饼块之间的间隙
        ds1.setDrawValues(false);  //将饼状图上的默认百分比子去掉
        ds1.setHighlightEnabled(true); // 允许突出显示饼块

        return new PieData(ds1);
    }

    @Override
    public int initVariableId() {
        return 0;
    }
}