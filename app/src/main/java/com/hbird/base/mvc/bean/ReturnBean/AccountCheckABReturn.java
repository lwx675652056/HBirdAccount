package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/14.
 */

public class AccountCheckABReturn extends BaseReturn {

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
        private int members;
        private int accountBookId;

        public int getMembers() {
            return members;
        }

        public void setMembers(int members) {
            this.members = members;
        }

        public int getAccountBookId() {
            return accountBookId;
        }

        public void setAccountBookId(int accountBookId) {
            this.accountBookId = accountBookId;
        }
    }
}
