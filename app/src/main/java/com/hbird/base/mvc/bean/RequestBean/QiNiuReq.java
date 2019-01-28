package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/11.
 */

public class QiNiuReq extends BaseBean {
   private int Flag;

    public QiNiuReq() {
    }

    public QiNiuReq(int flag) {
        Flag = flag;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }
}
