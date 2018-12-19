package com.hbird.base.mvp.model;

import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvp.model.http.DownloadApiService;
import com.hbird.base.mvp.model.imodel.IDownloadModel;
import com.ljy.devring.DevRing;

import io.reactivex.Observable;

public class DownloadModel implements IDownloadModel {

    /**
     * 下载文件
     * @return 返回下载文件的请求
     */
    @Override
    public Observable downloadFile() {
        return DevRing.httpManager().getService(DownloadApiService.class).downloadFile(UrlConstants.DOWNLOAD);
    }



}
