package com.hbird.ui.login_register;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.growingio.android.sdk.collection.GrowingIO;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.mvc.bean.RequestBean.SystemParamReq;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.UserInfo;
import com.hbird.common.Constants;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;
import com.hbird.util.Utils;
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

public class RegisterModle extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public RegisterModle(@NonNull Application application) {
        super(application);
    }

    // 获取验证码
    public void getCode(String mobile,CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("mobile", mobile);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        ApiClient.getGankDataService()
                .getRegisterCode(body)
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

    public void register(String phone, String password, String code, CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("mobile", phone);
        obj.put("password", password);
        obj.put("verifycode", code);
        obj.put("mobileSystem", "android");
        obj.put("mobileSystemVersion", "");
        obj.put("mobileDevice", Utils.getDeviceInfo(getApplication()));
        obj.put("androidChannel", Utils.getChannelName(getApplication()));
        obj.put("mobileManufacturer", android.os.Build.MODEL);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        ApiClient.getGankDataService()
                .register(body)
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

                        if (value.code == 200){
                            //登录成功保存Token及过期时间
                            String strExpire = value.getValueByKey("expire").toString();
                            String strToken = value.getValueByKey("X-AUTH-TOKEN").toString();

//                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN, strToken);
//                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);

                            SPUtil.setPrefString(getApplication(), CommonTag.GLOABLE_TOKEN, strToken);
                            SPUtil.setPrefString(getApplication(), CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);

                            //统计用户时以设备为标准 统计应用自身的账号
                            MobclickAgent.onProfileSignIn("手机注册登录", phone);
                            // 设置登录用户ID API（GrowingIO统计）
                            GrowingIO.getInstance().setUserId(phone);

                            // 获取个人信息
                            getUserInfo(strToken);
                            setCheckChargeType(strToken,callBack);
                        }else{
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

                        // 手机号新注册的，是空的没有值的
//                        SharedPreferencesUtil.put(Constants.USER_HEAD, value.result.avatarUrl);
//                        SharedPreferencesUtil.put(Constants.USER_NICKNAME, value.result.nickName);
                        SharedPreferencesUtil.put(Constants.USER_ID, String.valueOf(value.result.id));// 用户id
                        SharedPreferencesUtil.put(Constants.REGISTER_DATE, value.result.registerDate);// 注册时间

                        // 旧的也保存一份   避免出错
                        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_HEADER, value.result.avatarUrl);
                        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_NICK_NAME, value.result.nickName);
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

    private void setCheckChargeType(String token,CallBack callBack) {
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

                            setTypesToLocalDB(value.result,callBack);
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

    private void setTypesToLocalDB(SystemBiaoqReturn.ResultBean bean,CallBack callBack) {
        //系统标签类目
        if (null != bean.getLabel()) {
            for (int i = 0; i < bean.getLabel().size(); i++) {
                //个人userInfo
                Integer userInfoId = bean.getLabel().get(0).getUserInfoId();
                SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, userInfoId + "");
                List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> spendList = bean.getLabel().get(i).getSpend();
                Integer abTypeId = bean.getLabel().get(i).getAbTypeId();
                DBUtil.insertAllUserCommUseSpendToLocalDB(spendList, abTypeId, userInfoId);
                List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> incomeList = bean.getLabel().get(i).getIncome();
                DBUtil.insertAllUserCommUseIncomeToLocalDB(incomeList, abTypeId, userInfoId);
            }
        }

        //离线时间同步间隔 ( 单位 ms 1天 )--存储到本地SP
        String synInterval = bean.getSynInterval() + "";
        SPUtil.setPrefString(getApplication(), com.hbird.base.app.constant.CommonTag.SYNINTERVAL, synInterval);
        //个人账户账本id
        List<SystemBiaoqReturn.ResultBean.AbsBean> abs = bean.getAbs();
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

        //清空手势密码
        GestureUtil.clearGesturePassword();
        //关闭手势密码开关（必须重新打开设置）
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);


        String userId = (String) SharedPreferencesUtil.get("user_id", "");// 用户id
        long currentData = System.currentTimeMillis();// 当前时间
        long registerDate = (long) SharedPreferencesUtil.get("register_date", 0L);// 注册时间
        boolean filledIn = (boolean) SharedPreferencesUtil.get(userId + "_filled_in", false);// 是否填写过邀请码

        sing.util.LogUtil.e("currentData = " + currentData);
        sing.util.LogUtil.e("registerDate = " + registerDate);
        sing.util.LogUtil.e("filledIn = " + filledIn);
        sing.util.LogUtil.e("currentData-registerDate = " + (currentData - registerDate));
        if (currentData - registerDate > 180000 || filledIn) {// 当前时间超过注册时间3分钟，直接去首页不填写邀请码
            callBack.getSuccess(true);
        } else {
            callBack.getSuccess(false);
        }
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
