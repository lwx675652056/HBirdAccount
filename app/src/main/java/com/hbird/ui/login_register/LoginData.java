package com.hbird.ui.login_register;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class LoginData extends BaseObservable {

    private String phone;// 手机号
    private String phone1;// 手机号 132-1234-1234
    private String code;// 验证码
    private String password;// 密码
    private boolean showPassword = false;// 是否明文显示密码
    private String time;// 验证码 时间
    private boolean clickable;// 获取验证码按钮是否可以点击
    private boolean isPassword;// 是密码登录，还是验证码登录

    public LoginData() {
        setShowPassword(false);
        setIsPassword(true);
        setPhone("");
        setPassword("");
        setCode("");
        setTime("获取验证码");
        setClickable(true);
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
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
        notifyPropertyChanged(BR.showPassword);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        notifyPropertyChanged(BR.clickable);
    }

    @Bindable
    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        phone1 = phone1.replaceAll("-", "");
        this.setPhone(phone1);
        if (phone1.length() <= 3) {
            this.phone1 = phone1;
        } else if (phone1.length() > 3 && phone1.length() < 8) {
            this.phone1 = phone1.substring(0, 3) + "-" + phone1.substring(3, phone1.length());
        } else {
            this.phone1 = phone1.substring(0, 3) + "-" + phone1.substring(3, 7) + "-" + phone1.substring(7, phone1.length());
        }

        notifyPropertyChanged(BR.phone1);
    }

    @Bindable
    public boolean isPassword() {
        return isPassword;
    }

    public void setIsPassword(boolean password) {
        isPassword = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable({"phone", "password","code"})
    public boolean isLoginClickable() {
        if (isPassword) {
            if (phone.length() == 11 && password.length() >= 6) {
                return true;
            }
        } else {
            if (phone.length() == 11 && code.length() > 0) {
                return true;
            }
        }
        return false;
    }
}
