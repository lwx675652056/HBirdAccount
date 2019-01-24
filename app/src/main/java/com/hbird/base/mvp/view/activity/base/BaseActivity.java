package com.hbird.base.mvp.view.activity.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.view.dialog.DialogToGig;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.ToastUtils;
import com.hbird.base.util.VoiceUtils;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.ColorBar;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.ButterKnife;
import sing.common.util.StatusBarUtil;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseActivity {

    @BindColor(R.color.colorPrimary)
    protected int mColor;
    @BindColor(R.color.black)
    protected int mBlack;
    @Inject
    @Nullable
    protected P mPresenter;
    private Context mContext;

    protected abstract int getContentLayout();//返回页面布局id

    protected abstract void initView(Bundle savedInstanceState);//做视图相关的初始化工作

    protected abstract void initData(Bundle savedInstanceState);//做数据相关的初始化工作

    protected abstract void initEvent();//做监听事件相关的初始化工作

    public ProgressDialog myDialog;

    private static long lastClickTime;
    private DialogToGig dialogToGig;
    private VoiceUtils voiceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        voiceUtils = VoiceUtils.newInstance(getApplicationContext());

        if (getContentLayout() != 0) {
            setContentView(getContentLayout());
            ButterKnife.bind(this);
        }
        StatusBarUtil.setStatusBarLightMode(getWindow());
        initBarColor(Color.WHITE,Color.WHITE);
        initView(savedInstanceState);
        initData(savedInstanceState);
        initEvent();
    }

    public void initBarColor(int navColor,int statusColor) {
        ViewGroup parent = findViewById(android.R.id.content);
        if (parent.getChildAt(0) instanceof DrawerLayout) {
            ColorBar.newDrawerBuilder()
                    .applyNav(true)
                    .navColor(navColor)
                    .navDepth(0)
                    .statusColor(statusColor)
                    .statusDepth(0)
                    .build(this)
                    .apply();
        } else {
            ColorBar.newColorBuilder()
                    .applyNav(true)
                    .navColor(navColor)
                    .navDepth(0)
                    .statusColor(statusColor)
                    .statusDepth(0)
                    .build(this)
                    .apply();
        }
    }

    public void showMessage(String arg) {
        ToastUtils.ShowMessage(mContext, arg);
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public boolean isUseFragment() {
        return true;
    }

    public void showProgress(String title) {

        if (dialogToGig == null) {
            dialogToGig = new DialogToGig(this);
        }
        dialogToGig.builder().show();

    }

    public void hideProgress() {

        if (dialogToGig != null) {
            dialogToGig.hide();
            dialogToGig = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 0;
    }

    public void playVoice(int resid) {
        boolean opens = SPUtil.getPrefBoolean(this, CommonTag.VOICE_KEY, true);
        if (opens) {
            try {
                if (voiceUtils == null){
                    voiceUtils = VoiceUtils.newInstance(getApplicationContext());
                }
                voiceUtils.playVoice(resid);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }
}