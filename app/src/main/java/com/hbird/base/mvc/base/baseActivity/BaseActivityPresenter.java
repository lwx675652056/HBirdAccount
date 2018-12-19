package com.hbird.base.mvc.base.baseActivity;
import com.hbird.base.mvp.presenter.base.BasePresenter;

/**
 * Created by Liul on 2018/7/2.
 * 只为继承现有的BaseActivity 直接搞个空的实现,没有任何功能 逻辑
 */
public class BaseActivityPresenter extends BasePresenter<MIBaseView, MIBaseModel> {

    public BaseActivityPresenter(MIBaseView iView, MIBaseModel iModel) {
        super(iView, iModel);
    }

}
