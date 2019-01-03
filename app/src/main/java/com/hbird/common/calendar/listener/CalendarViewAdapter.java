package com.hbird.common.calendar.listener;

import android.view.View;
import android.widget.TextView;

import com.hbird.common.calendar.bean.DateBean;

public interface CalendarViewAdapter {
    /**
     * 返回阳历、阴历两个TextView
     */
    TextView[] convertView(View view, DateBean date);
}
