package com.hbird.base.mvp.view.activity.login;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.mvc.activity.WebViewActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.model.login.RegisterModel;
import com.hbird.base.mvp.presenter.login.RegisterPresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.mvp.view.iview.login.IRegisterView;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.common.Constants;
import com.hbird.ui.MainActivity;
import com.hbird.ui.fill_invitation.ActFillInvitation;
import com.hbird.ui.login_register.ActLogin;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;

import static com.hbird.base.R.id.userName;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterView {

    @BindView(userName)
    TextInputLayout mUserName;
    @BindView(R.id.et_yzm)
    TextInputEditText mYanZM;
    @BindView(R.id.tv_submit)
    TextView mSubmit;
    @BindView(R.id.pwd)
    TextInputLayout mPwd;
    @BindView(R.id.tv_suggest)
    TextView mSuggest;
    @BindView(R.id.bt_my_login)
    RelativeLayout mLogin;
    @BindView(R.id.ll_back)
    LinearLayout mBack;

    private TimeCount time;
    private RegisterPresenter mPresenter = null;
    private String token;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle bundle) {
        String strs = "注册即同意 " + "<font color='#468DE1'>" + "《蜂鸟记账使用协议》" + "</font>";
        mSuggest.setText(Html.fromHtml(strs));
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
                String passWord = mYanZM.getText().toString().trim();
                String pwd = mPwd.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord) && !TextUtils.isEmpty(pwd)) {
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                } else {
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
                String passWord = mYanZM.getText().toString().trim();
                String pwd = mPwd.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord) && !TextUtils.isEmpty(pwd)) {
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                } else {
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
                String passWord = mYanZM.getText().toString().trim();
                String pwd = mPwd.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord) && !TextUtils.isEmpty(pwd)) {
                    mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                } else {
                    mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    boolean bShowPassWord = false;

    @OnClick({R.id.et_yzm, R.id.bt_my_login, R.id.tv_submit, R.id.tv_suggest, R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_my_login: //注册并登录
                doRegister();
                break;
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
            case R.id.tv_submit:
                //showMessage("获取验证码");
                time = new TimeCount(60000, 1000);
                time.start();// 开始计时
                dGetVerifyCode();
                break;
            case R.id.tv_suggest: //注册即同意《蜂鸟记账使用协议》
                Intent intent3 = new Intent(this, WebViewActivity.class);
                intent3.putExtra("TYPE", "xieyi");
                startActivity(intent3);
                break;
        }
    }

    private void doRegister() {
        String strUserName = mUserName.getEditText().getText().toString();
        String strPwd = mPwd.getEditText().getText().toString();
        String strYanZM = mYanZM.getText().toString();
        String channelName = getChannelName();
        String mobileType = android.os.Build.MODEL;
        mPresenter = new RegisterPresenter(this, new RegisterModel());

        String deviceId = Utils.getDeviceInfo(this);
        mPresenter.register(strUserName, strPwd, strYanZM, "android", "", deviceId, channelName, mobileType);
    }

    private void dGetVerifyCode() {
        String strUserName = mUserName.getEditText().getText().toString();
        mPresenter = new RegisterPresenter(this, new RegisterModel());
        mPresenter.getRegisterVerifycode(strUserName);
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
    public void registerSuccess() {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        getUserInfos();
        setCheckChargeType();

    }

    private void getUserInfos() {
        NetWorkManager.getInstance().setContext(this)
                .getPersionalInfos(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GeRenInfoReturn b1 = (GeRenInfoReturn) b;
                        String avatarUrl = b1.getResult().getAvatarUrl();
                        SPUtil.setPrefString(RegisterActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_HEADER, avatarUrl);
                        String nickName = b1.getResult().getNickName();
                        SPUtil.setPrefString(RegisterActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_NICK_NAME, nickName);

                        SharedPreferencesUtil.put(Constants.USER_ID, String.valueOf(b1.getResult().getId()));// 用户id
                        SharedPreferencesUtil.put(Constants.REGISTER_DATE, b1.getResult().getRegisterDate());// 注册时间
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    private void setCheckChargeType() {
        showProgress("");
        String lableVersion = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION, "");
        NetWorkManager.getInstance().setContext(this)
                .postCheckChargeType(lableVersion, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        SystemBiaoqReturn b1 = (SystemBiaoqReturn) b;
                        //获取到所有常用收入支出类目 （并插入到本地数据库）
                        if (null != b1 && b1.getResult() != null) {
                            setTypesToLocalDB(b1);
                            hideProgress();
                            SPUtil.setPrefBoolean(RegisterActivity.this, com.hbird.base.app.constant.CommonTag.FIRST_TO_ACCESS, false);
                        }

                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    /**
     * 获取渠道名
     * 此处习惯性的设置为activity，实际上context就可以
     * 如果没有获取成功，那么返回值为空
     */
    public String getChannelName() {
        if (getApplicationContext() == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }


    @Override
    public void registerFaild() {
        RingToast.show("注册失败！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();//取消倒计时
        }
    }

    private void setTypesToLocalDB(SystemBiaoqReturn b1) {
        SystemBiaoqReturn.ResultBean result = b1.getResult();
        String labelVersion = result.getLabelVersion();
        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION, labelVersion);
        //系统标签类目
        if (null != result.getLabel()) {
            for (int i = 0; i < result.getLabel().size(); i++) {
                //个人userInfo
                Integer userInfoId = result.getLabel().get(0).getUserInfoId();
                SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, userInfoId + "");
                List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> spendList = result.getLabel().get(i).getSpend();
                Integer abTypeId = result.getLabel().get(i).getAbTypeId();
                DBUtil.insertAllUserCommUseSpendToLocalDB(spendList, abTypeId, userInfoId);
                List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> incomeList = result.getLabel().get(i).getIncome();
                DBUtil.insertAllUserCommUseIncomeToLocalDB(incomeList, abTypeId, userInfoId);
            }
        }
        //离线时间同步间隔 ( 单位 ms 1天 )--存储到本地SP
        String synInterval = result.getSynInterval() + "";
        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.SYNINTERVAL, synInterval);
        //个人账户账本id
        List<SystemBiaoqReturn.ResultBean.AbsBean> abs = result.getAbs();
        Set<String> set = new LinkedHashSet<String>();
        set.clear();
        if (null != abs) {
            for (int i = 0; i < abs.size(); i++) {
                String accountBookId = abs.get(i).getId() + "";
                if (i == 0) {
                    //默认账本id
                    String s = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
                    boolean firstGuide = SPUtil.getPrefBoolean(this, com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, true);
                    if (firstGuide) {
                        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, accountBookId);
                        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, accountBookId);
                        SPUtil.setPrefInt(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, abs.get(i).getAbTypeId());
                    } else {
                        if (TextUtils.isEmpty(s)) {
                            SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, accountBookId);
                            SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, accountBookId);
                            SPUtil.setPrefInt(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, abs.get(i).getAbTypeId());
                        }
                    }
                }
                set.add(accountBookId);

            }
        }
        if (null != set && set.size() > 0) {
            SPUtil.setStringSet(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
        }


        SPUtil.setPrefBoolean(RegisterActivity.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN, true);

        RingToast.show("注册成功！");
        //startActivity(new Intent(getApplicationContext(), loginActivity.class));
        //清空手势密码
        GestureUtil.clearGesturePassword();
        //关闭手势密码开关（必须重新打开设置）
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);
        //
        Intent intentNew = new Intent(RegisterActivity.this, CreateGestureActivity.class);

        String userId = (String) SharedPreferencesUtil.get("user_id","");// 用户id
        long currentData = System.currentTimeMillis();// 当前时间
        long registerDate = (long) SharedPreferencesUtil.get("register_date", 0L);// 注册时间
        boolean filledIn= (boolean) SharedPreferencesUtil.get(userId+"_filled_in",false);// 是否填写过邀请码

        LogUtil.e("currentData = " + currentData);
        LogUtil.e("registerDate = " + registerDate);
        LogUtil.e("filledIn = " + filledIn);
        LogUtil.e("currentData-registerDate = " + (currentData - registerDate));
        if (currentData - registerDate > 180000 || filledIn) {// 当前时间超过注册时间3分钟，直接去首页不填写邀请码
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), ActFillInvitation.class));
        }

        DevRing.activityListManager().killActivity(ActLogin.class); //退出loginActivity
        finish();
    }
}