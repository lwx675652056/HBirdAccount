package com.hbird.ui.login_register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hbird.base.R;
import com.hbird.base.databinding.ActRegisterBinding;
import com.hbird.base.mvc.activity.WebViewActivity;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvp.view.activity.login.loginActivity;
import com.hbird.bean.TitleBean;
import com.hbird.ui.fill_invitation.ActFillInvitation;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;

import sing.common.base.BaseActivity;
import sing.common.listener.OnTextChangedListener;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActRegister
 * @date: 2019/1/7 16:50
 * @Description: 注冊
 */
public class ActRegister extends BaseActivity<ActRegisterBinding, RegisterModle> {

    private RegisterData data;
    private TimeCount time;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_register;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        data = new RegisterData();
        data.setTime("获取验证码");
        data.setClickable(true);
        binding.setData(data);
        binding.setListener(new OnClick());

        TitleBean t = new TitleBean("登录注册");
        t.setBg_color(R.color.white);
        t.setRightColor(Color.parseColor("#333333"));
        t.setBackIcon(ContextCompat.getDrawable(this, R.mipmap.nav_back_normal));
        binding.setTitle(t);
        binding.toolbar.ivBack.setOnClickListener(v -> onBackPressed());

        String strs = "注册即同意 " + "<font color='#2F54EB'>" + "《蜂鸟记账使用协议》" + "</font>";
        data.setAgreement(Html.fromHtml(strs));

        Utils.initColor(this, Color.rgb(255, 255, 255));
        StatusBarUtil.setStatusBarLightMode(getWindow());

        binding.etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tvHint.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(data.getPassword())) {
                    binding.tvHint.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.etPhone.addTextChangedListener(new OnTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (data.getPhone1().length() > 0) {
                    binding.etPhone.setSelection(binding.etPhone.getText().toString().trim().length());
                }
            }
        });

        time = new TimeCount(60000, 1000);
    }

    public class OnClick {
        public void onFocus(View view) {
            binding.etPassword.setFocusable(true);
            binding.etPassword.requestFocus();
            binding.etPassword.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.etPassword, 0);
        }

        // 隐藏显示密码
        public void showPassword(View view) {
            binding.etPassword.setTransformationMethod(data.isShowPassword() ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
            data.setShowPassword(!data.isShowPassword());
        }

        // 协议
        public void agreement(View view) {
            Intent intent = new Intent(ActRegister.this, WebViewActivity.class);
            intent.putExtra("TYPE", "xieyi");
            startActivity(intent);
        }

        // 验证码
        public void getCode(View view) {
            if (data.getPhone() == null || data.getPhone().length() != 11 || !data.getPhone().startsWith("1")) {
                ToastUtil.showShort("请输入正确的手机号");
            } else {
                viewModel.getCode(data.getPhone(), toHome -> {
                    if (toHome){
                        time.start();// 开始计时
                    }
                });
            }
        }

        // 注册
        public void register(View view) {
            if (TextUtils.isEmpty(data.getPhone()) || data.getPhone().length()!=11 ||!data.getPhone().startsWith("1")){
                ToastUtil.showShort("请输入正确的手机号");
                return;
            }
            if (TextUtils.isEmpty(data.getCode())){
                ToastUtil.showShort("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(data.getPassword())||data.getPassword().length()<6){
                ToastUtil.showShort("请输入6位以上密码");
                return;
            }

            viewModel.register(data.getPhone(),data.getPassword(),data.getCode(), toHome -> {
                ToastUtil.showShort("注册成功");
                if (toHome){// 当前时间超过注册时间3分钟，直接去首页不填写邀请码
                    startActivity(new Intent(ActRegister.this, homeActivity.class));
                }else{
                    startActivity(new Intent(ActRegister.this, ActFillInvitation.class));
                }

                DevRing.activityListManager().killActivity(loginActivity.class); //退出loginActivity
                finish();
            });
        }
    }

    //倒计时的类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            data.setTime("获取验证码");
            data.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            data.setClickable(false);
            data.setTime(millisUntilFinished / 1000 + "s");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();//取消倒计时
        }
    }
}