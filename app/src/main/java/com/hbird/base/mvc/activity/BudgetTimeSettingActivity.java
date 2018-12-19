package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.YuSuanMoneyBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.APDUserDateDialog;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;
import sing.util.ToastUtil;

import static com.hbird.base.R.id.tv_zong;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/9.
 * 预算时间设置
 */

public class BudgetTimeSettingActivity extends BaseActivity<BaseActivityPresenter> {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.right_title2)
    TextView rightTitle2;
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_budget_time;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        centerTitle.setText("预算时间设置");
        rightTitle2.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(BudgetTimeSettingActivity.this, CommonTag.GLOABLE_TOKEN, "");
        accountBookId = getIntent().getStringExtra("accountBookId");
        String topTimes = getIntent().getStringExtra("topTime");
        String endTimes = getIntent().getStringExtra("endTime");
        if(TextUtils.equals(topTimes,"- - 年 - - 月 - - 日")){
            long time = System.currentTimeMillis();
            String yearMonthDay = DateUtils.getYearMonthDay(time);
            //设置当间
            tvBegin.setText(yearMonthDay);
            tvEnd.setText(yearMonthDay);
        }else {
            tvBegin.setText(topTimes);
            tvBegin.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvBegin.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_calendar_normal_select),null,null,null);
            tvEnd.setText(endTimes);
            tvEnd.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvEnd.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_calendar_normal_select),null,null,null);
            Long top = DateUtil.dateToLongs(topTimes);
            Long end = DateUtil.dateToLongs(endTimes);
            long l = end - top;
            long ls = l / 24 / 60 / 60 / 1000;
            String strs = "结束时间：共 " + "<font color='#F15C3C'>" + ls + "</font>"+"天";
            tvZong.setText(Html.fromHtml(strs));
        }

    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.iv_back, R.id.rl_budget_begin, R.id.rl_budget_end, R.id.bt_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.rl_budget_begin:
                playVoice(R.raw.changgui02);
                new APDUserDateDialog(this) {
                    @Override
                    protected void onBtnOkClick(String year, String month, String day) {
                        //判断今天跟选取的年月日 是否为同一天
                        String days = DateUtils.getDays(System.currentTimeMillis());
                        if(!TextUtils.isEmpty(days)){
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
                        if(!TextUtils.isEmpty(days)){
                            endTimes = year + "年" + month + "月" + day + "日";
                            tvEnd.setText(year + "年" + month + "月" + day+"日");
                        }
                        //mDate.setText(year + "/" + month + "/" + day);
                        endLong = DateUtil.dateToLongs(tvEnd.getText().toString().trim());
                        String beTime = tvBegin.getText().toString().trim();
                        Long beginLongs = DateUtil.dateToLongs(beTime);
                        long l = endLong - beginLongs;
                        long ls = l / 24 / 60 / 60 / 1000;
                        String strs = "结束时间：共 " + "<font color='#F15C3C'>" + ls + "</font>"+"天";
                        tvZong.setText(Html.fromHtml(strs));
                    }
                }.show();
                break;
            case R.id.bt_finish:
                playVoice(R.raw.changgui02);
                setBugets();
                break;
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
        YuSuanMoneyBean ysBean = new YuSuanMoneyBean();
        ysBean.setAccountBookId(accountBookId);//账本ID
        ysBean.setBeginTime(aa+"");//预算开始时间 场景账本
        ysBean.setEndTime(bb+"");//预算结束时间  场景账本
        String json = new Gson().toJson(ysBean);
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .postBudgetsV2(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        GloableReturn b1 = (GloableReturn) b;
                        Intent intentTemp  = new Intent();
                        intentTemp.putExtra("topTime",bTime);
                        intentTemp.putExtra("endTime",eTime);
                        setResult(104,intentTemp);
                        finish();
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }
}
