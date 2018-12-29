package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
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
import com.hbird.base.mvc.fragement.AccountComeFragementNew;
import com.hbird.base.mvc.fragement.AccountOutFragementNew;
import com.hbird.base.mvc.widget.NewGuidePop;
import com.hbird.base.mvc.widget.NoScrollViewPager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SuperSelectComeManager;
import com.hbird.base.util.SuperSelectManager;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * @author: LiangYX
 * @ClassName: ActSetAccountType
 * @date: 2018/12/17 14:21
 * @Description: 我的 - 账目类别 -
 */
public class ActSetAccountType extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
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
    private AccountOutFragementNew outFragement;
    private AccountComeFragementNew comeFragement;
    private BasePagerAdapter pagerAdapter;
    private String tag;
    private Bundle bundle;
    private final int FIRST_LENGHT = 4000;

    public int accountType = 1;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_account_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        String type = getIntent().getExtras().getString("account_id");
        accountType = Integer.parseInt(type);

        tag = getIntent().getStringExtra("TAG");
        mRightTitle.setVisibility(View.GONE);
        list = new ArrayList<>();
        outFragement = new AccountOutFragementNew();
        comeFragement = new AccountComeFragementNew();
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(tag)) {
            bundle.putString("TAG", "");
        } else {
            bundle.putString("TAG", "choose");
        }
        outFragement.setArguments(bundle);
        comeFragement.setArguments(bundle);

        list.add(outFragement);
        list.add(comeFragement);
        pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
        if (TextUtils.equals(tag, "shouru")) {
            viewPager.setCurrentItem(1);
            radioRight.setChecked(true);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mCenterTitle.setText("类别设置");
        boolean firstCome = SPUtil.getPrefBoolean(this, CommonTag.APP_FIRST_ZHIYIN_ACCOUNT, true);
        //第一次进入
        if (firstCome) {
            setNewGuide();
        }
    }

    private void setNewGuide() {
        View cv = getWindow().getDecorView();
        cv.post(new Runnable() {
            @Override
            public void run() {
                final NewGuidePop pop = new NewGuidePop(ActSetAccountType.this, mRadioGroup, "这里切换记账类型~", 1, new NewGuidePop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {
                        SPUtil.setPrefBoolean(ActSetAccountType.this, CommonTag.APP_FIRST_ZHIYIN_ACCOUNT, false);
                    }
                });
                pop.showPopWindow();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (pop != null) {
                            pop.hidePopWindow();
                            SPUtil.setPrefBoolean(ActSetAccountType.this, CommonTag.APP_FIRST_ZHIYIN_ACCOUNT, false);
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
                        if (visibilitys == 0) {
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
                        if (visibility == 0) {
                            setTypeSetting();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_backs:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_right_title:
                //完成点击分两部分（收入 支出） 通过type: 0   1 标识
                playVoice(R.raw.changgui02);
                setTypeSetting();
                break;
        }
    }

    private void setTypeSetting() {
        if (type == 0) {
            SuperSelectManager.getInstance().setnum(1, "支出完成");
        } else {
            SuperSelectComeManager.getInstance().setnum(2, "收入完成");
        }
    }
}