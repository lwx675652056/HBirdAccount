package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.FenXiMoneys;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;

/**
 * Created by Liul on 2018/8/15.
 * 可支配金额设置
 */

public class CanSettingMoneyActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.iv_finish)
    TextView mFinished;
    @BindView(R.id.et_moneyToBig)
    EditText mMoneyBig;
    @BindView(R.id.et_moneyToLife)
    EditText mMoneyLife;
    private String month;
    private String year;
    private String largeNum;
    private String lifeNum;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_can_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("可支配金额设置");
        mRightTitle.setVisibility(View.GONE);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        month = getIntent().getStringExtra("MONTH");
        year = getIntent().getStringExtra("YEAR");
        largeNum = getIntent().getStringExtra("LargeExpenditure");
        lifeNum = getIntent().getStringExtra("LifeExpenditure");
        if(!TextUtils.isEmpty(largeNum) && !TextUtils.equals(largeNum,"null")){
            mMoneyBig.setText(largeNum);
        }
        if(!TextUtils.isEmpty(lifeNum)&& !TextUtils.equals(lifeNum,"null")){
            mMoneyLife.setText(lifeNum);
        }

    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mFinished.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.iv_finish:
                playVoice(R.raw.changgui02);
                String BigMoney = mMoneyBig.getText().toString().trim();
                String lifeMoney = mMoneyLife.getText().toString().trim();
                boolean a=false;
                boolean b=false;
                if(TextUtils.isEmpty(BigMoney)){
                    BigMoney= -1+"";
                    a = true;
                }
                if(TextUtils.isEmpty(lifeMoney)){
                    lifeMoney = -1+"";
                    b = true;
                }
                double BigMoneys = Double.parseDouble(BigMoney);
                if(BigMoneys<0){
                    if(!a){
                        showMessage("每月固定大额总支出不能小于0");
                        return;
                    }
                }

                double lifeMoneys = Double.parseDouble(lifeMoney);
                if(lifeMoneys<0){
                    if(!b){
                        showMessage("每月固定生活总支出不能小于0");
                        return;
                    }

                }
                FenXiMoneys fenxi = new FenXiMoneys();
                //fenxi.setTime(year+"-"+month);
                //fenxi.setBudgetMoney(3000);
                fenxi.setFixedLargeExpenditure(BigMoneys);
                fenxi.setFixedLifeExpenditure(lifeMoneys);
                String json = new Gson().toJson(fenxi);
                String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");

                showProgress("");
                NetWorkManager.getInstance().setContext(this)
                        .postBudgets(json, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                hideProgress();
                                GloableReturn b1 = (GloableReturn) b;
                                setResult(331);
                                finish();

                            }

                            @Override
                            public void onError(String s) {
                                hideProgress();
                                showMessage(s);
                            }
                        });
                break;


        }
    }

}
