package com.hbird.ui.account;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: AccountData
 * @date: 2018/12/28 10:20
 * @Description:
 */
public class EditAccountData extends BaseObservable {

    private  boolean isEdit = true;// 显示为编辑，显示为删除

    @Bindable
    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyPropertyChanged(BR.edit);
    }
}
