package com.hbird.base.mvp.view.iview;

import com.hbird.base.mvp.model.entity.table.MovieCollect;
import com.hbird.base.mvp.view.iview.base.IBaseView;

import java.util.List;

/**
 * author:  admin
 * date:    2018/3/21
 * description:
 */

public interface ICollectView extends IBaseView {

    void getCollectSuccess(List<MovieCollect> list);

}
