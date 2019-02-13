package com.hbird.ui.index;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountBean;
import com.hbird.http.ApiClient;
import com.hbird.http.HttpResult;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sing.common.base.BaseViewModel;
import sing.common.util.LogUtil;
import sing.util.ToastUtil;

public class MyAccountModle extends BaseViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();

    {
        ABSENT.setValue(null);
    }

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MyAccountModle(@NonNull Application application) {
        super(application);
    }


    public void getAllzb(String token, CallBack callBack) {
        ApiClient.getGankDataService()
                .getAllzb(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showDialog())
                .subscribe(new Observer<HttpResult<List<AccountBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<List<AccountBean>> value) {
                        dismissDialog();
                        LogUtil.e(value.toString());

                        List<AccountBean> data = new ArrayList<>();
                        data.addAll(value.result);

//                        ZhangBenMsgBean bean = new ZhangBenMsgBean();
//                        bean.setZbImg("");
//                        bean.setZbName("总明细账本");
//                        bean.setZbType("总账本");
//                        bean.setZbUTime("");
//                        data.add(bean);
                        AccountBean temp = new AccountBean();
                        temp.abName = "总明细账本";
                        temp.abTypeName = "总账本";
                        data.add(0,temp);

                        Set<String> set = new LinkedHashSet<>();
                        for (int i = 0, size = value.result.size(); i < size; i++) {
                            set.add(value.result.get(i).id);
                        }
                        if (set.size() > 0) {
                            SPUtil.setStringSet(getApplication(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
                        }

//                        int k = 0;
//                        for (int i = 0; i < data.size(); i++) {
//                            if (TextUtils.equals(data.get(i).getId(), id)) {
//                                k = i;
//                            }
//                        }
//                        adapter = new MyZhangBenAdapter(MyZhangBenActivity.this, data);
//                        swipe.setAdapter(adapter);
//                        adapter.setData(k);

                        callBack.result(data);
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


    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public interface CallBack {
        void result(List<AccountBean> list);
    }
}
