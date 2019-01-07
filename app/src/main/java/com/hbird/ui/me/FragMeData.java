package com.hbird.ui.me;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.android.databinding.library.baseAdapters.BR;
import com.hbird.util.Utils;

public class FragMeData extends BaseObservable {

    private boolean showLine = false;// 是否显示那条线
    private int msgNum = 0;// 丰丰通知数
    private String headUrl;// 头像
    private String nickName;// 昵称
    private String phone;// 手机号（中间4个*号代替）
    private String name;// 昵称、手机号（中间4个*号代替）
    private int fengniaoId = -1;// 蜂鸟ID
    private String precent;// 资料完善度，拼接好的
    private boolean isPrecent;// 资料是否全部完善
    private int inviteNum;// 邀请的好友数
    private String monthNum;// 本月记账数
    private int totalNum;// 记账总笔数


    @Bindable
    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
        notifyPropertyChanged(BR.showLine);
    }

    @Bindable
    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
        notifyPropertyChanged(BR.msgNum);
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
    public String getPrecent() {
        return precent;
    }

    public void setPrecent(String precent) {
        this.precent = precent;
        notifyPropertyChanged(BR.precent);
    }

    @Bindable
    public boolean isPrecent() {
        return isPrecent;
    }

    public void setPrecent(boolean precent) {
        isPrecent = precent;
        notifyPropertyChanged(BR.precent);
    }

    @Bindable
    public int getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(int inviteNum) {
        this.inviteNum = inviteNum;
        notifyPropertyChanged(BR.inviteNum);
    }

    @Bindable
    public String getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(String monthNum) {
        this.monthNum = monthNum;
        notifyPropertyChanged(BR.monthNum);
    }

    @Bindable
    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
        notifyPropertyChanged(BR.totalNum);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public void setName(String name, String phone) {
        setNickName(name);
        setPhone(phone);
        if (!TextUtils.isEmpty(nickName)) {
            this.name = nickName;
        } else {
            this.name = Utils.getHiddenPhone(phone);
        }

        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getName() {
        return name;
    }
}
