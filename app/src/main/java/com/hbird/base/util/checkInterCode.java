package com.hbird.base.util;


import android.text.TextUtils;

import com.hbird.base.mvc.bean.ReturnBean.CommonReturn;

/**
 * Created by Liul on 2018/7/7.
 */

public class checkInterCode {
    public static String MSG = "";

    public static boolean isSuccess(String json) {
        CommonReturn request = new JSONUtil<String, CommonReturn>().JsonStrToObject(json, CommonReturn.class);
        //L.e(json);
        if(request==null){
            MSG = "解析错误";
            return false;
        }
        if (TextUtils.equals(request.getCode(),"200")) {
            return true;
        }else {
            MSG = request.getMsg();
            return false;
        }
    }

    public static boolean tokenInfos(String json) {
        CommonReturn request = new JSONUtil<String, CommonReturn>().JsonStrToObject(json, CommonReturn.class);
        //L.e(json);
        if(request==null){
            MSG = "解析错误";
            return false;
        }
        if(TextUtils.equals(request.getCode(),"01004")){
            MSG = request.getMsg();
            return true;
        }
        return false;
    }

}
