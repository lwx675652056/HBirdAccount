package com.hbird.ui.analysis;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.FragAnalysisBinding;
import com.hbird.base.mvc.activity.ActSetBudget;
import com.hbird.base.mvc.activity.CanSettingMoneyActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.ExpendScaleActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoneyReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanFinishReturn;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.bean.YoyListEntity;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.Dialog2ChooseZb;
import com.hbird.base.mvc.widget.MyTimer2Pop;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Util;
import com.hbird.bean.ConsumptionRatioBean;
import com.hbird.bean.SaveMoneyBean;
import com.hbird.bean.YuSuan1Bean;
import com.hbird.bean.YuSuan2Bean;
import com.hbird.common.chating.charts.LineChart;
import com.hbird.common.chating.components.Legend;
import com.hbird.common.chating.components.XAxis;
import com.hbird.common.chating.data.Entry;
import com.hbird.common.chating.data.LineData;
import com.hbird.common.chating.data.LineDataSet;
import com.hbird.common.chating.formatter.IndexAxisValueFormatter;
import com.hbird.common.chating.formatter.ValueFormatter;
import com.hbird.common.chating.highlight.Highlight;
import com.hbird.common.chating.interfaces.datasets.ILineDataSet;
import com.hbird.common.chating.listener.OnChartValueSelectedListener;
import com.hbird.util.Utils;
import com.hbird.widget.RoundMarker1;

import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseFragment;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;
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
    private List<YoyListEntity> yoyList = new ArrayList<>();
    private List<YoyListEntity> xlList;
    private YoyListEntity yoyListEntity;
    private boolean xiaoLvs = false;
    private boolean zhipei = false;

    private ConsumptionRatioAdapter adapter;
    private List<ConsumptionRatioBean> ratioBeanList = new ArrayList<>();
    private AnalysisData data;
    private int height_200;

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

        height_200 = getResources().getDimensionPixelSize(R.dimen.dp_200_x);

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

//                    NetWorkManager.getInstance().setContext(getActivity()).getYuSuanFinish(data.getMm() + "", types + "", abId, token, new NetWorkManager.CallBack() {
//
//                @Override
//                public void onSuccess(BaseReturn b) {
//                }
//
//                @Override
//                public void onError(String s) {
//                    ToastUtil.showShort(s);
//                }
//            });

        String type1 = SPUtil.getPrefString(getActivity(), CommonTag.INDEX_TYPE_BUDGET, "1");
        if (type1.equals("1")) {  // 1是日常账本
            viewModel.getYuSuanFinish1(data.getMm() + "", types + "", abId, token, new AnalysisModle.YuSuanCallBack() {
                @Override
                public void success(List<YuSuan1Bean> list) {
                    data.setShowSetBudget(false);
                    mRadioDown.setVisibility(View.GONE);

                    tvYusuanTime.setVisibility(View.INVISIBLE);

                    if (null == list || list.size() == 0) {
                        textDes.setVisibility(View.VISIBLE);
                        data.setShowSetBudget(true);
                        mRadioDown.setVisibility(View.GONE);
                        binding.flParent1.setVisibility(View.GONE);
                        zhipei = true;
                    } else {
                        data.setShowSetBudget(false);
                        binding.flParent1.setVisibility(View.VISIBLE);
                        zhipei = false;
                        textDes.setVisibility(View.GONE);
                        mRadioDown.setVisibility(View.VISIBLE);

                        finishList.clear();

                        // 自己填充数据，遍历接口数据匹配
                        int lastYear = data.getYyyy();// list中最后一个数据的年，
                        int lastMonth = data.getMm();
                        YuSuanFinishReturn.ResultBean t1 = new YuSuanFinishReturn.ResultBean();
                        t1.setTime(lastYear + "-" + (lastMonth > 9 ? lastMonth : ("0" + lastMonth)));
//            b.setTags("a");
                        finishList.add(0, t1);// 先组装当月的
                        for (int i = 0; i < types - 1; i++) {// -1是为了除去当月，比如type为3，则除去当月再组装2个月的数据
                            if (lastMonth == 1) {// 上一条是1月份的，这条数据要减年了
                                lastYear -= 1;
                                lastMonth = 12;
                            } else {
                                lastMonth -= 1;
                            }
                            YuSuanFinishReturn.ResultBean t2 = new YuSuanFinishReturn.ResultBean();
                            t2.setTime(lastYear + "-" + (lastMonth > 9 ? lastMonth : ("0" + lastMonth)));
//                b.setTags("a");
                            finishList.add(0, t2);
                        }
                        // 组装数据结束，
                        // 填充数据值
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < finishList.size(); j++) {
                                if (finishList.get(j).getTime().equals(list.get(i).time)) {
                                    finishList.get(j).setMonthSpend(list.get(i).monthSpend);
                                    finishList.get(j).setBudgetMoney(list.get(i).budgetMoney);
                                    finishList.get(j).setTag(null);
                                }
                            }
                        }

                        setToDates(types);
                    }
                }
            });
        } else { // 其它的是场景账本
            viewModel.getYuSuanFinish2(data.getMm() + "", types + "", abId, token, new AnalysisModle.YuSuanCallBack1() {

                @Override
                public void success(YuSuan2Bean b) {
                    LogUtil.e("123");
                    aa(b);
                }
            });
        }
    }

    private void aa(YuSuan2Bean b) {
        data.setShowSetBudget(false);
        mRadioDown.setVisibility(View.GONE);

        // 没有设置预算
        if (b == null || b.sceneBudget == null || b.sceneBudget.budgetMoney < 0) {
            data.setShowSetBudget(true);
            zhipei = false;
            binding.flParent1.setVisibility(View.GONE);
            return;
        } else {
            data.setShowSetBudget(false);
            zhipei = false;
            binding.flParent1.setVisibility(View.VISIBLE);
        }

        // 没有数据
        if (b == null || b.arrays == null || b.arrays.size() < 1) {
            setToDates(types);
            return;
        }

        tvYusuanTime.setVisibility(View.VISIBLE);
        String startTime = DateUtil.stampToDate(b.sceneBudget.beginTime, "yyyy年MM月dd日");
        String endTime = DateUtil.stampToDate(b.sceneBudget.endTime, "yyyy年MM月dd日");
        tvYusuanTime.setText(startTime + " 至 " + endTime);

        // 场景账本的绘图需求：
        // （1）按照3周21天；
        // （2）3周~5个月按照每周；
        // （3）5个月~24个月按照月；
        // （4）24个月以上按照年

        // 相差天数 后台已做判断
        String time = b.arrays.get(0).time;
        finishList.clear();
        int size = b.arrays.size();

        switch (time.length()) {
            case 10: // 日 "time": "2018-12-08"
//                        String start1 = DateUtil.stampToDate(b2.getResults().getSceneBudget().getBeginTime(), "yyyy-MM");
//                        String end1 = DateUtil.stampToDate(b2.getResults().getSceneBudget().getEndTime(), "yyyy-MM");
                String start2 = b.arrays.get(0).time;
                String end2 = b.arrays.get(size - 1).time;
                List<String> list2 = Util.getDayList(start2, end2);

                for (int i = 0; i < list2.size(); i++) {
                    YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();

//                        List<YuSuanZbFinishReturn.ResultsBean.ArraysBean> l = b.arrays;
                    for (int j = 0; j < b.arrays.size(); j++) {
                        temp.setTime(list2.get(i));
                        if (list2.get(i).equals(b.arrays.get(j).time)) {
                            temp.setMonthSpend(b.arrays.get(j).money);
                        }
                    }
                    if (TextUtils.isEmpty(temp.getTime())) {
                        temp.setMonthSpend(0);
                    }
                    temp.setBudgetMoney(b.sceneBudget.budgetMoney);
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
                String start3 = (String) b.arrays.get(0).weekTime;
                String end3 = (String) b.arrays.get(size - 1).weekTime;
                List<String> list3 = Util.getWeekList(start3, end3);

                for (int i = 0; i < list3.size(); i++) {
                    YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();

//                        List<YuSuanZbFinishReturn.ResultsBean.ArraysBean> l = b2.getResults().getArrays();
                    for (int j = 0; j < b.arrays.size(); j++) {
                        temp.setTime(list3.get(i));
                        if (list3.get(i).equals(b.arrays.get(j).weekTime)) {
                            temp.setMonthSpend(b.arrays.get(j).money);
                        }
                    }
                    if (TextUtils.isEmpty(temp.getTime())) {
                        temp.setMonthSpend(0);
                    }
                    temp.setBudgetMoney(b.sceneBudget.budgetMoney);
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
                String start1 = b.arrays.get(0).time;
                String end1 = b.arrays.get(size - 1).time;
                List<String> list = Util.getMonthList(start1, end1);

                for (int i = 0; i < list.size(); i++) {
                    YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();

//                        List<YuSuanZbFinishReturn.ResultsBean.ArraysBean> l = b2.getResults().getArrays();
                    for (int j = 0; j < b.arrays.size(); j++) {
                        temp.setTime(list.get(i).substring(5, list.get(i).length()));
                        if (list.get(i).equals(b.arrays.get(j).time)) {
                            temp.setMonthSpend(b.arrays.get(j).money);
                        }
                    }
                    if (TextUtils.isEmpty(temp.getTime())) {
                        temp.setMonthSpend(0);
                    }
                    temp.setBudgetMoney(b.sceneBudget.budgetMoney);
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
                int start = Integer.parseInt(b.arrays.get(0).time);
                int end = Integer.parseInt(b.arrays.get(size - 1).time);
                for (int i = 0; i < size; i++) {
                   YuSuan2Bean.ArraysBean bean=  b.arrays.get(i);
                    if (i == 0) {
                        YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();
                        temp.setTime(bean.time);
                        temp.setMonthSpend(bean.money);
                        temp.setBudgetMoney(b.sceneBudget.budgetMoney);
                        finishList.add(temp);
                    } else {
                        int lastTime = Integer.parseInt(b.arrays.get(i - 1).time);
                        if (Integer.parseInt(bean.time) - lastTime > 1) {
                            while (Integer.parseInt(bean.time) - lastTime != 1) {
                                YuSuanFinishReturn.ResultBean aa = new YuSuanFinishReturn.ResultBean();
                                aa.setTime((lastTime + 1) + "");
                                aa.setMonthSpend(0);
                                aa.setTag("a");

                                aa.setBudgetMoney(b.sceneBudget.budgetMoney);
                                finishList.add(aa);
                                lastTime++;
                            }
                            // 把当前循环的加上
                            YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();
                            temp.setTime(bean.time);
                            temp.setMonthSpend(bean.money);
                            temp.setBudgetMoney(b.sceneBudget.budgetMoney);
                            finishList.add(temp);
                        } else {
                            YuSuanFinishReturn.ResultBean temp = new YuSuanFinishReturn.ResultBean();
                            temp.setTime(bean.time);
                            temp.setMonthSpend(bean.money);
                            temp.setBudgetMoney(b.sceneBudget.budgetMoney);
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
        // 自己填充数据，遍历接口数据匹配
        int lastYear = data.getYyyy();// list中最后一个数据的年，
        int lastMonth = data.getMm();
        SaveMoneyReturn.ResultBean b = new SaveMoneyReturn.ResultBean();
        b.setTime(lastYear + "-" + (lastMonth > 9 ? lastMonth : ("0" + lastMonth)));
        b.setTags("a");
        list.add(0, b);// 先组装当月的
        for (int i = 0; i < type - 1; i++) {// -1是为了除去当月，比如type为3，则除去当月再组装2个月的数据
            if (lastMonth == 1) {// 上一条是1月份的，这条数据要减年了
                lastYear -= 1;
                lastMonth = 12;
            } else {
                lastMonth -= 1;
            }
            SaveMoneyReturn.ResultBean t = new SaveMoneyReturn.ResultBean();
            t.setTime(lastYear + "-" + (lastMonth > 9 ? lastMonth : ("0" + lastMonth)));
            b.setTags("a");
            list.add(0, t);
        }
        // 组装数据结束，
        // 填充数据值
        for (int i = 0; i < temp.arrays.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getTime().equals(temp.arrays.get(i).time)) {
                    list.get(j).setFixedLargeExpenditure(temp.fixedSpend.fixedLargeExpenditure);
                    list.get(j).setFixedLifeExpenditure(temp.fixedSpend.fixedLifeExpenditure);
                    list.get(j).setMonthIncome(temp.arrays.get(i).monthIncome);
                    list.get(j).setMonthSpend(temp.arrays.get(i).monthSpend);
                    list.get(j).setTags(null);
                }
            }
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
                String[] split = dates.split("-");
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
        yuSuanFinishList.clear();

        for (int i = 0; i < num; i++) {
            if (finishList.size() < num) {
                binding.flParent1.setVisibility(View.VISIBLE);
                binding.flParent1.removeAllViews();
                LineChart lineChart = new LineChart(getActivity());
                lineChart.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_200));
                binding.flParent1.addView(lineChart);
                lineChart.setBackgroundColor(Color.WHITE); // background color
                lineChart.getDescription().setEnabled(false);  // disable description text
                lineChart.setTouchEnabled(true); // enable touch gestures
                lineChart.setNoDataText("暂无记账记录");
                lineChart.setDrawGridBackground(false);
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
                String[] split = dates.split("-");
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
        if (zhipei) {
            binding.flParent1.setVisibility(View.GONE);
        } else {
            binding.flParent1.setVisibility(View.VISIBLE);
        }

        binding.flParent1.removeAllViews();
        LineChart lineChart = new LineChart(getActivity());
        lineChart.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_200));
        binding.flParent1.addView(lineChart);
        lineChart.setBackgroundColor(Color.WHITE); // background color
        lineChart.getDescription().setEnabled(false);  // disable description text
        lineChart.setTouchEnabled(true); // enable touch gestures

        lineChart.setDrawGridBackground(false);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String s = Utils.to2DigitString(e.getY()) + "%";

                TextView tv = ((RoundMarker1) lineChart.getMarker()).getText();
                tv.setText(s);

                lineChart.invalidate();
            }

            @Override
            public void onNothingSelected() {
            }
        });

        RoundMarker1 mv = new RoundMarker1(getActivity(), R.layout.custom_marker_view1);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // X轴的个数，Y轴最大范围
        ArrayList<Entry> values = new ArrayList<>();
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
                if (f != 0) {
                    values.add(new Entry(i, f, getResources().getDrawable(R.mipmap.shuju_icon_zhengshu_normal)));
                } else {
                    values.add(new Entry(i, f));
                }
            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "");

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
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
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

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        // set data
        lineChart.setData(data);

        // 绘制动画结束时间
        lineChart.animateX(200);

        // 透明化图例 仅在设置数据后可用
        lineChart.getLegend().setForm(Legend.LegendForm.NONE);

        List<ILineDataSet> sets = lineChart.getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            // 是否显示顶部的值
            set.setDrawValues(false);
            // 是否值要顶部的黑点
            set.setDrawCircles(true);
            // 是否要阴影
            set.setDrawFilled(true);

            set.setMode(LineDataSet.Mode.LINEAR);// 直角
            // 线条样式
            set.setColor(Color.parseColor("#E9371F"));//线条颜色
            set.setCircleColor(Color.parseColor("#E9371F"));//圆点颜色
            set.setCircleHoleRadius(0.5f);
            set.setLineWidth(1f);//线条宽度
        }

        // 移动到某个位置
//        lineChart.moveViewToX(pos > 0 ? (pos - 1) : pos);

        //设置图表右边的y轴禁用
        lineChart.getAxisRight().setEnabled(false);

        //设置图表左边的y轴禁用
//        lineChart.getAxisLeft().setEnabled(false);
        //是否绘制0所在的网格线
        lineChart.getAxisLeft().setDrawZeroLine(true);
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "%";
            }
        });

        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().enableGridDashedLine(10f, 15f, 0f);
        lineChart.getAxisLeft().setGridLineWidth(0.5f);
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#F5F5F5"));

        lineChart.getAxisLeft().setDrawAxisLine(false);//是否绘制轴线 竖线
        lineChart.getAxisLeft().setAxisMinimum(0);
        // 是否等比缩放、单独X或Y缩放
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(false); //是否可以缩放 仅y轴

        //设置x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#929292"));
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        xAxis.setDrawAxisLine(false);//是否绘制轴线

        lineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true

        xAxis.setLabelRotationAngle(0);
        xAxis.setTextSize(10f);

        List<String> t = new ArrayList<>();
        for (int i = 1; i < yoyList.size() + 1; i++) {
            String s = yoyList.get(i - 1).getMonth();
            if (types != 12) {
                s += unit;
            }
            t.add(s);
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(t));

        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        // 默认选择的marker
//        lineChart.highlightValue(xlList.size()-1, 0);

        lineChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose_zb: // 选择账本
                NetWorkManager.getInstance().setContext(getActivity()).getMyAccounts(token, new NetWorkManager.CallBack() {
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

                                SPUtil.setPrefString(getActivity(), CommonTag.INDEX_CURRENT_ACCOUNT_ID, listZb.get(i).getId() + "");
                                SharedPreferencesUtil.put(CommonTag.INDEX_CURRENT_ACCOUNT_ID, listZb.get(i).getId() + "");
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
        if (xiaoLvs) {
            binding.flParent.setVisibility(View.GONE);
            return;
        } else {
            binding.flParent.setVisibility(View.VISIBLE);
        }

        binding.flParent.removeAllViews();
        LineChart lineChart = new LineChart(getActivity());
        lineChart.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_200));
        binding.flParent.addView(lineChart);
        lineChart.setBackgroundColor(Color.WHITE); // background color
        lineChart.getDescription().setEnabled(false);  // disable description text
        lineChart.setTouchEnabled(true); // enable touch gestures

        lineChart.setDrawGridBackground(false);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String s = Utils.to2DigitString(e.getY()) + "%";

                TextView tv = ((RoundMarker1) lineChart.getMarker()).getText();
                tv.setText(s);

                lineChart.invalidate();
            }

            @Override
            public void onNothingSelected() {
            }
        });

        RoundMarker1 mv = new RoundMarker1(getActivity(), R.layout.custom_marker_view1);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // X轴的个数，Y轴最大范围
        ArrayList<Entry> values = new ArrayList<>();
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
                if (f > 0) {
                    values.add(new Entry(i, f, getResources().getDrawable(R.mipmap.shuju_icon_zhengshu_normal)));
                } else if (f < 0) {
                    values.add(new Entry(i, f, getResources().getDrawable(R.mipmap.shuju_icon_fushu_normal)));
                } else {
//                    values.add(new Entry(i, f, getResources().getDrawable(R.mipmap.shuju_icon_wushuju_normal)));
                    values.add(new Entry(i, f));
                }
            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "");

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
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
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

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        // set data
        lineChart.setData(data);

        // 绘制动画结束时间
        if (type == 3) {
            lineChart.animateX(100);
        } else {
            lineChart.animateX(200);
        }

        // 透明化图例 仅在设置数据后可用
        lineChart.getLegend().setForm(Legend.LegendForm.NONE);

        List<ILineDataSet> sets = lineChart.getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            // 是否显示顶部的值
            set.setDrawValues(false);
            // 是否值要顶部的黑点
            set.setDrawCircles(true);
            // 是否要阴影
            set.setDrawFilled(true);

            set.setMode(LineDataSet.Mode.LINEAR);// 直角
            // 线条样式
            set.setColor(Color.parseColor("#E9371F"));//线条颜色
            set.setCircleColor(Color.parseColor("#E9371F"));//圆点颜色
            set.setCircleHoleRadius(0.5f);
            set.setLineWidth(1f);//线条宽度
        }

        // 移动到某个位置
//        lineChart.moveViewToX(pos > 0 ? (pos - 1) : pos);

        //设置图表右边的y轴禁用
        lineChart.getAxisRight().setEnabled(false);

        //设置图表左边的y轴禁用
//        lineChart.getAxisLeft().setEnabled(false);
        //是否绘制0所在的网格线
        lineChart.getAxisLeft().setDrawZeroLine(true);
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + "%";
            }
        });

        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().enableGridDashedLine(10f, 15f, 0f);
        lineChart.getAxisLeft().setGridLineWidth(0.5f);
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#F5F5F5"));

        lineChart.getAxisLeft().setDrawAxisLine(false);//是否绘制轴线 竖线

        // 是否等比缩放、单独X或Y缩放
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(false); //是否可以缩放 仅y轴

        //设置x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#929292"));
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        xAxis.setDrawAxisLine(false);//是否绘制轴线

        lineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true

        xAxis.setLabelRotationAngle(0);
        xAxis.setTextSize(10f);

        List<String> t = new ArrayList<>();
        for (int i = 1; i < xlList.size() + 1; i++) {
            String s = xlList.get(i - 1).getMonth();
            if (type != 12) {
                s += "月";
            }
            t.add(s);
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(t));

        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        lineChart.invalidate();
    }
}