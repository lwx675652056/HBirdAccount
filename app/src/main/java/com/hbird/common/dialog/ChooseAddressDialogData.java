package com.hbird.common.dialog;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: AccountData
 * @date: 2018/12/28 10:20
 * @Description:
 */
public class ChooseAddressDialogData extends BaseObservable {

    private String province = "";// 选择的省
    private String city = "";// 选择的市
    private String county = "";// 选择的县
    private int pos = 0;// 当前第几页
    private int choose = 0;// 选择了几个，省 市 县 1 2 3

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
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
        notifyPropertyChanged(BR.pos);
    }

    @Bindable
    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
        notifyPropertyChanged(BR.choose);
    }
}
