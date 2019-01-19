package com.hbird.ui.analysis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.chart_3_0_1v.charts.LineChart;
import com.github.mikephil.chart_3_0_1v.components.AxisBase;
import com.github.mikephil.chart_3_0_1v.components.Legend;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.LineDataSet;
import com.github.mikephil.chart_3_0_1v.formatter.IAxisValueFormatter;
import com.github.mikephil.chart_3_0_1v.formatter.IValueFormatter;
import com.github.mikephil.chart_3_0_1v.utils.ViewPortHandler;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.FragAnalysisBinding;
import com.hbird.base.mvc.activity.ActSetBudget;
import com.hbird.base.mvc.activity.CanSettingMoneyActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.ExpendScaleActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RealListEntity;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoneyReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanFinishReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanZbFinishReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.Dialog2ChooseZb;
import com.hbird.base.mvc.widget.MyChart.LineChartEntity;
import com.hbird.base.mvc.widget.MyChart.NewMarkerView;
import com.hbird.base.mvc.widget.MyTimer2Pop;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Util;
import com.hbird.bean.ConsumptionRatioBean;
import com.hbird.bean.SaveMoneyBean;
import com.hbird.common.chart.LineChartInViewPager;
import com.hbird.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseFragment;
import sing.common.util.StringUtils;
import sing.util.LogUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: FragAnalysis
 * @date: 2019/1/16 10:20
 * @Description: 分析
 */
public class FragAnalysis extends BaseFragment<FragAnalysisBinding, AnalysisModle> implements View.OnClickListener {

    RadioGroup mRadio;
    RadioGroup mRadioDown;

    TextView textDes;
    LinearLayout mllViews;
    LinearLayout mViewChosse;
    TextView mTvChooseZb;
    TextView tvYusuanTime;


    private int type = 3;//存钱效率
    private int types = 3;//预算完成率
    private ArrayList<String> dataM;

    private ArrayList<YearAndMonthBean> xiaoLvList;
    private ArrayList<YearAndMonthBean> yuSuanFinishList;

    private ArrayList<SaveMoneyReturn.ResultBean> list = new ArrayList<>();


    private ArrayList<YuSuanFinishReturn.ResultBean> finishList = new ArrayList<>();
    private String token;
    private List<RealListEntity> realList = new ArrayList<>();//此数据集合是用来对比折线用的 暂未用到 为空即可（方便对比两年的数据）
    private List<YoyListEntity> yoyList = new ArrayList<>();
    private List<YoyListEntity> xlList;
    private DecimalFormat mFormat;
    private List<Entry> values1, values2;
    private RealListEntity realListEntity;
    private YoyListEntity yoyListEntity;
    private LineChartInViewPager lineChart;
    private LineChartInViewPager xLineChart; // 存钱效率的
    private boolean xiaoLvs = false;
    private boolean zhipei = false;

    private ConsumptionRatioAdapter adapter;
    private List<ConsumptionRatioBean> ratioBeanList = new ArrayList<>();
    private AnalysisData data;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.frag_analysis;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        data = new AnalysisData();
        binding.setData(data);
        binding.setListener(new OnClick());

        mRadio = binding.radioGroup;
        mRadioDown = binding.radioGroupDown;
        textDes = binding.tvTextDes;
        mllViews = binding.llViewLls;
        mViewChosse = binding.llViewChoose;
        mTvChooseZb = binding.tvChooseZb;
        tvYusuanTime = binding.tvYusuanTime;

        xiaoLvList = new ArrayList<>();
        yuSuanFinishList = new ArrayList<>();

        adapter = new ConsumptionRatioAdapter(getActivity(), ratioBeanList, R.layout.row_consumption_ratio, (position, data, type) -> clickRatio((ConsumptionRatioBean) data));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dataM = new ArrayList<>();
        dataM.clear();
        String temp = "月份";
        for (int i = 1; i < 13; i++) {
            dataM.add(i + temp);
        }

        mTvChooseZb.setOnClickListener(this);
        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_left:
                        Utils.playVoice(getActivity(), R.raw.changgui01);
                        type = 3;
                        getXiaoLvNet(true);
                        //setDates(3);
                        break;
                    case R.id.radio_middle:
                        Utils.playVoice(getActivity(), R.raw.changgui01);
                        type = 6;
                        getXiaoLvNet(true);

                        break;
                    case R.id.radio_right:
                        Utils.playVoice(getActivity(), R.raw.changgui01);
                        type = 12;
                        getXiaoLvNet(true);
                        //setDates(12);
                        break;
                }
            }
        });
        mRadioDown.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                Utils.playVoice(getActivity(), R.raw.changgui01);
                switch (i) {
                    case R.id.radio_d_left:
                        types = 3;
                        break;
                    case R.id.radio_d_middle:
                        types = 6;
                        break;
                    case R.id.radio_d_right:
                        types = 12;
                        break;
                }
                getYuSuanNet3();
            }
        });
    }

    // 点击了消费结构比
    private void clickRatio(ConsumptionRatioBean data) {
        Utils.playVoice(getActivity(), R.raw.changgui02);
        if (data.monthSpend != 0) {// 没有月支出应该不会出现
            double dd = (data.monthSpend - data.foodSpend) / data.monthSpend;
            if (dd > 0) {
                double pp = dd * 100;
                Intent intent = new Intent(getActivity(), ExpendScaleActivity.class);
                intent.putExtra("PRECENT", pp + "");
                intent.putExtra("TIME", data.time);
                startActivity(intent);
            }
        }
    }

    public class OnClick {
        // 选择月份
        public void chooseMonth(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            new MyTimer2Pop(getActivity(), binding.tvMonth, dataM, data.getMm() - 1, s -> {
                data.setMm(Integer.parseInt(s.split("月份")[0]));
                loadDataForNet();
            }, () -> {
            }).showPopWindow();
        }

        // 编辑存钱效率
        public void editor(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent1 = new Intent(getActivity(), CanSettingMoneyActivity.class);
            intent1.putExtra("MONTH", data.getMm() + "");
            intent1.putExtra("YEAR", data.getYyyy() + "");
            String aa = "";
            String bb = "";
            if (null != list && list.size() > 0) {
                aa = list.get(type - 1).getFixedLargeExpenditure() + "";
                bb = list.get(type - 1).getFixedLifeExpenditure() + "";
            }

            intent1.putExtra("LargeExpenditure", aa);
            intent1.putExtra("LifeExpenditure", bb);
            startActivityForResult(intent1, 330);
        }

        // 消费结构比切换个人、全部数据
        public void changeRatio(View view) {
            data.setRatioAll(!data.isRatioAll());
            getXiaoFeiNet2(true);
        }

        // 存钱效率切换个人、全部数据
        public void changeSaveEfficients(View view) {
            data.setSaveEfficientsAll(!data.isSaveEfficientsAll());
            getXiaoLvNet(true);
        }

        // 记一笔
        public void expense(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
        }

        // 设置预算
        public void setBudget(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent3 = new Intent(getActivity(), ActSetBudget.class);
            intent3.putExtra("MONTH", data.getMm() + "");
            intent3.putExtra("YEAR", data.getYyyy() + "");
            String abId = SPUtil.getPrefString(getActivity(), CommonTag.ACCOUNT_BOOK_ID, "");
            intent3.putExtra("accountBookId", abId + "");
            intent3.putExtra("topTime", "- - 年 - - 月 - - 日");
            intent3.putExtra("endTime", "- - 年 - - 月 - - 日");
            startActivityForResult(intent3, 1000);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            binding.top.setFocusable(true);
            binding.top.setFocusableInTouchMode(true);
            binding.top.requestFocus();
            loadDataForNet();
        }
    }

    public void loadDataForNet() {
        //调用获取存钱效率的接口
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");

        String bookName = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT, "默认账本");
        mTvChooseZb.setText(bookName);

        getXiaoLvNet(false);
        //获取消费结构比接口
        getXiaoFeiNet2(false);
        String zbId = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");

        data.setTotalAccount(TextUtils.isEmpty(zbId));// 如果id为空是总账本
        if (!data.isTotalAccount()) {
            getYuSuanNet3();
        }
    }

    // 存钱效率
    private void getXiaoLvNet(boolean showDialog) {
        viewModel.getSaveEfficients(showDialog, token, type, data.getMm(), data.isSaveEfficientsAll() ? -1 : null, new AnalysisModle.EfficientsCallBack() {
            @Override
            public void success(SaveMoneyBean bean) {
                setSaveMoneyDate(bean);
            }
        });
    }

    // 消费结构比
    private void getXiaoFeiNet2(boolean showDialog) {
        viewModel.getConsumptionStructureRatio(showDialog, token, String.valueOf(data.getMm()), data.isRatioAll() ? -1 : null, new AnalysisModle.CallBack() {
            @Override
            public void success(List<ConsumptionRatioBean> ratioList) {
                ratioBeanList.clear();
                if (ratioList != null && ratioList.size() > 0) {
                    data.setShowRatio(true);

                    ratioBeanList.addAll(ratioList);
                    adapter.notifyDataSetChanged();
                } else {
                    data.setShowRatio(false);
                }
            }
        });
    }

    private void getYuSuanNet3() {
        String abId = SPUtil.getPrefString(getActivity(), CommonTag.ACCOUNT_BOOK_ID, "");
        NetWorkManager.getInstance().setContext(getActivity()).getYuSuanFinish(data.getMm() + "", types + "", abId, token, new NetWorkManager.CallBack() {

            @Override
            public void onSuccess(BaseReturn b) {
                initFinishDatas(b);
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }

    private void initFinishDatas(BaseReturn b) {
        data.setShowSetBudget(false);
        mRadioDown.setVisibility(View.GONE);

        if (b instanceof YuSuanFinishReturn) {
            // 日常账本
            YuSuanFinishReturn b1 = (YuSuanFinishReturn) b;
            tvYusuanTime.setVisibility(View.INVISIBLE);

            List<YuSuanFinishReturn.ResultBean> resultF2 = b1.getResult();

            //处理预算完成率数据 resultF2
            finishList.clear();
            if (null == resultF2 || resultF2.size() == 0) {
                textDes.setVisibility(View.VISIBLE);
                //mFlViews.setVisibility(View.GONE);
//                mllViews.setVisibility(View.GONE);
                data.setShowSetBudget(true);
                mRadioDown.setVisibility(View.GONE);
                if (lineChart != null) {
                    lineChart.setVisibility(View.GONE);
                }
                zhipei = true;
            } else {
                data.setShowSetBudget(false);
                if (lineChart != null) {
                    lineChart.setVisibility(View.VISIBLE);
                }
                zhipei = false;
//                mllViews.setVisibility(View.VISIBLE);
                //mFlViews.setVisibility(View.VISIBLE);
                textDes.setVisibility(View.GONE);
                mRadioDown.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < types; i++) {
                int timeNum = data.getMm() - (types - 1) + i;//（开始查询的日期 开始值）
                int index = 0;
                String time = "";
                if (resultF2 != null) {
                    for (int j = 0; j < resultF2.size(); j++) {
                        String times = resultF2.get(j).getTime();
                        String ss = times.substring(times.length() - 2, times.length());
                        int i1 = Integer.parseInt(ss);
                        if (i1 == timeNum) {
                            index = j;
                            time = times;
                        }
                    }
                }
                YuSuanFinishReturn.ResultBean bean = new YuSuanFinishReturn.ResultBean();
                if (!TextUtils.isEmpty(time)) {
                    time = time.replace("-", "/");
                    if (types == 12) {
                        String s = time.substring(time.length() - 2, time.length());
                        int datetime = Integer.parseInt(s);
                        bean.setTime(datetime + "月");
                    } else {
                        bean.setTime(time);
                    }
                    bean.setBudgetMoney(resultF2.get(index).getBudgetMoney());
                    bean.setMonthSpend(resultF2.get(index).getMonthSpend());
                } else {
                    if (timeNum <= 0) {
                        DecimalFormat dfInt = new DecimalFormat("00");
                        String timeNums = dfInt.format(timeNum + 12);
                        if (types == 12) {
                            bean.setTime(timeNum + 12 + "月");//获取去年
                        } else {
                            bean.setTime(data.getYyyy() - 1 + "/" + timeNums);//获取去年
                        }
                        bean.setTag("a");//非手动添加

                    } else {
                        DecimalFormat dfInt = new DecimalFormat("00");
                        String timeNums = dfInt.format(timeNum);
                        if (types == 12) {
                            bean.setTime(timeNum + "月");//获取去年
                        } else {
                            bean.setTime(data.getYyyy() + "/" + timeNums);//获取当年的
                        }
                        bean.setTag("a");//非手动添加
                    }
                }
                finishList.add(bean);
            }

            setToDates(types);
        } else if (b instanceof YuSuanZbFinishReturn) {
            // 场景账本
            YuSuanZbFinishReturn b2 = (YuSuanZbFinishReturn) b;
            tvYusuanTime.setVisibility(View.VISIBLE);
            String startTime = DateUtil.stampToDate(b2.getResults().getSceneBudget().getBeginTime(), "yyyy年MM月dd日");
            String endTime = DateUtil.stampToDate(b2.getResults().getSceneBudget().getEndTime(), "yyyy年MM月dd日");
            tvYusuanTime.setText(startTime + " 至 " + endTime);

            // 没有设置预算
            if (b2.getResults() == null || b2.getResults().getSceneBudget() == null || b2.getResults().getSceneBudget().getBudgetMoney() < 0) {
                data.setShowSetBudget(true);
                zhipei = false;
                if (lineChart != null) {
                    lineChart.setVisibility(View.GONE);
                }
                return;
            } else {
                data.setShowSetBudget(false);
                zhipei = false;
                if (lineChart != null) {
                    lineChart.setVisibility(View.VISIBLE);
                }
            }

            // 没有数据
            if (b2.getResults() == null || b2.getResults().getArrays() == null || b2.getResults().getArrays().size() < 1) {
                if (lineChart != null) lineChart.clearValues();
                if (finishList != null) finishList.clear();
                setToDates(types);
                return;
            }
            // 场景账本的绘图需求：
            // （1）按照3周21天；
            // （2）3周~5个月按照每周；
            // （3）5个月~24个月按照月；
            // （4）24个月以上按照年

            // 相差天数 后台已做判断
//            long dateDiff = DateUtil.getDateDiff(b2.getResults().getSceneBudget().getEndTime(), b2.getResults().getSceneBudget().getBeginTime());
            if (b2.getResults().getArrays() != null && b2.getResults().getArrays().size() > 0) {
                String time = b2.getResults().getArrays().get(0).getTime();
                finishList.clear();
                int size = b2.getResults().getArrays().size();

                switch (time.length()) {
                    case 10: // 日 "time": "2018-12-08"
//                        String start1 = DateUtil.stampToDate(b2.getResults().getSceneBudget().getBeginTime(), "yyyy-MM");
//                        String end1 = DateUtil.stampToDate(b2.getResults().getSceneBudget().getEndTime(), "yyyy-MM");
                        String start2 = b2.getResults().getArrays().get(0).getTime();
                        String end2 = b2.getResults().getArrays().get(size - 1).getTime();
                        List<String> list2 = Util.getDayList(start2, end2);

                        for (int i = 0; i < list2.size(); i++) {
                            YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();

                            List<YuSuanZbFinishReturn.ResultsBean.ArraysBean> l = b2.getResults().getArrays();
                            for (int j = 0; j < l.size(); j++) {
                                temp.setTime(list2.get(i));
                                if (list2.get(i).equals(l.get(j).getTime())) {
                                    temp.setMonthSpend(l.get(j).getMoney());
                                }
                            }
                            if (TextUtils.isEmpty(temp.getTime())) {
                                temp.setMonthSpend(0);
                            }
                            temp.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                            finishList.add(temp);
                        }

                        yuSuanFinishList.clear();
                        for (int i = 0; i < finishList.size(); i++) {
                            YearAndMonthBean bean = new YearAndMonthBean();
                            bean.setmDate((finishList.get(i).getTime() + ""));
                            double xl = 0;
                            if (!TextUtils.isEmpty(finishList.get(i).getTag())) { //说明是自己手动添加的空数据集合
                                bean.setEmptyTag("empty");
                            } else {  //计算过程
                                double budgetMoney = finishList.get(i).getBudgetMoney();
                                double monthSpend = finishList.get(i).getMonthSpend();
                                if (budgetMoney != 0) {
                                    xl = monthSpend / budgetMoney;
                                }
                            }

                            if (i == 0) {
                                bean.setMoney(xl * 100);
                            } else {
                                if (xl * 100 <= 0) {
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size() - 1).getMoney());
                                } else {
                                    bean.setMoney(xl * 100);
                                }
                            }
                            yuSuanFinishList.add(bean);
                        }

                        //曲线图
                        yoyList = new ArrayList<>();
                        yoyList.clear();
                        for (int i = 0; i < yuSuanFinishList.size(); i++) {
                            YoyListEntity yoyListEntity = new YoyListEntity();
                            double money = yuSuanFinishList.get(i).getMoney();
                            String amount = String.valueOf(money);
                            String dates = yuSuanFinishList.get(i).getmDate();

                            yoyListEntity.setAmount(amount);
                            yoyListEntity.setMonth("");
                            yoyListEntity.setYear(dates.replace("-", ".") + " ");
                            yoyList.add(yoyListEntity);
                        }
                        initChart("");
                        break;
                    case 2:// 周 "time": "02"
                        String start3 = (String) b2.getResults().getArrays().get(0).getWeekTime();
                        String end3 = (String) b2.getResults().getArrays().get(size - 1).getWeekTime();
                        List<String> list3 = Util.getWeekList(start3, end3);

                        for (int i = 0; i < list3.size(); i++) {
                            YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();

                            List<YuSuanZbFinishReturn.ResultsBean.ArraysBean> l = b2.getResults().getArrays();
                            for (int j = 0; j < l.size(); j++) {
                                temp.setTime(list3.get(i));
                                if (list3.get(i).equals(l.get(j).getWeekTime())) {
                                    temp.setMonthSpend(l.get(j).getMoney());
                                }
                            }
                            if (TextUtils.isEmpty(temp.getTime())) {
                                temp.setMonthSpend(0);
                            }
                            temp.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                            finishList.add(temp);
                        }

                        yuSuanFinishList.clear();
                        for (int i = 0; i < finishList.size(); i++) {
                            YearAndMonthBean bean = new YearAndMonthBean();
                            bean.setmDate((finishList.get(i).getTime() + ""));
                            double xl = 0;
                            if (!TextUtils.isEmpty(finishList.get(i).getTag())) { //说明是自己手动添加的空数据集合
                                bean.setEmptyTag("empty");
                            } else {  //计算过程
                                double budgetMoney = finishList.get(i).getBudgetMoney();
                                double monthSpend = finishList.get(i).getMonthSpend();
                                if (budgetMoney != 0) {
                                    xl = monthSpend / budgetMoney;
                                }
                            }

                            if (i == 0) {
                                bean.setMoney(xl * 100);
                            } else {
                                if (xl * 100 <= 0) {
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size() - 1).getMoney());
                                } else {
                                    bean.setMoney(xl * 100);
                                }
                            }
                            yuSuanFinishList.add(bean);
                        }

                        //曲线图
                        yoyList = new ArrayList<>();
                        yoyList.clear();
                        for (int i = 0; i < yuSuanFinishList.size(); i++) {
                            YoyListEntity yoyListEntity = new YoyListEntity();
                            double money = yuSuanFinishList.get(i).getMoney();
                            String amount = String.valueOf(money);
                            String dates = yuSuanFinishList.get(i).getmDate();

                            yoyListEntity.setAmount(amount);
                            yoyListEntity.setMonth("");
                            yoyListEntity.setYear("");
                            yoyList.add(yoyListEntity);
                        }
                        initChart("");
                        break;
                    case 7:// 月 "time": "2019-04"
//                        String start1 = DateUtil.stampToDate(b2.getResults().getSceneBudget().getBeginTime(), "yyyy-MM");
//                        String end1 = DateUtil.stampToDate(b2.getResults().getSceneBudget().getEndTime(), "yyyy-MM");
                        String start1 = b2.getResults().getArrays().get(0).getTime();
                        String end1 = b2.getResults().getArrays().get(size - 1).getTime();
                        List<String> list = Util.getMonthList(start1, end1);

                        for (int i = 0; i < list.size(); i++) {
                            YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();

                            List<YuSuanZbFinishReturn.ResultsBean.ArraysBean> l = b2.getResults().getArrays();
                            for (int j = 0; j < l.size(); j++) {
                                temp.setTime(list.get(i).substring(5, list.get(i).length()));
                                if (list.get(i).equals(l.get(j).getTime())) {
                                    temp.setMonthSpend(l.get(j).getMoney());
                                }
                            }
                            if (TextUtils.isEmpty(temp.getTime())) {
                                temp.setMonthSpend(0);
                            }
                            temp.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                            finishList.add(temp);
                        }

                        yuSuanFinishList.clear();
                        for (int i = 0; i < finishList.size(); i++) {
                            YearAndMonthBean bean = new YearAndMonthBean();
                            bean.setmDate((finishList.get(i).getTime() + ""));
                            double xl = 0;
                            if (!TextUtils.isEmpty(finishList.get(i).getTag())) { //说明是自己手动添加的空数据集合
                                bean.setEmptyTag("empty");
                            } else {  //计算过程
                                double budgetMoney = finishList.get(i).getBudgetMoney();
                                double monthSpend = finishList.get(i).getMonthSpend();
                                if (budgetMoney != 0) {
                                    xl = monthSpend / budgetMoney;
                                }
                            }

                            if (i == 0) {
                                bean.setMoney(xl * 100);
                            } else {
                                if (xl * 100 <= 0) {
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size() - 1).getMoney());
                                } else {
                                    bean.setMoney(xl * 100);
                                }
                            }
                            yuSuanFinishList.add(bean);
                        }

                        //曲线图
                        yoyList = new ArrayList<>();
                        yoyList.clear();
                        for (int i = 0; i < yuSuanFinishList.size(); i++) {
                            YoyListEntity yoyListEntity = new YoyListEntity();
                            double money = yuSuanFinishList.get(i).getMoney();
                            String amount = String.valueOf(money);
                            String dates = yuSuanFinishList.get(i).getmDate();

                            yoyListEntity.setAmount(amount);
                            yoyListEntity.setMonth(dates);
                            yoyListEntity.setYear(dates);
                            yoyList.add(yoyListEntity);
                        }
                        initChart("月");
                        break;
                    case 4:// 年  "time": "2019"
                        int start = Integer.parseInt(b2.getResults().getArrays().get(0).getTime());
                        int end = Integer.parseInt(b2.getResults().getArrays().get(size - 1).getTime());
                        for (int i = 0; i < size; i++) {
                            YuSuanZbFinishReturn.ResultsBean.ArraysBean bean = b2.getResults().getArrays().get(i);
                            if (i == 0) {
                                YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();
                                temp.setTime(bean.getTime());
                                temp.setMonthSpend(bean.getMoney());
                                temp.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                                finishList.add(temp);
                            } else {
                                int lastTime = Integer.parseInt(b2.getResults().getArrays().get(i - 1).getTime());
                                if (Integer.parseInt(bean.getTime()) - lastTime > 1) {
                                    while (Integer.parseInt(bean.getTime()) - lastTime != 1) {
                                        YuSuanFinishReturn.ResultBean aa = new YuSuanFinishReturn.ResultBean();
                                        aa.setTime((lastTime + 1) + "");
                                        aa.setMonthSpend(0);
                                        aa.setTag("a");

                                        aa.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                                        finishList.add(aa);
                                        lastTime++;
                                    }
                                    // 把当前循环的加上
                                    YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();
                                    temp.setTime(bean.getTime());
                                    temp.setMonthSpend(bean.getMoney());
                                    temp.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                                    finishList.add(temp);
                                } else {
                                    YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();
                                    temp.setTime(bean.getTime());
                                    temp.setMonthSpend(bean.getMoney());
                                    temp.setBudgetMoney(b2.getResults().getSceneBudget().getBudgetMoney());
                                    finishList.add(temp);
                                }
                            }
                        }

                        yuSuanFinishList.clear();
                        for (int i = 0; i < finishList.size(); i++) {
                            YearAndMonthBean bean = new YearAndMonthBean();
                            bean.setmDate((finishList.get(i).getTime() + ""));
                            double xl = 0;
                            if (!TextUtils.isEmpty(finishList.get(i).getTag())) { //说明是自己手动添加的空数据集合
                                bean.setEmptyTag("empty");
                            } else {  //计算过程
                                double budgetMoney = finishList.get(i).getBudgetMoney();
                                double monthSpend = finishList.get(i).getMonthSpend();
                                if (budgetMoney != 0) {
                                    xl = monthSpend / budgetMoney;
                                }
                            }

                            if (i == 0) {
                                bean.setMoney(xl * 100);
                            } else {
                                if (xl * 100 <= 0) {
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size() - 1).getMoney());
                                } else {
                                    bean.setMoney(xl * 100);
                                }
                            }
                            yuSuanFinishList.add(bean);
                        }

                        //曲线图
                        yoyList = new ArrayList<>();
                        yoyList.clear();
                        for (int i = 0; i < yuSuanFinishList.size(); i++) {
                            YoyListEntity yoyListEntity = new YoyListEntity();
                            double money = yuSuanFinishList.get(i).getMoney();
                            String amount = String.valueOf(money);
                            String dates = yuSuanFinishList.get(i).getmDate();

                            yoyListEntity.setAmount(amount);
                            yoyListEntity.setMonth(dates);
                            yoyListEntity.setYear(dates);
                            yoyList.add(yoyListEntity);
                        }
                        initChart("年");
                        break;
                }
            }
        }
    }

    private void setSaveMoneyDate(SaveMoneyBean temp) {
        if (temp == null) {// 未设置可支配金额
            return;
        }
        if (temp.arrays == null || temp.arrays.size() < 1) {
            data.setShowEdit(false);

            mViewChosse.setVisibility(View.GONE);
            xiaoLvs = true;
        } else {
            data.setShowEdit(true);

            mViewChosse.setVisibility(View.VISIBLE);
            xiaoLvs = false;
        }

        list.clear();

        //如果 返回对应月数集合数 不对 则手动处理数据
        for (int i = 0; i < type; i++) {
            int timeNum = data.getMm() - (type - 1) + i;//（开始查询的日期 开始值）
            int index = 0;
            String time = "";
            for (int j = 0; j < temp.arrays.size(); j++) {
                String times = temp.arrays.get(j).time;
                if (TextUtils.isEmpty(times) || times == null) {
                    break;
                }
                String ss = times.substring(times.length() - 2, times.length());
                int i1 = Integer.parseInt(ss);
                if (i1 == timeNum) {
                    index = j;
                    time = times;
                }
            }

            SaveMoneyReturn.ResultBean bean = new SaveMoneyReturn.ResultBean();
            if (!TextUtils.isEmpty(time)) {
                time = time.replace("-", "/");
                if (type == 12) {
                    String s = time.substring(time.length() - 2, time.length());
                    int datetime = Integer.parseInt(s);
                    bean.setTime(datetime + "月");
                } else {
                    bean.setTime(time);
                }
                bean.setFixedLargeExpenditure(temp.fixedSpend.fixedLargeExpenditure);
                bean.setFixedLifeExpenditure(temp.fixedSpend.fixedLifeExpenditure);
                bean.setMonthIncome(temp.arrays.get(index).monthIncome);
                bean.setMonthSpend(temp.arrays.get(index).monthSpend);
            } else {
                if (timeNum <= 0) {
                    DecimalFormat dfInt = new DecimalFormat("00");
                    String timeNums = dfInt.format(timeNum + 12);
                    if (type == 12) {
                        bean.setTime(timeNum + 12 + "月");//获取去年
                    } else {
                        bean.setTime(data.getYyyy() - 1 + "/" + timeNums);//获取去年
                    }
                    bean.setTags("a");//非手动添加
                } else {
                    DecimalFormat dfInt = new DecimalFormat("00");
                    String timeNums = dfInt.format(timeNum);
                    if (type == 12) {
                        bean.setTime(timeNum + "月");//获取去年
                    } else {
                        bean.setTime(data.getYyyy() + "/" + timeNums);//获取当年的
                    }
                    bean.setTags("a");//非手动添加
                }
            }
            list.add(bean);
        }
        setDates(type);
    }

    private void setDates(int num) {
        xiaoLvList.clear();
        //三个月的实际值
        for (int i = 0; i < num; i++) {
            YearAndMonthBean bean = new YearAndMonthBean();
            bean.setmDate((list.get(i).getTime() + ""));
            double xl = 0;
            if (!TextUtils.isEmpty(list.get(i).getTags())) {
                //说明是自己手动添加的空数据集合
                bean.setEmptyTag("empty");
            } else {
                //计算过程
                Double monthIncome1 = list.get(i).getMonthIncome();

                Double monthIncome = list.get(i).getMonthIncome();
                Double monthSpend = list.get(i).getMonthSpend();

                Double fixedLargeExpenditure = list.get(i).getFixedLargeExpenditure();
                Double fixedLifeExpenditure = list.get(i).getFixedLifeExpenditure();
                if (monthIncome == null) {
                    monthIncome = 0d;
                }
                if (monthSpend == null) {
                    monthSpend = 0d;
                }
                if (fixedLargeExpenditure == null) {
                    fixedLargeExpenditure = 0d;
                }
                if (fixedLifeExpenditure == null) {
                    fixedLifeExpenditure = 0d;
                }
                Double jyNum = monthIncome - monthSpend;//当月结余
                Double zcNum = monthSpend - (fixedLargeExpenditure + fixedLifeExpenditure);//(当月总支出 - 当月固定支出)
                if (zcNum != 0) {
                    xl = jyNum / zcNum;
                }
            }

            bean.setMoney(xl * 100);
            xiaoLvList.add(bean);
        }

        //如果type=12  调整分割虚线
        boolean flages = true;
        if (xiaoLvList != null && xiaoLvList.size() > 0) {
            for (int i = 0; i < xiaoLvList.size(); i++) {
                double money1 = xiaoLvList.get(i).getMoney();
                if (money1 < 0) {
                    flages = false;
                }
            }
        }

        //曲线图
        xlList = new ArrayList<>();
        xlList.clear();
        for (int i = 0; i < xiaoLvList.size(); i++) {
            YoyListEntity yoyListEntity = new YoyListEntity();
            double money = xiaoLvList.get(i).getMoney();
            String amount = String.valueOf(money);
            String dates = xiaoLvList.get(i).getmDate();
            String month = "";
            String year = "";
            if (type == 12) {
                month = dates;
                int years = DateUtils.getYears();
                year = years + "";
            } else {
                String[] split = dates.split("/");
                month = split[1];
                year = split[0];
            }

            yoyListEntity.setAmount(amount);
            yoyListEntity.setMonth(month);
            yoyListEntity.setYear(year);
            xlList.add(yoyListEntity);
        }
        initChartToXiaolv();
    }

    private void setToDates(int num) {
        LogUtil.e("setToDates()");
        yuSuanFinishList.clear();

        for (int i = 0; i < num; i++) {
            if (finishList.size() < num) {
                return;
            }
            YearAndMonthBean bean = new YearAndMonthBean();
            bean.setmDate((finishList.get(i).getTime() + ""));
            double xl = 0;
            if (!TextUtils.isEmpty(finishList.get(i).getTag())) {
                //说明是自己手动添加的空数据集合
                bean.setEmptyTag("empty");
            } else {
                //计算过程
                double budgetMoney = finishList.get(i).getBudgetMoney();
                double monthSpend = finishList.get(i).getMonthSpend();
                if (budgetMoney != 0) {
                    xl = monthSpend / budgetMoney;
                }
            }

            bean.setMoney(xl * 100);
            yuSuanFinishList.add(bean);
        }

        //添加数据
        yoyList = new ArrayList<>();
        yoyList.clear();
        for (int i = 0; i < yuSuanFinishList.size(); i++) {
            YoyListEntity yoyListEntity = new YoyListEntity();
            double money = yuSuanFinishList.get(i).getMoney();
            String amount = String.valueOf(money);
            String dates = yuSuanFinishList.get(i).getmDate();
            String month = "";
            String year = "";
            if (types == 12) {
                month = dates;
                int years = DateUtils.getYears();
                year = years + "";
            } else {
                String[] split = dates.split("/");
                month = split[1];
                year = split[0];
            }

            yoyListEntity.setAmount(amount);
            yoyListEntity.setMonth(month);
            yoyListEntity.setYear(year + " ");
            yoyList.add(yoyListEntity);
        }
        initChart("月");
    }

    private void initChart(String unit) {
        LogUtil.e("initChart()");
        mFormat = new DecimalFormat("#,###.##");
        lineChart = binding.newLineChart;
        if (zhipei) {
            lineChart.setVisibility(View.GONE);
        } else {
            lineChart.setVisibility(View.VISIBLE);
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
            lastYear = yoyList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};

        updateLinehart(yoyList, realList, lineChart, callDurationColors, drawables, "%", values1, values2, labels, unit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose_zb:
                //showMessage("请选择账本");
                NetWorkManager.getInstance().setContext(getActivity())
                        .getMyAccounts(token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                AccountZbBean b1 = (AccountZbBean) b;
                                final List<AccountZbBean.ResultBean> listZb = b1.getResult();
                                new Dialog2ChooseZb(getActivity(), listZb, new Dialog2ChooseZb.onClickListener() {
                                    @Override
                                    public void onclicks(int i) {
                                        String s = listZb.get(i).getAbName();
                                        int typeBudget = listZb.get(i).getTypeBudget();
                                        int abTypeId = listZb.get(i).getAbTypeId();
                                        mTvChooseZb.setText(s);

                                        SPUtil.setPrefString(getActivity(), CommonTag.ACCOUNT_BOOK_ID, listZb.get(i).getId() + "");
                                        SPUtil.setPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT, s);
                                        SPUtil.setPrefInt(getActivity(), CommonTag.ACCOUNT_AB_TYPEID, listZb.get(i).getAbTypeId());
                                        SPUtil.setPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, abTypeId + "");
                                        SPUtil.setPrefString(getActivity(), CommonTag.INDEX_TYPE_BUDGET, typeBudget + "");

                                        //请求预算完成率接口
                                        getYuSuanNet3();
                                    }
                                }).builder().show();
                            }

                            @Override
                            public void onError(String s) {
                                ToastUtil.showShort(s);
                            }
                        });
                break;
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
    private void updateLinehart(final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables, final String unit, List<Entry> values2, List<Entry> values1, final String[] labels, final String units) {
        List<Entry>[] entries = new ArrayList[2];
        entries[0] = values1;
        entries[1] = values2;
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors, Color.parseColor("#999999"), 12f);
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);

        lineChart.setDragEnabled(false);//设置是否可拖拽
        lineChart.setScaleEnabled(false);//设置可缩放
        lineChart.setTouchEnabled(true); //可点击
        //lineChart.setPinchZoom(false);
        lineChart.setBackgroundColor(Color.WHITE); //设置背景颜色
        //移到某个位置
//        lineChart.moveViewToX(yoyList.size());
        //缩放第一种方式
        Matrix matrix = new Matrix();
        //1f代表不缩放
        matrix.postScale(1f, 1f);
        lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
        //重设所有缩放和拖动，使图表完全适合它的边界（完全缩小）。
        lineChart.fitScreen();
        //设置曲线图标 没有数据的时候 原点不显示 死了一万个脑细胞 mmp 此刻万圣节记录一下这鬼心情
        //lineChart.setRenderer(new EmptyLineChartRendererNew(lineChart));
        /**
         * 这里切换平滑曲线或者折现图
         */
//        lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER); LINEAR, STEPPED,
        lineChartEntity.setLineMode(LineDataSet.Mode.LINEAR);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        lineChartEntity.setAxisFormatter(new IAxisValueFormatter() {
                                             @Override
                                             public String getFormattedValue(float value, AxisBase axis) {
                                                 //设置折线图最底部月份显示
                                                 if (value == 1.0f) {
                                                     //return mFormat.format(value) + "月";
                                                     if (types == 12) {
                                                         return yoyList.get(0).getMonth();
                                                     } else {
                                                         return yoyList.get(0).getMonth() + units;
                                                     }

                                                 }
                                                 //String monthStr = mFormat.format(value);
                                                 String monthStr = mFormat.format(value);
                                                 int i = Integer.parseInt(monthStr);
                                                 if (i > yoyList.size()) {
                                                     return "";
                                                 }
                                                 if (types == 12) {
                                                     monthStr = yoyList.get(i - 1).getMonth();
                                                 } else {
                                                     monthStr = yoyList.get(i - 1).getMonth() + units;
                                                 }
                                                 if (monthStr.contains(".")) {
                                                     return "";
                                                 } else {
                                                     return monthStr;
                                                 }
                                             }
                                         },
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        //设置折线图最右边的百分比
                        return mFormat.format(value) + unit;
                    }
                });

        lineChartEntity.setDataValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //设置折线图最右边的百分比
                return mFormat.format(value) + unit;
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
                    //textTemp += yoyList.get(index - 1).getYear() + "." + yoyList.get(index - 1).getMonth() + "  " + mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                    textTemp += mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                }

                if (index <= realList.size()) {
                    textTemp += "\n";
                    textTemp += realList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(realList.get(index - 1).getAmount())) + unit;
                }
                markerView.getTvContent().setText(yoyList.get(index - 1).getYear() + textTemp);
            }
        });
        lineChartEntity.setMarkView(markerView);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 330 && resultCode == 331) {
            //执行刷新操作
            loadDataForNet();
        } else if (requestCode == 1000 && resultCode == 104) {
            getYuSuanNet3();
        }
    }

    private void initChartToXiaolv() {
        mFormat = new DecimalFormat("#,###.##");
        xLineChart = binding.xlLineChart;
        if (xiaoLvs) {
            xLineChart.setVisibility(View.GONE);
        } else {
            xLineChart.setVisibility(View.VISIBLE);
        }

        values1 = new ArrayList<>();
        values2 = new ArrayList<>();
        for (int i = 0; i < xlList.size(); i++) {
            yoyListEntity = xlList.get(i);
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
        if (xlList.size() > 0) {
            lastYear = xlList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};

        updateLinehartTo(xlList, realList, xLineChart, callDurationColors, drawables, "%", values1, values2, labels);
    }

    /**
     * 双平滑曲线传入数据，添加markview，添加实体类单位
     */
    private void updateLinehartTo(final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables, final String unit, List<Entry> values2, List<Entry> values1, final String[] labels) {
        List<Entry>[] entries = new ArrayList[2];
        entries[0] = values1;
        entries[1] = values2;
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors, Color.parseColor("#999999"), 12f);
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);

        lineChart.setDragEnabled(false);//设置是否可拖拽
        lineChart.setScaleEnabled(false);//设置可缩放
        lineChart.setTouchEnabled(true); //可点击
        //lineChart.setPinchZoom(false);
        lineChart.setBackgroundColor(Color.WHITE); //设置背景颜色
        //移到某个位置
//        lineChart.moveViewToX(yoyList.size());
        //缩放第一种方式
        Matrix matrix = new Matrix();
        //1f代表不缩放
        matrix.postScale(1f, 1f);
        lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
        //重设所有缩放和拖动，使图表完全适合它的边界（完全缩小）。
        lineChart.fitScreen();
        // 这里切换折现图
        lineChartEntity.setLineMode(LineDataSet.Mode.LINEAR);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        lineChartEntity.setAxisFormatter((value, axis) -> {
                    //设置折线图最底部月份显示
                    if (value == 1.0f) {
                        if (type == 12) {
                            return yoyList.get(0).getMonth();
                        } else {
                            return yoyList.get(0).getMonth() + "月";
                        }
                    }
                    String monthStr = mFormat.format(value);
                    int i = Integer.parseInt(monthStr);
                    if (i > yoyList.size()) {
                        return "";
                    }
                    if (type == 12) {
                        monthStr = yoyList.get(i - 1).getMonth();
                    } else {
                        monthStr = yoyList.get(i - 1).getMonth() + "月";
                    }
                    if (monthStr.contains(".")) {
                        return "";
                    } else {
                        return monthStr;
                    }
                },
                //设置折线图最右边的百分比
                (value, axis) -> mFormat.format(value) + unit);

        //设置折线图最右边的百分比
        lineChartEntity.setDataValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> mFormat.format(value) + unit);

        final NewMarkerView markerView = new NewMarkerView(getActivity(), R.layout.custom_marker_view_layout);
        markerView.setCallBack((x, value) -> {
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
                textTemp += mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
            }

            if (index <= realList.size()) {
                textTemp += "\n";
                textTemp += realList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(realList.get(index - 1).getAmount())) + unit;
            }
            markerView.getTvContent().setText(textTemp);
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
    }
}
