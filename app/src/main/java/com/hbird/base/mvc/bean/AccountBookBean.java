package com.hbird.base.mvc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: LiangYX
 * @ClassName: AccountBookBean
 * @date: 2018/12/11 16:03
 * @Description: 账本
 */
public class AccountBookBean extends BaseReturn{

    public String code;
    public String msg;
    public List<AccountBean> result;

    public static class AccountBean implements Serializable {
        public String iconDescribe;// 带描述图标url
        public String typeBudget;
        public String abTypeName;//账本类型名称
        public String icon;
        public String id;// 账本类型id
    }
}
