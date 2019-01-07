package com.hbird.ui.address;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hbird.bean.EditAddressBean;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ChooseAccountTypeViewModel
 * @date: 2018/12/18 17:05
 * @Description: 选择账户类别
 */
public class EditAddressViewModel extends BaseViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public EditAddressViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAddress(String token,CallBack callBack) {
        ApiClient.getGankDataService()
                .getAddress(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<HttpResult<EditAddressBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<EditAddressBean> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        callBack.getSuccess(value.result);
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

    public void saveAddress(String token,RequestBody body,CallBack callBack) {
        ApiClient.getGankDataService()
                .saveAddress(token,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(String value) {
                        dismissDialog();
                        LogUtil.e(value.toString());
                        callBack.getSuccess(null);
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
        void getSuccess(EditAddressBean bean);
    }
}