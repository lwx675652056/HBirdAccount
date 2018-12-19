package com.hbird.base.mvp.event;


public class WxLoginCodeEvent {

    public WxLoginCodeEvent(String code){
        this.code = code;
    }

    private String code ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
