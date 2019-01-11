package com.hbird.base.mvc.fragement;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.chart_3_0_1v.charts.LineChart;
import com.github.mikephil.chart_3_0_1v.components.AxisBase;
import com.github.mikephil.chart_3_0_1v.components.Legend;
import com.github.mikephil.chart_3_0_1v.components.XAxis;
import com.github.mikephil.chart_3_0_1v.components.YAxis;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.LineDataSet;
import com.github.mikephil.chart_3_0_1v.formatter.IAxisValueFormatter;
import com.github.mikephil.chart_3_0_1v.formatter.IValueFormatter;
import com.github.mikephil.chart_3_0_1v.utils.ViewPortHandler;
import com.hbird.base.R;
import com.hbird.base.mvc.adapter.ChartRecyclerViewAdapter;
import com.hbird.base.mvc.adapter.barChart2Adapter;
import com.hbird.base.mvc.adapter.barChartAdapter;
import com.hbird.base.mvc.adapter.barChartTotypeAdapter;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RealListEntity;
import com.hbird.base.mvc.bean.ReturnBean.HappynessReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRanking2Return;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.widget.MyChart.LineChartEntity;
import com.hbird.base.mvc.widget.MyChart.LineChartInViewPager;
import com.hbird.base.mvc.widget.MyChart.NewMarkerView;
import com.hbird.base.mvc.widget.MyListViews;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.StringUtils;
import com.hbird.base.util.Utils;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.ljy.devring.DevRing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import sing.common.util.LogUtil;

import static com.hbird.base.util.Utils.getDateByWeeks;


/**
 * Created by Liul on 2018/6/28.
 * 首页图表 -  统计
 */

public class ChartFragement extends BaseFragement {
    @BindView(R.id.list_bar)
    MyListViews listview;
    @BindView(R.id.list_xinqing)
    MyListViews lView;
    @BindView(R.id.radioGroup)
    RadioGroup mRadios;
    @BindView(R.id.tv_top_time)
    TextView topTime;
    @BindView(R.id.tv_top_money)
    TextView topMoney;
    @BindView(R.id.btnsRadio)
    RadioGroup mBtnsRadio;
    @BindView(R.id.recycler_view)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_paihangbang)
    TextView mPaihangText;
    @BindView(R.id.rl_title)
    RelativeLayout mTitle;
    @BindView(R.id.rl_tit)
    RelativeLayout mTit;
    @BindView(R.id.tv_text_empty)
    TextView mEmpty;
    @BindView(R.id.text_top_money)
    com.hbird.base.mvc.widget.NumberAnimTextView mTopMoney;


    private barChartAdapter mAdapter;
    private barChartTotypeAdapter mTypeAdapter;
    private barChart2Adapter dAdapter;
    private String token;
    private String flag = "1";//1日 2周 3月
    private String type = "1";//1为默认支出  2 为收入
    private LinearLayoutManager mLayoutManager;
    private ChartRecyclerViewAdapter adapter;
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
    private int firstCome = 0;
    private int years;
    private String accountId;
    private int orderType = 1;//1:支出  2:收入
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

    @Override
    public int setContentId() {
        return R.layout.fragement_chart;
    }

    @Override
    public void initView() {
        //L.liul("ChartFragement");
        //柱状图的数据
        //initNewBarDatas();//初始化动作一低要提前完成 即使初始化空数据（能有多提前就多提前当然不能在初始化view前）

    }

    @Override
    public void initData() {
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
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");

    }

    @Override
    public void initListener() {
        mRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_left:
                        //showMessage("支出");
                        playVoice(R.raw.changgui01);
                        mTopMoney.setTextColor(getResources().getColor(R.color.text_333333));
                        mTopMoney.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_coin), null, null, null);
                        mTopMoney.setCompoundDrawablePadding(6);
                       /* mTopMoney.setDuration(3000);
                        mTopMoney.setPostfixString("%");
                        mTopMoney.setPrefixString("¥");*/

                        type = "1";
                        orderType = 1;
                        thisTime = DateUtils.getYearMonthDay();
                        weeks = Utils.getYearToWeek();
                        monthCurrent = DateUtils.getMonthCurrent();
                        loadDataForNet();
                        break;
                    case R.id.radio_right:
                        //showMessage("收入");
                        playVoice(R.raw.changgui01);
                        mTopMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
                        mTopMoney.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_coin2), null, null, null);
                        mTopMoney.setCompoundDrawablePadding(6);
                         /* mTopMoney.setDuration(3000);
                        mTopMoney.setPostfixString("%");
                        mTopMoney.setPrefixString("¥");*/
                        type = "2";
                        orderType = 2;
                        thisTime = DateUtils.getYearMonthDay();
                        weeks = Utils.getYearToWeek();
                        monthCurrent = DateUtils.getMonthCurrent();
                        loadDataForNet();
                        break;
                }
            }
        });
        mBtnsRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.btn_left:
                        //showMessage("日");
                        playVoice(R.raw.changgui01);
                        flag = "1";
                        //刷新数据
                        thisTime = DateUtils.getYearMonthDay();
                        loadDataForNet();
                        break;
                    case R.id.btn_middle:
                        //showMessage("周");
                        playVoice(R.raw.changgui01);
                        flag = "2";
                        weeks = Utils.getYearToWeek();
                        loadDataForNet();
                        break;
                    case R.id.btn_ritle:
                        //showMessage("月");
                        playVoice(R.raw.changgui01);
                        flag = "3";
                        monthCurrent = DateUtils.getMonthCurrent();
                        loadDataForNet();
                        break;
                }
            }
        });
    }

    @Override
    public void loadData() {
        // L.liul("loadData===sssss==================");
    }

    @Override
    public void loadDate() {
        super.loadDate();
        LogUtil.e("loadDate=====================");
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        if (firstCome > 0) {
            //解决 点选柱状图其它日期后 跳转到其它界面再回来时 当前天刷新到当前天的问题而不是默认之前选中的某一天
            thisTime = DateUtils.getYearMonthDay();
            loadDataForNet();
        }

    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();
        //L.liul("loadDataForNet======================");
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        firstCome = firstCome + 1;
        thisTime = DateUtils.getYearMonthDay();
        if (orderType == 1) {//支出
            if (mPaihangText != null) {
                mPaihangText.setText("支出排行榜");
            }
        } else {  //收入
            if (mPaihangText != null) {
                mPaihangText.setText("收入排行榜");
            }
        }
        getApiBarDate();//获取日周月 竖向柱状图的数据信息
        getRankingBar();//获取支出排行情绪消费统计
    }

    private void getApiBar2Date() {

        NetWorkManager.getInstance().setContext(getActivity())
                .postChartToComeBar(token, flag, beginTime, endTime, beginWeek, endWeek, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        chartToBarReturn b1 = (chartToBarReturn) b;
                        List<chartToBarReturn.ResultBean.ArraysBean> arrays = b1.getResult().getArrays();
                        maxMoney = b1.getResult().getMaxMoney();
                        dayList.clear();
                        dayList = getYearDate(years);
                        setChartForks(arrays);
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void getRanking2Bar(final int i) {
        NetWorkManager.getInstance().setContext(getActivity())
                .postChartToComeRanking(token, flag, thisTime, weeks + "", monthCurrent, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        chartToRanking2Return b1 = (chartToRanking2Return) b;
                        List<chartToRanking2Return.ResultBean.StatisticsIncomeTopArraysBean> lists = b1.getResult().getStatisticsIncomeTopArrays();
                        Double money = b1.getResult().getTrueTotalMoney();
                        String formats = getEnumToNumer(money);
                        topMoney.setText(formats);
                        mTopMoney.setNumberString("0", formats);
                        if (null != lists && lists.size() > 0) {
                            mTypeAdapter = new barChartTotypeAdapter(getActivity(), ChartFragement.this, lists, true, money, i);
                            mTypeAdapter.setDate2(true);
                            listview.setAdapter(mTypeAdapter);
                            listview.setFocusable(false);
                            listview.setVisibility(View.VISIBLE);
                            mTitle.setVisibility(View.VISIBLE);
                            mEmpty.setVisibility(View.GONE);
                        } else {
                            listview.setVisibility(View.GONE);
                            mEmpty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                        listview.setVisibility(View.GONE);
                        mEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private String getEnumToNumer(Double money) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(money);
    }

    private void getRankingBar() {
        //支出排行统计
        //通过SQL获取日周月的支出排行榜和情绪消费统计
        String sql = "";
        String spendHappnessSql = "";
        if (TextUtils.equals(flag, "1")) {
            //日支出收入排行榜
            //sql = "select sum(money) money,wo.type_name as type_name,( CASE wo.order_type WHEN 1 THEN st.icon WHEN 2 THEN it.icon ELSE NULL END ) AS icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo LEFT JOIN HBIRD_SPEND_TYPE st ON wo.type_id = st.id LEFT JOIN HBIRD_INCOME_TYPE it ON wo.type_id = it.id where wo.account_book_id= '"+accountId+"' AND wo.delflag = 0 AND wo.order_type = "+orderType+" AND strftime('%Y-%m-%d', wo.charge_date2) = '"+thisTime+"' GROUP BY wo.type_id order by money desc LIMIT 5;";
            sql = "select sum(money) money, type_name as type_name , icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) wo  where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "' GROUP BY wo.type_id order by money desc LIMIT 5;";
            //日支出情绪消费排行榜
            spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND strftime('%Y-%m-%d', wo.charge_date2) = '" + thisTime + "';";

        } else if (TextUtils.equals(flag, "2")) {
            //周支出收入排行榜 年 - 周
            String weekTime = years + "-" + weeks;
            sql = "select sum(money) money,type_name as type_name,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%W', charge_date2) = '" + weekTime + "' GROUP BY wo.type_id order by money DESC LIMIT 5;";
            //周支出情绪消费排行榜
            spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND ACCOUNT_BOOK_ID=" + accountId + " AND delflag = 0 AND strftime('%Y-%W', charge_date2) = '" + weekTime + "';";

        } else if (TextUtils.equals(flag, "3")) {
            //月支出收入排行榜
            if (monthCurrent.length() == 1) {
                monthCurrent = "0" + monthCurrent;
            }
            String monthTime = years + "-" + monthCurrent;
            sql = "select sum(money) money,type_name as type_name ,icon as icon from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT   where 1=1) wo where wo.account_book_id= '" + accountId + "' AND wo.delflag = 0 AND wo.order_type = " + orderType + " AND strftime('%Y-%m', charge_date2) = '" + monthTime + "' GROUP BY wo.type_id order by money DESC LIMIT 5;";
            //月支出情绪消费排行榜
            spendHappnessSql = "select sum(wo.spend_happiness is not null and wo.spend_happiness != -1 ) as spend_happiness_count ,sum(wo.spend_happiness = 0 ) as happy ,sum(wo.spend_happiness = 1 ) as normal ,sum(wo.spend_happiness = 2 ) as sad from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1 ) as wo where wo.order_type = " + orderType + " AND ACCOUNT_BOOK_ID=" + accountId + " AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + monthTime + "';";

        }
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            List<StatisticsSpendTopArraysBean> list = new ArrayList<>();
            list.clear();
            if (null != cursor) {
                list = DBUtil.changeToListTJ(cursor, list, StatisticsSpendTopArraysBean.class);
            }
            if (TextUtils.isEmpty(topMoney.getText().toString())) {
                maxMoney = 0;
            } else {
                maxMoney = Double.parseDouble(topMoney.getText().toString());
            }
            if (null != list && list.size() > 0) {
                setXiaoFeiDate(list, maxMoney);
                setVisiables();
            } else {
                listview.setVisibility(View.GONE);
                mEmpty.setVisibility(View.VISIBLE);
            }

        } else {
            mEmpty.setVisibility(View.VISIBLE);
            mTit.setVisibility(View.GONE);
            lView.setVisibility(View.GONE);
        }


        Cursor cursors = DevRing.tableManager(WaterOrderCollect.class).rawQuery(spendHappnessSql, null);
        if (cursors != null) {
            List<HappynessReturn> ll = new ArrayList<>();
            ll.clear();
            if (null != cursors) {
                ll = DBUtil.changeToListHappy(cursors, ll, HappynessReturn.class);
            }
            List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> HappinessArrays = new ArrayList<>();
            HappinessArrays.clear();
            if (ll.get(0).getSpendHappinessCount() != 0) {

                if (!TextUtils.isEmpty(ll.get(0).getHappy() + "") && ll.get(0).getHappy() != 0) {
                    chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean be = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                    be.setSpendHappiness(0);
                    be.setCount(ll.get(0).getHappy());
                    HappinessArrays.add(be);
                }
                if (!TextUtils.isEmpty(ll.get(0).getNormal() + "") && ll.get(0).getNormal() != 0) {
                    chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean be = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                    be.setSpendHappiness(1);
                    be.setCount(ll.get(0).getNormal());
                    HappinessArrays.add(be);
                }
                if (!TextUtils.isEmpty(ll.get(0).getSad() + "") && ll.get(0).getSad() != 0) {
                    chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean be = new chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean();
                    be.setSpendHappiness(2);
                    be.setCount(ll.get(0).getSad());
                    HappinessArrays.add(be);
                }

            }
            if (null != HappinessArrays && HappinessArrays.size() > 0) {
                int totalCount = ll.get(0).getSpendHappinessCount();

                dAdapter = new barChart2Adapter(getActivity(), HappinessArrays, true, totalCount);
                dAdapter.setDate2(true);
                lView.setAdapter(dAdapter);
                lView.setFocusable(false);
                lView.setVisibility(View.VISIBLE);
                mTit.setVisibility(View.VISIBLE);
            } else {
                lView.setVisibility(View.GONE);
                mTit.setVisibility(View.GONE);
            }

        } else {
            lView.setVisibility(View.GONE);
            mTit.setVisibility(View.GONE);
        }

       /* NetWorkManager.getInstance().setContext(getActivity())
                .postChartToRanking(token, flag,thisTime,weeks+"",monthCurrent, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        chartToRankingReturn b1 = (chartToRankingReturn) b;
                        List<chartToRankingReturn.ResultBean.StatisticsSpendTopArraysBean> lists = b1.getResult().getStatisticsSpendTopArrays();
                        double money = b1.getResult().getTrueTotalMoney();
                        String formats = getEnumToNumer(money);
                        topMoney.setText(formats);
                        if(null!=lists &&lists.size()>0){
                            setXiaoFeiDate(lists,money);
                            setVisiables();
                        }else {
                            listview.setVisibility(View.GONE);
                            mEmpty.setVisibility(View.VISIBLE);
                        }

                        List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> HappinessArrays = b1.getResult().getStatisticsSpendHappinessArrays();
                        if(null!=HappinessArrays && HappinessArrays.size()>0){
                            int totalCount = b1.getResult().getTotalCount();
                            dAdapter = new barChart2Adapter(getActivity() , HappinessArrays,true,totalCount);
                            dAdapter.setDate2(true);
                            lView.setAdapter(dAdapter);
                            lView.setFocusable(false);
                            lView.setVisibility(View.VISIBLE);
                            mTit.setVisibility(View.VISIBLE);
                        }else {
                            lView.setVisibility(View.GONE);
                            mTit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                        mEmpty.setVisibility(View.VISIBLE);
                        mTit.setVisibility(View.GONE);
                        lView.setVisibility(View.GONE);
                    }
                });*/
    }

    private void setVisiables() {
        listview.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
    }

    private void setXiaoFeiDate(List<StatisticsSpendTopArraysBean> lists, Double money) {
        mAdapter = new barChartAdapter(getActivity(), ChartFragement.this, lists, true, money);
        mAdapter.setDate2(true);
        listview.setAdapter(mAdapter);
        listview.setFocusable(false);
    }


    private void getApiBarDate() {
        String SQL = "";
        String maxSQL = "";
        String persionId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        //flag 1 2 3代表 日、周、月
        if (TextUtils.equals(flag, "1")) {
            //查询全年 每日的统计数据

            SQL = "select sum(money) money,charge_date2 as day_time  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND ORDER_TYPE= " + orderType + " AND CREATE_BY= " + persionId + " AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%j', charge_date2 )  order by  charge_date2 desc;";
            //查询一年中日的最大值
            //maxSQL= "select MAX(money) money from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where account_book_id = '"+accountId+"' AND delflag = 0 AND order_type = "+orderType+" AND strftime('%Y', charge_date2) = '"+years+"'  order by  charge_date2 DESC;";
            maxSQL = "select max (moneyList.totalDay) as money from (select sum(money) as totalDay  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime')  charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ORDER_TYPE= " + orderType + " AND ACCOUNT_BOOK_ID=" + accountId + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m-%d', charge_date2 ) ) as moneyList ;";
//            maxSQL= "select sum(money) money from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where account_book_id = '"+accountId+"' AND delflag = 0 AND order_type = "+orderType+" AND strftime('%Y', charge_date2) = '"+years+"' GROUP BY strftime( '%Y-%m-%d', charge_date2 )  order by money DESC limit 1;";

        } else if (TextUtils.equals(flag, "2")) {
            //查询 一年中54周的统计
            SQL = "select sum(money) money ,strftime('%W', charge_date2) week from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND ORDER_TYPE= " + orderType + " AND CREATE_BY= " + persionId + " AND ACCOUNT_BOOK_ID=" + accountId + " AND DELFLAG = 0 AND strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%W', charge_date2 )  order by  week DESC;";
            //查询一年中周的最大值
            maxSQL = "SELECT max( moneyList.totalWeek) AS money FROM ( SELECT sum(money) AS totalWeek FROM (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from  WATER_ORDER_COLLECT   where 1=1) where account_book_id = '" + accountId + "' AND delflag = 0 AND order_type = " + orderType + " AND strftime( '%Y',charge_date2 ) LIKE strftime( '%Y', date('now') ) GROUP BY strftime( '%Y-% W ', charge_date2 ) ) AS moneyList;";

        } else if (TextUtils.equals(flag, "3")) {
            //查询 一年中12个月的统计
            SQL = "select sum(money) money ,strftime('%m', charge_date2) month from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2    from  WATER_ORDER_COLLECT   where 1=1) where delflag = 0 AND ACCOUNT_BOOK_ID=" + accountId + " AND ORDER_TYPE= " + orderType + " AND CREATE_BY= " + persionId + " AND  strftime('%Y', charge_date2) = '" + years + "' GROUP BY strftime( '%Y-%m', charge_date2 )  order by  month ASC;";
            //查询一年中月的最大值
            maxSQL = "SELECT max( moneyList.totalWeek) AS money FROM ( SELECT sum(money) AS totalWeek FROM (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT  where 1=1) where account_book_id =  '" + accountId + "' AND delflag = 0 AND order_type = " + orderType + " AND strftime( '%Y',charge_date2 ) LIKE strftime( '%Y', date('now') ) GROUP BY strftime( '%Y-%m', charge_date2 ) ) AS moneyList;";

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
                if (TextUtils.equals(flag, "1")) {
                    //只有日数据刷新时 执行此操作
                    dayList.clear();
                    dayList = getYearDate(years);
                }
                setChartForks(dbList);

            }
        } else {

        }
        //调用接口获取数据
       /* NetWorkManager.getInstance().setContext(getActivity())
                .postChartToZcBar(token, flag, beginTime, endTime,beginWeek,endWeek, new NetWorkManager.CallBack() {

                    @Override
                    public void onSuccess(BaseReturn b) {
                        chartToBarReturn b1 = (chartToBarReturn) b;
                        List<chartToBarReturn.ResultBean.ArraysBean> arrays = b1.getResult().getArrays();
                        maxMoney = b1.getResult().getMaxMoney();
                        //如果是月 周 此处不需要执行
                        if(TextUtils.equals(flag,"1")){
                            //只有日数据刷新时 执行此操作
                            dayList.clear();
                            dayList = getYearDate(years);
                        }
                        setChartForks(arrays);
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });*/
    }

    private void setChartForks(List<chartToBarReturn.ResultBean.ArraysBean> arrays) {
        if (flag.equals("1")) {
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
        } else if (flag.equals("2")) {
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

    //设置每周的
    private void setChartWeekView() {
        //判断指定一年中的本周 在集合中是第几个条目 方便后面跳转到指定条目
        int pos = Utils.getYearToWeek() - 1;
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new ChartRecyclerViewAdapter(getActivity(), weekList, maxMoney, pos);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(adapter);

        MoveToPosition(new LinearLayoutManager(getActivity()), mRecycleView, pos);
        adapter.setOnItemClickListener(new ChartRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setItemClick(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    //设置每天的
    private void setChartViews(final int item, final ArrayList<YearAndMonthBean> list) {
        //判断指定日期在集合中是第几个条目
        int pos = 0;
        String paySpend = "";
        if (TextUtils.equals(type, "1")) {
            //支出总额
            paySpend = ",支出总额";
        } else {
            //收入总额
            paySpend = ",收入总额";
        }
        if (item == 1) {
            //日 ，每天的数据
            String currentTime = DateUtils.getMonthDays(new Date().getTime());
            for (int j = 0; j < list.size(); j++) {
                String time = list.get(j).getmDate();
                if (TextUtils.equals(time, currentTime)) {
                    pos = j;
                }
            }
            String[] split = currentTime.split("-");
            topTime.setText(split[0] + "月" + split[1] + "日" + paySpend);
        } else if (item == 2) {
            //周 每周的数据
            pos = Utils.getYearToWeek() - 1;
            String s = list.get(pos).getmDate();
            topTime.setText(s + paySpend);
        } else {
            //月 获取当前月
            pos = DateUtils.getMonthToNum();
            String s = list.get(pos).getmDate();
            topTime.setText(s + paySpend);
        }

        //柱状图展示效果
       /* mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new ChartRecyclerViewAdapter(getActivity(), list,maxMoney,pos);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(adapter);

        MoveToPosition(new LinearLayoutManager(getActivity()),mRecycleView,pos);*/

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
        mTopMoney.setNumberString("0", formats);
        /*adapter.setOnItemClickListener(new ChartRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setItemClick(position);
                setDateToCharts(position, list);


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });*/

    }

    private void setDateToCharts(int position, ArrayList<YearAndMonthBean> list) {
        //选中对应条目 刷新界面
        String paySpend = "";
        if (TextUtils.equals(type, "1")) {
            //支出总额
            paySpend = ",支出总额";
        } else {
            //收入总额
            paySpend = ",收入总额";
        }
        String s = list.get(position).getmDate();
        double money = list.get(position).getMoney();
        String formats = getEnumToNumer(money);

        //BigDecimal moneys = new BigDecimal( Double.valueOf(money));

        if (TextUtils.equals(flag, "1")) {
            String[] split = s.split("-");
            topTime.setText(split[0] + "月" + split[1] + "日" + paySpend);
        } else {
            topTime.setText(s + paySpend);
        }

        topMoney.setText(formats);
        mTopMoney.setNumberString("0", formats);
        if (flag.equals("1")) {

            int years = DateUtils.getYears();
            thisTime = years + "-" + s;
        } else if (flag.equals("2")) {
            weeks = position + 1;
        } else {
            monthCurrent = position + 1 + "";
        }
        if (TextUtils.equals(type, "1")) {
            getRankingBar();
        } else {
            getRankingBar();//获取支出排行情绪消费统计
            //隐藏 收入情绪消费统计
            lView.setVisibility(View.GONE);
            mTit.setVisibility(View.GONE);
        }
    }

    //设置item的默认选中项  recycle刷新到某一条指定条目
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

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
        lineChart2 = (LineChartInViewPager) contentView.findViewById(R.id.new_lineChart2);
        lineChart = (LineChartInViewPager) contentView.findViewById(R.id.new_lineChart);
        lineChart3 = (LineChartInViewPager) contentView.findViewById(R.id.new_lineChart3);
        lineChartSr1 = (LineChartInViewPager) contentView.findViewById(R.id.sr_lineChart1);
        lineChartSr2 = (LineChartInViewPager) contentView.findViewById(R.id.sr_lineChart2);
        lineChartSr3 = (LineChartInViewPager) contentView.findViewById(R.id.sr_lineChart3);
        //这个地方不去复用的原因是 复用时数据之间会相互影响  单独的数据UI界面之间不会相互影响
        if (TextUtils.equals(flag, "3")) {
            //月
            if (TextUtils.equals(type, "2")) {
                //收入的
                lineChartSr3.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
            } else {
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.VISIBLE);
                lineChartSr3.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
            }
        } else if (TextUtils.equals(flag, "2")) {
            //周
            if (TextUtils.equals(type, "2")) {
                //收入的
                lineChartSr2.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
            } else {
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.VISIBLE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
                lineChartSr1.setVisibility(View.GONE);
            }

        } else if (TextUtils.equals(flag, "1")) {
            //日
            if (TextUtils.equals(type, "2")) {
                //收入的
                lineChartSr1.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
                lineChartSr3.setVisibility(View.GONE);
                lineChartSr2.setVisibility(View.GONE);
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

        if (TextUtils.equals(flag, "3")) {
            if (TextUtils.equals(type, "2")) {
                //收入的
                updateLinehart(yoyList, realList, lineChartSr3, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            } else {
                updateLinehart(yoyList, realList, lineChart3, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            }

        } else if (TextUtils.equals(flag, "2")) {
            if (TextUtils.equals(type, "2")) {
                //收入的
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

        } else if (TextUtils.equals(flag, "1")) {
            if (TextUtils.equals(type, "2")) {
                //收入
                if (first1s2 == 0) {
                    updateLinehart(yoyList, realList, lineChartSr1, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
                }
                first1s2 = first1s2 + 1;
                updateLinehart(yoyList, realList, lineChartSr1, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            } else {
                //支出
                if (first1s == 0) {
                    updateLinehart(yoyList, realList, lineChart, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
                }
                first1s = first1s + 1;
                updateLinehart(yoyList, realList, lineChart, callDurationColors, drawables, "元", values1, values2, labels, pos, list);
            }

        }


    }

    /**
     * 双平滑曲线传入数据，添加markview，添加实体类单位
     *
     * @param yoyList
     * @param realList
     * @param lineChart
     * @param colors
     * @param drawables
     * @param unit
     * @param values2
     * @param values1
     * @param labels
     */
    private void updateLinehart(final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables,
                                final String unit, List<Entry> values2, List<Entry> values1, final String[] labels, int pos, final ArrayList<YearAndMonthBean> list) {
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

        if (TextUtils.equals(flag, "3")) {
           /* lineChart.setDragEnabled(false);//设置是否可拖拽
            //缩放第一种方式
            Matrix matrix = new Matrix();
            //1f代表不缩放
            matrix.postScale(1f, 1f);
            lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
            //重设所有缩放和拖动，使图表完全适合它的边界（完全缩小）。
            lineChart.fitScreen();*/
        } else if (TextUtils.equals(flag, "2")) {
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
        lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);

        lineChartEntity.setAxisFormatter(
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        //设置折线图最底部月份显示
                        if (value == 1.0f) {
                            //return mFormat.format(value) + "月";
                            if (TextUtils.equals(flag, "3")) {
                                return yoyList.get(0).getMonth().split("份")[0];
                            } else if (TextUtils.equals(flag, "2")) {

                                return yoyList.get(0).getMonth();
                            } else if (TextUtils.equals(flag, "1")) {
                                return yoyList.get(0).getMonth();
                            }

                        }
                        String monthStr = mFormat.format(value);
                        int i = Integer.parseInt(monthStr);
                        if (i > yoyList.size()) {
                            return "";
                        }
                        if (TextUtils.equals(flag, "3")) {
                            monthStr = yoyList.get(i - 1).getMonth().split("份")[0];
                        } else {
                            monthStr = yoyList.get(i - 1).getMonth();
                        }
                        if (monthStr.contains(".")) {
                            return "";
                        } else {
                            return monthStr;
                        }
//                        return mMonthFormat.format(value);
                    }
                },
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        //设置折线图最右边的百分比
                        return "";
                    }
                });

        lineChartEntity.setDataValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //设置折线图最右边的百分比
                return "";
            }
        });

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
                    if (TextUtils.equals(flag, "2")) {
                        textTemp += yoyList.get(index - 1).getMonth().split("月份")[0] + "  " + mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                    } else {
                        textTemp += yoyList.get(index - 1).getYear() + "." + yoyList.get(index - 1).getMonth().split("月份")[0] + "  " + mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                    }
                    //textTemp += mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
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
                    playVoice(R.raw.changgui01);
                    setDateToCharts(index - 1, list);
                    aaa = index;
                }

            }
        });

        lineChartEntity.setMarkView(markerView);
        setDateToCharts(pos, list);
        lineChart.getData().setDrawValues(false);

    }

    /**
     * 双平滑曲线添加线下的阴影
     *
     * @param lineChartEntity
     * @param drawables
     * @param colors
     */
    private void toggleFilled(LineChartEntity lineChartEntity, Drawable[] drawables, int[] colors) {
        if (android.os.Build.VERSION.SDK_INT >= 18) {

            lineChartEntity.toggleFilled(drawables, null, true);
        } else {
            lineChartEntity.toggleFilled(null, colors, true);
        }
    }

}
