package com.hbird.base.mvp.presenter.login;

import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.login.IShouShiPasswordView;
import com.hbird.base.mvp.model.imodel.login.IShouShiPasswordModel;

public class ShouShiPasswordPresenter extends BasePresenter<IShouShiPasswordView, IShouShiPasswordModel> {

    public ShouShiPasswordPresenter(IShouShiPasswordView iView, IShouShiPasswordModel iModel) {
        super(iView, iModel);
    }


}
