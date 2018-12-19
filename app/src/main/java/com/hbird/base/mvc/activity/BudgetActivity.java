package com.hbird.base.mvc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.KeyboardUtil;
import com.hbird.base.util.L;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * Created by Liul on 2018/8/8.
 * 预算界面
 */

public class BudgetActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
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
   /* @BindView(R.id.keyboard_view)
    com.hbird.base.mvc.widget.MyKeyBoardView keyboard_view;*/


    private BudgetActivity.numberWatcher numberWatcher;
    private boolean inputTag;
    private int isFirst = 1;
    private String year;
    private String month;
    private String moneyIn;
    private Vibrator vibrator;
    private KeyboardUtil keyboardUtil;
    private String token;
    private String accountBookId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_budget;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        month = getIntent().getStringExtra("MONTH");
        year = getIntent().getStringExtra("YEAR");
        moneyIn = getIntent().getStringExtra("MONEY");
        accountBookId = getIntent().getStringExtra("accountBookId");
        mCenterTit.setText(month +"月份预算");
        mTitle2.setVisibility(View.GONE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        /*keyboardUtil = new KeyboardUtil(BudgetActivity.this);
        keyboardUtil.attachTo(mDownNum);*/
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        addFinishBtn.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mJian.setOnClickListener(this);
        //设置计算器的计算监听 实时计算结果并展示
        numberWatcher = new numberWatcher();
        mDownNum.addTextChangedListener(numberWatcher);
        if(!TextUtils.isEmpty(moneyIn)){
            mDownNum.setText(moneyIn);
            setColors(false);

        }

       /* mDownNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            //弹出框
                keyboardUtil.attachTo(mDownNum);
                return false;
            }
        });*/
      /*  keyboardUtil.setOnOkClick(new KeyboardUtil.OnOkClick() {
            @Override
            public void onOkClick() {
                if (validate()) {
                    //tv_price.setText(mDownNum.getText() + "/价格");
                }
            }
        });

        keyboardUtil.setOnCancelClick(new KeyboardUtil.onCancelClick() {
            @Override
            public void onCancellClick() {

            }
        });*/
    }
   /* public boolean validate() {
        if (mDownNum.getText().toString().equals("")) {
            Toast.makeText(getApplication(), "价格不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                //calculatorClear();
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.anime_finish:
                //showMessage("完成");
                playVoice(R.raw.jizhangfinish);
                saveAndFinish();
                break;
            case R.id.anime_add:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(30);
                if(inputTag){
                    showMessage("已输入过");
                    return;
                }
                moneyIn ="";
                inputTag = true;
                String add = "+";
                GlobalVariables.setHasDot(false);
                setNumberKey(add);
                break;
            case R.id.anime_jian:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(30);
                if(inputTag){
                    showMessage("已输入过");
                    return;
                }
                moneyIn ="";
                inputTag = true;
                String jian = "-";
                GlobalVariables.setHasDot(false);
                setNumberKey(jian);
                break;
            case R.id.clear:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(30);
                if(inputTag){
                    inputTag = false;
                }
                //如果是 点 那么则删除点
                if(GlobalVariables.getmHasDot()){
                    //已经点过了
                    GlobalVariables.setHasDot(false);
                }
                if(TextUtils.isEmpty(mDownNum.getText().toString())){
                    return;
                }
                String str="";
                if(!TextUtils.isEmpty(moneyIn)){
                    moneyIn ="";
                   str="";
                    GlobalVariables.setmInputMoney(str);
                    mDownNum.setText(str);
                }else {
                    str = mDownNum.getText().toString().substring(0, mDownNum.length() - 1);
                    GlobalVariables.setmInputMoney(str);
                    mDownNum.setText(str);
                }

                if (TextUtils.isEmpty(str)){
                    mUpNum.setText("0.00");
                    mMoneyIcon.setVisibility(View.VISIBLE);
                    mDownNum.setHint("￥请输入金额");
                    mDownNum.setTextColor(getResources().getColor(R.color.text_bdbdbd));
                }

                break;
        }
    }
    private void saveAndFinish() {
        String moneyString =  mUpNum.getText().toString();
       /* if (moneyString.equals("0.00") || GlobalVariables.getmInputMoney().equals(""))
            showMessage("你还没输入金额");
        else {
            setEditorToAccount();
        }*/
        setEditorToAccount();
    }

    private void setEditorToAccount() {
        FenXiMoneys fenxi = new FenXiMoneys();
        String sUp = mUpNum.getText().toString();
        String sDown = mDownNum.getText().toString().trim();
        String moneys ="0.00";
        if(null == sDown || TextUtils.isEmpty(sDown)){
            moneys="-1";
        }else {
            moneys = sUp;
            if(TextUtils.equals(moneys,"0.00")){
                moneys="-1";
            }
        }
        double sss = Double.parseDouble(moneys);
        if(sss<=0){
            showMessage("请设置有效的预算金额");
            return;
        }
       /* fenxi.setBudgetMoney(sss);
        fenxi.setTime(year+"-"+month);
        String json = new Gson().toJson(fenxi);
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .postBudgets(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        GloableReturn b1 = (GloableReturn) b;
                        showMessage("设置成功");
                        finish();
                        //calculatorClear();
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });*/
        postV2Budget(sss);
    }

    private void postV2Budget( double sss) {
        YuSuanMoneyBean ysBean = new YuSuanMoneyBean();
        ysBean.setAccountBookId(accountBookId);//账本ID
        ysBean.setBudgetMoney(BigDecimal.valueOf(sss));//预算金额
        ysBean.setTime(year+"-"+month);//设置预算/固定支出日期 例2018-07 不传默认当年当月  日常账本
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
        moneyIn ="";
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
            GlobalVariables.setmInputMoney(GlobalVariables.getmInputMoney()+".");
            GlobalVariables.setHasDot(true);
        }
    }
    private void setNumberKey(String add) {
       /* if(!Utils.isFastClick()){
            //showMessage("手速太快了");
            return;
        }*/
        String money = GlobalVariables.getmInputMoney();
        if (GlobalVariables.getmHasDot() && GlobalVariables.getmInputMoney().length()>2) {
            String dot = money.substring(money.length() - 3, money.length() - 2);
            L.liul( "calculatorNumOnclick: " + dot);
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
            if(pos.contains("-")){
                String[] negative = pos.split("\\-");
                Double temp = 0d;
                for (int is = 0; is < negative.length; is++) {
                    endStr = negative[is];
                }
                //endStr = negative[0];
            }else{
                endStr = pos;
            }
        }
        //判断超过8位数不再继续输入
        if(null!=endStr && endStr.contains(".")){
            String[] split = endStr.split("\\.");
            if(null!=split && split.length>0){
                if(split[0].length()>8){
                    showMessage("您输入的位数过大");
                    return;
                }
            }

        }else {
            if(!TextUtils.isEmpty(endStr)){
                if(endStr.length()>=8){
                    String sub = money.substring(money.length() - 1, money.length());
                    if(TextUtils.equals(sub,"+") || TextUtils.equals(sub,"-")){

                    }else {
                        if(TextUtils.equals(add,"+") || TextUtils.equals(add,"-") ){

                        }else {
                            showMessage("您输入的位数过大");
                            return;
                        }
                    }
                }
            }

        }

        if(isFirst==1){
            //清空之前的数字 重新输入（否则明细详情会把金额带过来导致问题）
            isFirst= isFirst+1;
            GlobalVariables.setmInputMoney(add);
        }else {
            GlobalVariables.setmInputMoney(money+add);
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
            temp=charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String ss = editable+"";
            try {
                if(TextUtils.isEmpty(ss)){
                    setColors(false);
                    return;
                }
                if(null!=ss){
                    if(ss.startsWith("+") || ss.startsWith("-")){
                        ss="0"+ss;
                    }
                    setColors(true);
                    setNumForResult(ss);
                }
            }catch (Exception e){
                e.printStackTrace();
                showMessage("数字非法，请重新输入");
                calculatorClear();
            }


        }

    }
    private void setNumForResult(String ss) {
        double sum = Utils.sum(ss);
        //支取小数点后两位数
        DecimalFormat df   = new DecimalFormat("######0.00");
        String format = df.format(sum);
        mUpNum.setText(format);
        mMoneyIcon.setVisibility(View.VISIBLE);
    }
    private void setColors(boolean aa){
        if(aa){
            //设置选中颜色
            mDownNum.setTextColor(getResources().getColor(R.color.text_F15C3C));
            mView1.setBackgroundColor(getResources().getColor(R.color.text_F15C3C));
            mView2.setBackgroundColor(getResources().getColor(R.color.text_F15C3C));
            mBgIcons.setImageResource(R.mipmap.ic_budget_bg_ora);
            mDescs.setTextColor(getResources().getColor(R.color.text_F15C3C));
            mDownNum.setVisibility(View.VISIBLE);
            mUpNum.setTextColor(getResources().getColor(R.color.text_F15C3C));
            mMoneyIcon.setTextColor(getResources().getColor(R.color.text_F15C3C));
        }else{
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
