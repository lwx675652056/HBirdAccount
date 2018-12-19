package com.hbird.base.mvc.bean;

import java.util.List;

/**
 * Created by Liul on 2018/7/4.
 */

public class AddMoreTypeBean extends BaseBean {

    private String msg;
    private ResultBean result;
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
            private String spendName;
            private int priority;
            private List<SpendTypeSonsBean> spendTypeSons;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

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

            public List<SpendTypeSonsBean> getSpendTypeSons() {
                return spendTypeSons;
            }

            public void setSpendTypeSons(List<SpendTypeSonsBean> spendTypeSons) {
                this.spendTypeSons = spendTypeSons;
            }

            public static class SpendTypeSonsBean {
                private String parentName;
                private String icon;
                private String id;
                private String spendName;
                private int priority;
                private int mark;
                private String parentId;
                private List<?> spendTypeSons;
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

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
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

                public List<?> getSpendTypeSons() {
                    return spendTypeSons;
                }

                public void setSpendTypeSons(List<?> spendTypeSons) {
                    this.spendTypeSons = spendTypeSons;
                }
            }
        }

        public static class CommonListBean {
            private String parentName;
            private String icon;
            private String id;
            private String spendName;
            private int mark;
            private String parentId;

            public String getParentName() {
                return parentName;
            }

            public void setParentName(String parentName) {
                this.parentName = parentName;
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

            public String getSpendName() {
                return spendName;
            }

            public void setSpendName(String spendName) {
                this.spendName = spendName;
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
        }
    }
}
