package com.hbird.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class HttpResult<T> implements Serializable {

    public int code;
    public String msg;
    public T result;


    @Override
    public String toString() {
        return "{" +
                "code:" + code +
                ", msg:'" + msg + '\'' +
                ", result:" + result +
                '}';
    }

    public Object getValueByKey(String key){
        if (result != null){
            JSONObject jsonObject = JSON.parseObject(result.toString());
            return jsonObject.get(key);
        }
        return "";
    }
}
