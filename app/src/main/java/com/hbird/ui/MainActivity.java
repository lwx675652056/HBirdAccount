package com.hbird.ui;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.NotificationMessageActivity;
import com.hbird.base.mvc.activity.SobotNotificationClickReceiver;
import com.hbird.base.mvc.activity.SobotUnReadMsgReceiver;
import com.hbird.base.mvc.base.BasePagerAdapter;
import com.hbird.base.mvc.fragement.LingPiaoFragement;
import com.hbird.base.mvc.widget.NoScrollViewPager;
import com.hbird.base.mvc.widget.TabRadioButton;
import com.hbird.base.util.SPUtil;
import com.hbird.ui.data.FragData;
import com.hbird.ui.index.IndexFragement;
import com.hbird.ui.me.FragMe;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.base.activity.IBaseActivity;
import com.sobot.chat.utils.ZhiChiConstant;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import sing.common.util.LogUtil;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements IBaseActivity {

    RadioGroup rg;
    NoScrollViewPager viewPager;
    LinearLayout mHomeView;
    TabRadioButton btnJz;
    LinearLayout mBottomDh;

    private ArrayList<Fragment> fragements = new ArrayList<>();
    private BasePagerAdapter pagerAdapter;
    private IndexFragement indexFragement;
    private FragMe meFragement;
    private FragData tuBiaoFragement;
    private LingPiaoFragement lpFragement;
    private Long lastClickTime = 0L;
    private static SobotNotificationClickReceiver nClickReceiver;//点击通知以后发出的广播接收者
    private static SobotUnReadMsgReceiver unReadMsgReceiver;//获取未读消息数的广播接收者
    public static boolean isForeground = false;

    TabRadioButton trb1;
    TabRadioButton trb2;
    TabRadioButton trb3;
    TabRadioButton trb4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

        initData();
        initEvent();
    }

    protected void initData() {
        trb1 = findViewById(R.id.button_mingxi);
        trb2 = findViewById(R.id.button_tubiao);
        trb3 = findViewById(R.id.button_lingpp);
        trb4 = findViewById(R.id.button_wo);

//        StatusBarUtil.setStatusBarLightMode(getWindow()); // 导航栏黑色字体

        rg = findViewById(R.id.rg_top);
        viewPager = findViewById(R.id.viewpager);
        mHomeView = findViewById(R.id.ll_home_view);
        btnJz = findViewById(R.id.btn_jizhang);
        mBottomDh = findViewById(R.id.ll_bottom_dh);


        indexFragement = new IndexFragement();
        //chartFragement = new ChartFragement();
        tuBiaoFragement = new FragData();
        lpFragement = new LingPiaoFragement();
        meFragement = new FragMe();

        fragements.add(indexFragement);
        fragements.add(tuBiaoFragement);
        fragements.add(lpFragement);
        fragements.add(meFragement);

        pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragements);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);

        //setView(0);
        /*mMingxi.setImageView(R.mipmap.ic_mingxi);
        mMingxi.setTextColor(getResources().getColor(R.color.colorPrimary));*/

        trb1.setNoClick(true);
        rg.check(R.id.button_mingxi);
        //智齿客服 注册通知
        regReceiver();
        //极光推送
        registerMessageReceiver();
    }

    protected void initEvent() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                trb1.setNoClick(false);
                trb2.setNoClick(false);
                trb3.setNoClick(false);
                trb4.setNoClick(false);
                switch (i) {
                    case R.id.button_mingxi:// 账本首页
                        if (viewPager.getCurrentItem() == 0) {
                            return;
                        }
                        trb1.setNoClick(true);
                        rg.check(R.id.button_mingxi);
                        viewPager.setCurrentItem(0, false);
                        Utils.playVoice(MainActivity.this, R.raw.jizhang);
                        break;
                    case R.id.button_tubiao:// 图标
                        if (viewPager.getCurrentItem() == 1) {
                            return;
                        }
                        trb2.setNoClick(true);
                        rg.check(R.id.button_tubiao);
                        viewPager.setCurrentItem(1, false);
                        Utils.playVoice(MainActivity.this, R.raw.jizhang);
                        break;
                    case R.id.button_lingpp:// 领票票
                        if (viewPager.getCurrentItem() == 2) {
                            return;
                        }
                        trb3.setNoClick(true);
                        rg.check(R.id.button_lingpp);
                        viewPager.setCurrentItem(2, false);
                        Utils.playVoice(MainActivity.this, R.raw.jizhang);
                        break;
                    case R.id.button_wo:// 我的
                        if (viewPager.getCurrentItem() == 3) {
                            return;
                        }
                        trb4.setNoClick(true);
                        rg.check(R.id.button_wo);
                        viewPager.setCurrentItem(3, false);
                        Utils.playVoice(MainActivity.this, R.raw.jizhang);
                        break;
                }
            }
        });
        btnJz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                long timeD = time - lastClickTime;
                lastClickTime = time;
                if (timeD > 300) {
                    String zhangbenId = SPUtil.getPrefString(MainActivity.this, CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
                    if (TextUtils.isEmpty(zhangbenId)) {
                        ToastUtil.showShort("请先选择账本");
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
        rg.check(R.id.button_tubiao);
        viewPager.setCurrentItem(i);
        if (i == 1) {
            tuBiaoFragement.setH5TiaoZhuan(m);
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    private void openJiZhang() {
        Utils.playVoice(MainActivity.this, R.raw.jizhang);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(MainActivity.this, ChooseAccountTypeActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        } else {
            startActivity(new Intent(MainActivity.this, ChooseAccountTypeActivity.class));
        }
    }

    public void setBottomDH2Visiable() {
        btnJz.setVisibility(View.VISIBLE);
        mBottomDh.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        int margins = (int) getResources().getDimension(R.dimen.dp_50_x);
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

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public boolean isUseFragment() {
        return false;
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (lpFragement != null && lpFragement.getUserVisibleHint()) {
            lpFragement.onBackPressed();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                DevRing.activityListManager().exitApp();
            }
        }
    }
}