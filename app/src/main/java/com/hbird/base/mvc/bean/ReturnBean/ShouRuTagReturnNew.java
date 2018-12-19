package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/7/10.
 */

public class ShouRuTagReturnNew extends BaseReturn{

    public String code;
    public String msg;
    public ResultBean result;

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
        public List<AllListBean> allList;
        public List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> commonList;

        public List<AllListBean> getAllList() {
            return allList;
        }

        public void setAllList(List<AllListBean> allList) {
            this.allList = allList;
        }

        public List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> getCommonList() {
            return commonList;
        }

        public void setCommonList(List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> commonList) {
            this.commonList = commonList;
        }

        public static class AllListBean {
            public String id;
            public String incomeName;
            public int priority;
            public List<IncomeTypeSonsBean> incomeTypeSons;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIncomeName() {
                return incomeName;
            }

            public void setIncomeName(String incomeName) {
                this.incomeName = incomeName;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public List<IncomeTypeSonsBean> getIncomeTypeSons() {
                return incomeTypeSons;
            }

            public void setIncomeTypeSons(List<IncomeTypeSonsBean> incomeTypeSons) {
                this.incomeTypeSons = incomeTypeSons;
            }

            public static class IncomeTypeSonsBean {
                private String icon;
                private String id;
                private String incomeName;
                private int mark;
                private String parentId;
                private String parentName;
                private int priority;
                private List<?> incomeTypeSons;

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getIncomeName() {
                    return incomeName;
                }

                public void setIncomeName(String incomeName) {
                    this.incomeName = incomeName;
                }

                public int getMark() {
                    return mark;
                }

                public void setMark(int mark) {
                    this.mark = mark;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public int getPriority() {
                    return priority;
                }

                public void setPriority(int priority) {
                    this.priority = priority;
                }

                public List<?> getIncomeTypeSons() {
                    return incomeTypeSons;
                }

                public void setIncomeTypeSons(List<?> incomeTypeSons) {
                    this.incomeTypeSons = incomeTypeSons;
                }
            }
        }

        public static class CommonListBean {
            public String icon;
            public int id;
            public String incomeName;
            public int mark;
            public String parentId;
            public String parentName;
            public int priority;
            public Integer abTypeId;
            public Integer userInfoId;

            public Integer getAbTypeId() {
                return abTypeId;
            }

            public void setAbTypeId(Integer abTypeId) {
                this.abTypeId = abTypeId;
            }

            public Integer getUserInfoId() {
                return userInfoId;
            }

            public void setUserInfoId(Integer userInfoId) {
                this.userInfoId = userInfoId;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
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

            public String getIncomeName() {
                return incomeName;
            }

            public void setIncomeName(String incomeName) {
                this.incomeName = incomeName;
            }

            public int getMark() {
                return mark;
            }

            public void setMark(int mark) {
                this.mark = mark;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getParentName() {
                return parentName;
            }

            public void setParentName(String parentName) {
                this.parentName = parentName;
            }
        }
    }
}
