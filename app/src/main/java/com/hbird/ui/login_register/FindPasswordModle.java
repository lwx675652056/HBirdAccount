package com.hbird.ui.login_register;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.util.SPUtil;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;
import com.ljy.devring.DevRing;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;
import sing.util.ToastUtil;

public class FindPasswordModle extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public FindPasswordModle(@NonNull Application application) {
        super(application);
    }

    // 获取验证码
    public void getCode(String mobile, CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("mobile", mobile);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        ApiClient.getGankDataService()
                .getVerifycodeToResetpwd(body)
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

    public void find(String phone, String password, String code, CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("mobile", phone);
        obj.put("password", password);
        obj.put("verifycode", code);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        ApiClient.getGankDataService()
                .resetpwd(body)
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
                            //登录成功保存Token及过期时间
                            String strExpire = value.getValueByKey("expire").toString();
                            String strToken = value.getValueByKey("X-AUTH-TOKEN").toString();

                            DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN, strToken);
                            DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);

                            SPUtil.setPrefString(getApplication(), CommonTag.GLOABLE_TOKEN, strToken);
                            SPUtil.setPrefString(getApplication(), CommonTag.GLOABLE_TOKEN_EXPIRE, strExpire);

                            DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.app.constant.CommonTag.SHOUSHI_PASSWORD_OPENED, false);
                            callBack.getSuccess(true);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public interface CallBack {
        void getSuccess(boolean toHome);
    }
}
