package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.hbird.base.mvc.bean.RequestBean.LoginModifi;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hbird.base.R.id.pwd;
import static com.hbird.base.R.id.text;
import static com.hbird.base.R.id.tv_submit;
import static com.hbird.base.R.id.userName;

/**
 * Created by Liul on 2018/7/21.
 * 修改手机密码
 */

public class ChangePasswordActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(userName)
    EditText mUserName;
    @BindView(R.id.et_yzm)
    EditText mYanZM;
    @BindView(pwd)
    EditText mPwd;
    @BindView(R.id.setpwd_view)
    CheckBox mSetpwd;
    @BindView(R.id.setpwd2_view)
    CheckBox mSet2pwd;
    @BindView(R.id.iv_login)
    RelativeLayout mLogin;

    boolean bShowPassWord = false;
    boolean bShowToPassWord = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTitle.setText("修改密码");
        mCenterTitle.setTextSize(18);
        mRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = mUserName.getText().toString().trim();
                String yzm = mYanZM.getText().toString().trim();
                String pwd = mPwd.getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(yzm) && !TextUtils.isEmpty(pwd)){
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
                String userName = mUserName.getText().toString().trim();
                String yzm = mYanZM.getText().toString().trim();
                String pwd = mPwd.getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(yzm) && !TextUtils.isEmpty(pwd)){
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                }else {
                    mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = mUserName.getText().toString().trim();
                String yzm = mYanZM.getText().toString().trim();
                String pwd = mPwd.getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(yzm) && !TextUtils.isEmpty(pwd)){
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
    @OnClick({R.id.setpwd_view,R.id.setpwd2_view})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.iv_login:
                //showMessage("确认修改");
                playVoice(R.raw.changgui02);
                String beforePassword = mUserName.getText().toString().trim();
                String thisPassword = mYanZM.getText().toString().trim();
                String confirmPwd = mPwd.getText().toString().trim();
                if(TextUtils.isEmpty(beforePassword)){
                    showMessage("请输入原密码");
                    return;
                }
                if(TextUtils.isEmpty(thisPassword) && thisPassword.length()>=6){
                    showMessage("请输入6位数以上新密码");
                    return;
                }
                if(TextUtils.isEmpty(confirmPwd)){
                    showMessage("请确认新密码");
                    return;
                }
                if(!TextUtils.equals(thisPassword,confirmPwd)){
                    showMessage("请确认修改密码是否一致");
                    return;
                }
                confimModifi(beforePassword,confirmPwd);
                break;
            //查看密码
            case R.id.setpwd_view:
                playVoice(R.raw.changgui02);
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
            case R.id.setpwd2_view:
                playVoice(R.raw.changgui02);
                bShowToPassWord = !bShowToPassWord;
                if(bShowToPassWord){
                    //如果选中，显示密码
                    mYanZM.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mYanZM.setSelection(mYanZM.getText().length());
                }else{
                    //否则隐藏密码
                    mYanZM.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mYanZM.setSelection(mYanZM.getText().length());
                }
                break;
        }
    }
    //确认修改
    private void confimModifi(String beforePassword,String confirmPwd) {
        LoginModifi lg = new LoginModifi();
        lg.setNewpwd(confirmPwd);
        lg.setOldpwd(beforePassword);
        String json = new Gson().toJson(lg);
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .postToLoginModifi(json, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        GloableReturn b1 = (GloableReturn) b;
                        showMessage("修改成功");
                        finish();
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }



}
