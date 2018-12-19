package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.Information;

import java.util.HashSet;

import butterknife.BindView;

/**
 * Created by Liul on 2018/10/11.
 */

public class KeFuActivity extends BaseActivity<BasePresenter> implements View.OnClickListener {
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.right_title2)
    TextView mRightTit;
    private Information info;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_kefu;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("联系客服");
        mRightTit.setVisibility(View.GONE);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

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
