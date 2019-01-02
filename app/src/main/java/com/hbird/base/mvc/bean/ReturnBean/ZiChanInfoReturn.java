package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.bean.AssetsBean;

import java.util.List;

/**
 * Created by Liul on 2018/10/23.
 */

public class ZiChanInfoReturn extends BaseReturn {

    private String code;
    private String msg;
    private ResultBean result;

    @Override
    public String toString() {
        return "{" +
                "code:'" + code + '\'' +
                ", msg:'" + msg + '\'' +
                ", result:" + result +
                '}';
    }

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
        private double netAssets;// 资产值
        private long initDate;// 同步时间
        private List<AssetsBean> assets;

        @Override
        public String toString() {
            return "{" +
                    "netAssets:" + netAssets +
                    ", initDate:" + initDate +
                    ", assets:" + assets +
                    '}';
        }

        public double getNetAssets() {
            return netAssets;
        }

        public void setNetAssets(double netAssets) {
            this.netAssets = netAssets;
        }

        public long getInitDate() {
            return initDate;
        }

        public void setInitDate(long initDate) {
            this.initDate = initDate;
        }

        public List<AssetsBean> getAssets() {
            return assets;
        }

        public void setAssets(List<AssetsBean> assets) {
            this.assets = assets;
        }
    }
}
