package com.hbird.base.mvc.bean.ReturnBean;

import com.google.gson.annotations.SerializedName;
import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/7/7.
 */

public class LoginReturn extends BaseReturn {

    private String code;
    private String msg;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String expire;
        @SerializedName("X-AUTH-TOKEN")
        private String XAUTHTOKEN;

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getXAUTHTOKEN() {
            return XAUTHTOKEN;
        }

        public void setXAUTHTOKEN(String XAUTHTOKEN) {
            this.XAUTHTOKEN = XAUTHTOKEN;
        }
    }
}
