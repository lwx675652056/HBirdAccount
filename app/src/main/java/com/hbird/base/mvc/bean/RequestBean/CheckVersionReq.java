package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/11.
 */

public class CheckVersionReq extends BaseBean {
   private String  version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
