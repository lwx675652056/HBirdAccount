package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/14.
 */

public class AccountMembersBean extends BaseReturn {

    private ResultBean result;
    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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

    public static class ResultBean {
        private String owner;
        private String yourSelf;
        private List<String> members;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getYourSelf() {
            return yourSelf;
        }

        public void setYourSelf(String yourSelf) {
            this.yourSelf = yourSelf;
        }

        public List<String> getMembers() {
            return members;
        }

        public void setMembers(List<String> members) {
            this.members = members;
        }

        @Override
        public String toString() {
            return "{" +
                    "owner:'" + owner + '\'' +
                    ", yourSelf:'" + yourSelf + '\'' +
                    ", members:" + members +
                    '}';
        }
    }
}
