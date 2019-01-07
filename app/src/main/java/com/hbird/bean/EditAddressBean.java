package com.hbird.bean;

/**
 * @author: LiangYX
 * @ClassName: EditAddressBean
 * @date: 2019/1/5 14:21
 * @Description: 收货地址
 */
public class EditAddressBean extends BaseBean {

    public Integer id;
    public String consigneeName; // 收货人
    public String consigneeMobile;// 收货人手机
    public String consigneeProvince;// 收货省份
    public String consigneeCity;// 收货城市
    public String consigneeDistrict;// 收货区县
    public String consigneeDetail;   // 收货地址详情

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", consigneeName:'" + consigneeName + '\'' +
                ", consigneeMobile:'" + consigneeMobile + '\'' +
                ", consigneeProvince:'" + consigneeProvince + '\'' +
                ", consigneeCity:'" + consigneeCity + '\'' +
                ", consigneeDistrict:'" + consigneeDistrict + '\'' +
                ", consigneeDetail:'" + consigneeDetail + '\'' +
                '}';
    }
}
