package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.hbird.base.mvc.activity.ActSetBudget;
import com.hbird.base.mvc.activity.CanSettingMoneyActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.ExpendScaleActivity;
import com.hbird.base.mvc.adapter.XiaoFeiBiAdapter;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RealListEntity;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoney2Return;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoneyReturn;
import com.hbird.base.mvc.bean.ReturnBean.XiaoFeiBiLiReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanFinishReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanZbFinishReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.Dialog2ChooseZb;
import com.hbird.base.mvc.widget.MyChart.LineChartEntity;
import com.hbird.base.mvc.widget.MyChart.LineChartInViewPager;
import com.hbird.base.mvc.widget.MyChart.NewMarkerView;
import com.hbird.base.mvc.widget.MyListViews;
import com.hbird.base.mvc.widget.MyTimer2Pop;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.StringUtils;
import com.hbird.base.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import sing.util.LogUtil;

import static com.hbird.base.R.id.ll_zhangben_choose;
import static com.hbird.base.R.id.new_lineChart;

/**
 * Created by Liul on 2018/8/8.
 * 分析界面
 */

public class FenXiFragement extends BaseFragement implements View.OnClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_editors)
    TextView mEditors;
    @BindView(R.id.ll_expense_content)
    LinearLayout mExpenseContent;
    @BindView(R.id.rl_expense_btn)
    RelativeLayout mExpenseBtn;
    @BindView(R.id.iv_fx_fg_down)
    ImageView mFxFgDown;
    @BindView(R.id.ll_viewgroup)
    LinearLayout mViewGroupLine;
    @BindView(R.id.radioGroup)
    RadioGroup mRadio;
    @BindView(R.id.radioGroup_down)
    RadioGroup mRadioDown;

    TextView tvMonth;// 顶部选择的年月
    @BindView(R.id.ll_center_fg)
    LinearLayout mCenterFg;
    @BindView(R.id.listview)
    MyListViews lv;
    @BindView(R.id.tv_text_des)
    TextView textDes;
    //    @BindView(R.id.fl_views)
//    FrameLayout mFlViews;
    @BindView(R.id.ll_view_lls)
    LinearLayout mllViews;
    @BindView(R.id.rl_setting_ys)
    RelativeLayout mSettingYs;
    @BindView(R.id.ll_view_kzp)
    LinearLayout mViewKzp;
    @BindView(R.id.ll_view_choose)
    LinearLayout mViewChosse;
    @BindView(R.id.ll_view_contents)
    LinearLayout mViewContents;
    @BindView(R.id.rl_setting_kzp)
    RelativeLayout mSettingKzp;
    @BindView(R.id.ll_center_fgsss)
    LinearLayout mCenterFgsss;
    @BindView(ll_zhangben_choose)
    LinearLayout mChooseZb;
    @BindView(R.id.tv_choose_zb)
    TextView mTvChooseZb;
    @BindView(R.id.ll_ys_finish)
    LinearLayout mYsFinish;
    @BindView(R.id.tv_yusuan_hint)
    TextView tvYusuanHint;
    @BindView(R.id.tv_yusuan_time)
    TextView tvYusuanTime;


    private double maxNum = 100d;
    private int type = 3;//存钱效率
    private int types = 3;//预算完成率
    private ArrayList<String> dataM;

    private ArrayList<YearAndMonthBean> xiaoLvList;
    private ArrayList<YearAndMonthBean> yuSuanFinishList;
    private int mm = 1;//默认
    private int yyyy;
    private ArrayList<SaveMoneyReturn.ResultBean> list;
    private XiaoFeiBiAdapter listAdapter;
    private List<XiaoFeiBiLiReturn.ResultBean> dataBiLi;

    private ArrayList<YuSuanFinishReturn.ResultBean> finishList = new ArrayList<>();
    private String token;
    private List<SaveMoney2Return.ResultBean.ArraysBean> result = new ArrayList<>();
    private int firstCome = 0;
    private List<RealListEntity> realList = new ArrayList<>();//此数据集合是用来对比折线用的 暂未用到 为空即可（方便对比两年的数据）
    private List<YoyListEntity> yoyList = new ArrayList<>();
    private List<YoyListEntity> xlList;
    private DecimalFormat mFormat;
    private List<Entry> values1, values2;
    private RealListEntity realListEntity;
    private YoyListEntity yoyListEntity;
    private LineChartInViewPager lineChart;
    private LineChartInViewPager xLineChart;
    private boolean xiaoLvs = false;
    private boolean zhipei = false;

    @Override
    public int setContentId() {
        return R.layout.fragement_fenxi;
    }

    @Override
    public void initView() {
        tvMonth = getActivity().findViewById(R.id.tv_month);
        //当前年
        yyyy = Integer.parseInt(DateUtils.getCurYear("yyyy"));
        //当前月
        String m = DateUtils.date2Str(new Date(), "MM");
        String currentMonth = m.substring(0, 2);
        mm = Integer.parseInt(currentMonth);
        tvMonth.setText(yyyy + "年" + currentMonth + "月");
        xiaoLvList = new ArrayList<>();
        yuSuanFinishList = new ArrayList<>();

        listAdapter = new XiaoFeiBiAdapter(getActivity());
        lv.setAdapter(listAdapter);
    }

    @Override
    public void initData() {
        setInitData();
    }

    private void setInitData() {
        LogUtil.e("setInitData()");
        dataM = new ArrayList<>();
        dataM.clear();
        String temp = "月份";
        for (int i = 1; i < 13; i++) {
            dataM.add(i + temp);
        }
    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();
        LogUtil.e("loadDataForNet()");
        firstCome = firstCome + 1;
        String bookName = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT, "默认账本");
        mTvChooseZb.setText(bookName);

        //调用获取存钱效率的接口
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        getXiaoLvNet(token);
        //获取消费结构比接口
        getXiaoFeiNet2(token);
        String zbId = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
        if (TextUtils.isEmpty(zbId)) {
            //总账本
            mYsFinish.setVisibility(View.GONE);
        } else {
            //获取预算完成率
            mYsFinish.setVisibility(View.VISIBLE);
            getYuSuanNet3(token);
        }
    }

    @Override
    public void loadDate() {
        super.loadDate();
        LogUtil.e("loadDate()");
        if (firstCome > 0) {
            loadDataForNet();
        }
    }

    private void getXiaoLvNet(String token) {
        LogUtil.e("getXiaoLvNet()");
        NetWorkManager.getInstance().setContext(getActivity())
                .getSaveEfficients(mm, type, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        SaveMoney2Return b1 = (SaveMoney2Return) b;
                        setSaveMoneyDate(b1);

                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void getXiaoFeiNet2(String token) {
        LogUtil.e("getXiaoFeiNet2()");
        NetWorkManager.getInstance().setContext(getActivity())
                .getXiaoFeiBiLi(mm + "", token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        XiaoFeiBiLiReturn b1 = (XiaoFeiBiLiReturn) b;
                        //计算比例为 食物支出/总支出
                        if (dataBiLi != null) {
                            dataBiLi.clear();
                        }
                        dataBiLi = b1.getResult();
                        if (null != dataBiLi && dataBiLi.size() > 0) {
                            lv.setVisibility(View.VISIBLE);
                            lv.setFocusable(false);
                            listAdapter.setDatas(dataBiLi);
                        } else {
                            //隐藏大图片  显示记一笔快捷入口
                            mExpenseContent.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void getYuSuanNet3(String token) {
        LogUtil.e("getXiaoFeiNet3()");
        String abId = SPUtil.getPrefString(getActivity(), CommonTag.ACCOUNT_BOOK_ID, "");
        NetWorkManager.getInstance().setContext(getActivity())
                .getYuSuanFinish(mm + "", types + "", abId, token, new NetWorkManager.CallBack() {

                    @Override
                    public void onSuccess(BaseReturn b) {
                        initFinishDatas(b);
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void initFinishDatas(BaseReturn b) {
        LogUtil.e("initFinishDatas()");
        mRadioDown.setVisibility(View.GONE);

        mSettingYs.setVisibility(View.GONE);

        if (b instanceof YuSuanFinishReturn) {
            // 日常账本
            YuSuanFinishReturn b1 = (YuSuanFinishReturn) b;
            tvYusuanHint.setText("(每月实际支出占预算的比例)");
            tvYusuanTime.setVisibility(View.INVISIBLE);

            List<YuSuanFinishReturn.ResultBean> resultF2 = b1.getResult();

            //处理预算完成率数据 resultF2
            finishList.clear();
            if (null == resultF2 || resultF2.size() == 0) {
                textDes.setVisibility(View.VISIBLE);
                //mFlViews.setVisibility(View.GONE);
//                mllViews.setVisibility(View.GONE);
                mSettingYs.setVisibility(View.VISIBLE);
                mRadioDown.setVisibility(View.GONE);
                if (lineChart != null) {
                    lineChart.setVisibility(View.GONE);
                }
                zhipei = true;
            } else {
                mSettingYs.setVisibility(View.GONE);
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
                int timeNum = mm - (types - 1) + i;//（开始查询的日期 开始值）
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
                            bean.setTime(yyyy - 1 + "/" + timeNums);//获取去年
                        }
                        bean.setTag("a");//非手动添加

                    } else {
                        DecimalFormat dfInt = new DecimalFormat("00");
                        String timeNums = dfInt.format(timeNum);
                        if (types == 12) {
                            bean.setTime(timeNum + "月");//获取去年
                        } else {
                            bean.setTime(yyyy + "/" + timeNums);//获取当年的
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
            tvYusuanHint.setText("(实际支出占预算的比例)");
            tvYusuanTime.setVisibility(View.VISIBLE);
            String startTime = DateUtil.stampToDate(b2.getResults().getSceneBudget().getBeginTime(), "yyyy年MM月dd日");
            String endTime = DateUtil.stampToDate(b2.getResults().getSceneBudget().getEndTime(), "yyyy年MM月dd日");
            tvYusuanTime.setText(startTime + " 至 " + endTime);

            // 没有设置预算
            if (b2.getResults() == null || b2.getResults().getSceneBudget() == null || b2.getResults().getSceneBudget().getBudgetMoney() < 0) {
                mSettingYs.setVisibility(View.VISIBLE);
                zhipei = false;
                if (lineChart != null) {
                    lineChart.setVisibility(View.GONE);
                }
                return;
            } else {
                mSettingYs.setVisibility(View.GONE);
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

                            if (i == 0){
                                bean.setMoney(xl * 100);
                            }else{
                                if (xl *100<=0){
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size()-1).getMoney());
                                }else {
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

                            if (i == 0){
                                bean.setMoney(xl * 100);
                            }else{
                                if (xl *100<=0){
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size()-1).getMoney());
                                }else {
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

                            if (i == 0){
                                bean.setMoney(xl * 100);
                            }else{
                                if (xl *100<=0){
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size()-1).getMoney());
                                }else {
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

                            if (i == 0){
                                bean.setMoney(xl * 100);
                            }else{
                                if (xl *100<=0){
                                    bean.setMoney(yuSuanFinishList.get(yuSuanFinishList.size()-1).getMoney());
                                }else {
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

    private void setSaveMoneyDate(SaveMoney2Return b1) {
        LogUtil.e("setSaveMoneyDate()");
        if (result != null) {
            result.clear();
        }
        if (b1.getResult() == null) {
            return;
        }
        result = b1.getResult().getArrays();
        list = new ArrayList<>();
        list.clear();
        if (this.result == null || this.result.size() == 0) {
            mViewKzp.setVisibility(View.VISIBLE);
            mViewChosse.setVisibility(View.GONE);
            xiaoLvs = true;
            mViewContents.setVisibility(View.GONE);
            mEditors.setVisibility(View.GONE);
        } else {
            mViewChosse.setVisibility(View.VISIBLE);
            xiaoLvs = false;
            mViewKzp.setVisibility(View.GONE);
            mViewContents.setVisibility(View.VISIBLE);
            mEditors.setVisibility(View.VISIBLE);
        }
        //如果 返回对应月数集合数 不对 则手动处理数据
        for (int i = 0; i < type; i++) {
            int timeNum = mm - (type - 1) + i;//（开始查询的日期 开始值）
            int index = 0;
            String time = "";
            for (int j = 0; j < this.result.size(); j++) {
                String times = this.result.get(j).getTime();
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
                //bean.setBudgetMoney(this.result.get(index).getBudgetMoney());
                bean.setFixedLargeExpenditure(b1.getResult().getFixedSpend().getFixedLargeExpenditure());
                bean.setFixedLifeExpenditure(b1.getResult().getFixedSpend().getFixedLifeExpenditure());
                bean.setMonthIncome(result.get(index).getMonthIncome());
                bean.setMonthSpend(result.get(index).getMonthSpend());

            } else {
                if (timeNum <= 0) {
                    DecimalFormat dfInt = new DecimalFormat("00");
                    String timeNums = dfInt.format(timeNum + 12);
                    if (type == 12) {
                        bean.setTime(timeNum + 12 + "月");//获取去年
                    } else {
                        bean.setTime(yyyy - 1 + "/" + timeNums);//获取去年
                    }
                    bean.setTags("a");//非手动添加

                } else {
                    DecimalFormat dfInt = new DecimalFormat("00");
                    String timeNums = dfInt.format(timeNum);
                    if (type == 12) {
                        bean.setTime(timeNum + "月");//获取去年
                    } else {
                        bean.setTime(yyyy + "/" + timeNums);//获取当年的
                    }
                    bean.setTags("a");//非手动添加
                }
            }
            list.add(bean);
        }
        setDates(type);
    }

    @Override
    public void initListener() {
        mEditors.setOnClickListener(this);
        mExpenseBtn.setOnClickListener(this);
        tvMonth.setOnClickListener(this);
        mSettingKzp.setOnClickListener(this);
        mSettingYs.setOnClickListener(this);
        mChooseZb.setOnClickListener(this);
        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_left:
                        playVoice(R.raw.changgui01);
                        type = 3;
                        getXiaoLvNet(token);
                        //setDates(3);
                        break;
                    case R.id.radio_middle:
                        playVoice(R.raw.changgui01);
                        type = 6;
                        getXiaoLvNet(token);

                        break;
                    case R.id.radio_right:
                        playVoice(R.raw.changgui01);
                        type = 12;
                        getXiaoLvNet(token);
                        //setDates(12);
                        break;
                }
            }
        });
        mRadioDown.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_d_left:
                        playVoice(R.raw.changgui01);
                        types = 3;
                        getYuSuanNet3(token);
                        //setToDates(3);
                        break;
                    case R.id.radio_d_middle:
                        playVoice(R.raw.changgui01);
                        types = 6;
                        getYuSuanNet3(token);
                        //setToDates(6);
                        break;
                    case R.id.radio_d_right:
                        playVoice(R.raw.changgui01);
                        types = 12;
                        getYuSuanNet3(token);
                        //setToDates(12);
                        break;
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playVoice(R.raw.changgui02);
                Double foodSpend = dataBiLi.get(i).getFoodSpend();
                Double monthSpend = dataBiLi.get(i).getMonthSpend();
                String time = dataBiLi.get(i).getTime();
                double dd = 0;
                if (monthSpend != null && monthSpend != 0) {// 没有月支出应该不会出现
                    if (foodSpend == null) {
                        foodSpend = 0.0;
                    }
                    dd = (monthSpend - foodSpend) / monthSpend;
                    double pp = dd * 100;
                    if (dd > 0) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ExpendScaleActivity.class);
                        intent.putExtra("PRECENT", pp + "");
                        intent.putExtra("TIME", time);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void setDates(int num) {
        LogUtil.e("setDates()");
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

       /* for(int i=0;i<num;i++){
            YearAndMonthBean bean = new YearAndMonthBean();
            bean.setmDate((i+1)+"月");
            if(i==6 || i==9 ||i==1 ||i==11){
                bean.setMoney(-50);
            }else {
                bean.setMoney(20+4*i);
                //bean.setMoney(50);
            }
            xiaoLvList.add(bean);
        }*/
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

       /* if(type==12){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mFxFgDown.getLayoutParams();
            int margins = (int) getActivity().getResources().getDimension(R.dimen.height_0_80);
            lp.setMargins(0,0,0,margins);
            mFxFgDown.setLayoutParams(lp);
            FrameLayout.LayoutParams lps =(FrameLayout.LayoutParams) mViewGroupLine.getLayoutParams();
            lps.setMargins(0,0,0,0);
            mViewGroupLine.setLayoutParams(lps);
            if(flages){
                //表示没有负值
                mFxFgDown.setVisibility(View.GONE);
                mCenterFg.setVisibility(View.GONE);
                mCenterFgsss.setVisibility(View.VISIBLE);
            }else {
                mFxFgDown.setVisibility(View.VISIBLE);
                mCenterFg.setVisibility(View.VISIBLE);
                mCenterFgsss.setVisibility(View.GONE);
                FrameLayout.LayoutParams lpes = (FrameLayout.LayoutParams)mCenterFg.getLayoutParams();
                int margines = (int) getActivity().getResources().getDimension(R.dimen.height_3_80);
                lpes.setMargins(0,margines,0,0);
                mCenterFg.setLayoutParams(lpes);
            }

        }else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mFxFgDown.getLayoutParams();
            int margins = (int) getActivity().getResources().getDimension(R.dimen.height_0_80);
            lp.setMargins(0,0,0,margins);
            mFxFgDown.setLayoutParams(lp);
            FrameLayout.LayoutParams lps =(FrameLayout.LayoutParams) mViewGroupLine.getLayoutParams();
            lps.setMargins(0,0,0,0);
            mViewGroupLine.setLayoutParams(lps);
            if(flages){
                //表示没有负值
                mFxFgDown.setVisibility(View.GONE);
                mCenterFg.setVisibility(View.GONE);
                mCenterFgsss.setVisibility(View.VISIBLE);
            }else {
                mCenterFg.setVisibility(View.VISIBLE);
                mCenterFgsss.setVisibility(View.GONE);
                FrameLayout.LayoutParams lpes = (FrameLayout.LayoutParams)mCenterFg.getLayoutParams();
                int margines = (int) getActivity().getResources().getDimension(R.dimen.height_3_80);
                lpes.setMargins(0,margines,0,0);
                mCenterFg.setLayoutParams(lpes);
            }

        }*/
        //柱状图显示 布局中记得显示出来
       /* adapter = new XiaoLvRecyclerViewAdapter(getActivity(), xiaoLvList, maxNum,type);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(adapter);
        if(type==12){
            mRecycleView.scrollToPosition(11);
        }
        mRecycleView.setFocusable(false);*/
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
        lineChart = (LineChartInViewPager) contentView.findViewById(new_lineChart);
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
    public void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting_kzp:
            case R.id.tv_editors:
                playVoice(R.raw.changgui02);
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), CanSettingMoneyActivity.class);
                intent1.putExtra("MONTH", mm + "");
                intent1.putExtra("YEAR", yyyy + "");
                String aa = "";
                String bb = "";
                if (null != list && list.size() > 0) {
                    aa = list.get(type - 1).getFixedLargeExpenditure() + "";
                    bb = list.get(type - 1).getFixedLifeExpenditure() + "";
                }

                intent1.putExtra("LargeExpenditure", aa);
                intent1.putExtra("LifeExpenditure", bb);
                startActivityForResult(intent1, 330);
                break;
            case R.id.rl_expense_btn:
                //showMessage("记一笔");
                playVoice(R.raw.changgui02);
                startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                break;
            case R.id.rl_setting_ys:
                //设置预算
                playVoice(R.raw.changgui02);
                Intent intent3 = new Intent(getActivity(), ActSetBudget.class);
                intent3.putExtra("MONTH", mm + "");
                intent3.putExtra("YEAR", yyyy + "");
                String abId = SPUtil.getPrefString(getActivity(), CommonTag.ACCOUNT_BOOK_ID, "");
                intent3.putExtra("accountBookId", abId + "");
                intent3.putExtra("topTime", "- - 年 - - 月 - - 日");
                intent3.putExtra("endTime", "- - 年 - - 月 - - 日");
                startActivityForResult(intent3, 1000);
                break;
            case R.id.tv_month:
                playVoice(R.raw.changgui02);
                new MyTimer2Pop(getActivity(), tvMonth, dataM, mm - 1, new MyTimer2Pop.OnDateListener() {
                    @Override
                    public void getMonthList(String s) {
                        String[] ss = s.split("月份");
                        tvMonth.setText(2018 + "年" + ss[0] + "月");
                        mm = Integer.parseInt(ss[0]);
                        loadDataForNet();
                    }
                }, new MyTimer2Pop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {

                    }
                }).showPopWindow();
                break;
            case ll_zhangben_choose:
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
                                        getYuSuanNet3(token);
                                    }
                                }).builder().show();
                            }

                            @Override
                            public void onError(String s) {
                                showMessage(s);
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
    private void updateLinehart(final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables,
                                final String unit, List<Entry> values2, List<Entry> values1, final String[] labels, final String units) {
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
        lineChartEntity.setAxisFormatter(  new IAxisValueFormatter() {
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
            getYuSuanNet3(token);
        }
    }

    private void initChartToXiaolv() {
        LogUtil.e("initChartToXiaolv()");
        mFormat = new DecimalFormat("#,###.##");
        xLineChart = (LineChartInViewPager) contentView.findViewById(R.id.xl_lineChart);
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
    private void updateLinehartTo(final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables,
                                  final String unit, List<Entry> values2, List<Entry> values1, final String[] labels) {
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
        /**
         * 这里切换平滑曲线或者折现图
         */
//        lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER); LINEAR, STEPPED,
        lineChartEntity.setLineMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        lineChartEntity.setAxisFormatter(
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        //设置折线图最底部月份显示
                        if (value == 1.0f) {
                            //return mFormat.format(value) + "月";
                            if (type == 12) {
                                return yoyList.get(0).getMonth();
                            } else {
                                return yoyList.get(0).getMonth() + "月";
                            }

                        }
                        //String monthStr = mFormat.format(value);
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
                markerView.getTvContent().setText(textTemp);
            }
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
    }

}
