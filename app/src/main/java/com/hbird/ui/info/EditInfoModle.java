package com.hbird.ui.info;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.hbird.base.mvc.bean.RequestBean.QiNiuReq;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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

public class EditInfoModle extends BaseViewModel {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    public EditInfoModle(@NonNull Application application) {
        super(application);
    }

    // 获取七牛的token
    public void getQiNiuToken(String token, String path, CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(new QiNiuReq(1)));

        ApiClient.getGankDataService()
                .postQiNiuToken(token, body)
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
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            uploadHead(path, (String) value.getValueByKey("auth"), callBack);
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

    // 上传图片
    private void uploadHead(String path, String auth, CallBack callBack) {
        //关键参数
        Configuration config = new Configuration.Builder().zone(Zone.zone1).build();
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(new File(path), null, auth, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                dismissDialog();
                if (info.isOK()) {
                    try {
                        ToastUtil.showShort("上传成功");
                        String key1 = res.getString("key");
                        callBack.result(key1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showShort("上传失败");
                    }
                } else {
                    ToastUtil.showShort("上传失败");
                }
            }
        }, null);
    }

    // 保存
    public void save(String token, String bean, CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bean);

        ApiClient.getGankDataService()
                .putPersionnalInfo(token, body)
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
                        LogUtil.e(value.toString());
                        if (value.code == 200) {
                            callBack.result("");
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
        void result(String url);
    }
}
