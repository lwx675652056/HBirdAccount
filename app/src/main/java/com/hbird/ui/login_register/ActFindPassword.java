package com.hbird.ui.login_register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hbird.base.R;
import com.hbird.base.databinding.ActFindPasswordBinding;
import com.hbird.bean.TitleBean;
import com.hbird.ui.MainActivity;
import com.hbird.util.Utils;

import sing.common.base.BaseActivity;
import sing.common.listener.OnTextChangedListener;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActFindPassword
 * @date: 2019/1/23 14:04
 * @Description: 找回密码
 */
public class ActFindPassword extends BaseActivity<ActFindPasswordBinding,FindPasswordModle>{

    private RegisterData data;
    private TimeCount time;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_find_password;
    }

    @Override
    public void initData() {
        data = new RegisterData();
        data.setTime("获取验证码");
        data.setClickable(true);
        binding.setData(data);
        binding.setListener(new OnClick());

        TitleBean t = new TitleBean("找回密码");
        binding.setTitle(t);
        binding.toolbar.ivBack.setOnClickListener(v -> onBackPressed());

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

    @Override
    public int initVariableId() {
        return 0;
    }

    public class OnClick {
        public void clear(View view) {
            data.setPhone1("");
        }

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

        // 验证码
        public void getCode(View view) {
            if (data.getPhone() == null || data.getPhone().length() != 11 || !data.getPhone().startsWith("1")) {
                ToastUtil.showShort("请输入正确的手机号");
            } else {
                viewModel.getCode(data.getPhone(), toHome -> time.start());// 开始计时
            }
        }

        // 找回
        public void register(View view) {
            if (TextUtils.isEmpty(data.getPhone()) || data.getPhone().length() != 11 || !data.getPhone().startsWith("1")) {
                ToastUtil.showShort("请输入正确的手机号");
                return;
            }
            if (TextUtils.isEmpty(data.getCode())) {
                ToastUtil.showShort("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(data.getPassword()) || data.getPassword().length() < 6) {
                ToastUtil.showShort("请输入6位以上密码");
                return;
            }

            viewModel.find(data.getPhone(), data.getPassword(), data.getCode(), toHome -> {
                ToastUtil.showShort("密码重置成功！");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
}
