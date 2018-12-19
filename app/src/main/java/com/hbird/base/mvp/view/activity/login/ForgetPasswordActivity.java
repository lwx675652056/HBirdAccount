package com.hbird.base.mvp.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.app.GestureUtil;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvp.model.login.ForgetPasswordModel;
import com.hbird.base.mvp.model.login.loginModel;
import com.hbird.base.mvp.presenter.login.loginPresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import com.hbird.base.mvp.view.iview.login.IForgetPasswordView;
import com.hbird.base.mvp.presenter.login.ForgetPasswordPresenter;

import com.hbird.base.R;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hbird.base.R.id.bt_my_login;
import static com.hbird.base.R.id.tv_submit;


public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements IForgetPasswordView {

    @BindView(R.id.userName)
    TextInputLayout mUserName;
    @BindView(R.id.et_yzm)
    TextInputEditText mYanZM;
    @BindView(tv_submit)
    TextView mSubmit;
    @BindView(R.id.pwd)
    TextInputLayout mPwd;
    @BindView(bt_my_login)
    RelativeLayout mLogin;
    @BindView(R.id.ll_back)
    LinearLayout mBack;

    private TimeCount time;
    ForgetPasswordPresenter mPresenter = null;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView(Bundle bundle) {

    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void initEvent() {
        mUserName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = mUserName.getEditText().getText().toString().trim();
                String password = mYanZM.getText().toString().trim();
                String pwds = mPwd.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(pwds)){
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                }else {
                    mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mYanZM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = mUserName.getEditText().getText().toString().trim();
                String password = mYanZM.getText().toString().trim();
                String pwds = mPwd.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(pwds)){
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                }else {
                    mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = mUserName.getEditText().getText().toString().trim();
                String password = mYanZM.getText().toString().trim();
                String pwds = mPwd.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(pwds)){
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                }else {
                    mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    boolean bShowPassWord = false;
    @OnClick({R.id.ll_back, bt_my_login, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            //登录
            case bt_my_login:
                //showMessage("登录");
                doReset();
                break;
            //查看密码
           /* case R.id.setpwd_view:
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
                break;*/
            //获取验证码
            case R.id.tv_submit:
                String strPhone    = mUserName.getEditText().getText().toString();
                if (strPhone==null || strPhone.trim().isEmpty()){
                    RingToast.show("请输入手机号！");
                }else {
                    time = new TimeCount(60000, 1000);
                    time.start();// 开始计时
                    mPresenter = new ForgetPasswordPresenter(this, new ForgetPasswordModel());
                    mPresenter.getVerifycodeToResetpwd(strPhone);
                }
                break;
        }
    }

    private void  doReset(){
        String strPhone    = mUserName.getEditText().getText().toString();
        String strPwd    = mPwd.getEditText().getText().toString();
        String strYanZM    = mYanZM.getText().toString();
        //
        mPresenter = new ForgetPasswordPresenter(this, new ForgetPasswordModel());
        mPresenter.resetPassword(strPhone, strPwd, strYanZM );
    }

    @Override
    public void resetPasswordSuccess() {
        RingToast.show("密码重置成功！");
        DevRing.activityListManager().killActivity(loginActivity.class); //退出loginActivity
        //
        Intent intentNew = new Intent(ForgetPasswordActivity.this, CreateGestureActivity.class);
        //清空手势密码
        GestureUtil.clearGesturePassword();
        //关闭手势密码开关（必须重新打开设置）
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);
        startActivity(new Intent(getApplicationContext(), homeActivity.class));
        finish();
    }

    @Override
    public void resetPasswordFaild() {
        RingToast.show("密码重置失败！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(time!=null){
            time.cancel();//取消倒计时
        }
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
