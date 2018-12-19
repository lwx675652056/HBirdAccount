package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/10/23.
 */

public class ZiChanInfoReturn extends BaseReturn {

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
        private double netAssets;
        private long initDate;
        private List<AssetsBean> assets;

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

        public static class AssetsBean {
            private long updateDate;
            private long createDate;
            private double money;
            private int assetsType;

            public long getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(long updateDate) {
                this.updateDate = updateDate;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public int getAssetsType() {
                return assetsType;
            }

            public void setAssetsType(int assetsType) {
                this.assetsType = assetsType;
            }
        }
    }
}
