package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseBean;
import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/13.
 */

public class AccountZbBean extends BaseReturn {

    private String code;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private long updateDate;
        private int member;
        private int abTypeId;
        private int userType;
        private String abName;
        private int defaultFlag;
        private String abTypeName;
        private int typeBudget;
        private String icon;
        private int id;

        public int getAbTypeId() {
            return abTypeId;
        }

        public void setAbTypeId(int abTypeId) {
            this.abTypeId = abTypeId;
        }

        public long getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(long updateDate) {
            this.updateDate = updateDate;
        }

        public int getMember() {
            return member;
        }

        public void setMember(int member) {
            this.member = member;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getAbName() {
            return abName;
        }

        public void setAbName(String abName) {
            this.abName = abName;
        }

        public int getDefaultFlag() {
            return defaultFlag;
        }

        public void setDefaultFlag(int defaultFlag) {
            this.defaultFlag = defaultFlag;
        }

        public String getAbTypeName() {
            return abTypeName;
        }

        public void setAbTypeName(String abTypeName) {
            this.abTypeName = abTypeName;
        }

        public int getTypeBudget() {
            return typeBudget;
        }

        public void setTypeBudget(int typeBudget) {
            this.typeBudget = typeBudget;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
