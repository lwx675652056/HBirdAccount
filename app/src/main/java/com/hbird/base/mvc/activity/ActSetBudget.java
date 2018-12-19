package com.hbird.base.mvc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.FenXiMoneys;
import com.hbird.base.mvc.bean.RequestBean.YuSuanMoneyBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.global.modle.GlobalVariables;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.APDUserDateDialog;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.KeyboardUtil;
import com.hbird.base.util.L;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import sing.util.LogUtil;
import sing.util.ToastUtil;

import static com.hbird.base.R.id.tv_zong;

/**
 * @author: LiangYX
 * @ClassName: ActSetBudget
 * @date: 2018/12/10 15:31
 * @Description: 设置预算及时间
 */
public class ActSetBudget extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {

    @BindView(R.id.ll_one)
    LinearLayout llOne;// 设置预算
    @BindView(R.id.ll_two)
    LinearLayout llTwo;// 设置预算时间

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.center_title)
    TextView mCenterTit;
    @BindView(R.id.right_title2)
    TextView mTitle2;
    @BindView(R.id.anime_finish)
    TextView addFinishBtn;
    @BindView(R.id.clear)
    ImageView mClear;
    @BindView(R.id.anime_add)
    TextView mAdd;
    @BindView(R.id.anime_jian)
    TextView mJian;
    @BindView(R.id.tv_input_money)
    EditText mDownNum;
    @BindView(R.id.tv_moneys)
    TextView mUpNum;
    @BindView(R.id.tv_moneys_icon)
    TextView mMoneyIcon;
    @BindView(R.id.view1)
    View mView1;
    @BindView(R.id.view2)
    View mView2;
    @BindView(R.id.iv_bg_icons)
    ImageView mBgIcons;
    @BindView(R.id.tv_descs)
    TextView mDescs;

    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.tv_begin)
    TextView tvBegin;
    @BindView(R.id.rl_budget_begin)
    RelativeLayout rlBudgetBegin;
    @BindView(tv_zong)
    TextView tvZong;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl_budget_end)
    RelativeLayout rlBudgetEnd;
    @BindView(R.id.bt_finish)
    RelativeLayout btFinish;
    private Long beginLong;
    private Long endLong;
    private String topTimes;
    private String endTimes;
    private String accountBookId;
    private String token;

    private numberWatcher numberWatcher;
    private boolean inputTag;
    private int isFirst = 1;
    private String year;
    private String month;
    private String moneyIn;
    private Vibrator vibrator;
    private KeyboardUtil keyboardUtil;

    @Override
    protected int getContentLayout() {
        return R.layout.act_set_budget;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        llOne.setVisibility(View.VISIBLE);
        llTwo.setVisibility(View.GONE);

        month = getIntent().getStringExtra("MONTH");
        year = getIntent().getStringExtra("YEAR");
        moneyIn = getIntent().getStringExtra("MONEY");
        accountBookId = getIntent().getStringExtra("accountBookId");
        mCenterTit.setText(month + "月份预算");
        mTitle2.setVisibility(View.GONE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
    }

    @Override
    protected void initEvent() {
        ivBack.setOnClickListener(this);
        addFinishBtn.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mJian.setOnClickListener(this);
        btFinish.setOnClickListener(this);
        rlBudgetBegin.setOnClickListener(this);
        rlBudgetEnd.setOnClickListener(this);
        //设置计算器的计算监听 实时计算结果并展示
        numberWatcher = new numberWatcher();
        mDownNum.addTextChangedListener(numberWatcher);
        if (!TextUtils.isEmpty(moneyIn)) {
            mDownNum.setText(moneyIn);
            setColors(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //calculatorClear();
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.bt_finish:
                playVoice(R.raw.changgui02);
                setBugets();
                break;
            case R.id.anime_finish:
                //showMessage("完成");
                playVoice(R.raw.jizhangfinish);
                saveAndFinish();
                break;
            case R.id.anime_add:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(30);
                if (inputTag) {
                    showMessage("已输入过");
                    return;
                }
                moneyIn = "";
                inputTag = true;
                String add = "+";
                GlobalVariables.setHasDot(false);
                setNumberKey(add);
                break;
            case R.id.anime_jian:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(30);
                if (inputTag) {
                    showMessage("已输入过");
                    return;
                }
                moneyIn = "";
                inputTag = true;
                String jian = "-";
                GlobalVariables.setHasDot(false);
                setNumberKey(jian);
                break;
            case R.id.clear:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(30);
                if (inputTag) {
                    inputTag = false;
                }
                //如果是 点 那么则删除点
                if (GlobalVariables.getmHasDot()) {
                    //已经点过了
                    GlobalVariables.setHasDot(false);
                }
                if (TextUtils.isEmpty(mDownNum.getText().toString())) {
                    return;
                }
                String str = "";
                if (!TextUtils.isEmpty(moneyIn)) {
                    moneyIn = "";
                    str = "";
                    GlobalVariables.setmInputMoney(str);
                    mDownNum.setText(str);
                } else {
                    str = mDownNum.getText().toString().substring(0, mDownNum.length() - 1);
                    GlobalVariables.setmInputMoney(str);
                    mDownNum.setText(str);
                }
                if (TextUtils.isEmpty(str)) {
                    mUpNum.setText("0.00");
                    mMoneyIcon.setVisibility(View.VISIBLE);
                    mDownNum.setHint("￥请输入金额");
                    mDownNum.setTextColor(getResources().getColor(R.color.text_bdbdbd));
                }
                break;
            case R.id.rl_budget_begin:
                playVoice(R.raw.changgui02);
                new APDUserDateDialog(this) {
                    @Override
                    protected void onBtnOkClick(String year, String month, String day) {
                        //判断今天跟选取的年月日 是否为同一天
                        String days = DateUtils.getDays(System.currentTimeMillis());
                        if (!TextUtils.isEmpty(days)) {
                            topTimes = year + "年" + month + "月" + day + "日";
                            tvBegin.setText(topTimes);
                        }
                        beginLong = DateUtil.dateToLongs(tvBegin.getText().toString().trim());
                    }
                }.show();
                break;
            case R.id.rl_budget_end:
                playVoice(R.raw.changgui02);
                new APDUserDateDialog(this) {

                    @Override
                    protected void onBtnOkClick(String year, String month, String day) {
                        //判断今天跟选取的年月日 是否为同一天
                        String days = DateUtils.getDays(System.currentTimeMillis());
                        if (!TextUtils.isEmpty(days)) {
                            endTimes = year + "年" + month + "月" + day + "日";
                            tvEnd.setText(year + "年" + month + "月" + day + "日");
                        }
                        //mDate.setText(year + "/" + month + "/" + day);
                        endLong = DateUtil.dateToLongs(tvEnd.getText().toString().trim());
                        String beTime = tvBegin.getText().toString().trim();
                        Long beginLongs = DateUtil.dateToLongs(beTime);
                        long l = endLong - beginLongs;
                        long ls = l / 24 / 60 / 60 / 1000;
                        String strs = "结束时间：共 " + "<font color='#F15C3C'>" + ls + "</font>" + "天";
                        tvZong.setText(Html.fromHtml(strs));
                    }
                }.show();
                break;
        }
    }

    private void saveAndFinish() {
        String moneyString = mUpNum.getText().toString();
        setEditorToAccount();
    }

    private void setEditorToAccount() {
        FenXiMoneys fenxi = new FenXiMoneys();
        String sUp = mUpNum.getText().toString();
        String sDown = mDownNum.getText().toString().trim();
        String moneys = "0.00";
        if (null == sDown || TextUtils.isEmpty(sDown)) {
            moneys = "-1";
        } else {
            moneys = sUp;
            if (TextUtils.equals(moneys, "0.00")) {
                moneys = "-1";
            }
        }
        sss = Double.parseDouble(moneys);
        if (sss <= 0) {
            showMessage("请设置有效的预算金额");
            return;
        }

        llOne.setVisibility(View.GONE);
        llTwo.setVisibility(View.VISIBLE);

        initTwo();
    }

    double sss;

    //    postV2Budget(sss);
    private void initTwo() {
        mCenterTit.setText("预算时间设置");
        mTitle2.setVisibility(View.GONE);
        token = SPUtil.getPrefString(ActSetBudget.this, CommonTag.GLOABLE_TOKEN, "");
        accountBookId = getIntent().getStringExtra("accountBookId");
        String topTimes = getIntent().getStringExtra("topTime");
        String endTimes = getIntent().getStringExtra("endTime");
        if (TextUtils.equals(topTimes, "- - 年 - - 月 - - 日")) {
            long time = System.currentTimeMillis();
            String yearMonthDay = DateUtils.getYearMonthDay(time);
            //设置当间
            tvBegin.setText(yearMonthDay);
            tvEnd.setText(yearMonthDay);
        } else {
            tvBegin.setText(topTimes);
            tvBegin.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvBegin.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_calendar_normal_select), null, null, null);
            tvEnd.setText(endTimes);
            tvEnd.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvEnd.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_calendar_normal_select), null, null, null);

            if (topTimes != null && endTimes != null) {
                Long top = DateUtil.dateToLongs(topTimes);
                Long end = DateUtil.dateToLongs(endTimes);
                long l = end - top;
                long ls = l / 24 / 60 / 60 / 1000;
                String strs = "结束时间：共 " + "<font color='#F15C3C'>" + ls + "</font>" + "天";
                tvZong.setText(Html.fromHtml(strs));
            }
        }
    }

    private void setBugets() {
        final String bTime = tvBegin.getText().toString();
        final String eTime = tvEnd.getText().toString();
        Long aa = DateUtil.dateToLongs(bTime);
        Long bb = DateUtil.dateToLongs(eTime);
        if (bb - aa < 0) {
            ToastUtil.showShort("结束时间不能小于开始时间");
            return;
        }
        LogUtil.e("aa = " + aa + ",bb = " + bb);
        YuSuanMoneyBean ysBean = new YuSuanMoneyBean();
        ysBean.setAccountBookId(accountBookId);//账本ID
        ysBean.setBeginTime(aa + "");//预算开始时间 场景账本
        ysBean.setEndTime(bb + "");//预算结束时间  场景账本
        ysBean.setBudgetMoney(BigDecimal.valueOf(sss));//预算金额
        ysBean.setTime(year + "-" + month);//设置预算/固定支出日期 例2018-07 不传默认当年当月  日常账本

        String json = new Gson().toJson(ysBean);
        showProgress("");

        NetWorkManager.getInstance().setContext(this)
                .postBudgetsV2(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        GloableReturn b1 = (GloableReturn) b;
                        Intent intentTemp = new Intent();
                        intentTemp.putExtra("topTime", bTime);
                        intentTemp.putExtra("endTime", eTime);
                        setResult(104, intentTemp);
                        finish();
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }

    private void postV2Budget(double sss) {
        YuSuanMoneyBean ysBean = new YuSuanMoneyBean();
        ysBean.setAccountBookId(accountBookId);//账本ID
        ysBean.setBudgetMoney(BigDecimal.valueOf(sss));//预算金额
        ysBean.setTime(year + "-" + month);//设置预算/固定支出日期 例2018-07 不传默认当年当月  日常账本
        String json = new Gson().toJson(ysBean);
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .postBudgetsV2(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        GloableReturn b1 = (GloableReturn) b;
                        showMessage("设置成功");

                        setResult(Activity.RESULT_OK);
                        finish();
                        //calculatorClear();
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }

    // 清零按钮
    public void calculatorClear() {
        GlobalVariables.setmInputMoney("");
        mUpNum.setVisibility(View.GONE);
        mMoneyIcon.setVisibility(View.GONE);
        mDownNum.setHint("￥请输入金额");
        GlobalVariables.setHasDot(false);
    }

    // 数字输入按钮
    public void calculatorNumOnclick(View v) {
        playVoice(R.raw.changgui01);
        Button view = (Button) v;
        view.setClickable(false);
        String digit = view.getText().toString();
        inputTag = false;
        moneyIn = "";
        //震动30毫秒
        vibrator.vibrate(30);
        setNumberKey(digit);
        view.setClickable(true);
    }

    // 小数点处理工作
    public void calculatorPushDot(View view) {
        playVoice(R.raw.changgui01);
        if (GlobalVariables.getmHasDot()) {
            Toast.makeText(getApplicationContext(), "已经输入过小数点了 ━ω━●", Toast.LENGTH_SHORT).show();
        } else {
            GlobalVariables.setmInputMoney(GlobalVariables.getmInputMoney() + ".");
            GlobalVariables.setHasDot(true);
        }
    }

    private void setNumberKey(String add) {
       /* if(!Utils.isFastClick()){
            //showMessage("手速太快了");
            return;
        }*/
        String money = GlobalVariables.getmInputMoney();
        if (GlobalVariables.getmHasDot() && GlobalVariables.getmInputMoney().length() > 2) {
            String dot = money.substring(money.length() - 3, money.length() - 2);
            L.liul("calculatorNumOnclick: " + dot);
            if (dot.equals(".")) {
                //showMessage("陛下，不到一分钱了");
                return;
            }
        }
        //mDownNum.addTextChangedListener(numberWatcher);
        //判断
        String[] positive = money.split("\\+");
        String endStr = null;
        for (String pos : positive) {
            if (pos.contains("-")) {
                String[] negative = pos.split("\\-");
                Double temp = 0d;
                for (int is = 0; is < negative.length; is++) {
                    endStr = negative[is];
                }
                //endStr = negative[0];
            } else {
                endStr = pos;
            }
        }
        //判断超过8位数不再继续输入
        if (null != endStr && endStr.contains(".")) {
            String[] split = endStr.split("\\.");
            if (null != split && split.length > 0) {
                if (split[0].length() > 8) {
                    showMessage("您输入的位数过大");
                    return;
                }
            }

        } else {
            if (!TextUtils.isEmpty(endStr)) {
                if (endStr.length() >= 8) {
                    String sub = money.substring(money.length() - 1, money.length());
                    if (TextUtils.equals(sub, "+") || TextUtils.equals(sub, "-")) {

                    } else {
                        if (TextUtils.equals(add, "+") || TextUtils.equals(add, "-")) {

                        } else {
                            showMessage("您输入的位数过大");
                            return;
                        }
                    }
                }
            }

        }

        if (isFirst == 1) {
            //清空之前的数字 重新输入（否则明细详情会把金额带过来导致问题）
            isFirst = isFirst + 1;
            GlobalVariables.setmInputMoney(add);
        } else {
            GlobalVariables.setmInputMoney(money + add);
        }

        mDownNum.setText(GlobalVariables.getmInputMoney());
        mDownNum.setSelection(GlobalVariables.getmInputMoney().length());
    }

    private class numberWatcher implements TextWatcher {
        private CharSequence temp;
        private int selectionStart;
        private int selectionEnd;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String ss = editable + "";
            try {
                if (TextUtils.isEmpty(ss)) {
                    setColors(false);
                    return;
                }
                if (null != ss) {
                    if (ss.startsWith("+") || ss.startsWith("-")) {
                        ss = "0" + ss;
                    }
                    setColors(true);
                    setNumForResult(ss);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("数字非法，请重新输入");
                calculatorClear();
            }


        }

    }

    private void setNumForResult(String ss) {
        double sum = Utils.sum(ss);
        //支取小数点后两位数
        DecimalFormat df = new DecimalFormat("######0.00");
        String format = df.format(sum);
        mUpNum.setText(format);
        mMoneyIcon.setVisibility(View.VISIBLE);
    }

    private void setColors(boolean aa) {
        if (aa) {
            //设置选中颜色
            mDownNum.setTextColor(getResources().getColor(R.color.text_F15C3C));
            mView1.setBackgroundColor(getResources().getColor(R.color.text_F15C3C));
            mView2.setBackgroundColor(getResources().getColor(R.color.text_F15C3C));
            mBgIcons.setImageResource(R.mipmap.ic_budget_bg_ora);
            mDescs.setTextColor(getResources().getColor(R.color.text_F15C3C));
            mDownNum.setVisibility(View.VISIBLE);
            mUpNum.setTextColor(getResources().getColor(R.color.text_F15C3C));
            mMoneyIcon.setTextColor(getResources().getColor(R.color.text_F15C3C));
        } else {
            //默认颜色
            mDownNum.setTextColor(getResources().getColor(R.color.text_bdbdbd));
            mView1.setBackgroundColor(getResources().getColor(R.color.line2));
            mView2.setBackgroundColor(getResources().getColor(R.color.line2));
            mBgIcons.setImageResource(R.mipmap.ic_budget_bg_gray);
            mDescs.setTextColor(getResources().getColor(R.color.text_bdbdbd));
            mUpNum.setTextColor(getResources().getColor(R.color.text_bdbdbd));
            mMoneyIcon.setTextColor(getResources().getColor(R.color.text_bdbdbd));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        calculatorClear();
    }
}
