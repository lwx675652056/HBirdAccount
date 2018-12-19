package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/7/10.
 */

public class ZhiChuTagReturn extends BaseReturn{

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
        public List<CommonListBean> commonList;

        public List<AllListBean> getAllList() {
            return allList;
        }

        public void setAllList(List<AllListBean> allList) {
            this.allList = allList;
        }

        public List<CommonListBean> getCommonList() {
            return commonList;
        }

        public void setCommonList(List<CommonListBean> commonList) {
            this.commonList = commonList;
        }

        public static class AllListBean {
            private String spendName;
            private int priority;
            private String serviceIds;
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
                return serviceIds;
            }

            public void setId(String id) {
                this.serviceIds = id;
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
                private String serviceIds;
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
                    return serviceIds;
                }

                public void setId(String id) {
                    this.serviceIds = id;
                }

                public List<?> getSpendTypeSons() {
                    return spendTypeSons;
                }

                public void setSpendTypeSons(List<?> spendTypeSons) {
                    this.spendTypeSons = spendTypeSons;
                }
            }
        }

        public static class CommonListBean {
            public int mark;
            public String parentName;
            public String parentId;
            public String spendName;
            public String icon;
            public int id;
            public int priority;
            public Integer abTypeId;
            public Integer userInfoId;

            @Override
            public String toString() {
                return "{" +
                        "mark:" + mark +
                        ", parentName:'" + parentName + '\'' +
                        ", parentId:'" + parentId + '\'' +
                        ", spendName:'" + spendName + '\'' +
                        ", icon:'" + icon + '\'' +
                        ", id:" + id +
                        ", priority:" + priority +
                        ", abTypeId:" + abTypeId +
                        ", userInfoId:" + userInfoId +
                        '}';
            }

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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
