package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.widget.IosLikeToggleButton;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.mvp.view.activity.login.ShouShiPasswordActivity;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul on 2018/10/10.
 * 设置
 */

public class SettingsActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTit;
    @BindView(R.id.toggle_btn)
    com.hbird.base.mvc.widget.IosLikeToggleButton mToggleButton;

    private String phones;
    private String weiXin;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRightTit.setVisibility(View.GONE);
        mCenterTitle.setText("设置");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //根据sp中存的值 显示是否打开
        boolean isOpen = SPUtil.getPrefBoolean(this, com.hbird.base.app.constant.CommonTag.VOICE_KEY, true);
        if (isOpen) {
            //mVoiceImg.setBackgroundResource(R.mipmap.icon_btn_open);
            mToggleButton.setChecked(true);
        } else {
            //mVoiceImg.setBackgroundResource(R.mipmap.icon_btn_close);
            mToggleButton.setChecked(false);
        }
        phones = getIntent().getStringExtra("PHONE");
        weiXin = getIntent().getStringExtra("WEIXIN");
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        //设置开关监听事件
        mToggleButton.setOnCheckedChangeListener(new IosLikeToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(IosLikeToggleButton buttonView, boolean isChecked) {
                boolean checked = mToggleButton.isChecked();//获取开关状态
                if (!checked) {
                    playVoice(R.raw.changgui02);
                }
                SPUtil.setPrefBoolean(SettingsActivity.this, com.hbird.base.app.constant.CommonTag.VOICE_KEY, checked);
            }
        });
    }

    @OnClick({R.id.ll_account_safe, R.id.ll_usefo, R.id.ll_about_us, R.id.ll_voice, R.id.ll_shoushi})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.ll_account_safe:// 账户安全
                playVoice(R.raw.changgui02);
                Intent intent3 = new Intent();
                intent3.setClass(this, AccountSafeActivity.class);
                intent3.putExtra("PHONE", phones);
                intent3.putExtra("WEIXIN", weiXin);
                startActivity(intent3);
                break;
            case R.id.ll_shoushi:
                playVoice(R.raw.changgui02);
                startActivity(new Intent(this, ShouShiPasswordActivity.class));
                break;
            case R.id.ll_usefo:// 使用手册
                playVoice(R.raw.changgui02);
                Intent intent2 = new Intent(this, WebViewActivity.class);
                intent2.putExtra("TYPE", "shouce");
                startActivity(intent2);
                break;
            case R.id.ll_about_us:// 关于我们
                playVoice(R.raw.changgui02);
                startActivity(new Intent(this, AboutOursActivity.class));
                break;
        }
    }
}
