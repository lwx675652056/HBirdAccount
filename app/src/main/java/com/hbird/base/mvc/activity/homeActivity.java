package com.hbird.base.mvc.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.base.BasePagerAdapter;
import com.hbird.base.mvc.fragement.ChartFragement;
import com.hbird.base.mvc.fragement.IndexFragement;
import com.hbird.base.mvc.fragement.LingPiaoFragement;
import com.hbird.base.mvc.fragement.MeFragement;
import com.hbird.base.mvc.fragement.TuBiaoFragement;
import com.hbird.base.mvc.widget.NoScrollViewPager;
import com.hbird.base.mvc.widget.TabRadioButton;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.sobot.chat.utils.ZhiChiConstant;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import sing.common.util.LogUtil;

/**
 * Created by Liul(245904552@qq.com) on 2018/6/28.
 * 主页面
 */

public class homeActivity extends BaseActivity<BasePresenter> implements View.OnClickListener {
    @BindView(R.id.rg_top)
    RadioGroup rg;

    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;
    /*@BindView(R.id.left_title)
    TextView mLeftTitle;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title)
    TextView mRightTitle;*/
    @BindView(R.id.ll_home_view)
    LinearLayout mHomeView;
    @BindView(R.id.index_fragment_img_filter)
    ImageView mImgFilter;
    @BindView(R.id.btn_jizhang)
    TabRadioButton btnJz;
    @BindView(R.id.ll_bottom_dh)
    LinearLayout mBottomDh;

    private ArrayList<BaseFragement> fragements = new ArrayList<>();
    private BasePagerAdapter pagerAdapter;
    private IndexFragement indexFragement;
    private ChartFragement chartFragement;
    private MeFragement meFragement;
    private TuBiaoFragement tuBiaoFragement;
    private LingPiaoFragement lpFragement;
    private Long lastClickTime = 0L;
    private static SobotNotificationClickReceiver nClickReceiver;//点击通知以后发出的广播接收者
    private static SobotUnReadMsgReceiver unReadMsgReceiver;//获取未读消息数的广播接收者
    public static boolean isForeground = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LogUtil.e("homeActivity");
    }

    @SuppressLint("NewApi")
    @Override
    protected void initData(Bundle savedInstanceState) {
        indexFragement = new IndexFragement();
        //chartFragement = new ChartFragement();
        tuBiaoFragement = new TuBiaoFragement();
        lpFragement = new LingPiaoFragement();
        meFragement = new MeFragement();

        fragements.add(indexFragement);
        fragements.add(tuBiaoFragement);
        fragements.add(lpFragement);
        fragements.add(meFragement);

        pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragements);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        mImgFilter.setVisibility(View.GONE);
        //setView(0);
        /*mMingxi.setImageView(R.mipmap.ic_mingxi);
        mMingxi.setTextColor(getResources().getColor(R.color.colorPrimary));*/
        rg.check(R.id.button_mingxi);
        //智齿客服 注册通知
        regReceiver();
        //极光推送
        registerMessageReceiver();
    }

    @Override
    protected void initEvent() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.button_mingxi:
                        mImgFilter.setVisibility(View.GONE);
                        rg.check(R.id.button_mingxi);
                        viewPager.setCurrentItem(0);
                        playVoice(R.raw.jizhang);
                        break;
                    case R.id.button_tubiao:
                        playVoice(R.raw.jizhang);
                        mImgFilter.setVisibility(View.GONE);
                        rg.check(R.id.button_tubiao);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.button_lingpp:
                        mImgFilter.setVisibility(View.GONE);
                        rg.check(R.id.button_lingpp);
                        viewPager.setCurrentItem(2);
                        playVoice(R.raw.jizhang);
                        break;
                    case R.id.button_wo:
                        mImgFilter.setVisibility(View.GONE);
                        rg.check(R.id.button_wo);
                        viewPager.setCurrentItem(3);
                        playVoice(R.raw.jizhang);
                        break;
                }
            }
        });
        mImgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openJiZhang();
            }
        });
        btnJz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                long timeD = time - lastClickTime;
                lastClickTime = time;
                if (timeD > 300) {
                    String zhangbenId = SPUtil.getPrefString(homeActivity.this, CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
                    if (TextUtils.isEmpty(zhangbenId)) {
                        showMessage("请先创建账本");
                        return;
                    }
                    openJiZhang();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        isForeground = true;
        handleOpenClick();
        super.onResume();
    }

    public void setTiaozhuanFragement(int i, int m) {
        if (i == 1) {
            LogUtil.e("wo bei zhixing la");
            rg.check(R.id.button_tubiao);
            viewPager.setCurrentItem(i);
            tuBiaoFragement.setH5TiaoZhuan(m);
        } else if (i == 0) {
            rg.check(R.id.button_tubiao);
            viewPager.setCurrentItem(i);
        } else if (i == 2) {
            rg.check(R.id.button_tubiao);
            viewPager.setCurrentItem(i);
        }

    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    private void openJiZhang() {
        playVoice(R.raw.jizhang);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(homeActivity.this, ChooseAccountTypeActivity.class), ActivityOptions.makeSceneTransitionAnimation(homeActivity.this).toBundle());
        } else {
            startActivity(new Intent(homeActivity.this, ChooseAccountTypeActivity.class));
        }
    }

    public void setBottomDH2Visiable() {
        btnJz.setVisibility(View.VISIBLE);
        mBottomDh.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        int margins = (int) getResources().getDimension(R.dimen.height_7_80);
        lp.setMargins(0, 0, 0, margins);
        viewPager.setLayoutParams(lp);
    }

    public void setBottomDH2Gone() {
        btnJz.setVisibility(View.GONE);
        mBottomDh.setVisibility(View.GONE);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        viewPager.setLayoutParams(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102) {
            indexFragement.onActivityResult(101, 102, null);
        }
        if (requestCode == 103 && resultCode == 104) {
            indexFragement.onActivityResult(103, 104, null);
        }
        if (requestCode == 131 && resultCode == 130) {
            indexFragement.onActivityResult(131, 130, null);
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                DevRing.activityListManager().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void regReceiver() {
        IntentFilter filter = new IntentFilter();
        if (nClickReceiver == null) {
            nClickReceiver = new SobotNotificationClickReceiver();
        }
        filter.addAction(ZhiChiConstant.SOBOT_NOTIFICATION_CLICK);
        registerReceiver(nClickReceiver, filter);

        if (unReadMsgReceiver == null) {
            unReadMsgReceiver = new SobotUnReadMsgReceiver();
        }
        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
        registerReceiver(unReadMsgReceiver, filter);
    }

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    private MessageReceiver mMessageReceiver;
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_MESSAGE = "message";

    private void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    //L.liul(showMsg.toString());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setCostomMsg(String msg) {
       /* if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }*/
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        UMShareAPI.get(this).release();
        try {
            if (nClickReceiver != null) {
                unregisterReceiver(nClickReceiver);
            }

            if (unReadMsgReceiver != null) {
                unregisterReceiver(unReadMsgReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    private void handleOpenClick() {
        if (getIntent().getStringExtra(JPushInterface.EXTRA_EXTRA) == null) return;

        try {

            String ss = getIntent().getStringExtra(JPushInterface.EXTRA_EXTRA);
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(ss);
            String jumpPage = jsonObject.getString("jumpPage");
            if (TextUtils.isEmpty(jumpPage)) return;
            String msgId = getIntent().getStringExtra(JPushInterface.EXTRA_MSG_ID);

            switch (jumpPage) {
                case "mxsy":
                    LogUtil.e("mxsy");
                    setTiaozhuanFragement(0, 0);
                    break;
                case "tbtj":
                    LogUtil.e("tbtj");
                    setTiaozhuanFragement(1, 0);
                    break;
                case "tbfx":
                    LogUtil.e("tbfx");
                    setTiaozhuanFragement(1, 1);
                    break;
                case "tbzc":
                    LogUtil.e("tbzc");
                    setTiaozhuanFragement(1, 2);
                    break;
                case "jzsy":
                    LogUtil.e("jzsy");
                    startActivity(new Intent(this, ChooseAccountTypeActivity.class));
                    break;
                case "lppsy":
                    setTiaozhuanFragement(2, 0);
                    break;
                case "fftz":
                    startActivity(new Intent(this, NotificationMessageActivity.class));
                    break;
            }


        } catch (Exception e) {
            LogUtil.e("parse notification error");
        }
    }

    private String getPushSDKName(byte whichPushSDK) {
        String name;
        switch (whichPushSDK) {
            case 0:
                name = "jpush";
                break;
            case 1:
                name = "xiaomi";
                break;
            case 2:
                name = "huawei";
                break;
            case 3:
                name = "meizu";
                break;
            case 8:
                name = "fcm";
                break;
            default:
                name = "jpush";
        }
        return name;
    }

}
