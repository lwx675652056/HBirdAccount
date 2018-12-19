package com.hbird.base.mvp.model.imodel;

import com.hbird.base.mvp.model.imodel.base.IBaseModel;

import io.reactivex.Observable;

/**
 * author:  admin
 * date:    2018/3/23
 * description:
 */

public interface IDownloadModel extends IBaseModel {

    Observable downloadFile();

}
