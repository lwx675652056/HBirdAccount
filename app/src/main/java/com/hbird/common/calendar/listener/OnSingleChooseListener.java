package com.hbird.common.calendar.listener;

import android.view.View;

import com.hbird.common.calendar.bean.DateBean;

/**
 * 日期点击接口
 */
public interface OnSingleChooseListener {
    /**
     * @param view
     * @param date
     */
    void onSingleChoose(View view, DateBean date);
}
