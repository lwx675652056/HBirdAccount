package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/14.
 */

public class AccountTypeBean extends BaseReturn {

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
        private String abTypeName;
        private int typeBudget;
        private String iconDescribe;
        private Object icon;
        private int id;

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

        public String getIconDescribe() {
            return iconDescribe;
        }

        public void setIconDescribe(String iconDescribe) {
            this.iconDescribe = iconDescribe;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
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
