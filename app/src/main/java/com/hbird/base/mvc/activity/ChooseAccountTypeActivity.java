package com.hbird.base.mvc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.BasePagerAdapter;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.fragement.AccountComeFragementNew1;
import com.hbird.base.mvc.fragement.AccountOutFragementNew1;
import com.hbird.base.mvc.widget.NewGuidePop;
import com.hbird.base.mvc.widget.NoScrollViewPager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SuperSelectComeManager;
import com.hbird.base.util.SuperSelectManager;
import com.hbird.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import sing.common.util.StatusBarUtil;

/**
 * Created by Liul on 2018/7/2.
 * 选择类别 (支出 收入) - 来记一笔点击进入
 */

public class ChooseAccountTypeActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_backs)
    ImageView mBack;
    @BindView(R.id.radio_left_out)
    RadioButton radioLeft;
    @BindView(R.id.radio_right_come)
    RadioButton radioRight;
    @BindView(R.id.tv_center_title)
    TextView mCenterTitle;
    @BindView(R.id.tv_right_title)
    TextView mRightTitle;
    @BindView(R.id.radioGroups)
    RadioGroup mRadioGroup;
    @BindView(R.id.vp)
    NoScrollViewPager viewPager;

    private int type = 0;
    private ArrayList<Fragment> list;
    private AccountOutFragementNew1 outFragement;
    private AccountComeFragementNew1 comeFragement;
    private BasePagerAdapter pagerAdapter;
    private String tag;
    private Bundle bundle;
    private final int FIRST_LENGHT = 4000;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_account_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Utils.initColor(this, Color.rgb(255, 255, 255));
        StatusBarUtil.setStatusBarLightMode(getWindow()); // 导航栏黑色字体

        tag = getIntent().getStringExtra("TAG");
        mRightTitle.setText("完成");
        mRightTitle.setTextColor(ContextCompat.getColor(this,R.color.color_D80200));
        mRightTitle.setVisibility(View.GONE);
        list = new ArrayList<>();
        outFragement = new AccountOutFragementNew1();
        comeFragement = new AccountComeFragementNew1();
        Bundle bundle = new Bundle();
        if(TextUtils.isEmpty(tag)){
            bundle.putString("TAG","");
        }else {
            bundle.putString("TAG","choose");
        }
        outFragement.setArguments(bundle);
        comeFragement.setArguments(bundle);

        list.add(outFragement);
        list.add(comeFragement);
        pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
        if(TextUtils.equals(tag,"shouru")){
            viewPager.setCurrentItem(1);
            radioRight.setChecked(true);
        }else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String name = getIntent().getStringExtra("name");
        if(!TextUtils.isEmpty(name)){
            mCenterTitle.setText(name);
        }
        boolean firstCome = SPUtil.getPrefBoolean(this, CommonTag.APP_FIRST_ZHIYIN_ACCOUNT, true);
        //第一次进入
        if(firstCome){
            setNewGuide();
        }
    }

    private void setNewGuide() {
        View cv = getWindow().getDecorView();
        cv.post(new Runnable() {
            @Override
            public void run() {
                final NewGuidePop pop = new NewGuidePop(ChooseAccountTypeActivity.this, mRadioGroup, "这里切换记账类型~", 1, new NewGuidePop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {
                        SPUtil.setPrefBoolean(ChooseAccountTypeActivity.this, CommonTag.APP_FIRST_ZHIYIN_ACCOUNT,false);
                    }
                });
                pop.showPopWindow();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        if(pop!=null){
                            pop.hidePopWindow();
                            SPUtil.setPrefBoolean(ChooseAccountTypeActivity.this, CommonTag.APP_FIRST_ZHIYIN_ACCOUNT,false);
                        }
                    }

                }, FIRST_LENGHT);
            }
        });
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_left_out:
                        //showMessage("支出");
                        //判断完成按钮 有没有显示出来 显示则执行以下操作
                        //setTypeSetting();
                        playVoice(R.raw.changgui01);
                        int visibilitys = mRightTitle.getVisibility();
                        if(visibilitys==0){
                            setTypeSetting();
                        }
                        viewPager.setCurrentItem(0);
                        type = 0;
                        setTypeSetting();

                        break;
                    case R.id.radio_right_come:
                        //showMessage("收入");
                        //setTypeSetting();
                        playVoice(R.raw.changgui01);
                        viewPager.setCurrentItem(1);
                        type = 1;
                        int visibility = mRightTitle.getVisibility();
                        if(visibility==0){
                            setTypeSetting();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_backs:
                playVoice(R.raw.changgui02);
                onBackPressed();
                break;
            case R.id.tv_right_title:
                //完成点击分两部分（收入 支出） 通过type: 0   1 标识
                playVoice(R.raw.changgui02);
                setTypeSetting();
                break;
        }
    }

    private void setTypeSetting() {
        if(type==0){
            SuperSelectManager.getInstance().setnum(1,"支出完成");
        }else {
            SuperSelectComeManager.getInstance().setnum(2,"收入完成");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // singleInstance 模式必须这么写
        overridePendingTransition( R.anim.slide_in_from_left,R.anim.slide_out_to_right);
    }
}
