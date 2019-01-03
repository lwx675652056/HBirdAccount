package com.hbird.base.mvp.view.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.R;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.RingApplication;
import com.hbird.base.mvc.activity.ActFillInvitation;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.bean.ZhangBenMsgBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.event.WxLoginEvent;
import com.hbird.base.mvp.model.login.loginModel;
import com.hbird.base.mvp.presenter.login.loginPresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.mvp.view.iview.login.IloginView;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.RingToast;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;

import static com.hbird.base.R.id.iv_wx_login;


public class loginActivity extends BaseActivity<loginPresenter> implements IloginView {
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.ll_left_content)
    LinearLayout mLeftContent;
    @BindView(R.id.til_phone)
    TextInputLayout mPhone;
    @BindView(R.id.til_password)
    TextInputLayout mPassword;
    @BindView(R.id.tv_find_password)
    TextView mFindpassword;
    @BindView(R.id.tv_register)
    TextView mRegister;
    @BindView(R.id.iv_login)
    RelativeLayout mLogin;
    @BindView(iv_wx_login)
    LinearLayout mWxLogin;

    @BindView(R.id.ll_right_content)
    LinearLayout mRightContent;
    @BindView(R.id.right_phone)
    TextInputLayout mRightPhone;
    @BindView(R.id.right_password)
    TextInputLayout mRightPassword;
    @BindView(R.id.tv_submit)
    TextView mSubmit;
    @BindView(R.id.tv_right_register)
    TextView mRightRegister;
    @BindView(R.id.tie_phone)
    android.support.design.widget.TextInputEditText phoneNum;

    boolean mbPasswordLogin = true;


    private loginPresenter mPresenter = null;
    private TimeCount time;
    private String phonePttern = "^[1]+\\d{10}";
    private Pattern pattern = Pattern.compile(phonePttern);
    private Matcher matcher;
    private String phoneNumbers;
    private boolean isLeft = true;
    private String token;

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    //接收事件总线发来的事件
    @org.greenrobot.eventbus.Subscribe //如果使用默认的EventBus则使用此@Subscribe
    @com.hbird.base.mvp.model.bus.support.Subscribe //如果使用RxBus则使用此@Subscribe
    public void handlerEvent(WxLoginEvent event) {
        //处理事件
        String strResult = event.getStatus();
        mPresenter = new loginPresenter(this, new loginModel());
        mPresenter.saveToken(strResult);
        //统计用户时以设备为标准 统计应用自身的账号（友盟统计）
        MobclickAgent.onProfileSignIn("微信登录", strResult);
        // 设置登录用户ID API（GrowingIO统计）
        GrowingIO.getInstance().setUserId("微信登录：" + strResult);
        SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.CURRENT_LOGIN_METHOD, "wx");
        loginSuccess();
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_login_in;
    }

    @Override
    protected void initView(Bundle bundle) {
        //默认手机号密码登录
        mbPasswordLogin = true;
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_left:
                        //showMessage("密码登录");
                        mLeftContent.setVisibility(View.VISIBLE);
                        mRightContent.setVisibility(View.GONE);
                        mbPasswordLogin = true;
                        phoneNumbers = mRightPhone.getEditText().getText().toString().trim();
                        isLeft = true;
                        initEvent();
                        mRightPhone.getEditText().setText(phoneNumbers);
                        phoneNum.setText(phoneNumbers);
                        phoneNum.setSelection(phoneNumbers.length());
                        mRightPassword.getEditText().setText("");

                        break;
                    case R.id.radio_right:
                        //showMessage("验证码登录");
                        mRightContent.setVisibility(View.VISIBLE);
                        mLeftContent.setVisibility(View.GONE);
                        mbPasswordLogin = false;
                        phoneNumbers = loginActivity.this.phoneNum.getText().toString().trim();
                        isLeft = false;
                        initEvent();
                        mRightPhone.getEditText().setText(phoneNumbers);
                        mRightPhone.getEditText().setSelection(phoneNumbers.length());
                        mPassword.getEditText().setText("");
                        break;
                }
            }
        });
    }

    @OnClick({iv_wx_login, R.id.iv_login, R.id.tv_register, R.id.tv_right_register
            , R.id.tv_find_password, R.id.tv_submit, R.id.til_phone})
    public void onClick(View view) {
        playVoice(R.raw.jizhang);
        switch (view.getId()) {
            //手机号密码登录 + 手机号验证码登录
            case R.id.iv_login:
                doLogin();
                break;
            //微信登录
            case iv_wx_login:
                doWeChatLogin();
                //showMessage("开发中，敬请期待！");
                break;
            //注册
            case R.id.tv_register:
            case R.id.tv_right_register:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            //找回密码
            case R.id.tv_find_password:
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
                break;
            case R.id.tv_submit:
                String strPhone = mRightPhone.getEditText().getText().toString();
                if (strPhone == null || strPhone.trim().isEmpty()) {
                    RingToast.show("请输入手机号！");
                } else {
                    time = new TimeCount(60000, 1000);
                    time.start();// 开始计时
                    mPresenter = new loginPresenter(this, new loginModel());
                    mPresenter.getLoginVerifyCode1(strPhone);
                }
                break;
        }
    }

    private void doWeChatLogin() {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!isWeChatAppInstalled(this)) {
            RingToast.show("您还未安装微信客户端");
        } else {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            //像微信发送请求
            RingApplication.mWxApi.sendReq(req);
        }
    }

    private static IWXAPI api; // 相应的包，请集成SDK后自行引入

    /**
     * 判断微信客户端是否存在
     *
     * @return true安装, false未安装
     */
    public static boolean isWeChatAppInstalled(Context context) {
        api = WXAPIFactory.createWXAPI(context, com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);
        if (api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
            return true;
        } else {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        //在View层初始化时，调用Presenter层方法即可
//        mPresenter = new loginPresenter(this, new loginModel());
//        mPresenter.login("15501233770","123456");
    }

    @Override
    protected void initEvent() {
        if (isLeft) {
            phoneNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String userName = phoneNum.getText().toString().trim();
                    String password = mPassword.getEditText().getText().toString().trim();
                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                        mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                    } else {
                        mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            mPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String userName = phoneNum.getText().toString().trim();
                    String password = mPassword.getEditText().getText().toString().trim();
                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                        mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                    } else {
                        mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mRightPhone.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String userName = mRightPhone.getEditText().getText().toString().trim();
                    String password = mRightPassword.getEditText().getText().toString().trim();
                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                        mLogin.setBackgroundResource(R.drawable.btn_bg_login);
                    } else {
                        mLogin.setBackgroundResource(R.drawable.shape_btn_login_hui);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            mRightPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String userName = mRightPhone.getEditText().getText().toString().trim();
                    String password = mRightPassword.getEditText().getText().toString().trim();
                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
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
    }

    @Override
    public void loginSuccess() {
        SPUtil.setPrefBoolean(loginActivity.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN, true);
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        //获取个人信息
        getUserInfos();
        setCheckChargeType();
        getMyAccount();
    }

    private void getUserInfos() {
        NetWorkManager.getInstance().setContext(this)
                .getPersionalInfos(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GeRenInfoReturn b1 = (GeRenInfoReturn) b;
                        String avatarUrl = b1.getResult().getAvatarUrl();
                        SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_HEADER, avatarUrl);
                        String nickName = b1.getResult().getNickName();
                        SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_NICK_NAME, nickName);

                        SharedPreferencesUtil.put("user_id", String.valueOf(b1.getResult().getId()));// 用户id
                        SharedPreferencesUtil.put("register_date", b1.getResult().getRegisterDate());// 注册时间
                    }

                    @Override
                    public void onError(String s) {
                        LogUtil.e(s);
                    }
                });
    }

    private void setCheckChargeType() {
        showProgress("");

//        String lableVersion = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION, "");
        String lableVersion = "";
        NetWorkManager.getInstance().setContext(this)
                .postCheckChargeType(lableVersion, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        SystemBiaoqReturn b1 = (SystemBiaoqReturn) b;
                        //获取到所有常用收入支出类目 （并插入到本地数据库）
                        if (null != b1 && b1.getResult() != null) {
                            setTypesToLocalDB(b1);
                            hideProgress();
                            SPUtil.setPrefBoolean(loginActivity.this, com.hbird.base.app.constant.CommonTag.FIRST_TO_ACCESS, false);
                        }
                    }

                    @Override
                    public void onError(String s) {
                        LogUtil.e(s);
                    }
                });
    }

    @Override
    public void loginFaild() {
        showMessage("登录失败");
    }

    private void doLogin() {
        mPresenter = new loginPresenter(this, new loginModel());
        SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.CURRENT_LOGIN_METHOD, "sj");
        if (mbPasswordLogin) {//密码登录
            String strPhone = mPhone.getEditText().getText().toString();
            String strPassword = mPassword.getEditText().getText().toString();
            mPresenter.login(strPhone, strPassword);
        } else { //验证码登录
            String strPhone = mRightPhone.getEditText().getText().toString();
            String strVerifyCode = mRightPassword.getEditText().getText().toString();
            mPresenter.loginByVerifyCode(strPhone, strVerifyCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
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

    public boolean validatePhone(String phone) {
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private void setTypesToLocalDB(SystemBiaoqReturn b1) {
        SystemBiaoqReturn.ResultBean result = b1.getResult();
        String labelVersion = result.getLabelVersion();
        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION, labelVersion);
        //系统标签类目
        if (null != result.getLabel()) {
            //个人userInfo
            Integer userInfoId = result.getLabel().get(0).getUserInfoId();
            SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, userInfoId + "");
            for (int i = 0; i < result.getLabel().size(); i++) {
                SystemBiaoqReturn.ResultBean.LabelBean bean = result.getLabel().get(i);
                // 收入
                if (bean.getIncome() != null) {
                    SharedPreferencesUtil.put("userId_" + bean.getUserInfoId() + "abTypeId_" + bean.getAbTypeId() + "type_income", bean.getIncome().toString());
                }

                // 支出
                if (bean.getSpend() != null) {
                    SharedPreferencesUtil.put("userId_" + bean.getUserInfoId() + "abTypeId_" + bean.getAbTypeId() + "type_spend", bean.getSpend().toString());
                }

                LogUtil.e("label = " + result.getLabel().toString());

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

        toHome();
    }

    // 获取个人账本
    private void getMyAccount() {
        NetWorkManager.getInstance().setContext(this)
                .getMyAccounts(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        AccountZbBean b1 = (AccountZbBean) b;
                        if (null != b1.getResult()) {
                            Set<String> set = new LinkedHashSet<>();
                            set.clear();
                            for (int i = 0; i < b1.getResult().size(); i++) {
                                ZhangBenMsgBean bean = new ZhangBenMsgBean();
                                bean.setZbImg(b1.getResult().get(i).getIcon());
                                bean.setZbName(b1.getResult().get(i).getAbName());
                                bean.setZbType(b1.getResult().get(i).getAbTypeName());
                                bean.setZbUTime(b1.getResult().get(i).getUpdateDate() + "");
                                bean.setId(b1.getResult().get(i).getId() + "");
                                set.add(b1.getResult().get(i).getId() + "");
                            }
                            if (null != set && set.size() > 0) {
                                SPUtil.setStringSet(loginActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
                            }


                            String accountId = SPUtil.getPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
                            String abtypeId = SPUtil.getPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "");
                            if (b1.getResult() != null && b1.getResult().size() > 0) {
                                if (TextUtils.isEmpty(accountId) || !set.contains(accountId) || TextUtils.isEmpty(abtypeId)) {
                                    AccountZbBean.ResultBean bean = b1.getResult().get(b1.getResult().size() - 1);// 默认显示最后一个，默认账本
                                    SPUtil.setPrefInt(loginActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, bean.getAbTypeId());
                                    SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, bean.getAbName());
                                    SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, bean.getId() + "");
                                    SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, bean.getId() + "");
                                    SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, bean.getAbTypeId() + "");
                                    SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, bean.getTypeBudget() + "");
                                    SPUtil.setPrefString(loginActivity.this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, bean.getAbTypeId() + "");
                                }
                            }
                        }
                        toHome();
                    }

                    @Override
                    public void onError(String s) {
                        LogUtil.e(s);
                    }
                });
    }

    private boolean canStart = false;

    private void toHome() {
        if (!canStart) {
            canStart = true;
        } else {
            Intent intent = getIntent();//获取传来的intent对象
            String data = intent.getStringExtra("comfrom");
            if (data != null && data.equals("splash_overmaxnum")) {//路径：splash - gesture - login,验证成功后进入主界面，并清空手势密码
                //清空手势密码
                GestureUtil.clearGesturePassword();
                //关闭手势密码开关（必须重新打开设置）
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);
            }

            String userId = (String) SharedPreferencesUtil.get("user_id", "");// 用户id
            long currentData = System.currentTimeMillis();// 当前时间
            long registerDate = (long) SharedPreferencesUtil.get("register_date", 0L);// 注册时间
            boolean filledIn = (boolean) SharedPreferencesUtil.get(userId + "_filled_in", false);// 是否填写过邀请码

            LogUtil.e("currentData = " + currentData);
            LogUtil.e("registerDate = " + registerDate);
            LogUtil.e("filledIn = " + filledIn);
            LogUtil.e("currentData-registerDate = " + (currentData - registerDate));
            if (currentData - registerDate > 180000 || filledIn) {// 当前时间超过注册时间3分钟，直接去首页不填写邀请码
                startActivity(new Intent(getApplicationContext(), homeActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), ActFillInvitation.class));
            }
            finish();
        }
    }
}
