package com.hbird.common.calendar.interf;

import com.hbird.common.calendar.model.CalendarDate;

public interface OnSelectDateListener {
    void onSelectDate(CalendarDate date);

    void onSelectOtherMonth(int offset);//点击其它月份日期
}
