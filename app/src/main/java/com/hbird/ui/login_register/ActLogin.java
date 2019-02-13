package com.hbird.ui.login_register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.RingApplication;
import com.hbird.base.databinding.ActLoginBinding;
import com.hbird.base.wxapi.WXEntryActivity;
import com.hbird.ui.MainActivity;
import com.hbird.ui.fill_invitation.ActFillInvitation;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import sing.common.base.BaseActivity;
import sing.common.listener.OnTextChangedListener;
import sing.common.ui.LoadingDialog;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;


public class ActLogin extends BaseActivity<ActLoginBinding, LoginModle> {

    private LoginData data;
    private LoadingDialog dialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_login;
    }

    public class OnClick {
        // 切换账号、验证码登录
        public void change() {
            data.setIsPassword(!data.isPassword());
        }

        // 登录
        public void login(View view) {
            if (dialog != null && !dialog.isShowing()) {
                runOnUiThread(() -> dialog.show());
            }
            if (data.isPassword()) {//密码登录
                viewModel.login(data.getPhone(), data.getPassword(), toHome -> toStart(toHome));
            } else { //验证码登录
                viewModel.loginByVerifyCode(data.getPhone(), data.getCode(), toHome -> toStart(toHome));
            }
        }

        //微信登录
        public void wxLogin(View view) {
            doWeChatLogin();
        }

        // 获取验证码
        public void getCode(View view) {
            Utils.playVoice(ActLogin.this, R.raw.jizhang);
            if (data.getPhone().length() == 11 && data.getPhone().startsWith("1")) {
                viewModel.getCode(data.getPhone(), toHome -> time.start());// 开始计时
            } else {
                ToastUtil.showShort("请输入正确的手机号");
            }
        }

        // 清除手机号
        public void clear(View view) {
            data.setPhone1("");
        }

        // 隐藏显示密码
        public void showPassword(View view) {
            binding.etPassword.setTransformationMethod(data.isShowPassword() ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
            data.setShowPassword(!data.isShowPassword());
        }

        // 注册
        public void register(View view) {
            startActivity(new Intent(ActLogin.this, ActRegister.class));
        }

        // 找回密码
        public void findPassword(View view) {
            startActivity(new Intent(ActLogin.this, ActFindPassword.class));
        }
    }

    @Override
    public void initData() {
        Utils.initColor(this, Color.parseColor("#FFFFFF"));
        StatusBarUtil.setStatusBarLightMode(getWindow()); // 导航栏白色字体

        dialog = new LoadingDialog(this);

        findViewById(R.id.toolbar).findViewById(R.id.iv_backs).setOnClickListener(v -> onBackPressed());
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.tv_center_title)).setText("登录蜂鸟记账");

        data = new LoginData();
        binding.setData(data);
        binding.setListener(new OnClick());

        time = new TimeCount(60000, 1000);

        // 微信登录回调
        WXEntryActivity.setListener((position, data, type) -> {
            if (dialog != null && !dialog.isShowing()) {
                runOnUiThread(() -> dialog.show());
            }
            String deviceId = Utils.getDeviceInfo(this);
            viewModel.getAccessToken((String) data, deviceId, Utils.getChannelName(this), toHome -> toStart(toHome));
        });

        binding.etPhone.addTextChangedListener(new OnTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (data.getPhone1().length() > 0) {
                    binding.etPhone.setSelection(binding.etPhone.getText().toString().trim().length());
                }
            }
        });
    }


    private TimeCount time;

    private void doWeChatLogin() {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!Utils.isWeChatAppInstalled(this)) {
            ToastUtil.showShort("您还未安装微信客户端");
        } else {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            //像微信发送请求
            RingApplication.mWxApi.sendReq(req);
        }
    }

    private void toStart(boolean toHome) {
        Intent intent = getIntent();//获取传来的intent对象
        String data = intent.getStringExtra("comfrom");
        if (data != null && data.equals("splash_overmaxnum")) {//路径：splash - gesture - login,验证成功后进入主界面，并清空手势密码
            //清空手势密码
            GestureUtil.clearGesturePassword();
            //关闭手势密码开关（必须重新打开设置）
            DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);
        }

        if (dialog != null && dialog.isShowing()) {
            runOnUiThread(() -> dialog.dismiss());
        }

        if (toHome) {// 当前时间超过注册时间3分钟，直接去首页不填写邀请码
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), ActFillInvitation.class));
        }
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();//取消倒计时
        }
    }

    @Override
    public int initVariableId() {
        return 0;
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
