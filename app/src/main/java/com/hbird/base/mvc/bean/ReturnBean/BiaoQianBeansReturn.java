package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/23.
 */

public class BiaoQianBeansReturn extends BaseReturn {

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
                private String icon;
                private String id;
                private String spendName;
                private int priority;
                private List<?> spendTypeSons;

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

                public List<?> getSpendTypeSons() {
                    return spendTypeSons;
                }

                public void setSpendTypeSons(List<?> spendTypeSons) {
                    this.spendTypeSons = spendTypeSons;
                }
            }
        }

        public static class CommonListBean {
            private String icon;
            private int id;
            private int priority;
            private String spendName;

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

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getSpendName() {
                return spendName;
            }

            public void setSpendName(String spendName) {
                this.spendName = spendName;
            }
        }
    }
}
