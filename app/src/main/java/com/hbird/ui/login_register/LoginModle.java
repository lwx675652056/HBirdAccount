package com.hbird.ui.login_register;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.bean.RequestBean.SystemParamReq;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountBean;
import com.hbird.bean.UserInfo;
import com.hbird.common.Constants;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;
import com.ljy.devring.DevRing;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

public class LoginModle extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public LoginModle(@NonNull Application application) {
        super(application);
    }

    // 获取验证码
    public void getCode(String mobile, CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("mobile", mobile);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        ApiClient.getGankDataService()
                .getLoginVerifycode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<String> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            callBack.getSuccess(true);
                            ToastUtil.showShort("获取验证码成功");
                        } else {
                            ToastUtil.showShort(value.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }


    // 手机号密码登录
    public void login(final String mobile, String password, CallBack callBack) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);

        ApiClient.getGankDataService()
                .login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<HttpResult<String>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<String> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.CURRENT_LOGIN_METHOD, "sj");
                            saveInfo(value.result, callBack);
                            //统计用户时以设备为标准 统计应用自身的账号(友盟统计)
                            MobclickAgent.onProfileSignIn("手机号码登录", mobile);
                            // 设置登录用户ID API（GrowingIO统计）
                            GrowingIO.getInstance().setUserId(mobile);
                        } else {
                            ToastUtil.showShort(value.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    // 手机验证码登录
    public void loginByVerifyCode(final String mobile, String verifyCode, CallBack callBack) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"verifycode\":\"" + verifyCode + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);

        ApiClient.getGankDataService()
                .loginByVerifyCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<HttpResult<String>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<String> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.CURRENT_LOGIN_METHOD, "sj");

                            saveInfo(value.result, callBack);
                            //统计用户时以设备为标准 统计应用自身的账号(友盟统计)
                            MobclickAgent.onProfileSignIn("手机号码登录", mobile);
                            // 设置登录用户ID API（GrowingIO统计）
                            GrowingIO.getInstance().setUserId(mobile);
                        } else {
                            ToastUtil.showShort(value.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // 登录成功后保存token信息
    public void saveInfo(String strResult, CallBack callBack) {
        //登录成功保存Token及过期时间
        JSONObject jsonObjToken = JSON.parseObject(strResult);
        String strExpire = jsonObjToken.getString("expire");
        String strToken = jsonObjToken.getString("X-AUTH-TOKEN");

        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN, strToken);
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);

        SharedPreferencesUtil.put(CommonTag.GLOABLE_TOKEN, strToken);
        SPUtil.setPrefString(getApplication(), com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, strToken);

        SharedPreferencesUtil.put(CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);
        SPUtil.setPrefString(getApplication(), com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);

        SPUtil.setPrefBoolean(getApplication(), CommonTag.OFFLINEPULL_FIRST_LOGIN, true);
        SharedPreferencesUtil.put(CommonTag.OFFLINEPULL_FIRST_LOGIN, true);
        //获取个人信息
        getUserInfo(strToken);
        getMyAccount(strToken);
        setCheckChargeType(strToken, callBack);
    }

    // 获取个人信息
    private void getUserInfo(String token) {
        ApiClient.getGankDataService()
                .getPersionalInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<UserInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<UserInfo> value) {
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            if (value.result.avatarUrl == null) {
                                value.result.avatarUrl = "";
                            }
                            if (value.result.nickName == null) {
                                value.result.nickName = "";
                            }
                            SharedPreferencesUtil.put(Constants.USER_HEAD, value.result.avatarUrl);
                            SharedPreferencesUtil.put(Constants.USER_NICKNAME, value.result.nickName);
                            SharedPreferencesUtil.put(Constants.USER_ID, String.valueOf(value.result.id));// 用户id
                            SharedPreferencesUtil.put(Constants.REGISTER_DATE, value.result.registerDate);// 注册时间

                            // 旧的也保存一份   避免出错
                            SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_HEADER, value.result.avatarUrl);
                            SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_NICK_NAME, value.result.nickName);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    private void getMyAccount(String token) {
        ApiClient.getGankDataService()
                .getAllzb(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<AccountBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<List<AccountBean>> value) {
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            if (value.result != null && value.result.size() > 0) {
                                Set<String> set = new LinkedHashSet<>();
                                set.clear();
                                for (int i = 0, size = value.result.size(); i < size; i++) {
                                    set.add(String.valueOf(value.result.get(i).id));
                                }
                                if (null != set && set.size() > 0) {
                                    SharedPreferencesUtil.put(CommonTag.ACCOUNT_BOOK_ID_ALL, set);
                                }

                                String accountId = (String) SharedPreferencesUtil.get(CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
                                String abtypeId = (String) SharedPreferencesUtil.get(CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "");
                                if (TextUtils.isEmpty(accountId) || !set.contains(accountId) || TextUtils.isEmpty(abtypeId)) {
                                    AccountBean bean = value.result.get(value.result.size() - 1);// 默认显示最后一个，默认账本
                                    SPUtil.setPrefInt(getApplication(), CommonTag.ACCOUNT_AB_TYPEID, bean.abTypeId);
                                    SPUtil.setPrefString(getApplication(), CommonTag.INDEX_CURRENT_ACCOUNT, bean.abName);
                                    SPUtil.setPrefString(getApplication(), CommonTag.INDEX_CURRENT_ACCOUNT_ID, bean.id);
                                    SPUtil.setPrefString(getApplication(), CommonTag.ACCOUNT_BOOK_ID, bean.id);
                                    SPUtil.setPrefString(getApplication(), CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, bean.abTypeId + "");
                                    SPUtil.setPrefString(getApplication(), CommonTag.INDEX_TYPE_BUDGET, bean.typeBudget + "");
                                    SPUtil.setPrefString(getApplication(), CommonTag.CURRENT_ACCOUNT_ID, bean.abTypeId + "");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    private void setCheckChargeType(String token, CallBack callBack) {
        String lableVersion = SPUtil.getPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION, "");
        SystemParamReq req = new SystemParamReq();
        req.setLabelVersion(lableVersion);
        String jsonInfo = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);

        ApiClient.getGankDataService()
                .postCheckChargeTypes(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<HttpResult<SystemBiaoqReturn.ResultBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<SystemBiaoqReturn.ResultBean> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());

                        //获取到所有常用收入支出类目 （并插入到本地数据库）
                        if (null != value.result) {
                            SPUtil.setPrefBoolean(getApplication(), com.hbird.base.app.constant.CommonTag.FIRST_TO_ACCESS, false);
                            SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION, value.result.getLabelVersion());

                            setTypesToLocalDB(value.result, callBack);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        ToastUtil.showShort(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("danxx", "onComplete------>");
                    }
                });
    }

    private void setTypesToLocalDB(SystemBiaoqReturn.ResultBean result, CallBack callBack) {
        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION, result.getLabelVersion());
        //系统标签类目
        if (null != result.getLabel()) {
            //个人userInfo
            Integer userInfoId = result.getLabel().get(0).getUserInfoId();
            SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, userInfoId + "");
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

                sing.util.LogUtil.e("label = " + result.getLabel().toString());

                List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> spendList = result.getLabel().get(i).getSpend();
                Integer abTypeId = result.getLabel().get(i).getAbTypeId();
                DBUtil.insertAllUserCommUseSpendToLocalDB(spendList, abTypeId, userInfoId);
                List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> incomeList = result.getLabel().get(i).getIncome();
                DBUtil.insertAllUserCommUseIncomeToLocalDB(incomeList, abTypeId, userInfoId);
            }
        }

        //离线时间同步间隔 ( 单位 ms 1天 )--存储到本地SP
        String synInterval = result.getSynInterval() + "";
        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.SYNINTERVAL, synInterval);
        //个人账户账本id
        List<SystemBiaoqReturn.ResultBean.AbsBean> abs = result.getAbs();
        Set<String> set = new LinkedHashSet<String>();
        set.clear();
        if (null != abs) {
            for (int i = 0; i < abs.size(); i++) {
                String accountBookId = abs.get(i).getId() + "";
                if (i == 0) {
                    //默认账本id
                    String s = SPUtil.getPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
                    boolean firstGuide = SPUtil.getPrefBoolean(getApplication(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, true);
                    if (firstGuide) {
                        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, accountBookId);
                        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, accountBookId);
                        SPUtil.setPrefInt(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, abs.get(i).getAbTypeId());
                    } else {
                        if (TextUtils.isEmpty(s)) {
                            SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, accountBookId);
                            SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, accountBookId);
                            SPUtil.setPrefInt(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, abs.get(i).getAbTypeId());
                        }
                    }
                }
                set.add(accountBookId);
            }
        }
        if (null != set && set.size() > 0) {
            SPUtil.setStringSet(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
        }

        SPUtil.setPrefBoolean(getApplication(), com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN, true);

        String userId = (String) SharedPreferencesUtil.get(Constants.USER_ID, "");// 用户id
        long currentData = System.currentTimeMillis();// 当前时间
        long registerDate = (long) SharedPreferencesUtil.get(Constants.REGISTER_DATE, 0L);// 注册时间
        boolean filledIn = (boolean) SharedPreferencesUtil.get(userId + Constants._FILLED_IN, false);// 是否填写过邀请码

        sing.util.LogUtil.e("currentData = " + currentData);
        sing.util.LogUtil.e("registerDate = " + registerDate);
        sing.util.LogUtil.e("filledIn = " + filledIn);
        sing.util.LogUtil.e("currentData-registerDate = " + (currentData - registerDate));
        if (currentData - registerDate > 180000 || filledIn) {// 当前时间超过注册时间3分钟，直接去首页不填写邀请码
            callBack.getSuccess(true);
        } else {
            callBack.getSuccess(false);
        }
        finish();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public interface CallBack {
        void getSuccess(boolean toHome);
    }
}
