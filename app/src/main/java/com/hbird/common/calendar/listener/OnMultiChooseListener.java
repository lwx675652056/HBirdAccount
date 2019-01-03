package com.hbird.common.calendar.listener;

import android.view.View;

import com.hbird.common.calendar.bean.DateBean;

/**
 * 多选接口
 */
public interface OnMultiChooseListener {
    /**
     * @param flag 多选时flag=true代表选中数据，flag=false代表取消选中
     */
    void onMultiChoose(View view, DateBean date, boolean flag);
}
