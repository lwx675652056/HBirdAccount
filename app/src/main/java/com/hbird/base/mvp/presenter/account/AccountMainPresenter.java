package com.hbird.base.mvp.presenter.account;

import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.iview.account.IAccountMainView;
import com.hbird.base.mvp.model.imodel.account.IAccountMainModel;

public class AccountMainPresenter extends BasePresenter<IAccountMainView, IAccountMainModel> {

    public AccountMainPresenter(IAccountMainView iView, IAccountMainModel iModel) {
        super(iView, iModel);
    }


}
