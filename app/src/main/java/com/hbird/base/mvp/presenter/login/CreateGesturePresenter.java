package com.hbird.base.mvp.presenter.login;

import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.login.ICreateGestureView;
import com.hbird.base.mvp.model.imodel.login.ICreateGestureModel;

public class CreateGesturePresenter extends BasePresenter<ICreateGestureView, ICreateGestureModel> {

    public CreateGesturePresenter(ICreateGestureView iView, ICreateGestureModel iModel) {
        super(iView, iModel);
    }


}
