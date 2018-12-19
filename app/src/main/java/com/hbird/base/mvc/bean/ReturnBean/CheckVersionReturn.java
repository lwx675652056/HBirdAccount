package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/7/11.
 */

public class CheckVersionReturn extends BaseReturn {

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
        private long createDate;
        private String url;
        private int mobileSystem;
        private String updateLog;
        private int installStatus;
        private String version;
        private Object id;
        private Object size;

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getMobileSystem() {
            return mobileSystem;
        }

        public void setMobileSystem(int mobileSystem) {
            this.mobileSystem = mobileSystem;
        }

        public String getUpdateLog() {
            return updateLog;
        }

        public void setUpdateLog(String updateLog) {
            this.updateLog = updateLog;
        }

        public int getInstallStatus() {
            return installStatus;
        }

        public void setInstallStatus(int installStatus) {
            this.installStatus = installStatus;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getSize() {
            return size;
        }

        public void setSize(Object size) {
            this.size = size;
        }
    }
}
