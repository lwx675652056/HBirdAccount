package com.hbird.base.mvp.view.activity.account;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;

import com.hbird.base.mvp.view.activity.base.BaseActivity;

import com.hbird.base.mvp.view.iview.account.IAccountMainView;
import com.hbird.base.mvp.presenter.account.AccountMainPresenter;

import com.hbird.base.R;

import butterknife.BindString;
import butterknife.BindView;


public class AccountMainActivity extends BaseActivity<AccountMainPresenter> implements IAccountMainView {

    @BindString(R.string.douban_movie)
    String mStrTitle;

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nv_menu)
    NavigationView mNavigationView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_account_main;
    }

    @Override
    protected void initView(Bundle bundle) {
        //如果调用了setSupportActionBar，那就必须在setSupportActionBar之前将标题置为空字符串，否则设置具体标题会无效
        mToolbar.setTitle("");
        this.setSupportActionBar(mToolbar);
        mToolbar.setTitle(mStrTitle);
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void initEvent() {

    }

}
