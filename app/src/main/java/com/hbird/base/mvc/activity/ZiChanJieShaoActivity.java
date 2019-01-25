package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hbird.base.R;

import sing.common.util.StatusBarUtil;

/**
 * Created by Liul on 2018/10/26.
 */

public class ZiChanJieShaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zichan_sm);
        initData();
    }

    protected void initData() {
        StatusBarUtil.clearStatusBarDarkMode(getWindow());

        findViewById(R.id.id_back).setOnClickListener(v -> onBackPressed());
        FrameLayout toolBar = findViewById(R.id.toolbar);
        toolBar.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_main_bg));
        ((TextView) findViewById(R.id.id_title)).setText("资产说明");
        ViewGroup.LayoutParams params = toolBar.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(this);
        toolBar.setLayoutParams(params);
        toolBar.setPadding(0, StatusBarUtil.getStateBarHeight(this), 0, 0);
    }
}