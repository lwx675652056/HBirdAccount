package com.hbird.ui.info;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class EditInfoData extends BaseObservable {

    private String headUrl;// 头像
    private String nickName;// 昵称
    private int fengniaoId;// 蜂鸟ID
    private String sex;// 性别 1为男 2为女
    private String birthday;// 生日 2018-12-11
    private String province;// 省份
    private String city;// 市
    private String profession;// 行业
    private String profion;// 职位

    public EditInfoData() {
        setFengniaoId(-1);
    }

    @Bindable
    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
        notifyPropertyChanged(BR.headUrl);
    }

    @Bindable
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        notifyPropertyChanged(BR.nickName);
    }

    @Bindable
    public int getFengniaoId() {
        return fengniaoId;
    }

    public void setFengniaoId(int fengniaoId) {
        this.fengniaoId = fengniaoId;
        notifyPropertyChanged(BR.fengniaoId);
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        notifyPropertyChanged(BR.sex);
    }

    @Bindable
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        notifyPropertyChanged(BR.birthday);
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
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
        notifyPropertyChanged(BR.profession);
    }

    @Bindable
    public String getProfion() {
        return profion;
    }

    public void setProfion(String profion) {
        this.profion = profion;
        notifyPropertyChanged(BR.profion);
    }
}
