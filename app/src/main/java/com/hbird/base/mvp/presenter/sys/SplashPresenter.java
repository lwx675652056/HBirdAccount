package com.hbird.base.mvp.presenter.sys;

import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.sys.ISplashView;
import com.hbird.base.mvp.model.imodel.sys.ISplashModel;

public class SplashPresenter extends BasePresenter<ISplashView, ISplashModel> {

    public SplashPresenter(ISplashView iView, ISplashModel iModel) {
        super(iView, iModel);
    }


}
