package com.hbird.ui.address;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: EditAddressData
 * @date: 2019/1/5 13:44
 * @Description: 编辑收货地址
 */
public class EditAddressData extends BaseObservable {

    private String name;// 姓名
    private String phone;// 手机号
    private String province;// 省份
    private String city;// 市
    private String county;// 县
    private String address;// 详细地址

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
        notifyPropertyChanged(BR.province);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
        notifyPropertyChanged(BR.county);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }
}