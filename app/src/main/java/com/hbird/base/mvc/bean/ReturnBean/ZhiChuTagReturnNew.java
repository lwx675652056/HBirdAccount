package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/7/10.
 */

public class ZhiChuTagReturnNew extends BaseReturn{

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
        public List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> commonList;

        public List<AllListBean> getAllList() {
            return allList;
        }

        public void setAllList(List<AllListBean> allList) {
            this.allList = allList;
        }

        public List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> getCommonList() {
            return commonList;
        }

        public void setCommonList(List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> commonList) {
            this.commonList = commonList;
        }

        public static class AllListBean {
            private String id;
            private String spendName;
            private int priority;
            private List<SpendTypeSonsBean> spendTypeSons;

            public String getSpendName() {
                return spendName;
            }

            public void setSpendName(String spendName) {
                this.spendName = spendName;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<SpendTypeSonsBean> getSpendTypeSons() {
                return spendTypeSons;
            }

            public void setSpendTypeSons(List<SpendTypeSonsBean> spendTypeSons) {
                this.spendTypeSons = spendTypeSons;
            }

            public static class SpendTypeSonsBean {
                private int mark;
                private String parentName;
                private String parentId;
                private String spendName;
                private String icon;
                private int priority;
                private String id;
                private List<?> spendTypeSons;

                public int getMark() {
                    return mark;
                }

                public void setMark(int mark) {
                    this.mark = mark;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getSpendName() {
                    return spendName;
                }

                public void setSpendName(String spendName) {
                    this.spendName = spendName;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public int getPriority() {
                    return priority;
                }

                public void setPriority(int priority) {
                    this.priority = priority;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public List<?> getSpendTypeSons() {
                    return spendTypeSons;
                }

                public void setSpendTypeSons(List<?> spendTypeSons) {
                    this.spendTypeSons = spendTypeSons;
                }
            }
        }

    }
}
