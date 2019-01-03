package com.hbird.base.mvc.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.android.databinding.library.baseAdapters.BR;

public class FillInvitationData extends BaseObservable {

    private String code = "";

    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        notifyPropertyChanged(BR.code);
    }

    @Bindable({"code"})
    public boolean isSkip() {
        if (TextUtils.isEmpty(code) || code.length() < 8) {
            return true;
        }
        return false;
    }
}
