package com.hbird.ui.statistics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.chart_3_0_1v.charts.PieChart;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.PieData;
import com.github.mikephil.chart_3_0_1v.data.PieDataSet;
import com.github.mikephil.chart_3_0_1v.data.PieEntry;
import com.github.mikephil.chart_3_0_1v.highlight.Highlight;
import com.github.mikephil.chart_3_0_1v.listener.OnChartValueSelectedListener;
import com.hbird.base.R;
import com.hbird.base.databinding.FragStatisticsBinding;
import com.hbird.base.mvc.bean.RealListEntity;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.hbird.bean.StatisticsTopBean;
import com.hbird.common.Constants;
import com.hbird.common.chating.charts.LineChart;
import com.hbird.common.chating.data.LineData;
import com.hbird.common.chating.formatter.IndexAxisValueFormatter;
import com.hbird.ui.statistics_details.ActPieChartRanking;
import com.hbird.ui.statistics_details.ActRankingDetails;
import com.hbird.util.Utils;
import com.hbird.widget.RoundMarker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sing.common.base.BaseFragment;

import static com.hbird.base.util.Utils.getDateByWeeks;

/**
 * @author: LiangYX
 * @ClassName: FragStatistics
 * @date: 2019/1/17 13:33
 * @Description: 统计
 */
public class FragStatistics extends BaseFragment<FragStatisticsBinding, FragStatisticsModle> {

    TextView topMoney;

    private int weeks;
    private String monthCurrent;
    private List<RealListEntity> realList = new ArrayList<>();//此数据集合是用来对比折线用的 暂未用到 为空即可（方便对比两年的数据）
    private List<YoyListEntity> yoyList;
    private DecimalFormat mFormat;
    private List<Entry> values1, values2;
    private YoyListEntity yoyListEntity;

    int aaa = 0;
    boolean aa = false;

    private StatisticsData data;

    private FragStatisticsAdapter adapter;
    private List<StatisticsSpendTopArraysBean> list = new ArrayList<>();// 实际上显示的数据
    private List<StatisticsSpendTopArraysBean> t = new ArrayList<>();// 全部数据

    private List<StatisticsTopBean> topList1 = new ArrayList<>();// 日的 年月信息
    private TopAdapter1 adapter1;
    private List<StatisticsTopBean> topList2 = new ArrayList<>();// 周月的 年信息
    private TopAdapter2 adapter2;
    private List<StatisticsTopBean> topList3 = new ArrayList<>();// 周月的 年信息
    private TopAdapter3 adapter3;
    private ArrayList<YearAndMonthBean> monthDayList = new ArrayList<>();// 每月中每天的数据
    private ArrayList<YearAndMonthBean> yearWeekList = new ArrayList<>();// 每年中周的数据
    private ArrayList<YearAndMonthBean> yearMonthList = new ArrayList<>();// 每年中月的数据
    private String persionId;// 个人的ID
    private int height_200;
    private int dayYyyy;// 日标签已选择的年
    private int dayMm;// 日标签已选择的月
    private int weekYyyy;// 日标签已选择的年
    private int monthYyyy;// 日标签已选择的年
    private String thisTime = "";// 2019-01-01

    // 点击折线的时候，
    private String firstDay;// 当前选择的开始天
    private String lastDay;// 当前选择的结束天

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.frag_statistics;
    }

    @Override
    public void initData() {
        persionId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        height_200 = getResources().getDimensionPixelSize(R.dimen.dp_200_x);

        data = new StatisticsData();
        binding.setData(data);
        binding.setListener(new OnClick());

        topMoney = binding.tvTopMoney;

        adapter = new FragStatisticsAdapter(getActivity(), list, R.layout.row_statistics, (position, data, type) -> onClickRanking((StatisticsSpendTopArraysBean) data));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setItemAnimator(null);

        //获取当前周
        weeks = Utils.getYearToWeek();
        //获取当前月
        monthCurrent = DateUtils.getMonthCurrent();

        getWeekDate();//初始化周数
        //初始化月的数据
        getMonthDate();

        thisTime = DateUtils.getYearMonthDay();

        dayYyyy = DateUtils.getYears();
        dayMm = DateUtils.getMonthToNum() + 1;
        weekYyyy = DateUtils.getYears();
        monthYyyy = DateUtils.getYears();

        adapter1 = new TopAdapter1(getActivity(), topList1, R.layout.row_top, (position, data, type) -> onTopItemClick(position, 1));
        adapter1.setYyyy(dayYyyy, dayMm);
        binding.recyclerView1.setAdapter(adapter1);
        binding.recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));
        binding.recyclerView1.setNestedScrollingEnabled(false);

        adapter2 = new TopAdapter2(getActivity(), topList2, R.layout.row_top, (position, data, type) -> onTopItemClick(position, 2));
        adapter2.setYyyy(weekYyyy);
        binding.recyclerView2.setAdapter(adapter2);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));
        binding.recyclerView2.setNestedScrollingEnabled(false);

        adapter3 = new TopAdapter3(getActivity(), topList3, R.layout.row_top, (position, data, type) -> onTopItemClick(position, 3));
        adapter3.setYyyy(monthYyyy);
        binding.recyclerView3.setAdapter(adapter3);
        binding.recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));
        binding.recyclerView3.setNestedScrollingEnabled(false);

        restTopList();
    }

    // 点击了排行
    private void onClickRanking(StatisticsSpendTopArraysBean b) {
        Utils.playVoice(getActivity(), R.raw.changgui02);
        Intent intent = new Intent(getActivity(), ActRankingDetails.class);
        intent.putExtra(Constants.START_INTENT_A, b.typeName);
        intent.putExtra(Constants.START_INTENT_B, String.valueOf(b.money));
        intent.putExtra(Constants.START_INTENT_C, firstDay);
        intent.putExtra(Constants.START_INTENT_D, lastDay);
        intent.putExtra(Constants.START_INTENT_E, data.getDateType());
        intent.putExtra(Constants.START_INTENT_F, (data.isInCome() ? 2 : 1));
        intent.putExtra(Constants.START_INTENT_G, data.isAll());
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            loadDataForNet();
        }
    }

    // 重置顶部数据
    private void restTopList() {
        topList1.clear();
        topList2.clear();
        topList3.clear();
        int yyyy = DateUtils.getYears();
        int mm = DateUtils.getMonthToNum();
        for (int i = 0; i < 12; i++) {
//            topList1.add(new StatisticsTopBean(yyyy, i + 1, i == mm));
            topList1.add(new StatisticsTopBean(yyyy, i + 1));
        }
        int minYyyy = 2018;
        while (minYyyy <= yyyy) {
            topList2.add(new StatisticsTopBean(minYyyy, 0));
            topList3.add(new StatisticsTopBean(minYyyy, 0));
            minYyyy += 1;
        }
    }

    // 日标签的顶部点击
    private void onTopItemClick(int position, int type) {
        if (type == 1) {
            dayYyyy = topList1.get(position).yyyy;
            dayMm = topList1.get(position).mm;
            // 已选择的年月下该月的信息
            adapter1.setYyyy(dayYyyy, dayMm);
        } else if (type == 2) {
            weekYyyy = topList2.get(position).yyyy;
            adapter2.setYyyy(weekYyyy);
        } else if (type == 3) {
            monthYyyy = topList3.get(position).yyyy;
            adapter3.setYyyy(monthYyyy);
        }

        loadDataForNet();
    }

    // 设置天的
    private void setChartForksDay(List<chartToBarReturn.ResultBean.ArraysBean> arrays) {
        getMonthDay(dayYyyy, dayMm - 1);
        for (int i = 0; i < arrays.size(); i++) {
            long time = DateUtils.dateToTimestamp(arrays.get(i).getDayTime());
            double money = arrays.get(i).getMoney();
            String day = DateUtils.getMonthDays(time);
            for (int j = 0; j < monthDayList.size(); j++) {
                String s = monthDayList.get(j).getmDate();
                if (TextUtils.equals(s, day)) {
                    monthDayList.get(j).setMoney(money);
                }
            }
        }
        //柱状图列表 每一天的
        setChartViews(1, monthDayList);
    }

    private void setChartForksWeek(List<chartToBarReturn.ResultBean.ArraysBean> arrays) {
        getWeekDate();//初始化周数
        for (int i = 0; i < arrays.size(); i++) {
            int week = Integer.parseInt(arrays.get(i).getWeek());
            double money = arrays.get(i).getMoney();
            for (int j = 0; j <= yearWeekList.size(); j++) {
                if (weekYyyy == 2018) {
                    if (week == (j + 1)) {
                        yearWeekList.get(j).setMoney(money);
                    }
                } else {
                    if (week == (j)) {
                        yearWeekList.get(j).setMoney(money);
                    }
                }
            }
        }
        //柱状图列表 每一周的
        setChartViews(2, yearWeekList);
    }

    private void setChartForksMOnth(List<chartToBarReturn.ResultBean.ArraysBean> arrays) {
        yearMonthList.clear();
        getMonthDate();
        for (int i = 0; i < arrays.size(); i++) {
            String month2M = arrays.get(i).getMonth();
            //获取当前月份
            int i1 = Integer.parseInt(month2M);
            double money = arrays.get(i).getMoney();
            for (int j = 0; j < yearMonthList.size(); j++) {
                if (i1 - 1 == j) {
                    yearMonthList.get(j).setMoney(money);
                }
            }
        }
        //柱状图列表 每一周的
        setChartViews(3, yearMonthList);
    }

    public void loadDataForNet() {
        getApiBarDate();//获取日周月
        getRankingBar();//获取支出排行情绪消费统计
    }

    public class OnClick {
        public void changeType(boolean isInCome) {
            data.setInCome(isInCome);
            Utils.playVoice(getActivity(), R.raw.changgui01);
            loadDataForNet();
        }

        // 切换了日期类型 1 2 3 , 日 周 年
        public void changeDateType(int pos) {
            data.setDateType(pos);
            Utils.playVoice(getActivity(), R.raw.changgui01);
            loadDataForNet();
        }

        // 个人数据全部数据的切换
        public void isAll(View view) {
            data.setAll(!data.isAll());
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

        public void pieChartRanking(View view) {
            Intent intent = new Intent(getActivity(), ActPieChartRanking.class);
            intent.putExtra(Constants.START_INTENT_A, data.getSelestStr());// 花的多、花的少、差不多
            intent.putExtra(Constants.START_INTENT_B, data.getCount());// 多少笔
            intent.putExtra(Constants.START_INTENT_C, firstDay);
            intent.putExtra(Constants.START_INTENT_D, lastDay);
            intent.putExtra(Constants.START_INTENT_E, data.getDateType());
            intent.putExtra(Constants.START_INTENT_F, (data.isInCome() ? 2 : 1));
            intent.putExtra(Constants.START_INTENT_G, data.isAll());
            startActivity(intent);
        }
    }

    private String getEnumToNumer(Double money) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(money);
    }

    // 获取日周月的支出排行榜和情绪消费统计
    private void getRankingBar() {
        int years = 2019;
        if (data.getDateType() == 1) {
            years = dayYyyy;
        } else if (data.getDateType() == 2) {
            years = weekYyyy;
        } else if (data.getDateType() == 3) {
            years = monthYyyy;
        }
        viewModel.getRanking(data.isAll(), persionId, data.getDateType(), firstDay, lastDay, years, weeks, monthCurrent, thisTime, (data.isInCome() ? 2 : 1), topMoney, new FragStatisticsModle.OnRankingCallBack() {
            @Override
            public void result(List<StatisticsSpendTopArraysBean> temp, double maxMoney) {
                if (null != temp && temp.size() > 0) {
                    data.setNoDataRanking(false);
                    data.setShowOpen(temp.size() > 5);// 不显示展开按钮
                    data.setOpen(temp.size() <= 5);// 设置默认显示“展开”

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

        viewModel.getHappnessRanking(data.isAll(), persionId, data.getDateType(), (data.isInCome() ? 2 : 1), thisTime, years, weeks, monthCurrent, new FragStatisticsModle.OnHappnessRankingCallBack() {
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
        if (data.getDateType() == 1) {// 日
            viewModel.getDayData(persionId, dayYyyy, dayMm, data.isAll(), data.isInCome() ? 2 : 1, new FragStatisticsModle.onMonthCallBack() {
                @Override
                public void result(List<chartToBarReturn.ResultBean.ArraysBean> dbList) {
                    setChartForksDay(dbList);
                }
            });
        } else if (data.getDateType() == 2) {// 周
            viewModel.getWeekData(persionId, weekYyyy, data.isAll(), data.isInCome() ? 2 : 1, new FragStatisticsModle.onMonthCallBack() {
                @Override
                public void result(List<chartToBarReturn.ResultBean.ArraysBean> dbList) {
                    setChartForksWeek(dbList);
                }
            });
        } else if (data.getDateType() == 3) {// 月
            viewModel.getMonthData(persionId, monthYyyy, data.isAll(), data.isInCome() ? 2 : 1, new FragStatisticsModle.onMonthCallBack() {
                @Override
                public void result(List<chartToBarReturn.ResultBean.ArraysBean> dbList) {
                    setChartForksMOnth(dbList);
                }
            });
        }
    }

    //设置每天的
    private void setChartViews(final int item, final ArrayList<YearAndMonthBean> list) {
        //判断指定日期在集合中是第几个条目
        int pos = 0;

        if (item == 1) { //日，每天的数据
            String currentTime = DateUtils.getMonthDays(new Date().getTime());
            for (int j = 0; j < list.size(); j++) {
                String time = list.get(j).getmDate();
                if (TextUtils.equals(time, currentTime)) {
                    pos = j;
                }
            }
        } else if (item == 2) {
            //周 每周的数据
            pos = Utils.getYearToWeek();
        } else {
            //月 获取当前月
            pos = DateUtils.getMonthToNum();
        }

        //折线图 曲线
        yoyList = new ArrayList<>();
        yoyList.clear();

        int years = 2019;
        if (data.getDateType() == 1) {
            years = dayYyyy;
        } else if (data.getDateType() == 2) {
            years = weekYyyy;
        } else if (data.getDateType() == 3) {
            years = monthYyyy;
        }

        for (int i = 0; i < list.size(); i++) {
            YoyListEntity yoyListEntity = new YoyListEntity();
            double money = list.get(i).getMoney();
            String amount = String.valueOf(money);
            String month = list.get(i).getmDate();
            String year = years + "";
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

    //选中对应条目 刷新界面
    private void setDateToCharts(int position, ArrayList<YearAndMonthBean> list) {
        String s = list.get(position).getmDate();
        double money = list.get(position).getMoney();
        String formats = getEnumToNumer(money);

        topMoney.setText(formats);
        binding.natvMoney.setNumberString("0", formats);
        if (data.getDateType() == 1) {
            // s为01-21
            firstDay = dayYyyy + "-" + s;
            // 将 firstDay 转为Date加一天后转回来
            Date d = DateUtils.parse(firstDay, "yyyy-MM-dd");
            DateUtils.addDay(d, 1);
            lastDay = DateUtils.stampToString(DateUtils.addDay(d, 1), "yyyy-MM-dd");

            thisTime = DateUtils.getYears() + "-" + s;
        } else if (data.getDateType() == 2) {
            // s为 01/14-01/20  、 12/31-01/06
            int t1 = Integer.parseInt(s.substring(0, 2));
            int t2 = Integer.parseInt(s.substring(6, 8));
            int lastD = Integer.parseInt(s.substring(9, 11)) + 1;
            if (t1 > t2) { // 12/31-01/06
                firstDay = (weekYyyy - 1) + "-" + s.substring(0, 2) + "-" + s.substring(3, 5);
                lastDay = weekYyyy + "-" + s.substring(6, 8) + "-" + ((lastD < 10) ? "0" + (lastD) : (lastD));// 最后一天要+1，比如12/31-01/06，是01/07之前
            } else { // 01/14-01/20
                firstDay = weekYyyy + "-" + s.substring(0, 2) + "-" + s.substring(3, 5);
                lastDay = weekYyyy + "-" + s.substring(6, 8) + "-" + ((lastD < 10) ? "0" + (lastD) : (lastD));
            }

            weeks = position + 1;
        } else {
            // s为 3月
            int temp = Integer.parseInt(s.substring(0, s.length() - 1));
            firstDay = monthYyyy + "-" + (temp < 10 ? "0" + temp : temp) + "-01";
            temp += 1;
            lastDay = monthYyyy + "-" + (temp < 10 ? "0" + temp : temp) + "-01";

            monthCurrent = position + 1 + "";
        }

        getRankingBar();//获取支出排行情绪消费统计
    }

    //获取一年中所有的月份
    private void getMonthDate() {
        yearMonthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            YearAndMonthBean bean = new YearAndMonthBean();
            bean.setmDate(i + "月");
            bean.setMoney(0);
            yearMonthList.add(bean);
        }
    }

    //获取一年中的所有周数
    private void getWeekDate() {
        yearWeekList.clear();
        for (int i = 1; i < 54; i++) {
            YearAndMonthBean bean = new YearAndMonthBean();
            String weekDay = getDateByWeeks(i);
            bean.setmDate(weekDay);
            bean.setMoney(0);
            yearWeekList.add(bean);
        }
    }

    // 获取每月中的每一天
    private ArrayList<YearAndMonthBean> getMonthDay(int year, int mm) {
        monthDayList.clear();
        int dayNum = 0;
        if (mm == 0 || mm == 2 || mm == 4 || mm == 6 || mm == 7 || mm == 9 || mm == 11) { //31天
            dayNum = 32;
            setList(mm, dayNum);
        } else if (mm == 1) {
            //29天
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                dayNum = 30;
                setList(mm, dayNum);
            } else {//28天
                dayNum = 29;
                setList(mm, dayNum);
            }
        } else { //有30天
            dayNum = 31;
            setList(mm, dayNum);
        }
        return monthDayList;
    }

    private void setList(int k, int dayNum) {
        for (int i = 1; i < dayNum; i++) {
            YearAndMonthBean bean = new YearAndMonthBean();
            if (i < 10) {
                if (k < 9) {
                    bean.setmDate("0" + (k + 1) + "-" + "0" + i);
                } else {
                    bean.setmDate((k + 1) + "-" + "0" + i);
                }
            } else {
                if (k < 9) {
                    bean.setmDate("0" + (k + 1) + "-" + i);
                } else {
                    bean.setmDate((k + 1) + "-" + i);
                }
            }
            bean.setMoney(0);
            monthDayList.add(bean);
        }
    }

    private void initChart(int pos, ArrayList<YearAndMonthBean> list) {
        mFormat = new DecimalFormat("#,###.##");

        binding.flParent.removeAllViews();
        LineChart lineChart = new LineChart(getActivity());
        lineChart.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_200));
        binding.flParent.addView(lineChart);
        lineChart.setBackgroundColor(Color.WHITE); // background color
        lineChart.getDescription().setEnabled(false);  // disable description text
        lineChart.setTouchEnabled(true); // enable touch gestures
        // set listeners
        lineChart.setOnChartValueSelectedListener(new com.hbird.common.chating.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(com.hbird.common.chating.data.Entry e, com.hbird.common.chating.highlight.Highlight h) {
                setDateToCharts((int) e.getX(), list);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        lineChart.setDrawGridBackground(false);
        // create marker to display box when values are selected
        RoundMarker mv = new RoundMarker(getActivity(), R.layout.custom_marker_view);
        // Set the marker to the chart
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);


        // X轴的个数，Y轴最大范围
        ArrayList<com.hbird.common.chating.data.Entry> values = new ArrayList<>();
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
                if (f == 0) {
                    values.add(new com.hbird.common.chating.data.Entry(i, f));
                } else {
                    values.add(new com.hbird.common.chating.data.Entry(i, f, getResources().getDrawable(R.mipmap.shuju_icon_zhengshu_normal)));
                }
            }
        }

        // create a dataset and give it a type
        com.hbird.common.chating.data.LineDataSet set1 = new com.hbird.common.chating.data.LineDataSet(values, "");

        set1.setDrawIcons(true);
        // 画虚线
//        set1.enableDashedLine(10f, 5f, 0f);
        // black lines and points
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.TRANSPARENT);
        // line thickness and point size
        set1.setLineWidth(1f);// 折线宽度
        set1.setCircleRadius(1f);// 顶点值圆的弧度
        // 将点绘制为实心圆
        set1.setDrawCircleHole(false);
        // text size of values
        set1.setValueTextSize(9f);
        // 将选择线绘制为虚线
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        // 设置填充区域
        set1.setDrawFilled(true);
        set1.setFillFormatter((dataSet, dataProvider) -> lineChart.getXAxis().getAxisMinimum());

        // set color of filled area
        if (com.hbird.common.chating.utils.Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }

        ArrayList<com.hbird.common.chating.interfaces.datasets.ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        // set data
        lineChart.setData(data);

        // 绘制动画结束时间
        lineChart.animateX(500);

        // 透明化图例 仅在设置数据后可用
        lineChart.getLegend().setForm(com.hbird.common.chating.components.Legend.LegendForm.NONE);

        List<com.hbird.common.chating.interfaces.datasets.ILineDataSet> sets = lineChart.getData().getDataSets();
        for (com.hbird.common.chating.interfaces.datasets.ILineDataSet iSet : sets) {
            com.hbird.common.chating.data.LineDataSet set = (com.hbird.common.chating.data.LineDataSet) iSet;
            // 是否显示顶部的值
            set.setDrawValues(false);
            // 是否值要顶部的黑点
            set.setDrawCircles(true);
            // 是否要阴影
            set.setDrawFilled(true);

            set.setMode(com.hbird.common.chating.data.LineDataSet.Mode.LINEAR);// 直角
            // 线条样式
            set.setColor(Color.parseColor("#E9371F"));//线条颜色
            set.setCircleColor(Color.parseColor("#E9371F"));//圆点颜色
            set.setCircleHoleRadius(0.5f);
            set.setLineWidth(1f);//线条宽度

//            set.setDrawHighlightIndicators(false);//关闭heightlight
            set.setDrawHorizontalHighlightIndicator(false);
//            set.setDrawVerticalHighlightIndicator(true); //或者使用
//            set.setDrawHorizontalHighlightIndicator(true);//来打开单独某一方向的hightlight
        }

        // 移动到某个位置
        lineChart.moveViewToX(pos > 0 ? (pos - 1) : pos);

        //设置图表右边的y轴禁用
        lineChart.getAxisRight().setEnabled(false);

        //设置图表左边的y轴禁用
        lineChart.getAxisLeft().setEnabled(false);
        //是否绘制0所在的网格线
        lineChart.getAxisLeft().setDrawZeroLine(true);

        // 是否等比缩放、单独X或Y缩放
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(false); //是否可以缩放 仅y轴

        //设置x轴
        com.hbird.common.chating.components.XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#929292"));
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(com.hbird.common.chating.components.XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        xAxis.setDrawAxisLine(false);//是否绘制轴线

        // 设置X轴文字选择
        if (this.data.getDateType() == 2) {
            xAxis.setLabelRotationAngle(45);
            lineChart.setVisibleXRange(0, 5);//设置x轴显示范围，如果不设置会一次加载所有的点，很难看
            xAxis.setTextSize(8f);
            lineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        }else{
            xAxis.setLabelRotationAngle(0);
            xAxis.setTextSize(10f);
            lineChart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true
        }

        List<String> t = new ArrayList<>();
        for (int i = 1; i < yoyList.size() + 1; i++) {
            if (this.data.getDateType() == 2) {
                t.add(yoyList.get(i - 1).getMonth());
            } else {
                t.add(i + "");
            }
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(t));

        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        // 默认选择的marker
        lineChart.highlightValue(pos, 0);

        lineChart.invalidate();
    }

    // 饼图相关
    private PieChart mChart;

    private void initChart(int totalCount) {
        mChart = binding.pieChart;

        //mChart的半径是根据整体的控件大小来动态计算的，设置外边距等都会影响到圆的半径
        mChart.getDescription().setEnabled(false); // 不显示数据描述
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

        String textStr = "<font color=\"#808080\">总笔数</font><br/>" + (totalCount == 0 ? "0" : totalCount) + "笔";
        mChart.setCenterText(Html.fromHtml(textStr)); //设置中心文本
        mChart.invalidate();
    }

    private void spin(float fromangle, float toangle, final int i) {
        mChart.setRotationAngle(fromangle);
        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(mChart, "rotationAngle", fromangle, toangle);
        spinAnimator.setDuration(500);
//        spinAnimator.setInterpolator(Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad));

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