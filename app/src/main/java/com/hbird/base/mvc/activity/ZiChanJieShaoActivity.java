package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import butterknife.BindView;

import static android.R.attr.id;

/**
 * Created by Liul on 2018/10/26.
 */

public class ZiChanJieShaoActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.iv_back_web)
    ImageView mBack;
    @BindView(R.id.left_title)
    TextView mLeftTitle;
    @BindView(R.id.center_title)
    TextView mCenterTitles;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_zichan_sm;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mLeftTitle.setVisibility(View.GONE);
        mCenterTitles.setVisibility(View.VISIBLE);
        mCenterTitles.setText("资产说明");
        mBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
