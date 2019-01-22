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
import com.hbird.base.databinding.FragStatisticsBinding;
import com.hbird.base.mvc.bean.RealListEntity;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.mvc.widget.MyChart.LineChartEntity;
import com.hbird.base.mvc.widget.MyChart.NewMarkerView;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.hbird.bean.StatisticsTopBean;
import com.hbird.common.Constants;
import com.hbird.ui.statistics_details.ActPieChartRanking;
import com.hbird.ui.statistics_details.ActRankingDetails;
import com.hbird.util.Utils;
import com.hbird.widget.LineChartInViewPager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sing.common.base.BaseFragment;
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

        loadDataForNet();
    }

    // 点击了排行
    private void onClickRanking(StatisticsSpendTopArraysBean b) {
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
            getWeekDate();
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
                if (week == (j + 1)) {
                    yearWeekList.get(j).setMoney(money);
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
        viewModel.getRanking(data.isAll(), persionId, data.getDateType(), firstDay,lastDay,years, weeks, monthCurrent, thisTime, (data.isInCome() ? 2 : 1), topMoney, new FragStatisticsModle.OnRankingCallBack() {
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
            pos = Utils.getYearToWeek() - 1;
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

    private void setDateToCharts(int position, ArrayList<YearAndMonthBean> list) {
        //选中对应条目 刷新界面
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
            if (t1 > t2) { // 12/31-01/06
                firstDay = (weekYyyy-1) + "-" + s.substring(0, 2) + "-" + s.substring(3, 5);
                lastDay = weekYyyy + "-" + s.substring(6, 8) + "-" + s.substring(9, s.length());
            } else { // 01/14-01/20
                firstDay = weekYyyy + "-" + s.substring(0, 2) + "-" + s.substring(3, 5);
                lastDay = weekYyyy + "-" + s.substring(6, 8) + "-" + s.substring(9, s.length());
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
        LineChartInViewPager lineCharts = new LineChartInViewPager(getActivity());
        lineCharts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_200));
        binding.flParent.addView(lineCharts);

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
        if (yoyList.size() > 0) {  //去掉右上角的 圆点2018
            lastYear = yoyList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};

        updateLinehart(yoyList, realList, lineCharts, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
        updateLinehart(yoyList, realList, lineCharts, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
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
//        lineChart.getAxisLeft().setLabelCount(4);

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