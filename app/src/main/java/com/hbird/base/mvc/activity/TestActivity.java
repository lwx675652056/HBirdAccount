package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

import static com.hbird.base.R.id.tv;

/**
 * Created by Liul on 2018/10/15.
 * 极光推送 点击消息时的展示界面
 */

public class TestActivity extends BaseActivity<BasePresenter> implements View.OnClickListener {
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.right_title2)
    TextView mRightTit;
    @BindView(R.id.text)
    TextView mTexts;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_texst_jpush;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("蜂鸟通知");
        mRightTit.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = null;
            String content = null;
            if(bundle!=null){
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            mTexts.setText(content);
        }
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
