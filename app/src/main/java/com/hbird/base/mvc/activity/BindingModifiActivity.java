package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hbird.base.util.Utils;

import butterknife.BindView;

import static com.hbird.base.R.id.et_yzm;
import static com.hbird.base.R.id.tv_submit;

/**
 * Created by Liul on 2018/7/21.
 * 修改绑定手机号
 */

public class BindingModifiActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(et_yzm)
    EditText mYanZM;
    @BindView(tv_submit)
    TextView mSubmit;
    @BindView(R.id.iv_login)
    TextView mLogin;
    private String phones;
    private String token;
    private TimeCount time;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_binging_modifi;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("修改绑定手机号");
        mCenterTitle.setTextSize(18);
        mRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        phones = getIntent().getStringExtra("PHONE");
        if (!TextUtils.isEmpty(phones)) {
            String phone = Utils.getHiddenPhone(phones);
            mUserName.setText(phone);
        }
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mYanZM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String yzm = mYanZM.getText().toString().trim();
                if (!TextUtils.isEmpty(yzm)) {
                    mLogin.setBackgroundResource(R.drawable.select_main_button);
                } else {
                    mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_submit:
                time = new TimeCount(60000, 1000);
                time.start();// 开始计时
                showProgress("");
                NetWorkManager.getInstance().setContext(this).postGetPhonesYzm(phones, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        showMessage("验证码获取成功");
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
                break;
            case R.id.iv_login:
                // showMessage("修改手机号");
                ModifiPhoneNumBean mb = new ModifiPhoneNumBean();
                mb.setMobile(phones);
                String yzm = mYanZM.getText().toString().trim();
                if (TextUtils.isEmpty(yzm)) {
                    showMessage("请输入验证码");
                    return;
                }
                mb.setVerifycode(yzm);
                String json = new Gson().toJson(mb);
                showProgress("");
                NetWorkManager.getInstance().setContext(this).postCheckOldPhoneNumber(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        //绑定手机号
                        Intent intent = new Intent();
                        intent.setClass(BindingModifiActivity.this, BindingActivity.class);
                        intent.putExtra("TYPE", "phone");
                        startActivityForResult(intent, 301);
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 301 && resultCode == 302) {
            finish();
        }
    }
}
