package com.hbird.base.mvp.model.imodel;

import com.hbird.base.mvp.model.entity.table.MovieCollect;
import com.hbird.base.mvp.model.imodel.base.IBaseModel;

import io.reactivex.Observable;

/**
 * author:  admin
 * date:    2018/3/21
 * description:
 */

public interface ICollectModel extends IBaseModel {

    Observable getAllCollect();

    void deleteFromMyCollect(MovieCollect movieCollect);

    int getCollectCount();

    void updateMenuCollectCount();
}
