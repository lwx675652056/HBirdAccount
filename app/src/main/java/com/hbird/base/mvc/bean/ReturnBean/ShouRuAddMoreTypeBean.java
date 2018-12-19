package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseBean;

import java.util.List;

/**
 * Created by Liul on 2018/7/4.
 */

public class ShouRuAddMoreTypeBean extends BaseBean {

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
        private List<AllListBean> allList;
        private List<CommonListBean> commonList;

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
            private String id;
            private String incomeName;
            private int priority;
            private List<IncomeTypeSonsBean> incomeTypeSons;

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
            private String icon;
            private int id;
            private String incomeName;
            private int mark;
            private String parentId;
            private String parentName;

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
