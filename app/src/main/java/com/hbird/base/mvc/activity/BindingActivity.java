package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.ModifiPhoneNumBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hbird.base.R.id.tv_submit;

/**
 * Created by Liul on 2018/7/21.
 * 绑定手机号
 */

public class BindingActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.userName)
    EditText mUserName;
    @BindView(R.id.et_yzm)
    EditText mYanZM;
    @BindView(tv_submit)
    TextView mSubmit;
    @BindView(R.id.pwd)
    EditText mPwd;
    @BindView(R.id.setpwd_view)
    CheckBox mSetpwd;
    @BindView(R.id.iv_login)
    RelativeLayout mLogin;
    @BindView(R.id.rl_password)
    RelativeLayout mPassword;
    @BindView(R.id.views)
    View mViews;

    boolean bShowPassWord = false;
    //判断页面中 是否需要显示密码
    private boolean passwod = false;
    private String token;
    private String phone;
    private TimeCount time;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_binging_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("绑定手机号");
        mCenterTitle.setTextSize(18);
        mRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        String type = getIntent().getStringExtra("TYPE");
        if(TextUtils.equals(type,"phone")){
            mPassword.setVisibility(View.GONE);
            mViews.setVisibility(View.GONE);
            passwod = false;
        }else {
            mPassword.setVisibility(View.VISIBLE);
            mViews.setVisibility(View.VISIBLE);
            passwod = true;
        }
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }
    @OnClick({R.id.iv_back, R.id.iv_login, R.id.setpwd_view, R.id.tv_submit})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_login:
                //showMessage("绑定手机号");
                String yzm = mYanZM.getText().toString().trim();
                phone = mUserName.getText().toString().trim();
                if (checkPhone()) return;
                if(TextUtils.isEmpty(yzm)){
                    showMessage("请输入验证码");
                    return;
                }
                String pwd="";
                if(passwod){
                    pwd = mPwd.getText().toString().trim();
                    if(TextUtils.isEmpty(pwd)){
                        showMessage("请输入密码");
                        return;
                    }
                }


                ModifiPhoneNumBean mb = new ModifiPhoneNumBean();
                mb.setMobile(phone);
                mb.setVerifycode(yzm);
                if(passwod){
                    mb.setPassword(pwd);
                }
                String json = new Gson().toJson(mb);
                showProgress("");
                NetWorkManager.getInstance().setContext(this)
                        .postModifiPhoneNumber(json, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                hideProgress();
                                //绑定手机号
                               showMessage("绑定成功");
                                Intent intent = new Intent();
                                intent.putExtra("PHONE",phone);
                                setResult(302,intent);
                                finish();

                            }

                            @Override
                            public void onError(String s) {
                                hideProgress();
                                showMessage(s);
                            }
                        });
                break;
            case R.id.tv_submit:
                //showMessage("获取验证码");
                phone = mUserName.getText().toString().trim();
                if (checkPhone()) return;

                time = new TimeCount(60000, 1000);
                time.start();// 开始计时
                showProgress("");
                NetWorkManager.getInstance().setContext(this)
                        .postGetPhonesNewYzm(phone, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                hideProgress();
                                showMessage("验证码获取成功,请稍等");
                            }

                            @Override
                            public void onError(String s) {
                                hideProgress();
                                showMessage(s);
                            }
                        });
                break;
            //查看密码
            case R.id.setpwd_view:
                bShowPassWord = !bShowPassWord;
                if(bShowPassWord){
                    //如果选中，显示密码
                    mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPwd.setSelection(mPwd.getText().length());
                }else{
                    //否则隐藏密码
                    mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPwd.setSelection(mPwd.getText().length());
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(time!=null){
            time.cancel();//取消倒计时
        }
    }
    private boolean checkPhone() {
        if(TextUtils.isEmpty(phone)){
            showMessage("请输入手机号");
            return true;
        }
        if(phone.length()!=11){
            showMessage("请检查手机号");
            return true;
        }
        return false;
    }
    //倒计时的类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mSubmit.setText("获取验证码");
            mSubmit.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mSubmit.setClickable(false);//防止重复点击
            mSubmit.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
