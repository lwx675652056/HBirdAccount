package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.widget.cycleView;
import com.hbird.base.mvp.presenter.login.loginPresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Liul on 2018/7/21.
 * 登录界面  手势密码忘记后跳转到此界面
 */

public class RegisterViewActivity extends BaseActivity<loginPresenter> implements View.OnClickListener {
    @BindView(R.id.ll_back)
    LinearLayout mBack;
    @BindView(R.id.imagess)
    cycleView mImages;
    @BindView(R.id.tv_nick_name)
    TextView mNickName;

    @BindView(R.id.ll_about_phone)
    LinearLayout mAboutPhone;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.et_yzm)
    EditText mYzm;
    @BindView(R.id.tv_submit)
    TextView mSubmit;

    @BindView(R.id.bt_my_login)
    Button mPhoneLogin;
    @BindView(R.id.rl_wx_login)
    RelativeLayout mWxLogin;
    @BindView(R.id.rl_exit)
    RelativeLayout mExit;
    @BindView(R.id.tv_desc)
    TextView mDesc;



    @Override
    protected int getContentLayout() {
        return R.layout.activity_register_views;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //判断当前是微信登录 还是手机登录
        if(true){
            //先默认手机
            mDesc.setText("获取登录手机号的验证码解锁，解锁后原手势密码失效");
        }else {
            //微信
            mAboutPhone.setVisibility(View.GONE);
            mPhoneLogin.setVisibility(View.GONE);
            mWxLogin.setVisibility(View.VISIBLE);
            mDesc.setText("使用微信授权登录解锁，解锁后原手势密码失效");
        }
        //昵称  头像设置
        mNickName.setText("昵称");
        mUserName.setText("17744588290");

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mWxLogin.setOnClickListener(this);
        mPhoneLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_exit:
                showMessage("退出登录");
                break;
            case R.id.rl_wx_login:
                showMessage("微信登录");
                break;
            case R.id.bt_my_login:
                showMessage("确认");
                break;
        }
    }
}
