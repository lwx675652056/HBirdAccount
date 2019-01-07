package com.hbird.bean;

import java.util.List;

/**
 * @author: LiangYX
 * @ClassName: AddressBean
 * @date: 2019/1/5 16:18
 * @Description: 地址
 */
public class AddressBean {

    public int area;
    public List<AddressBean> cities;
    public int code;
    public int level;
    public String name;
    public String prefix;

    @Override
    public String toString() {
        return "{" +
                "area:" + area +
                ", cities:" + cities +
                ", code:" + code +
                ", level:" + level +
                ", name:'" + name + '\'' +
                ", prefix:'" + prefix + '\'' +
                '}';
    }
}
