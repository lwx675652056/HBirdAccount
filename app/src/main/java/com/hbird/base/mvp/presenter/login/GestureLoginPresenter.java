package com.hbird.base.mvp.presenter.login;

import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.login.IGestureLoginView;
import com.hbird.base.mvp.model.imodel.login.IGestureLoginModel;

public class GestureLoginPresenter extends BasePresenter<IGestureLoginView, IGestureLoginModel> {

    public GestureLoginPresenter(IGestureLoginView iView, IGestureLoginModel iModel) {
        super(iView, iModel);
    }


}
